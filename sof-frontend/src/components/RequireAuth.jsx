import React from "react";
import {Outlet} from "react-router-dom";
import useAuth from "../utility/useAuth";
import useLogout from "../utility/useLogout";

function RequireAuth({allowedRoles}) {
  const {auth} = useAuth();
  const logout = useLogout();

  //real authentication happens at the backend
  //unauthorized response triggers logout
  const hasAuthState = auth?.username && auth?.roles;

  if (hasAuthState && auth.roles?.find((role) => allowedRoles.includes(role))) {
    return <Outlet/>;
  } else {
    logout();
    return null;
  }
}

export default RequireAuth;
