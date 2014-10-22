<?php
//session_start();
error_reporting(E_PARSE);
define("IN_USE", true);

require_once "./lib/common.php";
$usr = GetUserInfo();
if($usr)
{
  define("IS_LOGIN", true);
  if($usr["id"] == 1)
    define("IS_ADMIN", true);
}

$action = $_GET["action"];
if($action == "login")
{
  do_login($usr);
}
else if($action == "logout")
{
  if($usr)
  {
    setcookie("token", "");
  }
  echo "<script>location.href=\"./index.php\";</script>";
}
else if($action == "reg")
{
  do_reg($usr);
}
else if($action == "book")
{
  do_book($usr);
}
else if($action == "order")
{
  do_order($usr);
}
else //index
{
  if($usr) $param["un"] = $usr["un"];
  include "./template/index_tmpr.php"; 
}

function do_login($usr)
{
  if($usr)
  {
    echo "<script>location.href=\"./index.php\";</script>";
    return;
  }
  
  $un = $_GET["un"];
  $pw = $_GET["pw"];

  if($un == "")
  {
    include "./template/login_tmpr.php";
    return;
  }

  if($pw == "")
  {
    showmsg("请输入密码！", "./index.php?action=login");
    return;
  }

  include("./config.php");
  $conn = mysql_connect($db_host, $db_user, $db_pwd);
  if(!$conn)
  {
    showmsg("数据库访问错误！" . mysql_error(), "./index.php?action=login");
    return;
  }

  $pw = MD5($pw);
  mysql_select_db($db_name, $conn);
  $sql = "SELECT * FROM Users WHERE u_un='$un' and u_pw='$pw'";
  $res = mysql_query($sql, $conn);
  if(mysql_num_rows($res) == 0)
  {
    mysql_close($conn);
    showmsg("用户名或密码错误！", "./index.php?action=login");
    return;
  }
  $arr = mysql_fetch_array($res);

  mysql_close($conn);
  SetUserInfo($arr[0], $arr[1], $arr[2]);
  showmsg("登录成功！", "./index.php");
  
}

function do_reg($usr)
{
  if($usr)
  {
    echo "<script>location.href=\"./index.php\";</script>";
    return;
  }

  $un = $_GET["un"];
  $pw = $_GET["pw"];

  if($un == "")
  {
    include "./template/reg_tmpr.php";
    return;
  }

  $len = strlen($pw);
  if($len < 6 || $len > 16)
  {
    showmsg("密码应该在6~16位！", "./index.php?action=reg");
    return;
  }

  $conn = mysql_connect($db_host, $db_user, $db_pwd);
  if(!$conn)
  {
    showmsg("数据库访问错误！" . mysql_error(), "./index.php?action=reg");
    return;
  }
  mysql_select_db($db_name, $conn);
  $sql = "SELECT * FROM Users WHERE u_un='$un'";
  $res = mysql_query($sql, $conn);
  if(mysql_fetch_array($res))
  {
    mysql_close($conn);
    showmsg("用户已存在！", "./index.php?action=reg");
    return;
  }

  $pw = MD5($pw);
  $sql = "INSERT INTO Users (u_un, u_pw) VALUES ('$un', '$pw')";
  if(!mysql_query($sql, $conn))
  {
    mysql_close($conn);
    showmsg("用户创建失败！", "./index.php?action=reg");
    return;
  }
  
  $sql = "SELECT * FROM Users WHERE u_un='$un'";
  $res = mysql_query($sql, $conn);
  if(mysql_num_rows($res) == 0)
  {
    mysql_close($conn);
    showmsg("用户创建失败！", "./index.php?action=reg");
    return;
  }
  $arr = mysql_fetch_array($res);

  mysql_close($conn);
  SetUserInfo($arr[0], $arr[1], $arr[2]);
  showmsg("注册成功！", "./index.php");
}

function do_book($usr)
{
  if(!$usr)
  {
    echo "<script>location.href=\"./index.php?action=login\";</script>";
    return;
  }
  
  $res = GetBookInfo();
  $param["success"] = $res["success"];
  if($param["success"])
    $param["books"] = $res["books"];
  else
    $param["msg"] = $res["msg"];
  
  include "./template/book_tmpr.php";
}

function do_order($usr)
{
  if(!$usr)
  {
    echo "<script>location.href=\"./index.php?action=login\";</script>";
    return;
  }
  
  include "./template/order_tmpr.php";
}

function GetBookInfo($books)
{
  include("./config.php");
  $conn = mysql_connect($db_host, $db_user, $db_pwd);
  if(!$conn)
  {
    $msg = "database conn error" . mysql_error();
    return array("success" => false, "msg" => $msg);
  }
 
  mysql_select_db($db_name, $conn);

  $sql = "SELECT * FROM Books";
  $res = mysql_query($sql, $conn);
  if(!$res)
  {
    $msg = "book search error" . mysql_error();
    return array("success" => false, "msg" => $msg);
  }

  $books = array();
  while($row = mysql_fetch_array($res))
  {
    $book = array($row[0], $row[1]);
    $books[] = $book;
  }

  mysql_close($conn);
  return array("success" => true, "books" => $books);
}
?>