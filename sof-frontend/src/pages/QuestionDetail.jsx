import * as React from "react";
import {useEffect, useState} from "react";
import {Link, useParams} from "react-router-dom";
import publicFetch from "../utility/publicFetch";
import useAuth from "../utility/useAuth";


export default function QuestionDetail() {
  const {id} = useParams();
  const [question, setQuestion] = useState(null);

  const {auth} = useAuth();

  useEffect(() => {
    console.log(id);

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

    id && fetchQuestionDetails();
  }, []);

  if (question) {
    return (
      <div>
        <h2>{question.title}</h2>
        <p>{question.content}</p>
        <p>Asked by: {question.username}</p>
        <h2>Answers:</h2>
        {question?.answers?.length > 0 ? <div>
          <ul>
            {question.answers.map(answer => <li key={answer.id}>{answer.content}</li>)}
          </ul>
        </div> : <h3>No answers yet!</h3>}
        {auth?.userid?<Link to={`/user/questions/addanswer/${question.id}`}>
          <button>Add answer</button>
        </Link>:<></>}
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