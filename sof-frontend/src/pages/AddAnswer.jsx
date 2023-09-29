import UseAuthFetch from "../utility/useAuthFetch";
import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import useAuth from "../utility/useAuth";

export default function AddAnswer() {
  const {id} = useParams();
  const {auth} = useAuth();
  const navigate = useNavigate();

  const fetchWithAuth = UseAuthFetch();
  const [answer, setAnswer] = useState(null);

  useEffect(() => {
    async function postAnswer() {
      try {
        const responseObject = await fetchWithAuth(
          `answers`,
          "POST",
          {"userId": auth?.userid, "questionId": id, "content": answer.content}
        );
        if (responseObject?.message) {
          window.alert(responseObject.message);
          navigate(`/user/questions/${id}`);
        } else {
          throw new Error("Failed to add answer");
        }
      } catch (error) {
        console.error(error);
      }
    }

    answer?.content && postAnswer();
  }, [answer]);

  function handleAddAnswer(e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    const formObject = Object.fromEntries(formData.entries());
    setAnswer(formObject);
  }

  return (
    <div>
      <form onSubmit={handleAddAnswer}>
        <label htmlFor={"content"}>Content:</label>
        <textarea cols={40} rows={10}  id={"content"} name={"content"} minLength={1} maxLength={100} required={true}></textarea>
        <button type={"submit"}>Add</button>
      </form>
    </div>
  );
}