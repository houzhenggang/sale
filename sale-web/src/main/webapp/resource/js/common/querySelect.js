
var tempAjax ="";
var defaultValue = "<option value='' selected>--请选择--</option>";

$(function() {

	/**
	 * 行驶证使用性质：useCharacter:driver_license_use_character;
	 * 获取保险公司下拉列表：insuranceCompany:insurance_company;
	 * 获取GPS下拉列表：gpsFactory:gps_factory;
	 * 获取GPS类型下拉列表：gpsType:gps_type;
	 * 获取是否临牌下拉列表：licenseType:license_type;
	 * 获取车辆地域下拉列表：licenseAddress:license_address;
	 * 资金申请状态：applyStatus:apply_status;
	 * 放款类型：paidType:paid_type;
	 * 放款状态：paidStatus:paid_status;
	 * 初始化启用停用状态：schemeStatus:is_enable_status;
	 * 是否默认方案选择：isDefault:is_default;
	 * 获取所属架构下拉列表：organCode:null;
	 * 获取所属架构下拉列表(所有)：companyCode:null;
	 * 获取仓库下拉列表：storageId:null;
	 * 资金池下拉列表：capitalPool:null;
	 * 方案模板拉列表：scheme:null;
	 * 上险方：insurancePaidBy:bb;
	 * 账户卡类型：cardType: card_type
	 * 成本中心：costCenter: cost_center
	 * 保险费用请款状态：insurancePaymentStatus: car_fee_insurance_status
	 * 门店和中心库：organAndStorage;
	 * 测试：aa:bb;
	 * 测试：aa:null;
	 * @type {string[]}
	 * 系统码表/业务表所需要显示的下拉框统一查询显示方法
	 * 1：码表为默认查询方式
	 * 2：业务表需要特殊处理，switch(className)
	 * 3：对页面select是否存在className进行判断，如果有则查询数据库并添加
	 * 4：方法使用for循环添加，classSelectCloumn及classSelectType需要一一对应
	 * 5：classSelectCloumn及classSelectType不能为空，业务表特殊处理时，classSelectType没有则默认为null
	 * added by yipan 2016年12月30日 14:55:49
	 */
	// 当前select的class名称
	var classSelectCloumn = ["useCharacter", "insuranceCompany", "gpsFactory", "gpsType", "licenseType",
							"licenseAddress", "applyStatus", "paidType", "paidStatus", "schemeStatus",
							"isDefault", "organCode", "companyCode", "storageId", "capitalPool", "cardType", 
							"costCenter","insurancePaymentStatus", "scheme", "isHeadSafing",
							"organAndStorage"];
	// 当前class对应数据库的type
	var classSelectType = ["driver_license_use_character", "insurance_company", "gps_factory", "gps_type", "license_type",
							"license_address", "apply_status", "paid_type", "paid_status", "is_enable_status",
							"is_default", "null", "null", "null", "null", "card_type", 
							"cost_center","car_fee_insurance_status", "null", "is_head_safing",
							"null"];
	var classSelectCloumnName;
	var classSelectUrl;
	for (var i = 0; i < classSelectCloumn.length; i++) {
		// 当前select的class名称
		classSelectCloumnName = "." + classSelectCloumn[i];
		// 判断页面select中是否存在当前class
		if ($(classSelectCloumnName) && $(classSelectCloumnName).length > 0) {
			// 个性化的请求链接，可自定义
			switch (classSelectCloumn[i]) {
				case "organCode":
					classSelectUrl = BASE_PATH + "/mst/mstOrganCompany/queryOrganList.json";
					break;
				case "organAndStorage":
					classSelectUrl = BASE_PATH + "/mst/mstOrganCompany/queryOrganAndStorageList.json";
					break;
				case "companyCode":
					classSelectUrl = BASE_PATH + "/mst/mstOrganCompany/queryAllOrganCompany.json";
					break;
				case "storageId":
					classSelectUrl = BASE_PATH + "/storageInfo/queryStorageList.json";
					break;
				case "capitalPool":
					classSelectUrl = BASE_PATH + "/insurance/pool/capitalPoolCombobox.do";
					break;
				case "scheme":
					classSelectUrl = BASE_PATH + "/insurance/scheme/queryScheme.do";
					break;
				default:
					classSelectUrl = BASE_PATH + "/mst/mstDataDictionary/queryDictListByType.json?type=" + classSelectType[i];	// 当前class对应数据库的type
					break;
			}
			$.ajax({
				async: false,
				url: classSelectUrl,
				type: "POST",
				dataType: "json",
				success: function(data){
					tempAjax = "";
					$.each(data,function(j,n){
						// 个性化的赋值，可自定义
						switch (classSelectCloumn[i]) {
							case "organCode":
							case "companyCode":
								tempAjax += "<option value='" + n.code + "'>" + n.name + "</option>";
								break;
							case "storageId":
							case "costCenter":
								tempAjax += "<option value='" + n.id + "'>" + n.name + "</option>";
								break;
							default:
								tempAjax += "<option value='" + n.value + "'>" + n.name + "</option>";
								break;
						}
					});
					$(classSelectCloumnName).append(tempAjax);
				}
			});
			// 设置select
			$(classSelectCloumnName).selectpicker({
				liveSeach: true,
				maxOptions: 1,
				size: 7
			});
			// 设置回显
			$(classSelectCloumnName).each(function(){
				$(this).selectpicker('val', $(this).attr("value"));
			});
		}
	}

	/**
	 * 品牌级联下拉列表
	 */
	if ($(".carBrand") && $(".carBrand").length > 0) {
		$.ajax({
			async: false,
			url: BASE_PATH + "/mst/mstCarBrand/carBrandList.json",
			type: "POST",
			dataType: "json",
			success: function (data) {
				tempAjax = "";
				$.each(data, function (i, n) {
					tempAjax += "<option value='" + n.code + "'>" + n.name + "</option>";
				});
				$(".carBrand").append(tempAjax);
			}
		});
		$(".carBrand").selectpicker({
			liveSeach: true,
			maxOptions: 1,
			size: 5,
			noneSelectedText: '--请选择--'
		});
		$(".carBrand").each(function () {
			$(this).selectpicker('val', $(this).attr("value"));
		});
		$(".carBrand").on('changed.bs.select', function (e) {
			carSeriesByBrand();
		});
		if ($(".carBrand").val() != '' && $(".carSeries").val() != null) {
			carSeriesByBrand();
		}
	}

	/**
	 * 系列
	 */
	if ($(".carSeries") && $(".carSeries").length > 0) {
		$(".carSeries").selectpicker({
			liveSeach: true,
			maxOptions: 1,
			size: 5,
			noneSelectedText: '--请选择--'
		});
		$(".carSeries").each(function () {
			$(this).selectpicker('val', $(this).attr("value"));
		});
		$(".carSeries").on('changed.bs.select', function (e) {
			carModelBySeries();
		});
		if ($(".carSeries").val() != '' && $(".carSeries").val() != null) {
			carModelBySeries();
		}
	}

	/**
	 * 车型
	 */
	if ($(".carModel") && $(".carModel").length > 0) {
		$(".carModel").selectpicker({
			liveSeach: true,
			maxOptions: 1,
			size: 5,
			noneSelectedText: '--请选择--'
		});
		$(".carModel").each(function () {
			$(this).selectpicker('val', $(this).attr("value"));
		});
	}
	
	
	/**
	 * 地区-省
	 */
	if ($('.provinceCode') && $('.provinceCode').length > 0) {
		// 初始化下拉列表
		var option = '';
		$.ajax({
			async: false,
			url: BASE_PATH + '/mst/mstDataDictionary/getProvinceArray.json?getLevel=1&provinceCode=&cityCode=',
			type: 'POST',
			dataType: "json",
			success: function (data) {
				$.each(data.json, function (i, n) {
					option += '<option value="' + n.id + '">' + n.name + '</option>';
				});
				$('.provinceCode').append(option);
				$('.provinceCode').selectpicker('refresh');
			}
		});
		// 初始化默认值及级联下拉框
		$(".provinceCode").selectpicker({
			liveSeach: true,
			maxOptions: 1,
			size: 7
		});
		$('.provinceCode').each(function () {
			$(this).selectpicker('val', $(this).attr('value'));
			$(this).change(function(){
				childAreaListByParentCode('.cityCode', $(this).val());
			});
		});
	}
	
	/**
	 * 地区-市
	 */
	if ($('.cityCode') && $('.cityCode').length > 0){
		childAreaListByParentCode('.cityCode', $('.provinceCode').val());
	}
	
});

