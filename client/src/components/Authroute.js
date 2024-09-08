import { Navigate } from 'react-router-dom';
import { useContext } from 'react';
import axios from 'axios';

const AuthRoute = async ({children }) => {

    try {
        const response = await axios.get('http://localhost:8090/api/users/isAuthenticated', 
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
