import UseAuthFetch from "../utility/useAuthFetch";
import {useNavigate, useParams} from "react-router-dom";
import useAuth from "../utility/useAuth";
import {useEffect, useState} from "react";
import publicFetch from "../utility/publicFetch";

export default function EditQuestion() {
  const {id} = useParams();
  const {auth} = useAuth();
  const fetchWithAuth = UseAuthFetch();
  const navigate = useNavigate();
  const [question, setQuestion] = useState(null);

  useEffect(() => {
    async function loadQuestionById() {
      try {
        const responseObject = await publicFetch(`questions/${id}`);
        if (responseObject?.data) {
          setQuestion(responseObject.data);
        } else {
          throw new Error("Failed to load question details");
        }
      } catch (e) {
        console.error(e);
        setQuestion(null);
      }
    }

    loadQuestionById();
  }, []);

  async function handleEditQuestion(e) {
    try {
      e.preventDefault();
      const formData = new FormData(e.target);
      const question = Object.fromEntries(formData.entries());
      if (!auth?.userid || !question?.title || !question?.content) {
        throw new Error("Missing data");
      }
      /*
      Long id,Long userId,String title,String content,Long acceptedAnswerId
       */
      const responseObject = await fetchWithAuth(
        `questions`,
        "PATCH",
        {
          "id": id, "userId": auth?.userid, "title": question?.title, "content": question?.content,
          "acceptedAnswerId": null
        }
      );
      console.log(responseObject);
      if (responseObject?.data) {
        window.alert(`Question updated successfully`);
        navigate(`/user`);
      } else {
        throw new Error(responseObject?.error ?? "Failed to update question");
      }
    } catch (e) {
      console.error(e);
      window.alert("Failed to update question");
    }
  }

  return (
    <div>
      <form onSubmit={handleEditQuestion}>
        <label htmlFor={"title"}>Title:</label>
        <input type={"text"} id={"title"} name={"title"} minLength={1} maxLength={100}
               required={true}
               defaultValue={question?.title ?? ""}></input>
        <label htmlFor={"content"}>Content:</label>
        <textarea cols={40} rows={10} id={"content"} name={"content"} minLength={1} maxLength={100}
                  required={true}
                  defaultValue={question?.content ?? ""}></textarea>
        <button type={"submit"}>Edit</button>
      </form>
    </div>
  );
}