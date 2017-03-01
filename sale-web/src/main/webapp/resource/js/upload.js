/*公用文件上传js*/

/**
 * 上传文件
 */
function uploadForm(){
	var formData = new FormData($( "#upload_form" )[0]);  
	 $.ajax({
			url:'../upload/uploadFile.json',
		    type:"post",
		    data : formData,
		    async: false,  
		    cache: false,  
		    contentType: false,  
		    processData: false, 
		    error: function (XMLHttpRequest, textStatus, errorThrown) {//请求失败处理函数
		    	msg.bottomRight("错误提示", errorThrown);
			},
		    success:function(data){
		    	fileView(data);
		    }
		});
}

function fileView(data){
	if(data.success){
		$("#filePath").val(data.uploadFilePath);
		$("#pPath").html(data.fileName);
		var imgPath = "<img id='imgPath' width='50px' height='50px' src='getImg.do?imgPath="+data.uploadFilePath+"'/>";
		$("#filePath").parent().append(imgPath);
	}else{
		msg.bottomRight("错误提示", data.errorMess);
	}
}

$(function(){
	var file_size = 0;
	$("#clientFile").change(function(){
		file_size = this.files[0].size;
        if(file_size>=5000000){
        	msg.bottomRight("错误提示", "文件大小不能大于5M");;
        }else{
        	uploadbutton.click();
        }
	});
	
});
/**
 * 点击文件上传按钮
 */
function fileUploadClick(isCancel){
	if(isCancel){
		cancelFileTable();
	}
	$("#clientFile").click();
}

/**
 * 清除历史上传表单内容
 */
function cancelFileTable(){
	$("#pPath").html("");
	$("#filePath").val("");
	var imgPath = $("#imgPath");
	if(imgPath!='' ){
		$("#imgPath").remove();
	}
}
