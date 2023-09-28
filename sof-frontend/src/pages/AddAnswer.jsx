import UseAuthFetch from "../utility/useAuthFetch";
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";

export default function AddAnswer() {
    const { id } = useParams();

    const fetchWithAuth = UseAuthFetch();
    const [answer, setAnswer] = useState(null);

    useEffect(() => {
        async function postAnswer() {
            try {
                const responseObject = await fetchWithAuth(
                    `answers/`,
                    "POST",
                    answer.content,
                );

            } catch (error) {
                setAnswer(null);
                console.error(error);
            }
        }
    }, [answer]);

    function handleAddAnswer(e) {
        e.preventDefault();
        const formData = new FormData(e.target);
        const formObject = Object.fromEntries(formData.entries());
        setAnswer(formObject);
    }

    return (
        <div>
            <form onSubmit={handleAddAnswer}>
                <label htmlFor={"content"}>Content:</label>
                <textarea id={"content"} name={"content"}></textarea>
                <button type={"submit"}>Add</button>
            </form>
        </div>
    )
}