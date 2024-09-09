import { Navigate } from 'react-router-dom';
import { useContext } from 'react';
import axios from 'axios';

const AuthRoute = async ({children }) => {

    try {
        const response = await axios.get(`${process.env.REACT_APP_API_BASE_URL}/api/users/isAuthenticated`, 
            {headers: {
                "Authorization": "Bearer "+localStorage.getItem('token')
          }}
        );
      } catch (error) {
        return <Navigate to="/login" />;
      }

  return children;
};

export default AuthRoute;
