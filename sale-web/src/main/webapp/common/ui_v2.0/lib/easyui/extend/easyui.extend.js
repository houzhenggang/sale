/**
 * @summary easyui扩展集合
 * @namespace easyui.extend
 * @description easyui相关扩展插件集合
 * 
 * @copyright Copyright 2014 onlyang, all rights reserved.
 */
(function($, window, document, undefined) {
	//全局的ajax访问，处理ajax清求时sesion超时 
	$.ajaxSetup({ 
	contentType : "application/x-www-form-urlencoded;charset=utf-8", 
	complete : function(XMLHttpRequest, textStatus) { 
	var sessionstatus = XMLHttpRequest.getResponseHeader("sessionstatus"); // 通过XMLHttpRequest取得响应头，sessionstatus， 
	var loginUrl = XMLHttpRequest.getResponseHeader("loginUrl");
		if (sessionstatus == "timeout") { 
			// 如果超时就处理 ，指定要跳转的页面 
			window.location.replace(loginUrl); 
		} 
	} 
	}); 
	/**
	 * @class MyDatagrid
	 * @summary datagrid插件扩展
	 * @param {object}
	 *            Options are defined by {@link MyDatagrid.defaults}
	 * @return easyui的原始datagrid对象
	 * @requires jQuery 1.7+ , easyui-1.3.6
	 * 
	 * @description 用于初始化表格，覆盖默认参数、简化使用方式、扩展功能。
	 * @date 2014-6-18
	 * @author onlyang
	 * @contact oyloong@163.com
	 * 
	 * @example var dg = $("#example").myDatagrid({...});
	 */
	var MyDatagrid = function(oInit) {
		/**
		 * @summary 数据处理器
		 * @param {object} oGrid datagrid
		 * @param {object} dataHandle 处理器配置信息
		 * @param {boolean} bDetail 查看数据开关
		 * * @param {object} rowData 双击事件是双击选中的行数据
		 */
		function _dataHandle(oGrid, handler, bDetail, rowData) {
			var selectRow = rowData;
			var bMany = false;
			if(!selectRow){
				var dataGrid = $(oGrid);
				var selectRows = dataGrid.datagrid("getSelections");// var selectRow = $(oGrid).datagrid("getSelected");
				if (!selectRows || selectRows.length <= 0) {
					$.messager.alert("提示", "&nbsp;&nbsp;请选择一行数据！");
					return;
				}
				selectRow = selectRows[0]; // 默认对选中的第一条数据进行操作
				if (selectRows.length > 1) {
					bMany = true; // 用户选中多条数据
					var firstIndex = dataGrid.datagrid("getRowIndex", selectRow);
					dataGrid.datagrid("unselectAll");
					dataGrid.datagrid("selectRow", firstIndex);
				}
			}
			/* 查询数据 */
			if (handler.queryUrl) { // URL存在，则进行后台查询
				$.ajax({
					"url" : handler.queryUrl,
					"data" : selectRow,
					"dataType" : "json",
					"type" : "post",
					"cache" : false,
					"success" : function(data) { // 返回需要修改的数据信息
						if (data) {
							_dataFormHandle(oGrid, handler, data, bDetail, bMany);
						}
					},
					"error" : function() {
						$.messager.alert("提示", "AJAX请求失败，请检查URL：" + handler.queryUrl);
					}
				});
			} else { // URL不存在，直接用选中行数据填充表单
				_dataFormHandle(oGrid, handler, selectRow, bDetail, bMany);
			}
		}
		/**
		 * @summary 数据处理器
		 * @param {object} oGrid datagrid
		 * @param {object} dataHandle 处理器配置信息
		 * @param {object} formData 绑定到表单的数据
		 * @param {boolean} bDetail 查看数据开关
		 * @param {boolean} bMany 多条数据开关
		 */
		function _dataFormHandle(oGrid, handler, formData, bDetail, bMany) {
			var dataForm = $("#" + handler.form.id);
			if (!dataForm || dataForm.length <= 0) {
				$.messager.alert("提示", "ID为：" + handler.form.id + " 的表单不存在！");
				return;
			}
			dataForm.form("reset"); // 清空表单
			if (formData) {
				dataForm.form("load", formData); // 修改或查询，绑定数据到编辑表单
			}
			var dataDialog = $("#" + handler.dialog.id);
			var buttons = [];
			if (!bDetail) {
				/* 编辑或新增的时候的提交按钮 */
				buttons.push({
					"text" : "提交",
					"iconCls" : "icon-ok",
					"handler" : function() {
						var bValidate = true;
						if(handler.form.validate){
							bValidate = $(dataForm).form("validate");
						}else{
							bValidate = true;
						}
						if(bValidate){
							// 异步提交
							$.ajax({
								"url" : handler.form.submitUrl ? handler.form.submitUrl: dataForm.attr("action"),
								"data" : dataForm.form("formToJson"),
								"dataType" : "json",
								"type" : "post",
								"cache" : false,
								"success" : function(result) { // 返回需要修改的数据信息
									if(result && (result.state == true || result.state == "true")){
										$.messager.show({
											"title" : "操作成功",
											"msg" : result.msg
										});
										dataDialog.dialog("close"); // 关闭DIALOG
										dataForm.form("clear"); // 清空表单
										$(oGrid).datagrid("reload");// 重绘表格
										if (handler.form.successHandle) {
											handler.form.successHandle.call(dataForm,json);
										}
									} else {
										$.messager.alert("操作失败", result.msg);
									}
								},
								"error" : function(XMLHttpRequest, textStatus, errorThrown) {
									$.messager.alert("提示", XMLHttpRequest + textStatus+ errorThrown);
								}
							});
						}
					}
				});
			}
			//else {
			if(handler.handler){ //增删改回调方法，数据绑定表单之后，进行的操作。
				handler.handler.call(dataForm, formData);
			}
			//}
			/* 取消按钮 */
			buttons.push({
				"text" : "取消",
				"iconCls" : 'icon-cancel',
				"handler" : function() {
					dataDialog.dialog("close");
				}
			});
//			var wh = $(window).height();
//			var dh = dataDialog.height();
//			dh = dh>wh?wh:dh;
			/* 显示（修改、添加、查询）窗口 */
			dataDialog.show().dialog({
				"title" : handler.title,
				//"width": handler.dialog.width,
				//"height": dh,
				"closed" : false,
				"modal" : true,
				"buttons" : buttons
			});
			btnExt();
			/* 当用户选中多条数据时，提示用户将默认对其选择的第一条数据进行操作 */
			if (bMany) {
				$.messager.show({
					"title" : "温馨提示",
					"msg" : "<span style='color:red;'>*</span>由于您选择了多条数据，系统将默认对您选择的第一条数据进行操作。",
					"style" : {
						"right" : "",
						"top" : document.body.scrollTop + document.documentElement.scrollTop,
						"bottom" : ""
					},
					"timeout" : 2000
				});
			}
		}
		/**
		 * @summary 删除数据处理器
		 * @param {array} selectRows 选中行数据
		 * @param {object} removeHandle 删除处理器配置信息
		 * @param {object} oGrid datagrid
		 */
		function _removeHandle(selectRows, removeHandle, oGrid) {
			var removeObjIds = [];
			for (var i = 0; i < selectRows.length; i++) {
				removeObjIds.push(selectRows[i][removeHandle.idField]);
			}
			var params = {}; // 删除时的ID组参数
			params[removeHandle.idParams] = removeObjIds;
			$.ajax({
				"url" : removeHandle.removeUrl,
				"data" : params,
				"dataType" : "json",
				"type" : "post",
				"cache" : false,
				"success" : function(result) { // 返回需要修改的数据信息
					if(result && (result.state == true || result.state == "true")){
						$.messager.show({
							"title" : "操作成功",
							"msg" : result.msg
						});
						$(oGrid).datagrid("reload");// 重绘表格
						if(removeHandle.handler){
							removeHandle.handler.call(oGrid);
						}
					} else {
						$.messager.alert("操作失败", result.msg);
					}
				},
				"error" : function() {
					$.messager.alert("提示", "AJAX请求失败，请检查removeUrl："
							+ removeHandle.removeUrl);
				}
			});
		}
		/**
		 * @summary 参数默认匹配
		 * @param {string} sModel 数据模型
		 * @param {string} sType 类型
		 * @param {object} handler 处理器
		 * @return {object} handler 处理器
		 */
		function _defaultParams(sModel, sType, handler, sSystem, bRemove){
			if(!bRemove){
				if (!handler.form.id) {
					handler.form.id = sModel + "_"+ sType +"_form";
				}
				if (!handler.dialog.id) {
					handler.dialog.id = sModel + "_"+ sType +"_dialog";
				}
			}
			if(!handler.permission){
				handler.permission = sSystem + ":" + sModel + ":"+ sType;
			}
			return handler;
		}
		function _clearHandle(_table, clearHandler){
			$(_table).datagrid('loadData', {total: 0, rows: []});//清空下方DateGrid 
			if(clearHandler.handler){
				clearHandler.handler.call();
			}
		}
		function _delHandle(_table, delHandler){
			var $tb  = $(_table);
			var rows = $tb.datagrid("getSelections");
			if(rows && rows.length>0){
				$.each(rows,function(i, n){
					var ind = $tb.datagrid("getRowIndex", n);
					$tb.datagrid("deleteRow", ind);
				});
				if(delHandler.handler){
					delHandler.handler.call(_table, rows);
				}
			} else {
				$.messager.alert("提示", "请先选择所要删除的数据。");
			}
		}
		function _subHandle(_table, oInit){
			var $tb  = $(_table);
			var rows = $tb.datagrid("getSelections");
			if(!rows || rows.length<=0){
				$.messager.alert("提示", "&nbsp;&nbsp;请选择一行数据！");
				return;
			}
			var params = {};
			var rowsStr = JSON.stringify(rows);
			params[oInit.subHandler.dataParams] = rowsStr;
			$.ajax({
				"url" : oInit.subHandler.submitUrl,
				"data" : params,
				"dataType" : "json",
				"type" : "post",
				"success" : function(result) { // 返回需要修改的数据信息
					_clearHandle(_table, oInit.clearHandler);// 重绘表格
					if(oInit.subHandler.success){
						oInit.subHandler.success.call(_table, result);
					}
				},
				"error" : function() {
					$.messager.alert("提示", "AJAX请求失败，请检查URL：" + oInit.subHandler.submitUrl);
				}
			});
		}
		
		/*begin*/
		if (!this || this.length <= 0) {
			alert("选择器：\"" + this["selector"] + "\"为空！");
			return;
		}
		return this.each(function() {
			var _this = this;
			oInit = $.extend(true, {}, MyDatagrid.defaults, oInit);// 深度extend
			if (!oInit.singleSelect) { // 非单选，多选自动添加checkbox
				var ckColumns = [ {
					"field" : "ck",
					"checkbox" : true
				} ];
				$.merge(ckColumns, oInit.columns[0]); // 合并两个数组到第一个数组上。
				oInit.columns[0] = ckColumns;
			}
			//按钮权限控制
			var _toolbars = [];
			if(oInit.toolbar){
				$.each(oInit.toolbar, function(ei, en){
					if(permissable(en.permission)){
						_toolbars.push(en);
					}
				});
			} else {
				oInit.toolbar = [];
			}
			oInit.toolbar = _toolbars;
			
			/*动态 清空*/
			if (oInit.clearHandler && oInit.clearHandler.enable) {
				var clearBtn = [ {
					"id" : "myg_btnclear",
					"text" : oInit.clearHandler.title,
					"iconCls" : "icon-remove",
					"handler" : function() {
						_clearHandle(_this, oInit.clearHandler);
					}
				}];
				oInit.toolbar = $.merge(clearBtn, oInit.toolbar); // 合并两个数组到第一个数组上。
			}
			/*动态 删除*/
			if (oInit.delHandler && oInit.delHandler.enable) {
				var delBtn = [ {
					"id" : "myg_btndel",
					"text" : oInit.delHandler.title,
					"iconCls" : "icon-remove",
					"handler" : function() {
						_delHandle(_this, oInit.delHandler);
					}
				} ];
				oInit.toolbar = $.merge(delBtn, oInit.toolbar); // 合并两个数组到第一个数组上。
			}
			/*动态 提交*/
			if (oInit.subHandler && oInit.subHandler.enable) {
				var subBtn = [ {
					"id" : "myg_btndel",
					"text" : oInit.subHandler.title,
					"iconCls" : "icon-remove",
					"handler" : function() {
						_subHandle(_this, oInit);
					}
				} ];
				oInit.toolbar = $.merge(subBtn, oInit.toolbar); // 合并两个数组到第一个数组上。
			}
			
			/* 查看器处理 */
			if (oInit.detailHandler && oInit.detailHandler.enable) {
				oInit.detailHandler = _defaultParams(oInit.model, "detail", oInit.detailHandler, oInit.system, false);
				if(permissable(oInit.detailHandler.permission)){
					var detailBtn = [ {
						"id" : "myg_btndetail",
						"text" : oInit.detailHandler.title,
						"iconCls" : "icon-search",
						"handler" : function() {
							_dataHandle(_this, oInit.detailHandler, true);
						}
					} ];
					oInit.toolbar = $.merge(detailBtn, oInit.toolbar); // 合并两个数组到第一个数组上。
				}
			}
			/* 删除器处理 */
			if (oInit.removeHandler && oInit.removeHandler.enable) {
				oInit.removeHandler = _defaultParams(oInit.model, "remove", oInit.removeHandler, oInit.system,  true);
				if(permissable(oInit.removeHandler.permission)){
					var removeBtn = [{
						"id" : "myg_btnedit",
						"text" : oInit.removeHandler.title,
						"iconCls" : "icon-remove",
						"handler" : function() {
							var selectRows = $(_this).datagrid("getSelections");
							if (!selectRows || selectRows.length <= 0) {
								$.messager.alert("提示", "请选中一行数据。");
								return;
							}
							$.messager.confirm("提示", "确实删除选中的数据吗？", function(r) {
								if (r) {
									_removeHandle(selectRows, oInit.removeHandler, _this);
								}
							});
						}
					}];
					oInit.toolbar = $.merge(removeBtn, oInit.toolbar);
				}
			}
			/* 编辑器处理 */
			if (oInit.editHandler && oInit.editHandler.enable) {
				oInit.editHandler = _defaultParams(oInit.model, "edit", oInit.editHandler, oInit.system,  false);
				if(permissable(oInit.editHandler.permission)){
					var editBtn = [ {
						"id" : "myg_btnedit",
						"text" : oInit.editHandler.title,
						"iconCls" : "icon-edit",
						"handler" : function() {
							_dataHandle(_this, oInit.editHandler);
						}
					} ];
					oInit.toolbar = $.merge(editBtn, oInit.toolbar);
				}
			}
			/* 添加器处理 */
			if (oInit.addHandler && oInit.addHandler.enable) {
				oInit.addHandler = _defaultParams(oInit.model, "add", oInit.addHandler, oInit.system,  false);
				if(permissable(oInit.addHandler.permission)){
					var addBtn = [ {
						"id" : "myg_btnadd",
						"text" : oInit.addHandler.title,
						"iconCls" : "icon-add",
						"handler" : function() {
							_dataFormHandle(_this, oInit.addHandler);
							if(oInit.addHandler.handler){
								oInit.addHandler.handler.call(_this);
							}
						}
					} ];
					oInit.toolbar = $.merge(addBtn, oInit.toolbar);
				}
			}
			/*双击时间处理*/
			if(!oInit.onDblClickRow && oInit.dblClickHandler){
				if(oInit.dblClickHandler == "detailHandler" && oInit.detailHandler.enable){
					oInit.onDblClickRow = function(rowIndex, rowData){
						_dataHandle(_this, oInit.detailHandler, true, rowData);
					};
				} else if (oInit.dblClickHandler == "editHandler" && oInit.editHandler.enable){
					oInit.onDblClickRow = function(rowIndex, rowData){
						_dataHandle(_this, oInit.editHandler, false, rowData);
					};
				}
			}
			/*onLoadSuccess > 隔行变色*/
			var os = oInit.onLoadSuccess;
			var _onLoadSuccess = function(){
				$(_this).parent().parent().find("table.datagrid-btable tr.datagrid-row:odd").addClass("datagrid_tr_odd");
				if(os){
					os.call(_this);
				}
			};
			oInit.onLoadSuccess  = _onLoadSuccess;
			/* 初始化grid */
			var dg = $(_this).datagrid(oInit);
			if (oInit.pagination) {
				dg.datagrid("getPager").pagination({
					// 页数文本框前显示的汉字
					"beforePageText" : oInit.beforePageText,
					"afterPageText" : oInit.afterPageText,
					"displayMsg" : oInit.displayMsg
				});
			}
			return dg;
		});
	};
	/**
	 * MyDatagrid默认参数设置
	 */
	MyDatagrid.defaults = {
		"title" : "",
		"url" : "", /* 数据地址 */
		"method" : "post",
		"pagination" : true,// 分页开关，默认为true
		"iconCls" : "", // title前面的小图标 (icon-edit/icon-save)
		"collapsible" : true,// 是否可折叠的
		"rownumbers" : false,// 行号
		"multiple" : true,
		"multiSort" : false,// 排序
		"singleSelect" : true,// 是否单选
		"remoteSort" : true,
		"fitColumns" : true,// 自动大小
		"fit": true,
		"autoRowHeight" : false,
		"loadMsg" : "努力加载中，请稍后...",

		/* 分页相关配置属性 */
		"pageSize" : 20,// 每页显示的记录条数
		"pageList" : [ 10, 20, 25, 50 ],// 每页记录条数的列表
		"beforePageText" : "第", // /页数文本框前显示的文本
		"afterPageText" : "页    共 <span>{pages}</span> 页",
		"displayMsg" : "当前显示 <span>{from}</span> - <span>{to}</span> 条记录   共 <span>{total}</span> 条记录",
		"buttons" : null, // 分页后面的按钮

		/* easyui扩展 */
		"model": "demo", //当不指定form、dialog的ID，插件会根据该属性来自动匹配页面元素，如修改用户窗口，将自动匹配ID：user_edit_dialog
		"dblClickHandler": null, //双击行时进行的操作(当定义了onDblClickRow时，此参数将失效)
		/**
		 * @summary 编辑数据配置
		 * @type object
		 */
		"editHandler" : {
			"enable" : false,// 修改处理器开关，默认为false
			"title" : "", // 修改按钮文本及修改窗口标题
			"queryUrl" : "", // 获取修改用户信息的URL地址（如果没有指定，则会将选中行的数据填充编辑表单）
			"form" : {
				"id" : "",
				"validate" : true,
				"submitUrl" : "",
				"successHandle" : null
			}, // 修改表单信息 form_user_edit
			"dialog" : {
				"id" : "",
				"width" : 500,
				"height" : 300
			},
			"handler": null
		},
		/**
		 * @summary 新增数据配置
		 * @type object
		 */
		"addHandler" : {
			"enable" : false,// 新增处理器开关，默认为false
			"title" : "", // 新增按钮文本及新增窗口标题
			"form" : {
				"id" : "",
				"validate" : true,
				"submitUrl" : "",
				"successHandle" : null
			},// 新增表单信息
			"dialog" : {
				"id" : "",
				"width" : 500,
				"height" : 300
			},
			"handler": null
		// 新增窗口信息
		},
		/**
		 * @summary 查看数据配置
		 * @type object
		 */
		"detailHandler" : {
			"enable" : false,// 查看处理器开关，默认为false
			"title" : "", // 查看按钮文本及查看窗口标题
			"queryUrl" : "", // 获取用户信息的URL地址（如果没有指定，则会将选中行的数据填充编辑表单）
			"form" : {
				"id" : ""
			},// 查看表单信息
			"dialog" : {
				"id" : "",
				"width" : 500,
				"height" : 300
			},
			"handler": null
		// 查看窗口信息
		},
		/**
		 * @summary 删除数据配置
		 * @type object
		 */
		"removeHandler" : {
			"enable" : false,// 删除处理器开关，默认为false
			"title" : "", // 删除按钮文本
			"removeUrl" : "",// 删除URL
			"idField" : "id", // 主键字段名
			"idParams" : "ids", // 向后台传递的参数名，默认为'ids'。(值为选中数据的id数组)
			"handler": null
		},
		
		/*动态 增删改查配置*/
		"subHandler": {
			"enable": false,
			"title": "提交", 
			"submitUrl": null,
			"dataParams": "rowsData",
			"success": function(result){
				//do something
			}
		},
		"delHandler": {
			"enable": false,
			"title": "删除",
			"handler": function(){
				//do something
			}
		},
		"clearHandler": {
			"enable": false,
			"title": "清空",
			"handler": function(){
				//do something
			}
		}
	};
	/* MyDatagrid aliases */
	$.fn.myDatagrid = MyDatagrid;
	$.fn.MyDatagrid = MyDatagrid;

	/**
	 * @class MyDatagrid
	 * @summary datagrid插件扩展
	 * @param {object}
	 *            Options are defined by {@link MyDatagrid.defaults}
	 * @return easyui的原始datagrid对象
	 * @requires jQuery 1.7+ , easyui-1.3.6
	 * 
	 * @description 用于初始化表格，覆盖默认参数、简化使用方式、扩展功能。
	 * @date 2014-6-18
	 * @author onlyang
	 * @contact oyloong@163.com
	 * 
	 * @example var dg = $("#example").myDatagrid({...});
	 */
	var MyTreegrid = function(oInit) {
		/*begin*/
		if (!this || this.length <= 0) {
			alert("选择器：\"" + this["selector"] + "\"为空！");
			return;
		}
		return this.each(function() {
			var _toolbars = [];
			if(oInit.toolbar){
				$.each(oInit.toolbar, function(ei, en){
					if(permissable(en.permission)){
						_toolbars.push(en);
					}
				});
			} else {
				oInit.toolbar = [];
			}
			oInit.toolbar = _toolbars;
			var tg = $(this).treegrid(oInit);
			return tg;
		});
	};
	/* MyTreegrid aliases */
	$.fn.myTreegrid = MyTreegrid;
	$.fn.MyTreegrid = MyTreegrid;
	
	/**
	 * @class MyDialog
	 * @summary dialog插件扩展
	 * @param {object} Options are defined by {@link MyDialog.defaults}
	 * @return easyui的原始dialog对象
	 * @requires jQuery 1.7+ , easyui-1.3.6
	 * 
	 * @description 添加dialog可以装在一个页面功能(iframe模式)
	 * @date 2014-6-24
	 * @author onlyang
	 * @contact oyloong@163.com
	 * 
	 * @example var dg = $("#example").myDialog({...});
	 */
	var MyDialog = function(oInit) {
		if (!this || this.length <= 0) {
			alert("MyDialog选择器：\"" + this["selector"] + "\"为空！");
			return;
		}
		return this.each(function() {
			var $this = $(this);
			var wh = $(window).height();
			var dh = $this.height();
			var ww = $(window).width();
			var dw = $this.width();
			dh = dh>wh?wh:dh;
			dw = dw>ww?ww:dw;
			oInit.height = dh;
			oInit.width = dw;
			var $dialog = $this.dialog(oInit);
			if (oInit && oInit.url && oInit.url.length > 0) {
				var $iframe = $("<iframe src=\"\" frameborder=\"0\" scrolling=\"auto\" style='border: none; width: 100%; height: 100%;'></iframe>");
				$this.find(".dialog-content").css({
					"overflow" : "hidden"
				}).append($iframe);
				$iframe.attr("src", oInit.url);
			}
			return $dialog;
		});
	};
	MyDialog.defaults = {
		/* new */
		/**
		 * 新增参数，当URL存在时，将构造一个iframe加载目标页面
		 */
		"url" : null
	};
	/* MyDialog aliases */
	$.fn.myDialog = MyDialog;
	$.fn.MyDialog = MyDialog;

	/**
	 * @class MyWindow
	 * @summary window插件扩展类
	 * @param {object}
	 *            Options are defined by {@link MyWindow.defaults}
	 * @return easyui的原始window对象
	 * @requires jQuery 1.7+ , easyui-1.3.6
	 * 
	 * @description 添加window可以装载一个页面功能(iframe模式)
	 * @date 2014-6-24
	 * @author onlyang
	 * @contact oyloong@163.com
	 * 
	 * @example var dg = $("#example").MyWindow({...});
	 */
	var MyWindow = function(oInit) {
		if (!this || this.length <= 0) {
			alert("MyWindow选择器：\"" + this["selector"] + "\"为空！");
			return;
		}
		return this
				.each(function() {
					var $this = $(this);
					var $window = $this.window(oInit);
					if (oInit && oInit.url && oInit.url.length > 0) {
						var $iframe = $("<iframe src=\"\" frameborder=\"0\" scrolling=\"auto\" style='border: none; width: 100%; height: 100%;'></iframe>");
						// $this.find(".window-body").css({
						$this.css({
							"overflow" : "hidden"
						}).append($iframe);
						$iframe.attr("src", oInit.url);
					}
					return $window;
				});
	};
	MyWindow.defaults = {
		/* new */
		/**
		 * 新增参数，当URL存在时，将构造一个iframe加载目标页面
		 */
		"url" : null
	};
	/* extend to jquery */
	$.fn.MyWindow = MyWindow;
	$.fn.myWindow = MyWindow;

	/**
	 * easyui form插件扩展方法(表单数据获取)
	 * 
	 * @description 新增 formToObjArray(对象数组)、formToJson、formToUrl 三种表单数据获取方式
	 * @date 2014-6-24
	 * @author onlyang
	 * @contact oyloong@163.com
	 * 
	 * @example var formData =
	 *          $("#formId").form("formToObjArray");//formToJson/formToUrl
	 */
	$.extend($.fn.form.methods, {
		/**
		 * @summary 将表单元素获取为object数组格式数据
		 * @param {jq}
		 *            form的jquery对象
		 * @return {array} aParams
		 *         [{"name":"p1","value":"v1"},...,{"name":"pn","value":"vn"}]，其中checkbox的值为多个name相同的数组项"
		 */
		"formToObjArray" : function(jq) {
			return $(jq[0]).serializeArray();// 已包含hidden域
		},
		/**
		 * @summary 将表单元素获取为JSON格式数据
		 * @param {jq}
		 *            form的jquery对象
		 * @return {object} oParams {"p1":"v1",...,"pn":"vn","checkbox":
		 *         ["java","c","html"]}，其中checkbox的值多个则为数组
		 */
		"formToJson" : function(jq) {
			var valueJson = {};
			$.each($(jq[0]).serializeArray(), function(i, n) {
				if(n.value){
					if (valueJson[n.name]) { // 值已经存在，则为checkbox，将其值定义为数组
						if ($.isArray(valueJson[n.name])) {
							valueJson[n.name].push(n.value);
						} else {
							var va = [];
							va.push(valueJson[n.name]);
							va.push(n.value);
							valueJson[n.name] = va;
						}
					} else {
						valueJson[n.name] = n.value;
					}
				}
			});
			return valueJson;
		},
		/**
		 * @summary 将表单元素获取为URL格式数据
		 * @param {jq}
		 *            form的jquery对象
		 * @return {string} sParams p1=v1&p2=v2...pn=vn，其中checkbox的值为多个key相同的项"
		 */
		"formToUrl" : function(jq) {
			return $(jq[0]).serialize();// 已包含hidden域
		},
		/**
		 * @summary 表单jquery的ajax方式提交
		 * @param {jq} form的jquery对象
		 * @param {object} params参数
		 * 		url method validate success error
		 @example
		 *		$("#formId").form("jsubmit", {"url": "../user/create.do"});
		 */
		"jsubmit": function(jq, params){
			var $form = $(jq[0]);
			var options = {
				"url": "",
				"method": "post",
				"validate": true,
				"success": null,
				"error": null
			};
			params = $.extend(true, {}, options, params);
			var bValidate = true;
			if(params.validate){//验证
				bValidate = $form.form("validate");
			}else{
				bValidate = true; //不验证
			}
			if(bValidate){
				// 异步提交
				$.ajax({
					"url" : params.url ? params.url: $form.attr("action"),
					"data" : $form.form("formToJson"),
					"dataType" : "json",
					"type" : params.method,
					"success" : function(result) { //返回需要修改的数据信息
						if(params.success){
							params.success.call(jq[0], result);
						}
					},
					"error" : function(result) {
						$.messager.alert("提示", "表单提交失败，请检查URL：" + params.url);
						if(params.error){
							params.error.call(jq[0], result);
						}
					}
				});
			}
		}
	});

	/**
	 * easyui form验证方法扩展
	 * 
	 * @date 2014-7-2
	 * @example <input class="easyui-validatebox" required="true"
	 *          validType="minLength[6]">
	 */
	$.extend($.fn.validatebox.defaults.rules, {
		/**
		 *多重验证：start
		 */
		multiple: {  
			validator : function(value, vtypes) {  
                var returnFlag = true;  
                var opts = $.fn.validatebox.defaults;  
                //alert(vtypes.length);
                //alert(vtypes[0]);
                for (var i = 0; i < vtypes.length; i++) {  
                    var methodinfo = /([a-zA-Z_]+)(.*)/.exec(vtypes[i]);  
                    alert("1:"+methodinfo[1]);
                    alert("2:"+methodinfo[2]);
                    var rule = opts.rules[methodinfo[1]];  
                    if (value && rule) {  
                        var parame = eval(methodinfo[2]);  
                        if (!rule["validator"](value, parame)) {  
                            returnFlag = false;  
                            this.message = rule.message;  
                            break;  
                        }  
                    }  
                }  
                return returnFlag;  
            }  
	    },
		/**
		 *多重验证：end 
		 */
		//空格校验
		checkSpacebar:{
			validator : function(value) {
				//var falg = true;
				var strs= new Array();
				strs = 	value.split("");
				
				for (var i=0;i<strs.length ;i++ ){

						if(strs[i]==(" ")){
							return false;
						}
						break;
					} 

				return true;
				
				},
				message:'不能输入空格'
		} ,
		
		
		// 身份证号码验证
		cardID:{
			validator : function(value) {
			//	var reg = /^1[3|4|5|8|9]\d{9}$/;
			var reg=/^[1-9]{1}[0-9]{14}$|^[1-9]{1}[0-9]{16}([0-9]|[xX])$/;
			
			return reg.test(value);
			},
			message:'身份证格式不正确'
		},
		
		
		// 移动手机号码验证
		mobile : {// value值为文本框中的值
			validator : function(value) {
				var reg = /^1[3|4|5|8|9]\d{9}$/;
				return reg.test(value);
			},
			message : '输入手机号码格式不准确.'
		},
		minLength : { // 最小长度
			validator : function(value, param) {
				return value.length >= param[0];
			},
			message : '请输入最小{0}位字符.'
		},
		maxLength : { // 最大长度
			validator : function(value, param) {
				return param[0] >= value.length;
			},
			message : '请输入最大{0}位字符.'
		},
		date : { // 日期格式
			validator : function(value) {
				// return
				// /^[0-9]{4}[-][0-9]{2}[-][0-9]{2}$/i.test($.trim(value));
				// 格式yyyy-MM-dd或yyyy-M-d
				return /^(?:(?!0000)[0-9]{4}([-]?)(?:(?:0?[1-9]|1[0-2])\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\1(?:29|30)|(?:0?[13578]|1[02])\1(?:31))|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-]?)0?2\2(?:29))$/i
						.test($.trim(value));

			},
			message : '曰期格式错误,如2012-09-11.'
		},
		equalTo : { // 字符匹配，可以用于密码验证
			validator : function(value, param) {
				return value == $(param[0]).val();
			},
			message : '字段不匹配.'
		},
		intOrFloat : {// 验证整数或小数
			validator : function(value) {
				return /^\d+(\.\d+)?$/i.test($.trim(value));
			},
			message : '请输入数字，并确保格式正确'
		},
		currency : {// 验证货币
			validator : function(value) {
				return /^\d+(\.\d+)?$/i.test($.trim(value));
			},
			message : '货币格式不正确'
		},
		qq : {// 验证QQ,从10000开始
			validator : function(value) {
				return /^[1-9]\d{4,9}$/i.test($.trim(value));
			},
			message : 'QQ号码格式不正确'
		},
		integer : {// 验证整数
			validator : function(value) {
				return /^[+]?[1-9]+\d*$/i.test($.trim(value));
			},
			message : '请输入整数'
		},
		age : {// 验证年龄
			validator : function(value) {
				return /^(?:[1-9][0-9]?|1[01][0-9]|120)$/i
						.test($.trim(value));
			},
			message : '年龄必须是0到120之间的整数'
		},

		chinese : {// 验证中文
			validator : function(value) {
				return /^[\Α-\￥]+$/i.test($.trim(value));
			},
			message : '请输入中文'
		},
		english : {// 验证英语
			validator : function(value) {
				return /^[A-Za-z]+$/i.test($.trim(value));
			},
			message : '请输入英文'
		},
		unnormal : {// 验证是否包含空格和非法字符
			validator : function(value) {
				return /.+/i.test($.trim(value));
			},
			message : '输入值不能为空和包含其他非法字符'
		},
		account : {// 验证用户名
			validator : function(value) {
				return /^[a-zA-Z][a-zA-Z0-9_]{5,15}$/i.test($
						.trim(value));
			},
			message : '用户名不合法（字母开头，允许6-16字节，允许字母数字下划线）'
		},
		faxno : {// 验证传真
			validator : function(value) {
				return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i
						.test($.trim(value));
			},
			message : '传真号码不正确'
		},
		zip : {// 验证邮政编码
			validator : function(value) {
				return /^[1-9]\d{5}$/i.test($.trim(value));
			},
			message : '邮政编码格式不正确'
		},
		ip : {// 验证IP地址
			validator : function(value) {
				return /^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/.test($.trim(value));
				//return "/^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/".test($.trim(value));
				//return /d+.d+.d+.d+/i.test($.trim(value));
			},
			message : 'IP地址格式不正确'
		},
		name : {// 验证姓名，可以是中文或英文
			validator : function(value) {
				return /^[\Α-\￥]+$/i.test(value)
						| /^\w+[\w\s]+\w+$/i
								.test($.trim(value));
			},
			message : '请输入姓名'
		},
		telNum:{ //既验证手机号，又验证座机号
		      validator: function(value, param){
		          return /(^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$)|(^((\d3)|(\d{3}\-))?(1[358]\d{9})$)/.test(value);
		         },   
		         message: '请输入正确的电话号码。'
		},
		isIdCardNo:{
			
			validator:function(value){
				
				return idCardNoUtil.checkIdCardNo(value);
				
			},
			
			message:'请正确输入您的身份证号码'
			
		},
		
		custom_remote:{
			            validator: function(value, param) {
		                   var postdata = {};
			                postdata[param[1]] = value;
			               var m_result =$.ajax({ type: "POST",//http请求方式
			                     url: param[0],    //服务器段url地址
			                    data:postdata,      //发送给服务器段的数据
			                     dataType: "type", //告诉JQuery返回的数据格式
			                     async: false
			                 }).responseText;
			              
			                 if (m_result == "false") {
			                     $.fn.validatebox.defaults.rules.custom_remote.message = param[2];
			                     return false;
			                 }else{
			                     return true;
			                 }
			             },
			             
			             message:'参数不允许重复输入'
		
		}
	});
	/*时间控件*/
