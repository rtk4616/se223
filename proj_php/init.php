<?php

  include("config.php");
  $conn = mysql_connect($db_host, $db_user, $db_pwd);
  if(!$conn) exit("数据库连接失败！" . mysql_error());
  $tmp = mysql_select_db($db_name, $conn);
  
  $sql = "CREATE TABLE IF NOT EXISTS Books
(
  b_id integer(8) AUTO_INCREMENT PRIMARY KEY,
  b_name varchar(32) NOT NULL
)";
  $res = mysql_query($sql, $conn);
  if(!res) exit("数据表创建失败！" . mysql_error());
  
  $sql = "CREATE TABLE IF NOT EXISTS Users
(
  u_id integer(8) AUTO_INCREMENT PRIMARY KEY,
  u_un varchar(16) NOT NULL,
  u_pw varchar(32) NOT NULL
)";
  $res = mysql_query($sql, $conn);
  if(!res) exit("数据表创建失败！" . mysql_error());

  $sql = "CREATE TABLE IF NOT EXISTS Orders
(
  o_id integer(8) AUTO_INCREMENT PRIMARY KEY,
  o_user integer(8),
  o_book integer(8),
  FOREIGN KEY (o_user) REFERENCES Users(u_id),
  FOREIGN KEY (o_book) REFERENCES Books(b_id)
)";
  $res = mysql_query($sql, $conn);
  if(!res) exit("数据表创建失败！" . mysql_error());
  
  echo "数据表创建成功！";
  mysql_close($conn);
  
?>