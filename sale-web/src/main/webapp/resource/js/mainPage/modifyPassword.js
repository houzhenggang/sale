function modifyPassword(){
	$('#fm').form('submit', {
		url :"../mainPage/modifyPassword.json",
		data:{password:$[password]},
		onSubmit : function() {
			return $(this).form('validate');
		},
		success : function(result) {
			if (result) {
				msg.bottomRight("提示信息", "操作成功");
				$('#dlg').dialog('close'); // close the dialog
			} else {
				msg.bottomRight("失败信息", "操作失败");
			}
		}
	});
}


$.extend($.fn.validatebox.defaults.rules, {  
    /*必须和某个字段相等*/
    equalTo: {
        validator:function(value,param){
            return $(param[0]).val() == value;
        },
        message:'字段不匹配'
    }
           
});