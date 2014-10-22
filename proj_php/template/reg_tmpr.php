<?php if(!defined("IN_USE")) exit(); ?>
<?php include("./template/header.php"); ?>   
    <form id="form" action="./index.php" method="GET">
	  <div class="page-header">
	    <h2>用户注册</h2>
	  </div>
	  <input id="untxt" type="text" name="un" class="form-control" placeholder="用户名" required="" autofocus="" />
	  <br />
	  <input type="text" id="pwtxt" name="pw" class="form-control" placeholder="密码" required="" />
	  <br />
	  <input type="hidden" name="action" value="reg" />
	  <input type="submit" id="regbtn" value="登录" class="btn btn-lg btn-default btn-block"/>
	</form>
<?php include("./template/footer.php"); ?> 