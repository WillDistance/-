/**
 * 搜索
 */
define('tj/scripts/shouye/search.js',function(require, exports, module) {
	var service = require("mobileService").getInstance();
    var utils = require("utils");
    var pageJs = require("tj/scripts/common/page2.js");
    
	var page_id = "#shouye_search ";
	var search_type  = "0";
	var scrollerHeightIsHeight = 0;
	function init() { // 初始化方法
		utils.headEvent();
		scrollerHeightIsHeight = $(window).height() - $(page_id+" .header").height()-$(page_id+" .tab_box").height();
	}
	
	function load(){
		var key = $.getSStorageInfo("search_key");
		var type = $.getSStorageInfo("search_type");
		if(type){
			search_type = type;
		}
		key = delHtmlTag(key);
		if(key){
			listshow();
			$(page_id+" #keyword").val(key);
			searchList(key);
		}
	}
	
	function listshow(){
		if(search_type==0){
			$(page_id+" #study_list").show();
			$(page_id+" #article_list").hide();
			$(page_id+" #video_list").hide();
			$(page_id+" .tab_content").addClass("study_box").addClass("sub_type1");
		}else if(search_type==1){
			$(page_id+" #study_list").hide();
			$(page_id+" #article_list").show();
			$(page_id+" #video_list").hide();
			$(page_id+" .tab_content").addClass("study_box").addClass("sub_type1");
		}else{
			$(page_id+" #study_list").hide();
			$(page_id+" #article_list").hide();
			$(page_id+" #video_list").show();
			$(page_id+" .tab_content").removeClass("study_box").removeClass("sub_type1");
		}
		$(page_id +" .tab_box .tab_item").eq(search_type).addClass("current").siblings().removeClass("current");
	}
	
	function searchList(value){
		if(!value){
			$(page_id + " #article_list").empty();
			$(page_id + " #video_list").empty();
			$(page_id + " #study_list").empty();
			return;
		}
		var artTemplate = "_study_list";
		var pro_main = $(page_id + " #study_list");
		if(search_type==1){
			artTemplate = "_article_list";
			pro_main = $(page_id + " #article_list");
		}else if(search_type==2){
			artTemplate = "_video_list";
			pro_main = $(page_id + " #video_list");
		}
		utils.destroyScrollerForName("eduScroller.shouye_search.myScroller");
		pageJs.init({
			scrollerHeight:$(page_id+" .header").height()+$(page_id+" .tab_box").height(),
			scrollerHeightIsHeight:scrollerHeightIsHeight,
			queryFunService:service,
			queryFunServiceName:"searchList",
			curPageIdx:"1",
			numPerPage:"8",
			artTemplate_pro_main: artTemplate,
			$pro_main: pro_main,
			funcParam:{
				"key": value,
				"type":search_type
			},
			showNotDataStr : '<div class="no_data_box"><img src="/m/tj/images/pic_no_data.png"><p>抱歉！没有搜索到与<em class="c_red">'+value+'</em>有关的内容</p></div>',
			pageId:"shouye_search"
		});
	}
	
	function bindPageEvent() {
		$.bindEvent($(page_id + ".header_back"), function(e) {
			$.pageBack();
	    },'tap');
		$.bindEvent($(page_id+" #keyword"),function(e){
			var value = $(this).val();
			value = delHtmlTag(value);
			$(this).val(value);
			if(value){
				$.setSStorageInfo("search_key",value);
				listshow();
				searchList(value);
			}
		},"input");
		
		$.preBindEvent($(page_id+" .tab_box"),".tab_item",function(){
			$("#end_page").html("");
			search_type = $(this).index();
			listshow();
			var value = $(page_id+" #keyword").val();
			value = delHtmlTag(value);
			$(page_id+" #keyword").val(value);
			if(value){
				$.setSStorageInfo("search_key",value);
				listshow();
				searchList(value);
			}
		}, 'tap');
		
		$.preBindEvent($(page_id+" #study_list"),".article_detail",function(){
			var article_id = $(this).attr("article_id");
			$.pageInit("shouye/search","wyxx/details",{"article_id":article_id});
		},'tap');
		$.preBindEvent($(page_id+" #study_list"),".toTest",function(){
			$.pageInit("shouye/index","gscp/test",{"name":$(this).attr("catalog")});
		},'tap');
		$.preBindEvent($(page_id+" #article_list"),"li.article_detail",function(){
			var article_id = $(this).attr("article_id");
			$.pageInit("shouye/search","jdld/detail",{"article_id":article_id});
		},'tap');
		$.preBindEvent($(page_id + "#video_list"), ".video_item", function() {
			var video_id = $(this).attr("video_id");
			var type = $(this).attr("type");
			$.pageInit("shouye/search","spkc/play",{"video_id":video_id,"type":type});
		}, 'tap');
	}
	
	function destroy() {
		utils.destroyScrollerForName("eduScroller.search_index.myScroller");
		$(page_id + " #keyword").val('');
		$(page_id + " #article_list").empty();
		$(page_id + " #video_list").empty();
		$(page_id + " #study_list").empty();
		$(page_id +" .tab_box .tab_item:first").addClass("current").siblings().removeClass("current");
	}
	
	function delHtmlTag(str)
	{
		if(str)
		{
			return str.replace(/<[^>]+>/g,"")
		}
		return "";  //正则去掉所有的html标记
	}

	var search = {
		"init" : init,
		"load" : load,
		"bindPageEvent": bindPageEvent,
		"destroy": destroy
	};
	module.exports = search;
});