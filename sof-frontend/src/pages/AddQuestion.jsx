import UseAuthFetch from "../utility/useAuthFetch";
import {useNavigate} from "react-router-dom";
import useAuth from "../utility/useAuth";

export default function AddQuestion() {
  const {auth} = useAuth();
  const fetchWithAuth = UseAuthFetch();
  const navigate = useNavigate();

  async function handleAddQuestion(e) {
    try {
      e.preventDefault();
      const formData = new FormData(e.target);
      const question = Object.fromEntries(formData.entries());
      if (!auth?.userid || !question?.title || !question?.content) {
        throw new Error("Missing data");
      }
      const responseObject = await fetchWithAuth(
        `questions`,
        "POST",
        {"userId": auth?.userid, "title": question?.title, "content": question?.content}
      );
      console.log(responseObject);
      if (responseObject?.data) {
        window.alert(`Question created successfully`);
        navigate(`/user`);
      } else {
        throw new Error(responseObject?.error ?? "Failed to post question");
      }
    } catch (e) {
      console.error(e);
      window.alert("Failed to add question");
    }
  }

  return (
    <div>
      <form onSubmit={handleAddQuestion}>
        <label htmlFor={"title"}>Title:</label>
        <input type={"text"} id={"title"} name={"title"} minLength={1} maxLength={100} required={true}></input>
        <label htmlFor={"content"}>Content:</label>
        <textarea cols={40} rows={10}  id={"content"} name={"content"} minLength={1} maxLength={100}
                  required={true}></textarea>
        <button type={"submit"}>Add</button>
      </form>
    </div>
  );
}