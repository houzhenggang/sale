/*$(function() {
	var $account = $("#account");
	//浏览器缓存用户名时，此时用户名有效
	if(mytrim($account.val()) && $account.val()!="用户名"){
		$account.addClass("login_name_io");
	}
	//用户名获得焦点事件
	$account.on("focusin", function(event){
		$account.removeClass("login_name_io"); //移除有效样式
		$account.addClass("login_name_if"); //添加焦点样式
		if($account.val() == "用户名"){
			//当用户名="用户名"时，值为空
			$account.val("");
		}
	//用户名失去焦点事件
	}).on("focusout", function(event){
		$account.removeClass("login_name_if");//移除焦点样式
		var account = $account.val();
		if(!account || !(mytrim(account)) || mytrim(account)=="用户名"){
			//用户名为空，移除有效样式
			$account.removeClass("login_name_io");//
			$account.val("用户名");
		} else {
			//用户名不为空，添加有效样式
			$account.addClass("login_name_io");
		}
		
	});
	
	//密码框
	var $pwd = $("#password");
	var $pwdtext = $("#passwordtext");
	$pwd.on("focusin", function(event){
		$pwd.addClass("login_pwd_rs").addClass("login_pwd_if");
		$pwdtext.hide();
	}).on("focusout", function(event){
		$pwd.removeClass("login_pwd_if");
		var pwd = $pwd.val();
		if(!pwd || !(mytrim(pwd))){
			$pwd.removeClass("login_pwd_rs");
			$pwdtext.show();
		}else{
			$pwd.addClass("login_pwd_io");
		}
	});
	//提交按钮
	var $submit = $("#login_submit"); 
	$submit.on("mouseover", function(){
		var account = $account.val();
		var pwd = $pwd.val();
		if((mytrim(account) && mytrim(account)!="用户名") && mytrim(pwd)){
			$submit.addClass("login_btn_h");
		}
	}).on("mouseout", function(){
		$submit.removeClass("login_btn_h");
		//submitBtnHandle($account.val(), $pwd.val(), $submit);
	});
	
	$account.on("keyup", function(event){
		//submitBtnHandle($account.val(), $pwd.val(), $submit);
		if(event.asich==13){
			login();
		}
	});
	$pwd.on("keyup", function(event){
		//submitBtnHandle($account.val(), $pwd.val(), $submit);
		if(event.asich==13){
			login();
		}
	});
	
	$submit.on("click", function(){
		login();
	});
	
});*/
var loginFlag = true;
function login(){
	if(!loginFlag){
		return;
	}
	var account = $("#account").val();
	if(!mytrim(account) || mytrim(account)=="用户名"){
		loginMsg("请输入用户名！");
		return;
	}
	var pwd = $("#password").val();
	if(!mytrim(pwd)){
		loginMsg("请输入密码！");
		return;
	}
	// loginBtnController(false);
	var params = {};
	params.account = account;
	params.password = pwd;
	params.sysId = $("#sysId").val();
	$.ajax({
		"url" : "login.json",
		"data" : params,
		"dataType" : "json",
		"type" : "post",
		"cache" : false,
		"success" : function(data) { // 返回需要修改的数据信息
			
			var operateResult = data.success;
			if(operateResult){//登录成功.直接进入系统
				if(data.isApp){
					// window.location.href="moving/inspectionList.htm";
					alert(111);
					window.location.href="index.htm?r="+Math.random();
				}else{
					window.location.href="index.htm?r="+Math.random();
				}
				
			}else{
				var errorCode = data.ErrorCode;
				if(errorCode==2004){
					loginMsg(data.ErrorMessage);
				}else if(errorCode==2005){//重复登录的情况
					loginMsg(data.ErrorMessage);
					window.location.href="index.htm";
				}else{
					loginMsg("登录失败!"+data.ErrorMessage);
				}
				// loginBtnController(true);
			}
		},
		"error" : function() {
			loginMsg("登录失败！提交登录请求发生异常。");
			// loginBtnController(true);
		}
	});
}
function loginBtnController(flag){
	if(flag){
		loginFlag = true;//开启登录，可登录
		$("#login_submit").removeClass("login_btn_dis");
	} else {
		loginFlag = false;//关闭登录，防止重复登录
		$("#login_submit").addClass("login_btn_dis");	
	}
}

function loginMsg(msg){
	/*$("#login_msg").html("温馨提示："+msg).stop().slideDown(300);
	setTimeout(function(){
		$("#login_msg").slideUp(500);
	}, 5000);*/
	notify(msg, 'danger');
}

/*去左空格*/
function ltrim(s){
	//s.replace( /^\s*/, "")
	//如果是去掉半角和全角空格就把 \s 替换成   [" "|"　"] 就变成
	//s.replace( /^[" "|" "]*/, "");
	return s.replace( /^\s*/, "").replace( /^[" "|"　"]*/, "");
}
/*去右空格*/
function rtrim(s){
   	return s.replace( /\s*$/, "").replace( /[" "|"　"]*$/, "");
}
/**
 * 去空格函数
 */
function mytrim(str){
	if(!str){
		return "";
	}
	return rtrim(ltrim(str));
}

///**
// * 提交登录请求
// */
//function submitLoginInfo(){
//	
//	$("#loginFm").form("jsubmit", {
//	    "url": "login.json",
//	    "method": "post",//默认为post
//	    "validate": true,//默认为true
//	    "success": function(data){
//	    	
//	    	   var operateResult = data.success;
//	   		
//			   if(operateResult){//登录成功.直接进入系统
//				   window.location.href="index.htm";
//			   }else{
//				   var errorCode = data.ErrorCode;
//				   if(errorCode==2004){
//					   $.messager.alert("提示", data.ErrorMessage);
//				   }else if(errorCode==2005){//重复登录的情况
//					   $.messager.alert("提示",data.ErrorMessage);
//					   window.location.href="index.htm";
//				   }else{
//					   $.messager.alert("提示","登录失败!"+data.ErrorMessage);
//				   }
//			   }
//	    },
//	    "error": function(msg){
//	    	  msg.bottomRight("登录失败!提交登录请求发生异常.");
//	    }
//	});
//
//}
//
//
///**
// * 重置录入框
// */
//function resetLoginForm(){
//	document.getElementById('loginFm').reset();
//}