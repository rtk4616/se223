$(function()
{
	var rmbtn_cb = function()
	{
		if(!confirm('真的要删除吗？'))
			return;
		var row = $(this).parent().parent();
		var isbn = $(row.children()[0]).text();
		$.get("./ajax.action?action=rmcart&isbn=" + isbn, function(res)
		{
			var json = eval("(" + res + ")");
			if(json.errno == 0)
				row.remove();
			else
				alert("删除失败！" + json.errmsg);
		});
	};
	
	var fixbtn_cb = function()
	{
		var num = prompt("请输入数量：", "");
		if(num == null || num == "")
			return;
		var row = $(this).parent().parent();
		var isbn = $(row.children()[0]).text();
		$.get("./ajax.action?action=fixcart&isbn=" + isbn + 
				  "&num=" + num, function(res)
		{
			var json = eval("(" + res + ")");
			if(json.errno == 0)
			{
				$(row.children()[2]).text(num);
				alert("修改成功！");
			}
			else
				alert("修改失败！" + json.errmsg);
		});
	};
	
	$(".rmbtn").click(rmbtn_cb);
	$(".fixbtn").click(fixbtn_cb);
	
	$("#clearbtn").click(function()
	{
		if(!confirm("确定要删除吗？"))
			return;
		$.get("./ajax.action?action=clearcart", function(res)
		{
			var json = eval("(" + res + ")");
			if(json.errno != "0")
				alert("删除失败！");
			else
			    $(".cart-item").remove();
		});
	});
	
	$("#orderbtn").click(function()
	{
		$.get("./ajax.action?action=addorder", function(res)
		{
			var json = eval("(" + res + ")");
			if(json.errno != "0")
				alert("下单失败！");
			else
			{
				$(".cart-item").remove();
				alert("下单成功！");
			}
		});
	});

});