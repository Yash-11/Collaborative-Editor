import React, { useContext, useEffect } from 'react'
import Plus from './plus.png'
import { useNavigate } from 'react-router-dom';
import noteContext from '../context/notes/noteContext';
import { v4 as uuidV4 } from 'uuid'
import Noteitem from './Noteitem';
import axios from 'axios';
import InviteItem from './Inviteitem';

const Home = (props) => {
    const getContext = useContext(noteContext)
    // console.log(getContext)
    const { notes, getNotes, user, fetchUser, invites, getInvites, addNote } = getContext;
    let navigate = useNavigate();
    useEffect(() => {
        console.log("token");
        
        console.log(localStorage.getItem('token'));
        
        if (localStorage.getItem('token')) {
            fetchUser();
            getNotes();
            getInvites();

            console.log("notes")
            console.log(notes)

            console.log("invites")
            console.log(invites)
        } else {
            navigate('/login')
        }
    }, [])
    const handle = async () => {
        
        var etitle = window.prompt("Enter Page Title")
        
        const documentId = uuidV4();
        await axios.post(`${process.env.REACT_APP_API_BASE_URL}/create-doc/${documentId}`, { title: etitle },
            {headers: {
                            "Authorization": "Bearer "+localStorage.getItem('token')
                      }});
        
        navigate(`/documents/${documentId}`)
    }

    const updateNote = (note) => {
        console.log("hello from update",note);
        navigate(`/documents/${note}`)
    }
    return (
        <div className='container my-4'>
            <div className='container'>
                <div className='container my-3' style={{ height: 210, width: 120 }}>
                    <button className='btn btn-outline-secondary' data-toggle="tooltip" data-placement="top" title="Create new document" onClick={handle} style={{ borderRadius: 10, borderColor: "grey" }}>
                        <img src={Plus} alt="" style={{ height: 210, width: 120, borderRadius: 10 }} />
                    </button>
                </div>
            </div>
            <div className='container'>
                <div className="row my-3">
                    <h1>Invites </h1>
                </div>
                {invites.length > 0 ? (
                    invites.map((invite) => {
                        return <InviteItem key={invite.id} invite={invite} />
                    })
                ) : (
                    <p className="ms-3">No invites.</p>
                )}
            </div>

            <div className='container'>
                <div className="row my-3">
                    <h1>Your Notes </h1>
                </div>
                {notes.length > 0 ? (
                    notes.map((note) => {
                        return <Noteitem key={note.id} note={note} updateNote={updateNote}/>
                        
                    })
                ) : (
                    <p className="ms-3">No documents.</p>
                )}
            </div>
        </div>
    )
}

export default Home
