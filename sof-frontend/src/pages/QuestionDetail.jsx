import * as React from 'react';
import { Routes, Route, useParams } from 'react-router-dom';
import {useEffect, useState} from "react";
import publicFetch from "../utility/publicFetch";




export default function QuestionDetail() {
    let { questionId } = useParams();
    const [question, setQuestion] = useState(null);
    const [answers, setAnswers] = useState([]);

    useEffect(() => {
      async function fetchQuestionDetails() {
        try {
          const responseObject = await publicFetch(`questions/${questionId}`);
          if (!responseObject?.data){
            throw new Error(responseObject?.error??"Failed to load all questions");
          }
          setQuestion(responseObject.data);
        } catch (e){
          setQuestion(null);
          console.error(e);
        }
      }
      fetchQuestionDetails();
    }, [question]);

    if (question) {
        return(
            <div>
                <h2>{question.title}</h2>
                <p>{question.content}</p>
                <p>Asked by: {question.username}</p>
                <div>
                    Answers:
                    <ul>
                        {answers.map(answer => <li>{answer.content}</li>)}
                    </ul>
                </div>
            </div>
        );
    } else {
        return(
            <div>
                Waiting for question.
            </div>
        );
    }
}