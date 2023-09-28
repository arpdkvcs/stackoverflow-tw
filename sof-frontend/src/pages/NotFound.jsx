import React from "react";
import BackButton from "../components/utility/BackButton";

function NotFound() {
  return (
    <div className="NotFound">
      <h2>The page you are looking for does not exist.</h2>
      <BackButton path="/" text="Home"/>
    </div>
  );
}

export default NotFound;
