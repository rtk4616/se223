<?php if(!defined("IN_USE")) exit(); ?>
<?php include("./template/header.php"); ?>
<?php if(!defined("IS_LOGIN")) { ?>
	<div class="panel panel-default panel-body">
      您还没有<a href="./login.php">登录</a>或<a href="./sign.php">注册</a>。
    </div>
<?php } else { ?>
    <p class="lead">
    Welcome back, <?php echo $param["un"]; ?>.
    </p>
<?php } ?>
<?php include("./template/footer.php"); ?>