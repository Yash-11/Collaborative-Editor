import React, { useContext } from 'react'
import noteContext from '../context/notes/noteContext'

const Noteitem = (props) => {
  const getContext = useContext(noteContext);
  const {deleteNote} = getContext;
  const { note, updateNote } = props;
  return (
    <div>

      <div className="card mt-2">
        <div className="card-body">
          <h5 className="card-title">{props.note.title}</h5>
          <i className="fa-solid fa-trash mx-2" onClick={()=>{deleteNote(note.id);}} style={{cursor:"pointer"}} ></i>
          <i className="fa-regular fa-pen-to-square mx-2" onClick={()=>{updateNote(note.id)}} style={{cursor:"pointer"}}></i>
        </div>
      </div>

    </div>
  )
}

export default Noteitem