//	$.fn.datebox.defaults = $.extend({}, $.fn.datebox.defaults, {
//		panelWidth : 300,
//	});
//	alert(JSON.stringify($.fn.datebox.defaults));
	/**
	 * 消息提示类(for msg.js)
	 * 
	 * @description
	 * @date 2014-7-9
	 * @author onlyang
	 * @contact oyloong@163.com
	 * 
	 */
	var myMsg = {
		"topLeft" : function(title, msg) {
			$.messager.show({
				title : title,
				msg : msg,
				showType : 'show',
				timeout : 2000,
				style : {
					right : '',
					left : 0,
					top : document.body.scrollTop
							+ document.documentElement.scrollTop,
					bottom : ''
				}
			});
		},
		"topCenter" : function(title, msg) {
			$.messager.show({
				title : title,
				msg : msg,
				showType : 'slide',
				timeout : 2000,
				style : {
					right : '',
					top : document.body.scrollTop
							+ document.documentElement.scrollTop,
					bottom : ''
				}
			});
		},

		"topRight" : function(title, msg) {
			$.messager.show({
				title : title,
				msg : msg,
				showType : 'show',
				timeout : 2000,
				style : {
					left : '',
					right : 0,
					top : document.body.scrollTop
							+ document.documentElement.scrollTop,
					bottom : ''
				}
			});
		},
		"centerLeft" : function(title, msg) {
			$.messager.show({
				title : title,
				msg : msg,
				showType : 'fade',
				timeout : 2000,
				style : {
					left : 0,
					right : '',
					bottom : ''
				}
			});
		},
		"center" : function center(title, msg) {
			$.messager.show({
				title : title,
				msg : msg,
				showType : 'fade',
				timeout : 2000,
				style : {
					right : '',
					bottom : ''
				}
			});
		},
		"centerRight" : function(title, msg) {
			$.messager.show({
				title : title,
				msg : msg,
				showType : 'fade',
				timeout : 2000,
				style : {
					left : '',
					right : 0,
					bottom : ''
				}
			});
		},
		"bottomLeft" : function(title, msg) {
			$.messager.show({
				title : title,
				msg : msg,
				showType : 'show',
				timeout : 2000,
				style : {
					left : 0,
					right : '',
					top : '',
					bottom : -document.body.scrollTop
							- document.documentElement.scrollTop
				}
			});
		},
		"bottomCenter" : function(title, msg) {
			$.messager.show({
				title : title,
				msg : msg,
				showType : 'slide',
				timeout : 2000,
				style : {
					right : '',
					top : '',
					bottom : -document.body.scrollTop
							- document.documentElement.scrollTop
				}
			});
		},
		"bottomRight" : function(title, msg) {
			$.messager.show({
				title : title,
				msg : msg,
				timeout : 2000,
				showType : 'show'
			});
		},
		"alert": function(title, msg){
			$.messager.alert(title, msg);
		}
	};
	/* extend to jquery */
	$.extend({ "myMsg": myMsg });
	/* more widgets */
}(jQuery, window, document, undefined));
var msg = $.myMsg;
/* for msg.js */


