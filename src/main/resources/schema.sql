
create table if not exists authors(
  author_id int not null GENERATED ALWAYS AS IDENTITY  PRIMARY KEY,
  author_name varchar(100) not null UNIQUE,
  author_bio varchar(500),
  country  varchar(100) not null
);



create table if not exists books(
  book_id int not null GENERATED ALWAYS AS IDENTITY  PRIMARY KEY,
  title varchar(200) not null UNIQUE,
  author_id int not null,

  foreign key (author_id) references authors(author_id)
);