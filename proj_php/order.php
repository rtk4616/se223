<?php
  session_start();
  error_reporting(0);
  
  $un = $_SESSION["un"];
  if($un == "")
  {
    echo "<script>location.href=\"./index.php\";</script>";
	return;
  }
  
  define("IS_LOGIN", true);
  define("IN_USE", true);
  
  include("./template/header.php");
  
  include("./template/footer.php");
  
?>