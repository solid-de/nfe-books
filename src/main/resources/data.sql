--delete from books;
--delete from authors;


MERGE INTO authors (author_name, author_bio, country) 
KEY (author_name)
    VALUES ('DICKENS C.', 'Charles John Huffam Dickens was an English novelist.', 'England');
MERGE INTO authors (author_name, author_bio, country) 
KEY (author_name)
    VALUES ('FLAUBERT G.', 'Gustave Flaubert was a French novelist.', 'France');


MERGE INTO books (title, author_id) 
KEY(title)
 SELECT 'David Copperfield', author_id  from authors where author_name = 'DICKENS C.';

MERGE INTO books (title, author_id) 
KEY(title)
 SELECT 'Hard Times', author_id  from authors where author_name = 'DICKENS C.';

MERGE INTO books (title, author_id) 
KEY(title)
 SELECT 'Madame Bovary', author_id  from authors where author_name = 'FLAUBERT G.';