var GM_PERMISSIONS = {
	"enable": false,
	"url": "../loginPermission.json",
	"data": null, //upms:user:add
	"splitor": ","
};
function getPermissions(){
	if(!GM_PERMISSIONS.data){
		GM_PERMISSIONS.data = [];
		$.ajax({
			"url" : GM_PERMISSIONS.url,
			"data" : {},
			"dataType" : "json",
			"type" : "post",
			"cache" : false,
			"async": false, //同步
			"success" : function(result) {
				//alert(JSON.stringify(result));
				if (result) {
					GM_PERMISSIONS.data = result.split(GM_PERMISSIONS.splitor);
				} 
			},
			"error" : function() {
				$.messager.alert("提示", "获取权限失败，请检查。");
			}
		});
//		var data = {
//			"user": "",
//			"role": "",
//			"permissions": "...,...,..."
//		}
		//GM_PERMISSIONS.data = "upms:user:role,upms:user:add,upms:user:remove,upms:user:edit,upms:user:detail";
//		var sData = "upms:user:detail,upms:user:add,upms:user:remove,upms:user:edit,upms:user:role,";//用户权限
//		sData += ",upms:department:edit,upms:department:add"; //部门权限
//		sData += ",upms:resource:edit,upms:resource:add,upms:resource:remove"; //资源权限
//		sData += ",upms:role:edit,upms:role:remove,upms:role:add"; //角色管理权限
//		sData += ",upms:systemModel:edit,upms:systemModel:remove,upms:systemModel:add"; //系统模型权限
//		GM_PERMISSIONS.data = sData.split(GM_PERMISSIONS.splitor);
	}
	return GM_PERMISSIONS.data;
}
function permissable(permission){
	if(GM_PERMISSIONS.enable){
		if(permission && permission.length>0){
			var aPermissions = getPermissions();
			if(aPermissions && aPermissions.length>0 && jQuery.inArray(permission, aPermissions)>=0){ //toLowerCase toUpperCase
				return true;
			}
		}
		return false;
	}else{
		return true;
	}
}

$(function() {
	btnExt();
});
function btnExt(){
	$(".l-btn.l-btn-small:not(.l-btn-plain)").addClass("btn_ext");
}

$(function(){
	/*重置dialog高度*/
	var dialogs = $(".easyui-dialog");
	var wh = $(window.parent).height();
	$.each(dialogs, function(i, n){
		var d = $(n);
		var dh = d.height();
		if(dh>wh){
			d.dialog({
				"height": wh
			});
		}
	});
});

