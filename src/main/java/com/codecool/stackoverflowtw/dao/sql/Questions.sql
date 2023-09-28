--1. Main page listing all questions with details, date and answer count
SELECT q.id,
       q.title,
       q.created_at,
       COUNT(a.id) AS answer_count
FROM questions q
         LEFT JOIN answers a ON q.id = a.question_id
GROUP BY q.id, q.title, q.created_at
--Feature to sort questions on Main page by alphabet, date or answer count
ORDER BY q.title ASC/*DESC*/;
--ORDER BY q.created_at DESC / ASC;
--ORDER BY answer_count DESC / ASC;

--2. Detail page of a question shows who asked it and lists all answers with date and users
SELECT q.id,
       q.title,
       q.content,
       q.created_at,
       ua.username,
       a.id,
       a.content,
       a.created_at,
       uq.username
FROM questions q
         INNER JOIN answers a ON q.id = a.question_id
         INNER JOIN users ua ON a.user_id = ua.id
         INNER JOIN users uq ON q.user_id = uq.id
WHERE q.id = ?;

--3. Feature to add new question (redirects to question detail page after save)
INSERT INTO questions (user_id, title, content)
VALUES (?, ?, ?);

--4. Feature to delete question (later extend so only the owner can delete it)
DELETE
FROM questions
WHERE id=?;

--5. Feature to mark the accepted answer for the user's own question
UPDATE questions
SET accepted_answer_id = ?
WHERE id = ?;
--  AND user_id = ?;

--6. OPTIONAL: Filter by question titles  (location: main page)
SELECT *
FROM questions
WHERE title LIKE '%' || ? || '%';
-- '%' || ? || '%' => %parameter%

--7. OPTIONAL: Feature to edit questions (limited to the ones created by the user)
UPDATE questions
SET title   = ?,
    content = ?
WHERE id = ?;
--  AND user_id = ?;