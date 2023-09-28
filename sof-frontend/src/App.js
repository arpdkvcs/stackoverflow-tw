import './App.css';
import UserSignIn from "./components/UserSignIn";
import UserSignUp from "./components/UserSignUp";
import {useState} from "react";


function App() {
    const [showSignUp, setShowSignUp] = useState(false);

    const toggleSignUp = () => {
        setShowSignUp(!showSignUp);
    }


    return (
        <>
            <div className="App">
                <h1>Welcome to StakeDownFlow</h1>
                {showSignUp ?
                    <UserSignUp/>
                    :
                    <UserSignIn onSignUp={toggleSignUp}/>
                }
            </div>
        </>
    );
}

export default App;
