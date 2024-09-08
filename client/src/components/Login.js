import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from 'axios';


const Login = (props) => {
  const [credentials, setCredentials] = useState({ email: "", password: "" })
  let navigate = useNavigate();
  const handleSubmit = async (e) => {
    e.preventDefault();

    const user_cred = {
      username: credentials.email,
      password: credentials.password
    }

    try {
      const response = await axios.post('http://localhost:8090/authenticate', user_cred);
      console.log(response.data);

      localStorage.setItem('token', response.data.jwt)
      navigate('/');
    } catch (error) {
      console.error('There was an error!', error);
    }

    // const response = await fetch(`http://localhost:5000/authenticate`, {
    //   method: "POST",
    //   headers: {
    //     "Content-Type": "application/json",
    //   },
    //   body: JSON.stringify({ email: credentials.email, password: credentials.password })
    // });
    // const json = await response.json()
    // console.log(json);
    // if (json.success === true) {
    //   //save the auth-Token and redirect  
    //   localStorage.setItem('token', json.jwt)
    //   navigate('/');
    //   //   props.showAlert("success","Login success")
    // } else {
    //   // alert('Invalid credentials')
    //   //   props.showAlert( "danger","Invalid cred")
    // }
  };

  const onChange = (e) => {
    setCredentials({ ...credentials, [e.target.name]: e.target.value })
  }
  return (
    <div className="mt-2">
      <div className="container mt-5" style={{ "width": 628 }}>
        <form className="container" onSubmit={handleSubmit}>

          <h2 className="my-2">Login</h2>
          <div className="mb-3">
            <label htmlFor="email" className="form-label">
              Email address
            </label>
            <input
              type="email"
              className="form-control"
              id="email"
              name="email"
              aria-describedby="emailHelp"
              value={credentials.email}
              onChange={onChange}
              minLength={3}
              required
            />
          </div>
          <div className="mb-3">
            <label htmlFor="password" className="form-label">
              Password
            </label>
            <input
              type="password"
              className="form-control"
              id="password"
              name="password"
              value={credentials.password}
              onChange={onChange}
              minLength={3}
              required
            />
          </div>
          <button
            type="submit"
            className="btn btn-primary"
          >
            Submit
          </button>
        </form>
      </div>
    </div>
  );
};

export default Login;
