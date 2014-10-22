$(function(){

  $("#lgnbtn, #regbtn").click(function()
  {
    var untxt = $("#untxt").val();
	var pwtxt = $("#pwtxt").val();
	if(untxt == "" || pwtxt == "")
	{
	  alert("请输入用户名和密码！");
	  event.preventDefault();
	  return;
	}
	if(pwtxt.length < 6 || pwtxt.length > 16)
	{
	  alert("密码应为6~16位！");
	  event.preventDefault();
	  return;
	}
  });
  
  $("#rfbtn").click(function()
  {
    var cbfunc = function(res)
	{
	  var json = eval("(" + res + ")");
	  if(json.errno != "0")
	  {
	    alert("刷新失败！");
		return;
	  }
	  $("#booktb").empty();
	  var row = $("<div class=\"row\"></tr>");
      var c1 = $("<div class=\"col-md-2\">编号</div>");
	  var c2 = $("<div class=\"col-md-4\">名称</div>");
	  row.append(c1);
	  row.append(c2);
	  $("#booktb").append(row);
	  for(var i = 0; i < json.data.length; i++)
	  {
	    var row = $("<div class=\"row\"></tr>");
		var c1 = $("<div class=\"col-md-2 b-id\">" + json.data[i].id + "</div>");
		var c2 = $("<div class=\"col-md-4 b-name\">" + json.data[i].name + "</div>");
		row.append(c1);
		row.append(c2);
		$("#booktb").append(row);
	  }
	};
	$.get("./book.php", cbfunc);
  });
  
  $("#addbtn").click(function()
  {
    var name = prompt("请输入书名：");
	if(name == null || name == "")
	  return;
    var cbfunc = function(res)
	{
	  var json = eval("(" + res + ")");
	  if(json.errno == 0)
	  {
	    var row = $("<div class=\"row\"></tr>");
		var c1 = $("<div class=\"col-md-2 b-id\">" + json.id + "</div>");
		var c2 = $("<div class=\"col-md-4 b-name\">" + name + "</div>");
		row.append(c1);
		row.append(c2);
		$("#booktb").append(row);
	    alert("添加成功！");
	  }
	  else
	    alert("添加失败！" + json.errmsg);
	};
	$.get("./book.php?action=add&name=" + name, cbfunc);
  });
  
  $("#rmbtn").click(function()
  {
    var name = prompt("请输入书名：");
	if(name == null || name == "")
	  return;
    var cbfunc = function(res)
	{
	  var json = eval("(" + res + ")");
	  if(json.errno == 0)
	  {
	    var cons = $("div.b-id");
		for(var i = 0; i < cons.length; i++)
		{
		  if(cons[i].textContent == json.id)
		  {
		    cons[i].parentNode.remove();
		    break;
		  }
		}
	    alert("删除成功！");
	  }
	  else
	    alert("删除失败！" + json.errmsg);
	};
	$.get("./book.php?action=rm&name=" + name, cbfunc);
  });
  
  $("#fixbtn").click(function()
  {
    var oldname = prompt("请输入书名：");
	if(oldname == null || oldname == "")
	  return;
	var newname = prompt("请输入新的书名：");
	if(newname == null || newname == "")
	  return;
    var cbfunc = function(res)
	{
	  var json = eval("(" + res + ")");
	  if(json.errno == 0)
	  {
	    var cons = $("div.b-id");
		for(var i = 0; i < cons.length; i++)
		{
		  if(cons[i].textContent == json.id)
		  {
		    $("div.b-name")[i].textContent = newname;
		    break;
		  }
		}
	    alert("修改成功！");
	  }
	  else
	    alert("修改失败！" + json.errmsg);
	};
	$.get("./book.php?action=fix&oldname=" + oldname + 
	      "&newname=" + newname, cbfunc);
  });
});

