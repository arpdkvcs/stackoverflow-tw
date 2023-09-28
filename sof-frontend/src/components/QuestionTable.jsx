import {Link} from "react-router-dom";

export default function QuestionTable({ questions, search }) {
    return(
        <div>
        {error && <p>Error: {error}</p>}
        <h2>Browse questions</h2>
        <table>
            <tr>
                <th>Title</th>
                <th>Date</th>
                <th>Asked by</th>
                <th></th>
            </tr>
            {questions?.length ? questions.map( (question) => (
                <tr key={question.id}>
                    <td>{question.title}</td>
                    <td>{question.createdAt}</td>
                    <td>{question.username}</td>
                    <td><Link to={`/questiondetails/${id}`}><button>Details</button></Link></td>

                </tr>
            )):<h1>No questions</h1>}
        </table>
    </div>
    )
}