import UseAuthFetch from "../utility/useAuthFetch";
import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import useAuth from "../utility/useAuth";
import publicFetch from "../utility/publicFetch";

export default function EditAnswer() {
  const {id} = useParams();
  const {auth} = useAuth();
  const navigate = useNavigate();

  const fetchWithAuth = UseAuthFetch();
  const [answer, setAnswer] = useState(null);

  useEffect(() => {
    async function loadAnswerById() {
      try {
        // /id/id .... bc of backend controller endpoints
        const responseObject = await publicFetch(
          `answers/id/${id}`
        );
        console.log(responseObject);
        if (responseObject?.data) {
          setAnswer(responseObject?.data);
        } else {
          throw new Error("Failed to get answer");
        }
      } catch (e) {
        console.error(e);
        setAnswer(null);
      }
    }
    id && loadAnswerById()
  }, []);

  async function editAnswer(answerId, userId, content) {
    try {
      const responseObject = await fetchWithAuth(
        `answers`,
        "PATCH",
        {"id": answerId, "userId": userId, "content": content}
      );
      if (responseObject?.message) {
        window.alert(responseObject.message);
        navigate(-1);
      } else {
        throw new Error("Failed to edit answer");
      }
    } catch (error) {
      console.error(error);
    }
  }

  function handleAddAnswer(e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    const answer = Object.fromEntries(formData.entries());
    editAnswer(id, auth?.userid, answer?.content);
  }

  return (
    <div>
      <form onSubmit={handleAddAnswer}>
        <label htmlFor={"content"}>Content:</label>
        <textarea cols={40} rows={10} id={"content"} name={"content"} minLength={1} maxLength={100}
                  required={true}
        defaultValue={answer?.content??""}></textarea>
        <button type={"submit"}>Edit</button>
      </form>
    </div>
  );
}