import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import React, { useCallback, useContext, useEffect, useState } from 'react'
import Quill from 'quill'

import 'quill/dist/quill.snow.css'
import { useNavigate, useParams } from 'react-router-dom'
import noteContext from './context/notes/noteContext'
import { v4 as uuidv4 } from 'uuid';
import axios from 'axios';

// const QuillCursors = require('quill-cursors')
// const Delta = Quill.import('delta');

var isSending = false;
const messageQueue = [];

const getDeltaSent = () => {
  let deltaSent = sessionStorage.getItem("deltaSent");
  if (!deltaSent) {
    deltaSent = null;
    sessionStorage.setItem("deltaSent", deltaSent);
  }
  return deltaSent;
};

var deltaSent = null;


const SOCKET_URL = `${process.env.REACT_APP_API_BASE_URL}/ws-message`;

const getClientId = () => {
  let clientId = sessionStorage.getItem("clientId");
  console.log("set cleintid: " + clientId);

  if (!clientId) {
    clientId = uuidv4();
    sessionStorage.setItem("clientId", clientId);
  }
  return clientId;
};

const clientId = getClientId();

const TOOLBAR_OPTIONS = [
  [{ header: [1, 2, 3, 4, 5, 6, false] }],
  [{ font: [] }],
  [{ list: "ordered" }, { list: "bullet" }],
  ["bold", "italic", "underline"],
  [{ color: [] }, { background: [] }],
  [{ script: "sub" }, { script: "super" }],
  [{ align: [] }],
  ["image", "blockquote", "code-block"],
  ["clean"],
]

export default function TextEditor() {
  const [socket, setSocket] = useState()
  const [quill, setQuill] = useState()
  const { id: documentId } = useParams()
  const getContext = useContext(noteContext);
  const { user, fetchUser, getNote } = getContext;
  const [title, setTitle] = useState("");

  let navigate = useNavigate();

  const sendinvite = async () => {

    var recipientEmail = window.prompt("Enter recipient email");
    try {
      const response = await axios.post(`${process.env.REACT_APP_API_BASE_URL}/sendinvite`,
        {
          recipientEmail: recipientEmail,
          documentId: documentId,
          title: title
        },
        {
          headers: { "Authorization": "Bearer " + localStorage.getItem('token') }
        });
    } catch (error) {
      console.log(error);

    }
  }

  const sendMessage = (message) => {

    if (!isSending) {
      deltaSent = message.delta;
      socket.send("/app/send-changes", {}, JSON.stringify(message));
      isSending = true;
    } else {
      console.log("pushing to queue");
      messageQueue.push(message);
    }
  };

  const processNextMessage = () => {
    if (messageQueue.length > 0) {
      messageQueue.shift(); // Remove the first message
      if (messageQueue.length > 0) {
        sendMessage(messageQueue[0]);
      } else {
        isSending = false; // No more messages to send
      }
    } else {
      isSending = false; // No more messages to send
    }
  };

  const wrapperRef = useCallback(wrapper => {

    if (wrapper == null) return

    wrapper.innerHTML = ""
    var editor = document.createElement("div")
    wrapper.append(editor)
    var q = new Quill(editor, {
      theme: "snow",
      modules: { toolbar: TOOLBAR_OPTIONS },
    })

    setQuill(q);

    async function onConnected(frame) {
      console.log("Connected!!")

      console.log("subscribing to" + `/topic/receive-changes/${documentId}`);

      client.subscribe(`/topic/receive-changes/${documentId}`, function (msg) {

        console.log("receiving changes");
        if (msg.body) {
          var jsonBody = JSON.parse(msg.body);

          console.log("clientId: " + clientId);
          console.log("message from clientId: " + jsonBody.clientId);
          if (clientId !== jsonBody.clientId) {
            console.log("updating quill");
            console.log("deltaReceive");

            var deltaReceive = jsonBody.delta;
            console.log("deltasent");
            console.log(deltaSent);

            if (deltaSent != null) {
              console.log("transformation applied");
              deltaReceive = deltaSent.transform(deltaReceive, false);
              console.log("deltaReceive");
              console.log(deltaReceive);
              console.log("deltaSent");
              console.log(deltaSent);
            }


            q.updateContents(deltaReceive);
            q.setSelection(q.getLength() - 1);
          } else {
            q.enable(true)
            q.setSelection(q.getLength() - 1)
            deltaSent = null;
            processNextMessage();
          }
        }
      });

      const response = await axios.get(`${process.env.REACT_APP_API_BASE_URL}/get-doc/${documentId}`,
        {
          headers: {
            "Authorization": "Bearer " + localStorage.getItem('token')
          }
        });
      console.log(response.data);
      for (const element of response.data.doc) {
        q.updateContents(element);
      }

      q.enable()
      console.log("title:" + response.data.title);

      if (!response.data.title || response.data.title === "") {

        var etitle = window.prompt("Enter Page Title")
        setTitle(etitle)
      } else {
        setTitle(response.data.title)
      }



      client.subscribe(`/topic/load-document/${documentId}`, function (msg) {
        console.log("receiving changes");
        if (msg.body) {
          var jsonBody = JSON.parse(msg.body);
          q.setContents(jsonBody.document)
          q.enable()
          setTitle(jsonBody.headline)
        }
      })
    }

    let onDisconnected = () => {
      console.log("Disconnected!!")
    }

    const socket1 = new SockJS(SOCKET_URL);
    const client = Stomp.over(socket1);

    // Connect with JWT token in headers
    client.connect(
      { "Authorization": 'Bearer ' + localStorage.getItem('token') }, // Pass the JWT token as an Authorization header
      async (frame) => {
        await onConnected(frame);
      },
      function (error) {}
    );

    setSocket(client);
  }, [])


  useEffect(() => {
    if (quill == null || socket == null) return
    const handler = (delta, oldDelta, source) => {

      console.log(source);
      console.log("delta:");

      console.log(delta);

      if (source !== "user") return;

      console.log("publishing: " + documentId);
      console.log("clientId: " + clientId);
      if (!socket.connected) {
        console.error("WebSocket is not connected.");
        return;
      }
      sendMessage({
        clientId: clientId,
        documentId: documentId,
        delta: delta
      });
      try {

        // quill.enable(false);
        // socket.send("/app/send-changes", {}, JSON.stringify({
        //   clientId: clientId,
        //   documentId: documentId,
        //   delta: delta
        // }))
        // function sendmess(){
        //   socket.send("/app/send-changes", {}, JSON.stringify({
        //     clientId: clientId,
        //     documentId: documentId,
        //     delta: delta
        //   }))

        // }

        // setTimeout(sendmess, 3000);


      } catch (error) {
        console.error("Error publishing message:", error);
      }
    }
    quill.on("text-change", handler)

    return () => {
      quill.off("text-change", handler)
    }
  }, [quill, socket])

  return (
    <>
      <div className='container mt-2'>
        <div className="d-flex flex-row justify-content-between input-group mb-3">
          <h4 className=''>{title}</h4>
          <button onClick={sendinvite}>Send Invite</button>
        </div>
      </div>
      <div className="container" ref={wrapperRef}></div>
    </>

  )
}