/**
 * 根据车辆品牌查询车辆系列
 */
var carSeriesByBrand = function(){
	$.ajax({
		async: false,
		url: BASE_PATH + "/mst/mstCarSeries/carSeriesByBrand.json",
		data: {brandCode: $(".carBrand").val()},
		type: "POST",
		dataType: "json",
		success: function (data) {
			var tempAjax = "";
			$.each(data, function (i, n) {
				tempAjax += "<option value='" + n.code + "'>" + n.name + "</option>";
			});
			$(".carSeries").eq(0).empty();
			$(".carSeries").eq(0).append(defaultValue);
			$(".carSeries").eq(0).append(tempAjax);
			$(".carSeries").selectpicker('refresh');
		}
	});
};

/**
 * 根据车辆系列查询车辆类型
 */
var carModelBySeries = function () {
	$.ajax({
		async: false,
		url: BASE_PATH + "/mst/mstCarModel/carModelBySeries.json",
		data: {seriesCode: $(".carSeries").val()},
		type: "POST",
		dataType: "json",
		success: function (data) {
			tempAjax = "";
			$.each(data, function (i, n) {
				tempAjax += "<option value='" + n.code + "'>" + n.autoType + "</option>";
			});
			$(".carModel").eq(0).empty();
			$(".carModel").eq(0).append(defaultValue);
			$(".carModel").eq(0).append(tempAjax);
			$(".carModel").selectpicker('refresh');
		}
	});
};

/**
 * 根据当前地区码查询下一级地区
 */
var childAreaListByParentCode = function(className,areaCode){
	$(className).eq(0).empty();
	$(className).eq(0).append('<option value="">--请选择--</option>');
	if( areaCode && areaCode != '' ){
		var option = '';
		$.ajax({
			async: false,
			url: BASE_PATH + "/mst/mstDataDictionary/getProvinceArray.json?getLevel=2&provinceCode=" + areaCode + "&cityCode=",
			type: 'POST',
			dataType: 'json',
			success: function(data){
				$.each(data.json, function(i,n){
					option += '<option value="' + n.id + '">' + n.name + '</option>';
				})
			}
		});
		$(className).eq(0).append(option);
	}

	$(className).selectpicker({
		liveSeach: true,
		maxOptions: 1,
		size: 7
	});
	$(className).selectpicker('refresh');
	$(className).each(function () {
		$(this).selectpicker('val', $(this).attr('value'));
	});
}
