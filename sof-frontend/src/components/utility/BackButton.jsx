import React from "react";
import {useNavigate} from "react-router-dom";

function BackButton({path, text}) {
  const navigate = useNavigate();

  return (
    <button type="button" onClick={() => navigate(path ?? -1)}>
      {text ?? "Back"}
    </button>
  );
}

export default BackButton;
