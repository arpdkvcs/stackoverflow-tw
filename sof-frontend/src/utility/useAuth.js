import {useContext} from "react";
import AuthContext from "../context/AuthProvider";

//simplify usage of the auth state (only need to import this)
function useAuth() {
  return useContext(AuthContext);
}

export default useAuth;
