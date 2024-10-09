import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import React, { useCallback, useContext, useEffect, useState, useRef } from 'react'
import Quill from 'quill'

import 'quill/dist/quill.snow.css'
import { useNavigate, useParams } from 'react-router-dom'
import noteContext from './context/notes/noteContext'
import { v4 as uuidv4 } from 'uuid';
import axios from 'axios';
import './TextEditor.css'


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
  const messageQueue = useRef([]);
  const subscription = useRef();
  const deltaSent = useRef(null);

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
    console.log("socket: "+socket);

    messageQueue.current.push(message);
    console.log("messageQueue.length "+messageQueue.current.length);
    
    
    if (messageQueue.current.length == 1) {
      socket.send("/app/send-changes", {}, JSON.stringify(message));
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
  }, [])

  useEffect(() => {
    if (quill == null) return;
    
    const socket1 = new SockJS(SOCKET_URL);
    const client = Stomp.over(socket1);

    async function onConnected(frame) {

      // console.log("subscribing to" + `/topic/receive-changes/${documentId}`);

      subscription.current = client.subscribe(`/topic/receive-changes/${documentId}`, function (msg) {        

        console.log("receiving changes");
        if (msg.body) {
          var jsonBody = JSON.parse(msg.body);

          // console.log("clientId: " + clientId);
          // console.log("message from clientId: " + jsonBody.clientId);
          if (clientId !== jsonBody.clientId) {
            // console.log("updating quill");
            // console.log("deltaReceive");

            var deltaReceive = jsonBody.delta;

            if (deltaSent.current != null) {
              // console.log("transformation applied");
              deltaReceive = deltaSent.current.transform(deltaReceive, false);
            }


            quill.updateContents(deltaReceive);
            quill.setSelection(quill.getLength() - 1);
          } else {
            quill.enable(true)
            quill.setSelection(quill.getLength() - 1)
            deltaSent.current = null;
            // processNextMessage();
            if (messageQueue.current.length > 0) {
              messageQueue.current.shift();
              if (messageQueue.current.length > 0) {
                client.send("/app/send-changes", {}, JSON.stringify(messageQueue.current[0]));
              }
            } 
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
        quill.updateContents(element);
      }

      quill.enable()
      console.log("title:" + response.data.title);

      if (!response.data.title || response.data.title === "") {

        var etitle = window.prompt("Enter Page Title")
        setTitle(etitle)
      } else {
        setTitle(response.data.title)
      }
    }
   
  
    // Connect with JWT token in headers
    client.connect(
      { "Authorization": 'Bearer ' + localStorage.getItem('token') }, // Pass the JWT token as an Authorization header
      async (frame) => {
        await onConnected(frame);
      },
      function (error) {}
    );

    setSocket(client);

    return () => {
      subscription.current.unsubscribe();
    }
  }, [quill])

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
    }
    quill.on("text-change", handler);
    console.log("text-change handler set");
    

    return () => {
      quill.off("text-change", handler)
    }
  }, [quill, socket])

  return (
    <>
      <div className='container mt-2'>
        <div className="d-flex flex-row justify-content-between input-group mb-3">
          <div className='doc-title'>{title}</div>
          <button onClick={sendinvite}>Send Invite</button>
        </div>
      </div>
      <div className="container" ref={wrapperRef}></div>
    </>

  )
}
