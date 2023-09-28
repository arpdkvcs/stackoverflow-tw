import {useEffect, useState} from "react";
import QuestionTable from "../../components/QuestionTable";
import useAuthFetch from "../../utility/useAuthFetch";

function UserQuestions() {
  const [questions, setQuestions] = useState([]);
  const [error, setError] = useState(null);
  const [filteredQuestions, setFilteredQuestions] = useState([]);
  const [search, setSearch] = useState("");

  const fetchWithAuth = useAuthFetch();

  useEffect(() => {
    const fetchQuestions = async () => {
      try {
        const responseObject = await fetchWithAuth("questions/all");

        if (!responseObject?.data) {
          throw new Error(responseObject?.error ?? "Failed to load questions");
        }

        const data = responseObject.data;
        console.log(data);
        setQuestions(data);
        setFilteredQuestions(data);
      } catch (error) {
        setError(error?.message ?? "Failed to load questions");
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
      {questions?.length && <QuestionTable questions={filteredQuestions} questionDetailsPath="user/questions"/>}
    </div>
  );
}

export default UserQuestions;
