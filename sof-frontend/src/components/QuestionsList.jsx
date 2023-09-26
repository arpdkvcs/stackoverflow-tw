import {useEffect, useState} from "react";

function QuestionsList() {
    const [questions, setQuestions] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchQuestions = async () => {
            try {
                const response = await fetch('/questions/all');
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                const data = await response.json();
                setQuestions(data);
            } catch (error) {
                setError(error.message);
            }
        };
        fetchQuestions();
    }, []);

    return (
        <div>
            {error &&
                <p>
                    Error: {error}
                </p>}
            <ul>
                {questions.map((question) => (
                    <li key={question.id}>
                        Title: {question.title}
                        Content: {question.content}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default QuestionsList;
