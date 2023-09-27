--Feature to add new answer (location: question detail page)
INSERT INTO answers (id, user_id, content, created_at)
VALUES (?,?,?,?);
--Feature to delete answer (location: question delete page) (later extend so only the owner can
DELETE
FROM answers
WHERE id=?;
--OPTIONAL: Feature to edit answers (limited to the ones created by the user)
UPDATE answers
SET content = ?
WHERE id = ?;
