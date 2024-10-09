import React, { useContext } from 'react'
import './Noteitem.css'
import noteContext from '../context/notes/noteContext'

const Noteitem = (props) => {
  const getContext = useContext(noteContext);
  const {deleteNote} = getContext;
  const { note, updateNote } = props;
  return (
    <div>

      <div className="card mt-2">
        <div className="card-body d-flex align-items-center mx-3">
          {/* <h5 className="card-title">{props.note.title}</h5> */}
          <div style={{ flexBasis: '3%' }} className="fa-solid fa-file-line mx-2">
          <i class="fa-solid fa-file-lines file-lines-icon"></i>
          {/* <i className="fa-solid fa-trash mx-2" onClick={()=>{deleteNote(note.id);}} style={{cursor:"pointer"}} ></i> */}
          </div>
          <div style={{ flexBasis: '87%' }}>
            <div className='note-title'>

                    {props.note.title}
            </div>
          </div>
          <div style={{ flexBasis: '5%' }}>

          <i className="fa-solid fa-trash mx-2 trash-icon" onClick={()=>{deleteNote(note.id);}} style={{cursor:"pointer"}} ></i>
          </div>
          <div style={{ flexBasis: '5%' }}>

          <i className="fa-regular fa-pen-to-square mx-2" onClick={()=>{updateNote(note.id)}} style={{cursor:"pointer"}}></i>
          </div>
        </div>
      </div>

    </div>
  )
}

export default Noteitem
