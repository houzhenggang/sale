/**
 * @summary 	requirejs main函数
 */
requirejs.config( {
	baseUrl: "/tms-web/common/ui_v1.0/lib",	//baseUrl: "http://localhost:8080/gui/plugins/ui_v1.0/lib",
	paths: {
		"jquery": "jquery-1.7.2.min", 					//JQ jquery-1.7.2.min/jquery-1.9.1.min/jquery-1.11.0.min
		"common": "ui.common", 							//UI插件全局通用JS 	(依赖引入了jquery)
		"easyui_base": "easyui/jquery.easyui.min",		//easyui原始库		(依赖引入了jquery)
		"easyui_lang": "easyui/locale/easyui-lang-zh_CN",//easyui语言		(依赖引入了jquery、easyui_base)
		"easyui": "easyui/extend/easyui.extend",		//easyui扩展	库		(依赖引入了jquery、common、easyui_base)
		"colorPicker": "colorPicker/ui.colorPicker"		//颜色选择器			(依赖引入了jquery)
	},
	/*shim：依赖关系*/
	shim : {
		"common":{ //common也必须在上面定义
			"deps": ["jquery"], //deps数组，表明该模块的依赖性
			"exports": "common" //输出的变量名
		},
		"easyui_base": {
			"deps": ["jquery"]
		},
		"easyui_lang": {
			"deps": ["easyui_base"]
		},
		"easyui": {
			"deps": ["common", "easyui_lang"]
		},
		"colorPicker": {
			"deps": ["colorPicker"]
		}
	},
	//	paths: {	//当前面的路径没有成功载入时可接着使用后面的路径。
	//		jquery: [
	//			'http://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min',
	//			'lib/jquery'
	//		]
	//	}	//当google cdn上的jquery.min.js没有获取时（假如google宕机），可以使用本地的lib/jquery.js。
	waitSeconds : 10
});
