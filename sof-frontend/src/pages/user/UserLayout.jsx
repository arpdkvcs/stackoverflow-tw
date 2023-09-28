import React from "react";
import {Link, Outlet} from "react-router-dom";
import UserLogout from "./UserLogout";
import useAuth from "../../utility/useAuth";

//nav could be exported to refactor
function UserLayout() {
  const {auth} = useAuth();

  return (
    <div className="Layout">
      <header>
        {auth?.username && <h3>Welcome {auth.username}!</h3>}
        <nav>
          <Link to="/user">
            <button>Questions</button>
          </Link>
          {auth?.username && (
            <UserLogout/>
          )}
        </nav>
      </header>
      <main>
        <Outlet className="Outlet"/>
      </main>
    </div>
  );
}

export default UserLayout;