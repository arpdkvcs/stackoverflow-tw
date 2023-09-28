import {useEffect, useState} from "react";
import publicFetch from "../utility/publicFetch";
import QuestionTable from "../components/QuestionTable";

function QuestionsList() {
  const [questions, setQuestions] = useState([]);
  const [error, setError] = useState(null);
  const [filteredQuestions, setFilteredQuestions] = useState([]);
  const [search, setSearch] = useState("");

  useEffect(() => {
    const fetchQuestions = async () => {
      try {
        //example of using the public endpoint fetch function
        const responseObject = await publicFetch("questions/all");
        if (!responseObject?.data) {
          throw new Error(responseObject?.error ?? "Failed to load questions");
        }
        const data = responseObject.data;
        console.log(data);
        setQuestions(data);
        setFilteredQuestions(data);
      } catch (error) {
        setError(error?.message??"Failed to load questions");
      }
    };
    fetchQuestions();
  }, []);

  function handleSearch(e) {
    setSearch(e.target.value);
    const allQuestions = [...questions];
    const filteredQuestions = allQuestions.filter(question => question.title.includes(e.target.value));
    setFilteredQuestions(filteredQuestions);
  }

  return (
      <div>
        Search: <input type="search" onChange={(e) => {
        handleSearch(e);
      }} value={search}></input>
        {questions?.length&&<QuestionTable questions={questions}/>}
      </div>
  )
}

export default QuestionsList;
