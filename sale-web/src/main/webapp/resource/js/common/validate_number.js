/**
 * Created by YClimb on 2016/4/14.
 */

/**
 * 功能：将浮点数四舍五入，取小数点后2位
 * 用法：changeTwoDecimal(3.1415926) 返回 3.14
 * @param x
 * @returns {*}
 */
function changeTwoDecimal(x)
{
    var f_x = parseFloat(x);
    if (isNaN(f_x))
    {
        // alert('function:changeTwoDecimal->parameter error');
        return false;
    }
    f_x = Math.round(f_x *100)/100;

    return f_x;
}