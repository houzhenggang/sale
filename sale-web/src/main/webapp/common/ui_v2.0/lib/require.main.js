/**
 * @summary 	requirejs main函数
 */
requirejs.config( {
	baseUrl: "/as-web/common/ui_v2.0/lib",	//baseUrl: "http://localhost:8080/gui/plugins/ui_v1.0/lib",
	paths: {
		/*由于jquery、easyui每次都要引入，所以他们将不用requirejs进行管理*/
		"colorPicker": "colorPicker/ui.colorPicker"		//颜色选择器
	},
	/*shim：依赖关系*/
	shim : {
	},
	//	paths: {	//当前面的路径没有成功载入时可接着使用后面的路径。
	//		jquery: [
	//			'http://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min',
	//			'lib/jquery'
	//		]
	//	}	//当google cdn上的jquery.min.js没有获取时（假如google宕机），可以使用本地的lib/jquery.js。
	waitSeconds : 10
});
