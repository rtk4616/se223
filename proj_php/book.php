<?php
//add.php?name=...
//return {"errno":xxx,"errmsg":"xxx"[,"id": ]}
  
//fix.php?oldname=...&newname=...
//return {"errno":xxx,"errmsg":"xxx"[, "id"=xxx]}

//rm.php?name=...
//return {"errno":xxx,"errmsg":"xxx"[, "id"=xxx]}

//query.php[?name=...]
//return {"errno":xxx,"errmsg":"xxx","data":[...]}

error_reporting(E_PARSE);

require_once "./lib/common.php";
$usr = GetUserInfo();
if(!$usr)
{
  echo "{\"errno\":1026,\"errmsg\":\"no auth\"}";
  return;
}

$action = $_GET["action"];
if($action == "add")
{
  do_add($usr);
}
else if($action == "rm")
{
  do_rm($usr);
}
else if($action == "fix")
{
  do_fix($usr);
}
else //query as default
{
  do_query($usr);
}

function do_add($usr)
{
  if($usr["id"] != 1)
  {
    echo "{\"errno\":1026,\"errmsg\":\"no auth\"}";
    return;
  }
  
  $name = $_GET["name"];
  if($name == "")
    exit("{\"errno\":1,\"errmsg\":\"name not found\"}");

  include("./config.php");
  $conn = mysql_connect($db_host, $db_user, $db_pwd);
  if(!$conn)
    exit("{\"errno\":1024,\"errmsg\":\"database conn error\"}");
  mysql_select_db($db_name, $conn);
  
  $sql = "SELECT * FROM Books WHERE b_name='$name'";
  $res = mysql_query($sql, $conn);
  if(!$res)
    exit("{\"errno\":1025,\"errmsg\":\"book search error\"}");
  if(mysql_fetch_array($res))
    exit("{\"errno\":2,\"errmsg\":\"book already exists\"}");
  
  $sql = "INSERT INTO Books (b_name) VALUES ('$name')";
  $res = mysql_query($sql, $conn);
  if(!$res)
    exit("{\"errno\":1026,\"errmsg\":\"book add error\"}");
	
  $sql = "SELECT b_id FROM Books WHERE b_name='$name'";
  $res = mysql_query($sql, $conn);
  if(!$res)
    exit("{\"errno\":1025,\"errmsg\":\"book search error\"}");
  $arr = mysql_fetch_array($res);
  
  echo "{\"errno\":0,\"errmsg\":\"success\",\"id\":$arr[0]}";
  mysql_close($conn);
}

function do_rm($usr)
{
  if($usr["id"] != 1)
  {
    echo "{\"errno\":1026,\"errmsg\":\"no auth\"}";
    return;
  }
  
  $name = $_GET["name"];
  if($name == "")
    exit("{\"errno\":1,\"errmsg\":\"name not found\"}");

  include("./config.php");
  $conn = mysql_connect($db_host, $db_user, $db_pwd);
  if(!$conn)
    exit("{\"errno\":1024,\"errmsg\":\"database conn error\"}");
  mysql_select_db($db_name, $conn);	
	
  $sql = "SELECT * FROM Books WHERE b_name='$name'";
  $res = mysql_query($sql, $conn);
  if(!$res)
    exit("{\"errno\":1025,\"errmsg\":\"book search error\"}");
  $arr = mysql_fetch_array($res);
  if(!$arr)
    exit("{\"errno\":3,\"errmsg\":\"book not exists\"}");
  $id = $arr[0];

  $sql = "DELETE FROM Books WHERE b_name='$name'";
  $res = mysql_query($sql, $conn);
  if(!res)
    exit("{\"errno\":1027,\"errmsg\":\"book delete error\"}");

  echo "{\"errno\":0,\"errmsg\":\"success\",\"id\":$id}";
  mysql_close($conn);
}

function do_fix($usr)
{
  if($usr["id"] != 1)
  {
    echo "{\"errno\":1026,\"errmsg\":\"no auth\"}";
    return;
  }
  
  $oldname = $_GET["oldname"];
  $newname = $_GET["newname"];
  if($oldname == "" || $newname == "")
    exit("{\"errno\":1,\"errmsg\":\"name not found\"}");

  include("./config.php");
  $conn = mysql_connect($db_host, $db_user, $db_pwd);
  if(!$conn)
    exit("{\"errno\":1024,\"errmsg\":\"database conn error\"}");
  mysql_select_db($db_name, $conn);	
	
  $sql = "SELECT * FROM Books WHERE b_name='$oldname'";
  $res = mysql_query($sql, $conn);
  if(!$res)
    exit("{\"errno\":1025,\"errmsg\":\"book search error\"}");
  $arr = mysql_fetch_array($res);
  if(!$arr)
    exit("{\"errno\":3,\"errmsg\":\"book not exists\"}");
  $id = $arr[0];

  $sql = "UPDATE Books SET b_name='$newname' WHERE b_name='$oldname'";
  $res = mysql_query($sql, $conn);
  if(!res)
    exit("{\"errno\":1028,\"errmsg\":\"bookname fix error\"}");

  echo "{\"errno\":0,\"errmsg\":\"success\",\"id\":$id}";
  mysql_close($conn);
}

function do_query($usr)
{
  include("./config.php");
  $conn = mysql_connect($db_host, $db_user, $db_pwd);
  if(!$conn)
    exit("{\"errno\":1024,\"errmsg\":\"database conn error\"}");
  mysql_select_db($db_name, $conn);
  
  $sql = "SELECT * FROM Books";
  if($_GET["name"] != "")
    $sql .= " WHERE b_name='$name'";
  $res = mysql_query($sql, $conn);
  if(!$res)
    exit("{\"errno\":1025,\"errmsg\":\"book search error\"}");

  echo "{\"errno\":0,\"errmsg\":\"success\",\"data\":[";
  while($row = mysql_fetch_array($res))
    echo "{\"id\":$row[0],\"name\":\"$row[1]\"},";
  echo "]}";
  
  mysql_close($conn);
}