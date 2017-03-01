//加载三级联动中的省份，参数type：用来区分不同表单的。defaultProvince，defaultCity，DefaultCounty 省市县默认值，当在新增是，默认都为0.数据回显时，需要传入数据库存储相关数据
var codeArray;
/**
 * 加载三级联动中
 * @param type：用来区分不同表单的,表单中省份以province开头，市级以city开头，县级以county开头
 * @param defaultProvince    在编辑时提供省级代码回显
 * @param defaultCity        在编辑时提供市级代码回显
 * @param defaultCounty        在编辑时提供县级代码回显
 * @param level                加载级次，仅显示省为1，
 */
function loadProvince(type, defaultProvince, defaultCity, defaultCounty, level) {
    comboboxClear("city" + type);
    comboboxClear("county" + type);
    $.ajax({
        url: "../../mst/mstDataDictionary/getProvinceArray.htm?getLevel=1&provinceCode=&cityCode=",
        datatype: "json",//请求页面返回的数据类型
        type: "POST",
        success: function (data) {//这里的data是由请求页面返回的数据
            var dataJson = $.parseJSON(data); // 使用json2.js中的parse方法将data转换成json格式
            /*var obj = dataJson.json;
            codeArray = obj;*/
            $('#province' + type).combobox({
                data: dataJson.json,
                valueField: 'code',
                textField: 'name',
                editable: false,
                onLoadSuccess: function () { //加载完成后,设置选中第一项
                    var val = $(this).combobox("getData");
                    if (val.length > 0) {
                        if (defaultProvince != 0) {
                            $(this).combobox("setValue", defaultProvince);
                            if (level > 1) {
                                loadCity(defaultProvince, type, defaultCity, defaultCounty, level);
                            }
                        } else {
                            $(this).combobox("setValue", val[0].code);
                        }
                    }
                },
                onSelect: function () {
                    var provinceCode = $(this).combobox('getValue');
                    if (provinceCode == '') {
                        comboboxClear("city" + type);
                        comboboxClear("county" + type);
                    } else {
                        if (level > 1) {
                            loadCity(provinceCode, type, 0, 0, level);
                        }
                    }
                }
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}
/**
 * 加载三级联动中的市，
 * 参数type：用来区分不同表单的。
 * defaultProvince，defaultCity，DefaultCounty 省市县默认值，
 * 当在新增是，默认都为0.
 * 数据回显时，需要传入数据库存储相关数据
 */
function loadCity(provinceCode, type, defaultCity, defaultCounty, level) {
    comboboxClear("county" + type);
    $.ajax({
        url: "../../mst/mstDataDictionary/getProvinceArray.htm?getLevel=2&provinceCode=" + provinceCode + "&cityCode=",
        datatype: "json",//请求页面返回的数据类型
        type: "POST",
        success: function (data) {//这里的data是由请求页面返回的数据
            var dataJson = $.parseJSON(data); // 使用json2.js中的parse方法将data转换成json格式
            $('#city' + type).combobox({
                data: dataJson.json,
                valueField: 'code',
                textField: 'name',
                editable: false,
                onLoadSuccess: function () { //加载完成后,设置选中第一项
                    var val = $(this).combobox("getData");
                    if (val.length > 0) {
                        if (defaultCity != 0) {
                            $(this).combobox("setValue", defaultCity);
                            if (level > 2) {
                                loadCounty(defaultCity, type, defaultCounty, level);
                            }
                        } else {
                            $(this).combobox("setValue", val[0].code);
                        }
                    }
                },
                onSelect: function () {
                    var cityCode = $(this).combobox('getValue');
                    if (cityCode == '') {
                        comboboxClear("county" + type);
                    } else {
                        if (level > 2) {
                            loadCounty(cityCode, type, 0, level);
                        }
                    }
                }
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

/**
 * 加载三级联动中的县，参数type：用来区分不同表单的。
 * defaultProvince，defaultCity，DefaultCounty 省市县默认值，
 * 当在新增是，默认都为0.
 * 数据回显时，需要传入数据库存储相关数据
 * @param cityCode
 * @param type
 * @param defaultCounty
 * @param level
 */
function loadCounty(cityCode, type, defaultCounty, level) {
    $.ajax({
        url: "../../mst/mstDataDictionary/getProvinceArray.htm?getLevel=3&cityCode=" + cityCode,
        datatype: "json",//请求页面返回的数据类型
        type: "POST",
        success: function (data) {//这里的data是由请求页面返回的数据
            var dataJson = $.parseJSON(data); // 使用json2.js中的parse方法将data转换成json格式
            $('#county' + type).combobox({
                data: dataJson.json,
                valueField: 'code',
                textField: 'name',
                editable: false,
                onLoadSuccess: function () { //加载完成后,设置选中第一项
                    var val = $(this).combobox("getData");
                    if (val.length > 0) {
                        if (defaultCounty != 0) {
                            $(this).combobox("setValue", defaultCounty);
                        } else {
                            $(this).combobox("setValue", val[0].code);
                        }
                    }
                },
                onSelect: function () {
                }
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function comboboxClear(name) {
    $('#' + name).combobox({
        data: []
    });
}
