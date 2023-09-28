import * as React from "react";
import {useEffect, useState} from "react";
import {Link, useParams} from "react-router-dom";
import publicFetch from "../utility/publicFetch";


export default function QuestionDetail() {
  const {id} = useParams();
  const [question, setQuestion] = useState(null);
  const [answers, setAnswers] = useState([]);

  useEffect(() => {
    console.log(id);
    async function fetchQuestionDetails() {
      try {
        const responseObject = await publicFetch(`questions/${id}`);
        if (!responseObject?.data) {
          throw new Error(responseObject?.error ?? "Failed to load all questions");
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
        <div>
          Answers:
          <ul>
            {answers.map(answer => <li>{answer.content}</li>)}
          </ul>
          <Link to={`/user/questions/${question.id}/addanswer`}><button>Add answer</button></Link>
        </div>
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