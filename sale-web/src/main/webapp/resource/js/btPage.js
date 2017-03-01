/**
 * 调用$("#pagination").page("form1");
 * 需传两个参数
 * form1 form的名称
 * currenPage 当前页的id
 * 通过post方式提交
 */
;(function($){
    $.fn.extend({
    "page":function(formName){
    var $this=this;
    //定义分页结构体
    var pageinfo={
             url:$(this).attr("url"),
             currentPage : $(this).attr("currentPage")*1, // 当前页码
             pageCount : $(this).attr("pageCount")*1 // 总页码
    };
    if(pageinfo.pageCount<2)
        return false;
    var html=[];
    var code = ''; var i;
    var total_page = parseInt( pageinfo.pageCount );
    var current_page = parseInt( pageinfo.currentPage );
    var pager_length = 11;
    var header_length = 2; 
    var tailer_length = 2;
    var main_length = pager_length - header_length - tailer_length; //必须为奇数
    html.push("<nvl><ul class='pagination' >");
    if( total_page < pager_length ){
        for(i = 0; i <     total_page; i++){
            code += (i+1 != current_page) ? fillTag("nomal", i+1) : fillTag("active", i+1);
        }
    }
    else{
        //先计算中心偏移量
        var offset = ( pager_length - 1) / 2;
        //分三种情况，第一种左边没有...
        if( current_page <= offset + 1) {
            var tailer = '';
            //前header_length + main_length 个直接输出之后加一个...然后输出倒数的    tailer_length 个
            for( i = 0; i < header_length + main_length; i ++)
                code += (i+1 != current_page) ? fillTag("nomal", i+1) : fillTag("active",i+1);
            code += fillTag("nomal", '...');
            for(i = total_page; i > total_page - tailer_length; i --)
                tailer = fillTag("nomal", i) + tailer;
            code += tailer;
        }
        //第二种情况是右边没有...
        else if( current_page >= total_page - offset )
        {
            var header = '';
            //后tailer_length + main_length 个直接输出之前加一个...然后拼接 最前面的 header_length 个
            for( i = total_page; i >= total_page-main_length - 1; i --)
                code = (( current_page != i ) ? fillTag("nomal", i) : fillTag("active", i)) + code;
            code = fillTag("nomal", '...') + code;
            for( i = 0; i < header_length ; i++)
                header +=  fillTag("nomal", i + 1);
            
            code = header + code;
        }
        //最后一种情况，两边都有...
        else
        {
            var header = '';
            var tailer = '';
            //首先处理头部
            for( i = 0; i < header_length; i ++)
                header += fillTag("nomal", i + 1);
            header += fillTag("nomal", '...');
            //处理尾巴
            for(i = total_page; i > total_page - tailer_length; i --)
                tailer = fillTag("nomal", i) + tailer;
            tailer = fillTag("nomal", '...') + tailer;
            //处理中间
            //计算main的中心点
            var offset_m = ( main_length - 1 ) / 2;
            var partA = '';
            var partB = '';
            var j;
            var counter = (parseInt(current_page) + parseInt(offset_m));
            for(i = j = current_page ; i <= counter; i ++, j --)
            {
                partA = (( i == j ) ? '' : fillTag("nomal", j)) + partA;
                partB += ( i == j ) ? fillTag("active", i) : fillTag("nomal", i);
            }
            //拼接
            code = header + partA + partB + tailer;
        }
    }
    var prev = ( current_page == 1 ) ? fillTag("disabled", '<') : fillTag("page_prev", '<');
    var next = ( current_page == total_page ) ? fillTag("disabled", '>') : fillTag("page_next", '>');
    code = prev + code + next;
    html.push(code);
    html.push("</ul></nvl>");
    $this.html(html.join("")); 
    //绑定数据处理函数
    $this.find(".nomal a").bind("click",function(){
          redirectTo($(this).html());
    });
    $this.find(".page_prev a").bind("click",function(){
          redirectTo(pageinfo.currentPage-1);
    });
    $this.find(".page_next a").bind("click",function(){
         redirectTo(pageinfo.currentPage+1);
    });
    function redirectTo(page){
    	if(page=='...'){
    		return;
    	}
    	//$("#currenPage").val(page);
    	$("form[name='"+formName+"']").find("#currenPage").val(page);
    	$("form[name='"+formName+"']").attr("action",pageinfo.url);
        $("form[name='"+formName+"']").submit();
    }
    function fillTag(a_class,a_html){
        return "<li class='"+a_class+"'><a href='javascript:void(0);'>"+a_html+"</a></li>";
    }
    return $this;
    }
    });
 
    })(jQuery);