import {useState} from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEye, faEyeSlash} from "@fortawesome/free-solid-svg-icons";
import "./UserSignIn.css";
import {Link, useNavigate} from "react-router-dom";
import publicFetch from "../utility/publicFetch";
import useAuth from "../utility/useAuth";

function UserSignIn() {
  const [userName, setUserName] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const navigate = useNavigate();

  const { setAuth } = useAuth();

  const handleSignInSubmit = async (user) => {
    try {
      const responseObject = await publicFetch("auth/login", "POST", user);
      if (!responseObject?.data) {
        throw new Error(responseObject?.error ?? "Login failed");
      } else {
        handleSuccessfulLogin(responseObject.data.userid,responseObject.data.username, responseObject.data.roles);
      }
      resetForm();
    } catch (error) {
      setAuth({});
      alert(
        "Failed to sign in. Please check your username and password, then try again!"
      );
      console.error("An error occurred while signing in:", error);
    }
  };

  function handleSuccessfulLogin(receivedUserid,receivedUsername, receivedRoles) {
    setAuth({"userid":receivedUserid, "username": receivedUsername, "roles": receivedRoles });
    navigate("/user");
  }

  const resetForm = () => {
    setUserName("");
    setPassword("");
  };

  const onSubmit = (e) => {
    e.preventDefault();
    handleSignInSubmit({
      "username": userName,
      "password": password
    });
  };

  const handleTogglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  return (
    <div className="form-container" id="signin">
      <h3>Sign in here!</h3>
      <form className="UserSignInForm" onSubmit={onSubmit}>
        <label>
          User Name:
          <input
            type="text"
            value={userName}
            onChange={(e) => setUserName(e.target.value)}
            required
          />
        </label>
        <label>
          Password:
          <div className="password-input-container">
            <input
              type={showPassword ? "text" : "password"}
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
            <FontAwesomeIcon
              icon={showPassword ? faEye : faEyeSlash}
              className="password-toggle-icon"
              onClick={handleTogglePasswordVisibility}
            />
          </div>
        </label>
        <button id="submitlogin" type="submit">
          Login
        </button>
      </form>
      <p>Don't have an account? </p>

      <Link to="/register">
        <span className="clickable-text">Click here to sign up!</span>
      </Link>
    </div>
  );
}

export default UserSignIn;