import {Link} from "react-router-dom";

export default function QuestionTable({ questions,questionDetailsPath }) {
    return(
        <div>
        <h2>Browse questions</h2>
        <table>
          <thead>
            <tr>
                <th>Title</th>
                <th>Date</th>
                <th>Asked by</th>
                <th></th>
            </tr>
          </thead>
          <tbody>
            {questions?.length ? questions.map( (question) =>  {
              return (<tr key={question.id}>
                <td>{question.title}</td>
                <td>{question.createdAt}</td>
                <td>{question.username}</td>
                <td><Link to={`/${questionDetailsPath}/${question.id}`}>
                  <button>Details</button>
                </Link></td>
              </tr>);
            }):<tr><td><h3>No questions found</h3></td></tr>}
          </tbody>
        </table>
    </div>
    )
}