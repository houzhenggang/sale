//初始化LODOP
document.write("<script language=javascript src='/as-web/resource/js/LodopFuncs.js'></script>");
var LODOP; //声明为全局变量 
/**
 * 打印模板-241*279复写纸
 * @param templateName
 * @param intOrient
 */
function getTemplateDefault(templateName,intOrient) {	
	//设置打印参数
	getTemplate(templateName,intOrient,2410,2790,"CreateCustomPage");
};

/**
 * 打印模板-A4纸张
 * @param templateName
 * @param intOrient
 */
function getTemplateA4(templateName,intOrient) {	
	//设置打印参数
	getTemplate(templateName,intOrient,0,0,"A4");
};

/**
 * 查询模板打印
 * @param templateName 模板名称
 * @param intOrient	打印方向；1：纵向；2：横向
 * @param width	打印纸宽度
 * @param height	打印纸高度
 * @param printName	打印纸名称
 */
function getTemplate(templateName,intOrient,width,height,printName){
	$.ajax({
		url:'../template/getTemplate.json',
		data:{templateName:templateName},
		cache:false,
	    type:"post",
	    error: function () {//请求失败处理函数
	    	msg.bottomRight("错误提示", '请求失败');
		},
		
	    dataType:"json",
	    success:function(data){
//	    	printArea(data.content);
//	    	window.open(data.);
	    	prn1_preview(data.content,intOrient,width,height,printName);
	    }
	});
}
/**
 * 传入页面打印纸纸张大小打印预览
 * @param content 打印内容
 * @param intOrient	打印方向；1：纵向；2：横向
 * @param width	打印纸宽度
 * @param height	打印纸高度
 * @param printName	打印纸名称
 */
function prn1_preview(content,intOrient,width,height,printName) {	
	//设置打印参数
	CreateOneFormPage(content,intOrient,width,height,printName);	
	//打印预览
	LODOP.PREVIEW();	
};

/**
 * 传入页面内容打印复写纸张241*279
 * @param content 打印内容
 * @param intOrient	打印方向；1：纵向；2：横向
 */
function previewDefault(content,intOrient) {	
	//设置打印参数
	CreateOneFormPage(content,intOrient,2410,2790,'CreateCustomPage');	
	//打印预览
	LODOP.PREVIEW();	
};
/**
 * 传入页面内容打印A4纸张
 * @param content 打印内容
 * @param intOrient	打印方向；1：纵向；2：横向
 */
function previewA4(content,intOrient) {	
	//设置打印参数
	CreateOneFormPage(content,intOrient,0,0,"A4");	
	//打印预览
	LODOP.PREVIEW();	
};
/**
 * 设置打印参数
 * @param content 打印内容
 * @param intOrient	打印方向；1：纵向；2：横向
 * @param width	打印纸宽度
 * @param height	打印纸高度
 * @param printName	打印纸名称
 */
function CreateOneFormPage(content,intOrient,width,height,printName){
	LODOP=getLodop();  
	LODOP.SET_PRINT_PAGESIZE(intOrient,width,height,printName);	
	if(width==0 && height==0){
		LODOP.ADD_PRINT_HTM(0,0,'100%','100%',content);
	}else{
		LODOP.ADD_PRINT_HTM(0,0,width,height,content);
	}
	
};	 	
function getSelectedPrintIndex(){
	if (document.getElementById("Radio2").checked) 
	return document.getElementById("PrinterList").value;
	else return -1; 		
};