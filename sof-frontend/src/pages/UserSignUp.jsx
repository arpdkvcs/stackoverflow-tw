import {useState} from "react";
import {Link} from "react-router-dom";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEye, faEyeSlash} from "@fortawesome/free-solid-svg-icons";
import "./UserSignUp.css";

import publicFetch from "../utility/publicFetch";

function UserSignUp() {
  const [username, setUserName] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [registrationSuccess, setRegistrationSuccess] = useState(false);

  const handleSignUpSubmit = async (e) => {
    e.preventDefault();
    const data = { "username": username, "password": password };
    try {
      const jsonResponse = await publicFetch("auth/register", "POST", data);
      setUserName("");
      setPassword("");
      if (jsonResponse.message) {
        setRegistrationSuccess(true);
      } else {
        setRegistrationSuccess(false);
        window.alert(`Registration failed`);
      }
    } catch (error) {
      setRegistrationSuccess(false);
      console.error(error);
      window.alert(`Registration failed`);
    }
  };

  const handleTogglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  return (
    <div className="form-container" id="signup">
      <h3>Create your account here!</h3>
      {!registrationSuccess ? (
        <>
          <form onSubmit={handleSignUpSubmit}>
            <label>
              User Name:
              <input
                type="text"
                value={username}
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
            <button id="submitcomplete" type="submit">
              Complete sign up
            </button>
          </form>
        </>
      ) : (
        <div>
          <p>Thank you for registering! Have fun!</p>
          <Link to="/login">
            <button>Login</button>
          </Link>
        </div>
      )}
    </div>
  );
}

export default UserSignUp;