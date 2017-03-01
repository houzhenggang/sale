$(function() {

	'use strict';

	var console = window.console || {
		log : function() {
		}
	};
	var $images = $('.docs-pictures');
	// var $toggles = $('.docs-toggles');
	// var $buttons = $('.docs-buttons');
	var handler = function(e) {
		console.log(e.type);
	};
	var options = {
		// inline: true,
		url : 'data-original',
		build : handler,
		built : handler,
		show : handler,
		shown : handler,
		hide : handler,
		hidden : handler,
		navbar : false,
		title : false
	};
	$images.on({
		'build.viewer' : handler,
		'built.viewer' : handler,
		'show.viewer' : handler,
		'shown.viewer' : handler,
		'hide.viewer' : handler,
		'hidden.viewer' : handler
	}).viewer(options);

});

/**
 * 用于动态执行js时调用
 * added by yipan 2016年8月5日 18:17:02
 */
function initialImgView() {
	'use strict';

	var console = window.console || {
			log : function() {
			}
		};
	var $images = $('.docs-pictures');
	// var $toggles = $('.docs-toggles');
	// var $buttons = $('.docs-buttons');
	var handler = function(e) {
		console.log(e.type);
	};
	var options = {
		// inline: true,
		url : 'data-original',
		build : handler,
		built : handler,
		show : handler,
		shown : handler,
		hide : handler,
		hidden : handler,
		navbar : false,
		title : false
	};
	$images.on({
		'build.viewer' : handler,
		'built.viewer' : handler,
		'show.viewer' : handler,
		'shown.viewer' : handler,
		'hide.viewer' : handler,
		'hidden.viewer' : handler
	}).viewer(options);
}
