/**
 * @summary 	公用函数
 * @namespace 	ui
 * @description 
 * @date 		2014-6-30
 * @version 	1.0
 * @file		ui.common.js
 * @author 		OYLoong
 */
(function ($, window, document, undefined){ //闭包，立即执行函数表达式 (immediately-invoked function expression ,IIFE)
	"use strict"; //启用严谨模式
	var commonUtils = {
		"version": "v1.0",
		/** 
		 * @summary 	Position
		 * @description 位置属性工具类
		 */
		"Position": {
			/**
			 * @summary 获取容器的相关属性
			 * @param {object} oContainer JQuery页面对象，默认为：body
			 * @param {boolean}是否为body（body只是一个象征，实际是按照window/documnet来处理的），
			 * 		窗口和滚动条的问题
			 * @return {object} 容器的相关属性
			 * @example
			 * 		UI.Position.getPosition($("#example"), false);
			 */
			"getPosition": function(oContainer, bBody){
				//w:可见宽、h:可见高、sw:实际宽、sh:实际高、sl:左偏移、st:上偏移、cx:可见中间X、cy:可见中间Y
				var oPosition={};
				/*强制为body、目标对象为空、标签名为body*/
				if( bBody || (!oContainer || oContainer.length<=0) || 
					(oContainer && oContainer.length>0 && oContainer[0].nodeName.toLowerCase()=='body')){
					oPosition.bBody = true;
					oContainer = $(window);
					oPosition.w = oContainer.width();//宽度
					oPosition.h = oContainer.height();//高度
					/*兼容性处理：<!DOCTYPE html>使用W3C标准后，无兼容性问题*/
					oPosition.sw = $(document).width();//内容宽度 相当于：scrollWidth
					oPosition.sh = $(document).height();//内容高度
					if(document.all){//IE
						oPosition.sw = document.documentElement.clientWidth;
						oPosition.sh = document.body.clientHeight -4;
					}
				} else {
					oPosition.bBody = false;
					oPosition.w = oContainer.width();//宽度
					oPosition.h = oContainer.height();//高度
					oPosition.sw = oContainer[0].scrollWidth;	//内容宽度
					oPosition.sh = oContainer[0].scrollHeight;	//内容高度
				}
				oPosition.sl = oContainer.scrollLeft();//左偏移
				oPosition.st = oContainer.scrollTop();//上偏移return oPosition;
				//oContainer 中间点
				oPosition.cx = oContainer.width()/2 + oPosition.sl;
				oPosition.cy = oContainer.height()/2 + oPosition.st;
				return oPosition;
			}
		},
		/** 
		 * @summary 	Iframe
		 * @description iframe性能处理
		 */
		"Iframe": {
			/**
			 * @summary 根据iframe的ID删除iframe
			 * @param {string} iframeId 
			 * @example
			 * 		UI.Iframe.removeIframeById("example");
			 */
			"removeIframeById": function(iframeId) {
				var $iframe = $("#" + iframeId);
				if(!$iframe||$iframe.length<=0||$iframe[0].nodeName.toLowerCase()!='iframe'){return;}
				$iframe[0].src = '';
				$iframe.remove();//删除iframe
			},
			/**
			 * @summary 根据容器删除里面所有的iframe
			 * @param {object} oContainer 容器
			 * @example
			 * 		UI.Iframe.removeIframesByContainer($("#example"));
			 */
			"removeIframesByContainer": function(oContainer) {
				var $iframes = oContainer.find("iframe");
				if(!$iframes||$iframes.length<=0){return;}
				$.each($iframes, function() {
					this.src = "";
					$(this).remove();//删除iframe
				});
			}
		},
		
		"Url": {
			/**
			 * @summary url参数处理-序列化
			 * @param {string} url 地址
			 * @param {object} params 参数集合(JSON数据)
			 * @return {string} 将参数添加到URL后的URL地址
			 * @example
			 * 		UI.Url.setParams("http://localhost:8080/tms/user/userList.action", {"userId": "007", "roleId": "1007"});
			 * 		result：http://localhost:8080/tms/user/userList.action?userId=007&roleId=1007
			 */
			"setParams": function(url, params){
				var sURL = url;
				var sParam=jQuery.param(params);//width=1680&height=1050
				if(sURL && sURL.length>0 && sURL.indexOf("?")<0){
					sURL =sURL+"?"+sParam;
				}else if(sURL && sURL.length>0 && sURL.indexOf("?")>0){
					sURL=sURL+"&"+sParam;
				}
				return sURL;
			},
			/**
			 * @summary url参数处理-反序列化
			 * @param {string} url 地址
			 * @return {object} params 参数集合(JSON数据)
			 * @example
			 * 		UI.Url.getParams("http://localhost:8080/tms/user/userList.action?userId=007&roleId=1007&roleId=1008");
			 * 		result： {"userId": "007", "roleId": ["1007","1008"]}
			 */
			"getParams": function(url){
				if(!url)return; 
				var paramsArr = url.split('?')[1].split('&');
				var args={}, param, name,value; 
				args['url']=encodeURIComponent(tourl.split('?')[0]); //首先载入url,问号"?"前面的部分 
				for(var i=0;i<paramsArr.length;i++){ 
					param=paramsArr[i].split('='); 
					name=param[0],value=param[1]; 
					if(name=="")name="unkown"; 
					if(typeof args[name]=="undefined"){ //参数尚不存在 
						args[name]=value; 
					}else if(typeof args[name]=="string"){ //参数已经存在则保存为数组 
						args[name]=[args[name]]; 
						args[name].push(value); 
					}else{ //已经是数组的 
						args[name].push(value); 
					} 
				}
				return args; //以json格式返回获取的所有参数 
			}
		},
		
		"Date": {
			/**
			 * @summary 根据时间及格式获取时间的字符串
			 * @description 根据时间及格式获取时间的字符串
			 * @param {date} dDate 时间
			 * @param {string} sFormat 格式
			 		yyyymmdd、yyyymmddhhmmss、yyyymmddhhmmsssss、hhmmsssss、hhmmss、
			 		yyyy.mm.dd、yyyy.mm.dd hh:mm:ss、yyyy.mm.dd hh:mm:ss sss、
			 		yyyy-mm-dd、yyyy-mm-dd hh:mm:ss、yyyy-mm-dd hh:mm:ss sss、
			 		yyyy/mm/dd、yyyy/mm/dd hh:mm:ss、yyyy/mm/dd hh:mm:ss sss
			 * @return {string} 格式化后的日期字符串
			 * @example
			 * 		UI.Date.dateStr(new Date(), "yyyy-mm-dd hh:mm:ss");//2013-6-6 11:11:11
			 */
			"dateStr": function(dDate, sFormat) {
				if (!dDate || dDate=="null"){
					return "";
				}
				if(!(dDate instanceof Date)){
					dDate = new Date(dDate);
				}
				if(isNaN(dDate.getTime())){
					return "";
				}
				var year = dDate.getFullYear();
				var month = (dDate.getMonth() + 1)>9?(dDate.getMonth() + 1):"0"+(dDate.getMonth() + 1);
				var date = dDate.getDate()>9?dDate.getDate():"0"+dDate.getDate();
				var hour = dDate.getHours()>9?dDate.getHours():"0"+dDate.getHours();
				var minute = dDate.getMinutes()>9?dDate.getMinutes():"0"+dDate.getMinutes();
				var second = dDate.getSeconds()>9?dDate.getSeconds():"0"+dDate.getSeconds();
				var millisecond = dDate.getMilliseconds();
				if(millisecond<10){
					millisecond = "00"+millisecond;
				}else if(millisecond<100){
					millisecond = "0"+millisecond;
				}
				var sFormat = sFormat ? sFormat.toLowerCase():"";
				var sDate = "";
				if (sFormat == "yyyy-mm-dd hh:mm:ss") {
					sDate = year + "-" + month + "-" + date + " " + hour + ":" + minute
						+ ":" + second;
				} else if (sFormat == "yyyymmddhhmmsssss") {
					sDate = year + "" + month + "" + date + "" + hour + "" + minute
							+ "" + second + "" + millisecond;
				} else if (sFormat == "yyyymmddhhmmss") {
					sDate = year + "" + month + "" + date + "" + hour + "" + minute
							+ "" + second;
				} else if (sFormat == "hhmmsssss") {
					sDate = hour + "" + minute + "" + second + "" + millisecond;
				} else if (sFormat == "hhmmss") {
					sDate = hour + "" + minute + "" + second;
				} else if (sFormat == "yyyy.mm.dd") {
					sDate = year + "." + month + "." + date;
				} else if (sFormat == "yyyy.mm.dd hh:mm:ss") {
					sDate = year + "." + month + "." + date + " " + hour + ":" + minute
							+ ":" + second;
				} else if (sFormat == "yyyy.mm.dd hh:mm:ss sss") {
					sDate = year + "." + month + "." + date + " " + hour + ":" + minute
							+ ":" + second + ":" + millisecond;
				} else if (sFormat == "yyyy-mm-dd") {
					sDate = year + "-" + month + "-" + date;
				} else if (sFormat == "yyyymmdd") {
					sDate = year + "" + month + "" + date;
				} else if (sFormat == "yyyy-mm-dd hh:mm:ss sss") {
					sDate = year + "-" + month + "-" + date + " " + hour + ":" + minute
							+ ":" + second + ":" + millisecond;
				} else if (sFormat == "yyyy/mm/dd") {
					sDate = year + "/" + month + "/" + date;
				} else if (sFormat == "yyyy/mm/dd hh:mm:ss") {
					sDate = year + "/" + month + "/" + date + " " + hour + ":" + minute
							+ ":" + second;
				} else if (sFormat == "yyyy/mm/dd hh:mm:ss sss") {
					sDate = year + "/" + month + "/" + date + " " + hour + ":" + minute
							+ ":" + second + ":" + millisecond;
				} else {
					sDate = year + "-" + month + "-" + date + " " + hour + ":" + minute
						+ ":" + second;
				}
				return sDate;
			},
			/**
			 * @summary date转换
			 * @description date转换
			 * @param {string} dateStr 时间字符串
			 * @return {date} date
			 * @example
			 * 		UI.Date.parseDate("2009-12-12 12:12:12");
			 */
			"parseDate": function(dateStr){
				try {
					return new Date(dateStr);
				} catch (e) {
					alert(dateStr+" 不是一个有效的时间。");
					return null;
				}
			}
		},
		/** 
		 * @summary 	Cookie
		 * @description JS操作cookie
		 */
		"Cookie": {
			"outTime": 1*24, //cookie失效时间，单位为小时，默认为24小时
			/**
			 * @summary 添加cookie
			 * @param {string} cookieName cookie名称
			 * @param {object} cookieValue cookie值
			 * @param {int} outTime cookie失效时间（单位为小时）
			 * @example
			 * 		UI.Cookie.setCookie(cookieName, cookieValue, outTime);
			 */
			"setCookie": function(cookieName, cookieValue, outTime){
				var expdate=new Date();
				if(outTime==null || outTime==undefined){
					outTime = UI.Cookie.outTime;//默认为24小时
				}
				var outms=outTime*60*60*1000;//过期时间，以天为单位"1"表示一小时  
				expdate.setTime(expdate.getTime()+outms);
				var cookieStr=cookieName+"="+escape(cookieValue)+";expires="+expdate.toGMTString();
				//escape方法的作用是进行编码，主要防止value中有特殊字符   
				document.cookie=cookieStr;
			},
			/**
			 * @summary 删除cookie cookie的删除并不是物理意义上的直接删除，而是将cookie的有效期设置为失效，然后由浏览器删除失效的cookie删除  
			 * @param {string} cookieName cookie名称
			 * @example
			 * 		UI.Cookie.deleteCookie(cookieName);
			 */
			"deleteCookie": function (cookiename){
				var date = new Date();
				var outTime=date.getTime()-1000;//将cookie的有效期设置为失效
				date.setTime(outTime);
				document.cookie=cookiename+"='';expires="+date.toGMTString();
			},
			/**
			 * @summary 读取cookie
			 * @param {string} cookieName cookie名称
			 * @retrun {object} cookieValue cookie值
			 * @example
			 * 		UI.Cookie.getCookie(cookieName);
			 */
			"getCookie": function(cookieName){
				var cookieStr=document.cookie;
				var cookievalue="";
				var cookieObjStr = "";
				if(cookieStr){
					var arrayCookie=cookieStr.split(';');
					if(arrayCookie.length > 0){
						for(var i=0;i<arrayCookie.length;i++){
							var arrayDetail=arrayCookie[i].split('=');
							cookieObjStr+='"'+arrayDetail[0]+'":"'+arrayDetail[1]+'",';
						}
						if(cookieObjStr && cookieObjStr.length>0){
							cookieObjStr = cookieObjStr.substring(0, cookieObjStr.length-1);
						}
						cookieObjStr = "{" + cookieObjStr + "}";
					}
				}
				var s = cookieObjStr.replace(/\s/g,"");//去掉空格   
				var cookieObj = $.parseJSON(s);//var cookieObj=JSON.parse(s);
				for(var item in cookieObj){
					if(item==cookieName){
						cookievalue=unescape(cookieObj[item]);//ookievalue = $.parseJSON(unescape(cookieObj[item]));
					}
				}
				return cookievalue;
			}
		},
		/** 
		 * @summary 	form工具类
		 * @description 
		 */
		"Form": {
			/**
			 * @summary checkboxChecker 多选框（checkbox）全选/反选/全不选
			 * @param {string|object} mCheckbox  checkbox的name 或 checkbox对象集合(jquery对象，如$(":checkbox"))
			 * @param {string} checkbox选中方式（all：全选，inverse：反选，allnot：全不选），默认为全选
			 * @example
			 * 		UI.Form.checkboxChecker($(":checkbox"), "inverse"); //反选
			 */
			"checkboxChecker": function(mCheckbox, sChecker){ //inverse all allnot
				var $checkboxs = mCheckbox;
				if(typeof mCheckbox == "string"){
					$checkboxs = $("input:checkbox[name='"+mCheckbox+"']");
				}
				if(sChecker == "allnot"){//全选
					$.each($checkboxs, function(){
						this.checked = false;
					});
				}else if(sChecker == "inverse"){
					$.each($checkboxs, function(){
						if(this.checked==true){
							this.checked = false;
						}else{
							this.checked = true;
						}
					});
				} else {
					$.each($checkboxs, function(){
						this.checked = true;
					});
				}
			}
		}
	};
	//扩展到jquery类
	$.extend({ "commonUtils": commonUtils });
}(jQuery,window,document,undefined));
/*简化实用方式*/
var UI = $.commonUtils;
var ui = $.commonUtils;

/**
 * @summary checkboxSelector checkbox全选/反选
 * @param {string|object} mCheckbox  checkbox的name 或 checkbox对象集合(jquery对象，如$(":checkbox"))
 * @param {boolean} bChecked 是否选中，true为全选,false为反选
 */
function checkboxSelector(mCheckbox, bChecked){
	var $checkboxs = mCheckbox;
	if(typeof mCheckbox == "string"){
		$checkboxs = $("input:checkbox[name='"+mCheckbox+"']");
	}
	if(bChecked){//全选
		$.each($checkboxs, function(){
			this.checked = true;
		});
	}else{
		$.each($checkboxs, function(){
			if(this.checked==true){
				this.checked = false;
			}else{
				this.checked = true;
			}
		});
	}
}





