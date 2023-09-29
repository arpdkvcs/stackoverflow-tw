import * as React from "react";
import {useEffect, useState} from "react";
import {Link, useNavigate, useParams} from "react-router-dom";
import publicFetch from "../utility/publicFetch";
import useAuth from "../utility/useAuth";
import UseAuthFetch from "../utility/useAuthFetch";


export default function QuestionDetail() {
  const {id} = useParams();
  const [question, setQuestion] = useState(null);

  const {auth} = useAuth();
  const navigate = useNavigate();
  const fetchWithAuth = UseAuthFetch();


  async function fetchQuestionDetails() {
    try {
      const responseObject = await publicFetch(`questions/${id}`);
      if (!responseObject?.data) {
        throw new Error(responseObject?.error ?? "Failed to load question");
      }
      console.log(responseObject.data);
      setQuestion(responseObject.data);
    } catch (e) {
      setQuestion(null);
      console.error(e);
    }
  }

  useEffect(() => {
    console.log(id);
    id && fetchQuestionDetails();
  }, []);

  async function handleDelete(path) {
    try {
      const confirmed = window.confirm("Are you sure you want to delete?");
      if (!confirmed) {
        return;
      }

      const responseObject = await fetchWithAuth(`${path}`, "DELETE");

      if (responseObject?.message) {
        window.alert(responseObject.message);
      } else {
        throw new Error(responseObject?.error ?? "Failed to delete");
      }
    } catch (e) {
      console.error(e);
    }
  }

  if (question) {
    return (
      <div>
        <h2>{question.title}</h2>
        <p>{question.content}</p>
        <p>Asked by: {question.username}</p>
        {question?.username === auth?.username ? <div>
          <Link to={`/user/questions/edit/${id}`}>
            <button>Edit question</button>
          </Link>
          <button onClick={() => {
            handleDelete(`questions/${id}`).then(() => {
              navigate("/user");
            });
          }}>Delete question
          </button>
        </div> : <></>}
        <h2>Answers:</h2>
        {question?.answers?.length > 0 ? <div>
          <ul>
            {question.answers.map(answer =>
              <li key={answer.id}>
                {answer.content}
                {auth?.userid && auth?.username === answer?.username
                  ? <div>
                    <Link to={`/user/questions/editanswer/${answer.id}`}>
                      <button>Edit answer</button>
                    </Link>
                    <button onClick={() => {
                      handleDelete(`answers/${answer.id}`).then(()=>{
                        fetchQuestionDetails();
                      });
                    }}>Delete answer
                    </button>
                  </div>
                  : <></>}
              </li>)}
          </ul>
        </div> : <h3>No answers yet!</h3>}
        {auth?.userid ? <Link to={`/user/questions/addanswer/${question.id}`}>
          <button>Add answer</button>
        </Link> : <></>}
      </div>
    );
  } else {
    return (
      <div>
        Loading...
      </div>
    );
  }
}