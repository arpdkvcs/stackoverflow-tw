import React from "react";
import { Link, Outlet } from "react-router-dom";
import UserLogout from "./UserLogout";
import useAuth from "../utility/useAuth";

//nav could be exported to refactor
function Layout() {
  const { auth } = useAuth();

  return (
    <div className="Layout">
      <header>
        <nav>
          {auth?.username && <h3>Welcome {auth.username}</h3>}
          <Link to="/">
            <button>Home</button>
          </Link>
          <Link to="/questions">
            <button>Questions</button>
          </Link>
          {auth?.username ? (
            <UserLogout />
          ) : (
            <>
              <Link to="/register">
                <button>Register</button>
              </Link>
              <Link to="/login">
                <button>Login</button>
              </Link>
            </>
          )}
        </nav>
      </header>
      <main>
        <Outlet className="Outlet" />
      </main>
    </div>
  );
}

export default Layout;
