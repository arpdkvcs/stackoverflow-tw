import { useEffect, useState } from "react";
import publicFetch from "../utility/publicFetch";

function QuestionsList() {
  const [questions, setQuestions] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchQuestions = async () => {
      try {
        //example of using the public endpoint fetch function
        const responseObject = await publicFetch("/questions/all");
        if (responseObject?.error || !responseObject?.data) {
          throw new Error(responseObject?.error ?? "Failed to load questions");
        }
        const data = responseObject.data;
        setQuestions(data);
      } catch (error) {
        setError(error?.message);
      }
    };
    fetchQuestions();
  }, []);

  return (
    <div>
      {error && <p>Error: {error}</p>}
      <ul>
        {questions?.length ? (
          questions.map((question) => (
            <li key={question.id}>
              Title: {question.title} --- Content: {question.content}
            </li>
          ))
        ) : (
          <h3>No questions found</h3>
        )}
      </ul>
    </div>
  );
}

export default QuestionsList;
