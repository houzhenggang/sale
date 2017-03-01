$(window).load(function () {

    // 判断$('#pagination')是否存在page方法，只有存在时才调用 added by yipan 2016年12月30日 14:55:49
    if ('function' == typeof $('#pagination').page) {
        $("#pagination").page("form1");
    }

    $(".dateInput").click(function(){
        laydate({istime:true,istoday:false, format:'YYYY-MM-DD'});
    });
    /*if (!$('.login-content')[0]) {
     notify('Welcome back Mallinda Hollaway', 'inverse');
     }*/
});

function SetWinHeight(obj)
{
    var doc;
    if(obj.contentDocument)
    {
        doc = obj.contentDocument.body;
    }else if(obj.Document)
    {
        doc = obj.Document.body;
    }
    /*var s = "";
     for(var p in doc)
     {
     if(p.indexOf("Height")>0)
     {
     s += p +"=" + doc[p] + "\n";
     }
     }
     alert(s);*/
    var h =  Math.max(doc.scrollHeight,doc.offsetHeight,doc.clientHeight );
    obj.style.height =  h+ "px";
}

/**
 * Notifications
 * @param message
 * @param type
 */
function notify(message, type) {
    $.growl({
        message: message
    }, {
        type: type,
        allow_dismiss: false,
        label: 'Cancel',
        className: 'btn-xs btn-inverse',
        placement: {
            from: 'top',
            align: 'right'
        },
        delay: 2500,
        animate: {
            enter: 'animated fadeIn',
            exit: 'animated fadeOut'
        },
        offset: {
            x: 20,
            y: 85
        }
    });
}

/**
 * 弹出信息 相当于alert
 * 调用方式 return showMsg('123');
 */
function showMsg(msg) {
    swal(msg);
}

/**
 * A title with a text under
 * @param title
 * @param msg
 */
function showMsg(title, msg) {
    swal(title, msg);
}

/**
 * Success Message
 * @param title
 * @param msg
 * @param type
 */
function showMsg(title, msg, type) {
    swal(title, msg, type); // type default is success
}

/**
 * 询问信息 相当于 confirm -- Parameter
 * 调用方式 return confirm('确定要删除?',function(){alert(1)},function(){alert(2)});
 * @param title 标题
 * @param msg 消息内容
 * @param yes 确定回调
 * @param no  取消回调
 */
function confMsg(title, msg, yes, no) {
    swal({
        title: title,
        text: msg,
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        closeOnConfirm: true,
        closeOnCancel: true
    }, function (isConfirm) {
        if (isConfirm) {
            if(yes){
                yes();
            }
            // swal("OK!", "SUCCESS.", "success");
        } else {
            if(no){
                no();
            }
            // swal("CANCEL!", "ERROR.", "error");
        }
    });
}

/**
 * 显示提示信息和返回方法(类型自定义，无取消按钮)
 * @param title
 * @param content
 * @param type
 * @param callBack
 */
function showMsgAndCallback(title, content, type, callBack){
	swal({   
        title: title,   
        text: content,   
        type: type,   
        showCancelButton: false,   
        confirmButtonColor: "#DD6B55",   
        confirmButtonText: "确定",  
        closeOnConfirm: true
    }, function(isConfirm){
    	if(isConfirm){
    		callBack();
    	}
    });
}

/**
 * 显示提示信息和返回方法(默认类型warning)
 * @param title
 * @param content
 * @param callBack
 */
function confirmMsg(title, content, callBack){
	swal({   
        title: title,   
        text: content,   
        type: "warning",   
        showCancelButton: true,   
        confirmButtonColor: "#DD6B55",   
        confirmButtonText: "确定",  
        cancelButtonText: "取消",
        closeOnConfirm: false
    }, function(isConfirm){
    	if(isConfirm){
    		callBack();
    	}
    });
}

//Custom Image
/*$('#sa-image').click(function () {
    swal({
        title: "Sweet!",
        text: "Here's a custom image.",
        imageUrl: "img/thumbs-up.png"
    });
});*/

/**
 * Auto Close Timer
 * @param title
 * @param msg
 * @param timer
 */
function showMsgTimer(title, msg, timer) {
    swal({
        title: title,
        text: msg,
        timer: timer,
        showConfirmButton: false
    });
}

/**
 * 显示提示信息，且timer毫秒之后自动刷新
 * @param title
 * @param msg
 * @param timer
 */
function showMsgAndReload(title, msg, timer) {
    showMsg(title, msg);
    setTimeout('window.location.reload()', timer);
}

/**
 * FORM 表单必填项校验样式
 */
function initValidate(form){
	$(form).find('.has-error').each(function(){
		$(this).removeClass('has-error');
	});
}

function validate(form){
	initValidate(form);
	var success = true;
	for(var i = 0;i < form.elements.length-1; i++){
		var el = form.elements[i];
		var val = el.value;
		if($(el).attr('required')=='required' || $(el).attr('required')){
			if(typeof(val)=='undefined' || val==''){
				$(el).parent().parent().addClass('has-error');
				success = false;
			} else if(el.checkValidity()==false){
				$(el).parent().parent().addClass('has-error');
				success = false;
			} else {
				$(el).parent().parent().removeClass('has-error');
			}
		}
    }
	return success;
}
