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
          throw new Error(responseObject.error ?? "Failed to load questions");
        }
        const data = responseObject.data;
        setQuestions(data);
      } catch (error) {
        setError(error.message);
      }
    };
    fetchQuestions();
  }, []);

  return (
    <div>
      {error && <p>Error: {error}</p>}
      <h2>Browse questions</h2>
      <table>
        <tr>
          <th>Title</th>
          <th>Date</th>
          <th>Asked by</th>
        </tr>
        {questions?.length ? questions.map( (question) => (
          <tr key={question.id}>
            <td>{question.title}</td>
            <td>{question.createdAt}</td>
            <td>{question.username}</td>
          </tr>
        )):<h1>No questions</h1>}
      </table>
    </div>
  );
}

export default QuestionsList;
