CREATE TABLE IF NOT EXISTS Books
(
  b_id integer(8) AUTO_INCREMENT PRIMARY KEY,
  b_name varchar(32) NOT NULL
)

CREATE TABLE IF NOT EXISTS Users
(
  u_id integer(8) AUTO_INCREMENT PRIMARY KEY,
  u_un varchar(16) NOT NULL,
  u_pw varchar(32) NOT NULL
)

CREATE TABLE IF NOT EXISTS Orders
(
  o_id integer(8) AUTO_INCREMENT PRIMARY KEY,
  o_user integer(8),
  o_book integer(8),
  FOREIGN KEY (o_user) REFERENCES Users(u_id),
  FOREIGN KEY (o_book) REFERENCES Books(b_id)
)