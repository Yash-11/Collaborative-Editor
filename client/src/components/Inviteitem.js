import React, { useContext } from 'react'
import noteContext from '../context/notes/noteContext'

const InviteItem = (props) => {
  const getContext = useContext(noteContext);
  const {acceptInvite, declineInvite} = getContext;
  const { invite } = props;
  return (
    <div>

      <div className="card mt-2">
        <div className="card-body">
          <h5 className="card-title">{props.invite.title}</h5>
          <i className="fa-solid fa-check mx-2" onClick={()=>{acceptInvite(invite);}} style={{cursor:"pointer"}} ></i>
          <i className="fa-regular fa-x mx-2" onClick={()=>{declineInvite(invite)}} style={{cursor:"pointer"}}></i>
        </div>
      </div>

    </div>
  )
}

export default InviteItem
