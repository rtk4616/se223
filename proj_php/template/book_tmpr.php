<?php if(!defined("IN_USE")) exit(); ?>
<?php include("./template/header.php"); ?>

  <div class="page-header">
    <h1>图书列表</h1>
  </div>

  <div class="panel panel-primary panel-body">
    <div id="booktb"> 
	  <div class="row">
	    <div class="col-md-2">编号</div>
	    <div class="col-md-4">名称</div>
	  </div>
	  
<?php
  if(!$param["success"])
    echo $param["msg"];
  else
  {
    foreach($param["books"] as $book)
    {
      echo "<div class=\"row\">\n";
      echo "<div class=\"col-md-2 b-id\">$book[0]</div>\n" .
	       "<div class=\"col-md-4 b-name\">$book[1]</div>\n";
	  echo "</div>\n";
    }
  }
?>
	</div>

    <br />
    <input type="button" id="rfbtn" value="刷新" class="btn btn-lg btn-primary btn-block book-btns"/>
<?php if(defined("IS_ADMIN")) { ?>
    <input type="button" id="addbtn" value="添加" class="btn btn-lg btn-primary btn-block book-btns"/>
    <input type="button" id="rmbtn" value="删除" class="btn btn-lg btn-primary btn-block book-btns"/>
    <input type="button" id="fixbtn" value="修改" class="btn btn-lg btn-primary btn-block book-btns"/>
<?php } ?>
  </div>
  
<?php include("./template/footer.php"); ?>