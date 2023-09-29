import {useEffect, useState} from "react";
import publicFetch from "../utility/publicFetch";
import QuestionTable from "../components/QuestionTable";
import useAuth from "../utility/useAuth";
import {useLocation} from "react-router-dom";

function QuestionsList() {
  const [questions, setQuestions] = useState([]);
  const [error, setError] = useState(null);
  const [filteredQuestions, setFilteredQuestions] = useState([]);
  const [search, setSearch] = useState("");
  const {auth} = useAuth();
  const location = useLocation();
  console.log(location);

  useEffect(() => {
    const fetchQuestions = async () => {
      try {
        const path = (location.pathname.toString() === "/user/myquestions")
          ? `questions/user/${auth.userid}`
          : "questions/all";

        const responseObject = await publicFetch(path);
        if (!responseObject?.data) {
          throw new Error(responseObject?.error ?? "Failed to load questions");
        }
        const data = responseObject.data;

        setQuestions(data);
        setFilteredQuestions(data);
      } catch (error) {
        setError(error?.message ?? "Failed to load questions");
      }
    };
    fetchQuestions();
  }, [location]);

  function handleSearch(e) {
    setSearch(e.target.value);
    const allQuestions = [...questions];
    const filteredQuestions = allQuestions.filter(question => question.title.includes(e.target.value));
    setFilteredQuestions(filteredQuestions);
    console.log(filteredQuestions);
  }

  return (
    <div>
      Search: <input type="search" onChange={(e) => {
      handleSearch(e);
    }} value={search}></input>
      {questions?.length ?
        <QuestionTable questions={filteredQuestions}
                       questionDetailsPath={auth?.userid ? "user/questions" : "questions"}/> :
        <h3>No questions found</h3>}
    </div>
  );
}

export default QuestionsList;
