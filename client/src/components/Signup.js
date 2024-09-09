import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const Signup = (props) => {
  const [credentials, setCredentials] = useState({ name: "", email: "", password: "", cpassword: "" })
  let navigate = useNavigate();
  const onChange = (e) => {
    setCredentials({ ...credentials, [e.target.name]: e.target.value })
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      const response1 = await axios.post(`${process.env.REACT_APP_API_BASE_URL}/api/users/createuser`, { name: credentials.name, email: credentials.email, password: credentials.password });
      const user_cred = {
        username: credentials.email,
        password: credentials.password
      }
      const response = await axios.post(`${process.env.REACT_APP_API_BASE_URL}/authenticate`, user_cred);
  
      localStorage.setItem('token', response.data.jwt)
      navigate('/');
      
    } catch (error) {
      console.error('There was an error!', error);
    }
  };

  return (
    <div className="container mt-2">
        <div className="container mt-5" style={{ "width": 628 }}>
          <form onSubmit={handleSubmit}>
            <h2 className="my-2 justify-content-center">SignUp</h2>
            <div className="mb-3">
              <label htmlFor="name" className="form-label">
                Name
              </label>
              <input
                type="text"
                className="form-control"
                id="name"
                name="name"
                aria-describedby="emailHelp"
                onChange={onChange}
                value={credentials.name}
              />
            </div>
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
                onChange={onChange}
                value={credentials.email}
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
                onChange={onChange}
                value={credentials.password}
              />
            </div>

            <div className="mb-3">
              <label htmlFor="cpassword" className="form-label">
                Confirm Password
              </label>
              <input
                type="password"
                className="form-control"
                id="cpassword"
                name="cpassword"
                onChange={onChange}
                value={credentials.cpassword}
              />
            </div>

            <button type="submit" className="btn btn-primary">
              Submit
            </button>
          </form>
        </div>
    </div>
  );
};

export default Signup;
