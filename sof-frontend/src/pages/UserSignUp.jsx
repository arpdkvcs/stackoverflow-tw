import {useState} from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEye, faEyeSlash} from "@fortawesome/free-solid-svg-icons";
import "./UserSignUp.css";

function UserSignUp({onSignUpComplete, onBackToLogin}) {
  const [name, setName] = useState("");
  const [userName, setUserName] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [registrationSuccess, setRegistrationSuccess] = useState(false);
  const [registeredUsername, setRegisteredUsername] = useState("");

  const handleSignUpSubmit = async (e) => {
    e.preventDefault();
    const data = {name, userName, password};
    try {
      const response = await fetch("/apiURL" /*input our URL*/, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
      });
      const jsonResponse = await response.json();
      console.log(jsonResponse);
      setName("");
      setUserName("");
      setPassword("");

      if (jsonResponse.success) {
        setRegistrationSuccess(true);
        setRegisteredUsername(jsonResponse.username);
      }
    } catch (error) {
      console.error(error);
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
              Name:
              <input type="text" value={name} onChange={(e) => setName(e.target.value)} required/>
            </label>
            <label>
              User Name:
              <input type="text" value={userName} onChange={(e) => setUserName(e.target.value)}
                     required/>
            </label>
            <label>
              Password:
              <div className="password-input-container">
                <input
                  type={showPassword ? "text" : "password"}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required/>
                <FontAwesomeIcon
                  icon={showPassword ? faEye : faEyeSlash}
                  className="password-toggle-icon"
                  onClick={handleTogglePasswordVisibility}/>
              </div>
            </label>
            <button id="submitcomplete" type="submit">Complete sign up</button>
          </form>
        </>
      ) : (
        <div>
          <p>Thank you for registering, {registeredUsername}! Have fun!</p>
        </div>
      )}
    </div>
  );
}

export default UserSignUp;
