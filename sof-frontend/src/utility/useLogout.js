import {useCallback} from "react";
import useAuth from "./useAuth";
import {useLocation, useNavigate} from "react-router-dom";

function useLogout() {
  const {setAuth} = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  //fetch logout, navigate to login or to /, clear auth state
  const logout = useCallback(
    async (willfulLogout = false) => {
      try {
        const apiUrl = process.env.REACT_APP_API_URL;
        const path = "auth/logout";
        await fetch(`${apiUrl}/${path}`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          credentials: "include"
        });
      } catch (err) {
        console.error(err);
      } finally {
        const path = willfulLogout ? "/" : "/login";
        navigate(path, {state: {from: location}, replace: true});
        setAuth({});
      }
    },
    [setAuth, location, navigate]
  );

  return logout;
}

export default useLogout;
