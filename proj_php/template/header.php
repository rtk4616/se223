<?php if(!defined("IN_USE")) exit(); ?>
<html>
<head>
  <title>图书管理系统</title>
  <meta charset="utf-8" />
  <link rel="stylesheet" href="./css/bootstrap.css" />
  <link rel="stylesheet" href="./css/bootstrap-theme.min.css" />
  <link rel="stylesheet" href="./css/style.css" />
  <script src="./js/jquery.min.js"></script>
  <script src="./js/common.js"></script>
</head>
<body>
<div id="main" class="container">
 
  <div id="maindir" class="navbar navbar-default" role="navigation">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="./index.php">主页</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
<?php if(!defined("IS_LOGIN")){ ?>
            <li><a href="./index.php?action=login">登录</a></li>
            <li><a href="./index.php?action=reg">注册</a></li>
<?php } else { ?>
            <li><a href="./index.php?action=book">图书</a></li>
			<li><a href="./index.php?action=order">订单</a></li>
            <li><a href="./index.php?action=logout">退出</a></li>
<?php } ?>
          </ul>
        </div><!--/.nav-collapse -->
  </div>
  
  <div id="banner" class="jumbotron">
    <h1>图书管理系统</h1>
  </div>
  