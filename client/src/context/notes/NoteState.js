import react, { useState } from "react";
import NoteContext from "./noteContext";
import axios from 'axios';


const NoteState = (props)=>{
    const notesInitial = []
    const invitesInitial = []
    const usr = {}
    const [notes, setNotes] = useState(notesInitial)
    const [invites, setInvites] = useState(invitesInitial)
    const [user, setUser] = useState(usr)
    const getNotes = async ()=>{
        const response = await axios.get(`http://localhost:8090/fetchallnotes`, 
            {headers: {
                            "Authorization": "Bearer "+localStorage.getItem('token')
                      }});
        console.log(response.data)
        setNotes(response.data)
    }

    const getInvites = async ()=>{
        const response = await axios.get(`http://localhost:8090/fetchallinvites`, 
            {headers: {
                            "Authorization": "Bearer "+localStorage.getItem('token')
                      }});
        console.log(response.data)
        setInvites(response.data)
    }

    const acceptInvite = async (invite)=>{

        const response = await axios.post(`http://localhost:8090/acceptinvite`, 
            invite,
            {headers: {
                            "Authorization": "Bearer "+localStorage.getItem('token')
                      }});
        var newInvites = invites.filter((inv)=>{
            return inv.id !== invite.id
        }) 
        getNotes();
        setInvites(newInvites)
    }

    const declineInvite = async (invite)=>{

        const response = await axios.post(`http://localhost:8090/declineinvite`, 
            invite,
            {headers: {
                            "Authorization": "Bearer "+localStorage.getItem('token')
                      }});
        var newInvites = invites.filter((inv)=>{
            return inv.id !== invite.id
        }) 
        getNotes();
        setInvites(newInvites)
    }

    const fetchUser = async ()=>{

        // const response = await fetch(`http://localhost:5000/api/auth/getuser`,{
        //     method: 'POST',
        //     headers: {
        //         'Content-Type': 'application/json',
        //         "auth-token": localStorage.getItem('token')
        //     },
        // });
        // const json = await response.json();
        // console.log(json)
        // setUser(json)
    }

    //delete a note
    const deleteNote = async (id)=>{

        const response = await axios.delete(`http://localhost:8090/deletenote/${id}`, 
            {headers: {
                            "Authorization": "Bearer "+localStorage.getItem('token')
                      }});

        console.log('deleteing note with id : ',id)
        var newNotes = notes.filter((note)=>{
            return note.id !== id
        }) 
        setNotes(newNotes)
    }

    //get note
    const getNote = async (id)=>{
        const response = await fetch(`http://localhost:5000/api/notes/getnote/${id}`,{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                "auth-token": localStorage.getItem('token')
            }
        });

        const json = await response.json();
        console.log(json);
        return json.message;
    }
    
    //add note

    const addNote = async (title, description)=>{
        //todo : API call
        //API call
        const response = await fetch(`http://localhost:5000/api/notes/addnote/`,{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                "auth-token": localStorage.getItem('token')
            },
            body: JSON.stringify({title, description})
        });
        const json = await response.json();
        console.log(json)
        const note = json
        setNotes(notes.concat(note))
    }


    // const [state,setState] = useState("hello")
    return (
        <NoteContext.Provider value={{notes, getNotes,user, fetchUser, invites, getInvites, deleteNote, getNote, addNote, acceptInvite, declineInvite}}>
            {props.children}
        </NoteContext.Provider>
    )

}

export default NoteState;