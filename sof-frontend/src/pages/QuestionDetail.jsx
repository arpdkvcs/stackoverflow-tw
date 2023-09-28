import * as React from 'react';
import { Routes, Route, useParams } from 'react-router-dom';
import {useEffect, useState} from "react";

function fetchQuestion(questionId) {
    return fetch(`/endpoint/questiondetails/${questionId}`).then((res) => res.json());
}

function fetchAnswer(answerId) {
    return fetch(`/endpoint/questiondetails/${answerId}`).then((res) => res.json());
}

export default function QuestionDetail() {
    let { questionId } = useParams();
    const [question, setQuestion] = useState(null);
    const [answers, setAnswers] = useState([]);

    useEffect(() => {
        fetchQuestion(questionId).then((q) => setQuestion(q));
    }, [questionId]);

    useEffect(() => {
        const answers = [];
        question.answersIds.forEach(answerId => {
            const answer = fetchAnswer(answerId);
            answers.push(answer);
        })
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