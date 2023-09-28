import React from "react";
import useLogout from "../../utility/useLogout";

function UserLogout() {
  const logout = useLogout;

  return <button onClick={logout(true)}>Logout</button>;
}

export default UserLogout;