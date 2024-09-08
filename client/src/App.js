import TextEditor from "./TextEditor";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { v4 as uuidV4 } from 'uuid'
import Home from "./components/Home";
import Navbar from "./components/Navbar";
import Login from "./components/Login"
import Signup from "./components/Signup"
import NoteState from "./context/notes/NoteState";
import { useState } from "react";

function App() {
  const [headln, setHeadln] = useState("");
  const setHeadLine = (s) => {
    setHeadln(s)
  }
  return (
    <>
      <NoteState>
        <BrowserRouter>
          <Navbar />
          <Routes>
            <Route exact path='/' element={<Home setHeadLine={setHeadLine} />} />
            <Route exact path='/create' element={<Navigate to={`/documents/${uuidV4()}`} />} />
            <Route exact path='/documents/:id' element={<TextEditor />} />
            <Route exact path="/login" element={<Login />} />
            <Route exact path="/signup" element={<Signup showAlert />} />
          </Routes>
        </BrowserRouter>
      </NoteState>
    </>


  );
}

export default App;
