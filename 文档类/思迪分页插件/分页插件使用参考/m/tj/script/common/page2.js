/**
 * 分页插件
 * @author 60
 * 
 * html:
 <!-- 分页 -->
	<div class="iscroll_vWrapper" id="wrapper" style="position: relative;">
		<div class="iscroll_vScroller">
			<div class="iscroll_pullDown scrolleduScrollerp" style="display: none;text-align: center;">
				<span class="pullDownIcon"></span>
				<div class="pullDownLabel">下拉刷新</div>
			</div>
	
			<div class="vertical_list" data-is-content="true">
				<!--多个分页 TODO ID要改-->
				<!--TODO   id值格式："_"+getPageCode().replaceAll("/", "_")+"_pro_main"   -->
				<script id="_point_index_pro_main" type="text/html">
					{{each data as value i}}
						{{if data.length != 0 }}
							<!--TODO  有数据时显示的样式   -->
							<p><span>粉丝：{{value.fansnum}}</span><span>组合：{{value.portfolio_num}}</span></p>
						{{/if}}
					{{/each}}
				</script>
				<!--多个分页 TODO ID要改-->
				<div id="pro_main"></div>
				<!--多个分页 TODO ID要改-->
				<div id="end_page"></div>
			</div>
	
			<div class="iscroll_pullUp" style="display: none;text-align: center;">
				<span class="pullUpIcon"></span>
				<div class="pullUpLabel">上拉加载下一页</div>
			</div>
		</div>
	</div>
 <!-- 分页 -->
 
 js:
 	 var pageJs = require("page");
	 pageJs.init({
		scrollerHeight:$(_pageId + " .header_inner").height() + $(_pageId + " .footer_nav").height(),
		queryFunService:service,
		queryFunServiceName:"queryHotViewPage",
		funcParam:{user_id: user_id, type: 0},
		curPageIdxStr:"cur_page",
		numPerPageStr:"num_per_page"
	});
	
  注:所有滑动插件的销毁都统一调用destroyScrollerForName("滑动插件名字")方法,例:utils.destroyScrollerForName("eduScroller.draftPoint.myScroller");
 */
define('tj/scripts/common/page2.js', function(require, exports, module) {
	var artTemplate = require("artTemplate");
	var iscrollUtils = require("iscrollUtils");
	var utils = require("utils");
	
	function init(param) {

		artTemplate.config("escape", false); //是否以html显示
		var _pageScroller = param.pageId;
		var _pageId = "#"+param.pageId;
		var _pageId_ = "_" + _pageScroller;
		// *********************必传参数*********************
		var scrollerHeight = Number(param.scrollerHeight ? param.scrollerHeight : 0); // 滑动区域以外的高度  例:$(_pageId + " .back_btn").height() + $(_pageId + " .footer_nav").height()
		var queryFunService = param.queryFunService;                                  // 查数据的service 例:require("pointService").getInstance()
		var queryFunServiceName = param.queryFunServiceName; // service中查数据方法的名字 例:"queryHotViewPage"
		var funcParam = param.funcParam ? param.funcParam : {}; // 查数据时需要的参数 例:{"user_id": user_id, "type": 0}
		// *********************必传参数*********************
		// *********************选传参数*********************
		var myScroller = param.myScroller ? param.myScroller : "myScroller";                        // 滑动组件的名字,当一个页面有多个滑动组件时传,例:"myScroller_one",默认值:"myScroller"
		var $wrapper = param.$wrapper ? param.$wrapper : $(_pageId + " #wrapper");                  // wrapper对应的元素, 例:$(_pageId + " #wrapper_one"),默认值:$(_pageId + " #wrapper")
		var $end_page = param.$end_page ? param.$end_page : $(_pageId + " #end_page");               // 显示"已加载到最后一页"的元素,例:$(_pageId + " #end_page_one"),默认值:$(_pageId + " #end_page")
		var $pro_main = param.$pro_main ? param.$pro_main : $(_pageId + " #article_list");               // 显示数据元素,例:$(_pageId + " #pro_main_one"),默认值:$(_pageId + " #pro_main")
		var showNotDataStr = param.showNotDataStr ? param.showNotDataStr : '<div class="no_data_box"><img src="/m/tj/images/pic_no_data.png"><p>精彩内容即将上线，敬请期待</p></div>';             // 暂无数据显示值,例:"<div>暂无数据</div>",默认值:getNotData()
		var artTemplate_pro_main = param.artTemplate_pro_main ? param.artTemplate_pro_main : _pageId_ + "_article_list"; // artTemplate ID值,例:artTemplate_pro_main, 默认值:"_"+getPageCode().replaceAll("/", "_")+"_pro_main"
		var numPerPage = param.numPerPage ? param.numPerPage : 8;                                   // 每页显示多少数据,例:10  默认值 :8
		var curPageIdxStr = param.curPageIdxStr ? param.curPageIdxStr : "curPage";                  // 查询数据 当前页参数对应的名字,例:"cur_page",默认值:"curPage"
		var numPerPageStr = param.numPerPageStr ? param.numPerPageStr : "numPerPage";               // 查询数据 每页显示条数参数对应的名字,例:"num_per_page",默认值:"numPerPage"
		var afterFunc = param.afterFunc; // 数据查询出来后执行的方法
		var listFunc = param.listFunc;																// 数据查询出来后执行的方法
		var doNotData = param.doNotData || function (){$wrapper.css("background","#fff")}; // 暂无数据时执行的方法
		var pullDownHandlerFun = param.pullDownHandlerFun;                                          // 下拉时时执行的方法
		var pullUpHandlerFunc = param.pullUpHandlerFunc;                                            // 上拉时时执行的方法
		var defaultImagePath = param.defaultImagePath;                                              // 图片加载失败时,默认加载图片路径
		var not_data_error_no = param.not_data_error_no;                                            // 暂无数据时的error_no,不要问我为什么会有这个参数,因为我也不知道写接口的人为什么会在暂无数据的时候抛异常啊!!!!
		var scrollerHeightIsHeight = param.scrollerHeightIsHeight?param.scrollerHeightIsHeight:"";	// 当键盘升起webview缩小导致滑动插件偏移问题
		var return_content = param.return_content;													// 拼接的html内容
		var error_func = param.error_func;															// 报错时,执行的方法 
		var resultStr = param.resultStr ? param.resultStr : "resultsVo.results[0].data";            // 数据集合 默认:"resultsVo.results[0].data"
		var totalPagesStr = param.totalPagesStr ? param.totalPagesStr : "resultsVo.results[0].totalPages"; //总页数 默认:"resultsVo.results[0].totalPages"
		// *********************选传参数*********************

		var _myScroller ="eduScroller."+_pageScroller + "." + myScroller;

		// 初始化滑动插件
		(function() {
			//滑动监听函数执行顺序：onScrollStart -> onScrollMove -> [pullDownHandler||pullUpHandler ->] onScrollEnd
			try {
				if(eval("!eduScroller." + _pageScroller + " || !eduScroller." + _pageScroller + "." + myScroller)) {
					var scrollOptions = {
						scrollerHeight: $(window).height() - scrollerHeight, // 滚动组件的高度
						pullThreshold: 5, // 拖动刷新或者加载的阀值，默认 5 像素
						$wrapper: $wrapper,
						hasPullDown: true, // 是否显示下拉提示，默认 true
						hasPullUp: true, // 是否显示上拉提示，默认 true
						isAlwaysShowPullUp: false, // 是否一直显示上拉提示，true 一直显示上拉加载的提示，false 仅在上拉的时候显示提示，默认 true
						oPullDownTips : {  //下拉显示文字
				            still: "下拉刷新",
				            flip: "释放立即刷新",
				            loading: "正在刷新..."
				        },
				        oPullUpTips : { //上拉显示文字
					            still: "上拉加载下一页",
					            flip: "释放加载下一页",
					            loading: "正在加载..."
					    },
						pullDownHandler: function() {//下拉刷新执行
							console.log("下拉刷新执行");
							this.hasPullUp = true;
							pullDownHandler();
						},
						pullUpHandler: function() { //上拉加载下一页执行
							console.log("上拉加载下一页执行");
							pullUpHandler();
							if((eval(_myScroller + ".totalPages") <= eval(_myScroller + ".curPageIdx"))&&(eval(_myScroller + ".totalPages != 0") || eval(_myScroller + ".curPageIdx != 1"))) {
								this.hasPullUp = false;
							}
							else
							{
								this.hasPullUp = true;
							}
						},
						onScrollStart: function() { //滑动开始时执行
							console.log("滑动开始时执行");
						},
						onScrollMove: function(){ //滑动中执行
							console.log("滑动中执行");
						},
						onScrollEnd:function(){ //滑动完成执行
							console.log("滑动完成执行");
						}
					};
					console.info("高度："+($(window).height() - scrollerHeight));
					//用于兼容手机键盘升起导致webview缩小导致滑动插件显示间距问题
					if(scrollerHeightIsHeight){
						scrollOptions.scrollerHeight=scrollerHeightIsHeight;
					}
					// 内容的高度比滚动区域高度小时
					// iscrollUtils 已有相同判断的代码，此处这样写的目的是为了解决 Android UC、微信等浏览器滚动失效的问题，在 iscrollUtils 设置高度，在初始化组件时，取到的值不准确
					if(scrollOptions.$wrapper.find("[data-is-content='true']").height() <= scrollOptions.scrollerHeight) {
						scrollOptions.$wrapper.children().height(scrollOptions.scrollerHeight + 1);
					}
					if(typeof(eval("eduScroller."+_pageScroller)) == "undefined") {
						eval("eduScroller."+_pageScroller + "=new Object()");
					}
					eval(_myScroller + "=iscrollUtils.vScroller(scrollOptions,{tap:true})");
					eval(_myScroller + ".curPageIdx = 1");
					eval(_myScroller + ".numPerPage = " + numPerPage);
					eval(_myScroller + ".funcParam=funcParam");
					// 点击头部滑动显示第一页数据
					(function(_pageScroller) {
						$.bindEvent($(_pageId + "header"), function(e) {
							_pageScroller.scrollTo(0, 0, 1000);
						}, 'tap');
					})(eval(_myScroller));

					queryFun(eval(_myScroller + ".refresh()"), "html");
				} else {
//					eval(_myScroller + ".refresh()");
					queryFun(eval(_myScroller + ".refresh()"), "html");
				}
			} catch(e) {
				console.info("pagejs error!");
			}
		})();
		/**
		 * 下拉时执行的事件 
		 */
		function pullDownHandler() {
			$end_page.html("");
			if(typeof(pullDownHandlerFun) == "function") pullDownHandlerFun();
			eval(_myScroller + ".curPageIdx = 1");
			queryFun(eval(_myScroller + ".refresh()"), "html"); // 模拟查询接口数据操作
		}

		/**
		 * 上拉时执行的事件
		 */
		function pullUpHandler() {
			if(typeof(pullUpHandlerFunc) == "function") pullUpHandlerFunc();
			if(eval(_myScroller + ".totalPages") > eval(_myScroller + ".curPageIdx")) {
				$end_page.html("");
				eval(_myScroller + ".curPageIdx++");
				queryFun(eval(_myScroller + ".refresh()"), "append"); // 模拟查询接口数据操作
			} else {
				if(eval(_myScroller + ".totalPages != 0") || eval(_myScroller + ".curPageIdx != 1")) {
					$end_page.html("已加载到最后一页").css("text-align", "center");
				}
				eval(_myScroller + ".refresh()");
			}
		}

		function queryFun(queryFinish, insertType) {
			eval(_myScroller + ".funcParam." + curPageIdxStr + "=" + _myScroller + ".curPageIdx");
			eval(_myScroller + ".funcParam." + numPerPageStr + "=" + _myScroller + ".numPerPage");
			queryFunService[queryFunServiceName](eval(_myScroller + ".funcParam"), function(resultVo) {
				if(not_data_error_no && not_data_error_no == resultVo.error_no) {
					$pro_main.html(showNotDataStr);
					eval(_myScroller+".totalPages = 0");
					eval(_myScroller+".refresh()");
					return false;
				}
				if(resultVo.error_no !=0 && typeof(error_func) == "function"){
					error_func();
					eval(_myScroller + ".totalPages = 0");
					eval(_myScroller + ".refresh()");
					return false;
				}
				if(resultVo.error_no==0) {
					if(resultVo.results[0]!=null){
					templet(insertType, resultVo, queryFinish);
				}}
			});
		}

		function templet(insertType, resultsVo, queryFinish) {
			utils.htmlIsEmpty(insertType, $pro_main);
			var content = "";
			var noData = true;
			if(resultsVo.results && resultsVo.results.length > 0) {
				if(resultStr == "resultsVo.results[0].data" && resultsVo.results[0].data && resultsVo.results[0].data.length > 0) {
					if(typeof(afterFunc) == "function") afterFunc(resultsVo);
					if(typeof(return_content) == "function") {
						content = return_content(resultsVo);
					} else {
						content = artTemplate(artTemplate_pro_main, resultsVo.results[0]);
					}
					eval(_myScroller+".totalPages = "+totalPagesStr+";");
					noData = false;
				} else if(resultStr == "resultsVo.results") {
					if(typeof(afterFunc) == "function") afterFunc(resultsVo);
					if(typeof(return_content) == "function") {
						content = return_content(resultsVo);
					} else {
						content = artTemplate(artTemplate_pro_main, resultsVo);
					}
					eval(_myScroller+".totalPages = "+totalPagesStr+";");
					noData = false;
				}else if(resultStr == "resultsVo.results[0].data.data"){
					if(resultsVo.results[0].data.data && resultsVo.results[0].data.data.length>0){
						if(typeof(afterFunc) == "function") afterFunc(resultsVo);
						if(typeof(return_content) == "function") {
							content = return_content(resultsVo);
						} else {
							content = artTemplate(artTemplate_pro_main, resultsVo.results[0].data);
						}
						eval(_myScroller+".totalPages = "+totalPagesStr+";");
						noData = false;
					}else{
						noData = true;
					}
				}
			}
			if(noData) {
				if(typeof(doNotData) == "function") doNotData();
				content = showNotDataStr;
				eval(_myScroller + ".totalPages = 0");
			}
			$pro_main.append(content);
			eval(_myScroller + ".refresh()");
			if(typeof(listFunc) == "function") listFunc(resultsVo);//数据加载完后执行操作
			// 解决图片加载失败问题
			$pro_main.find("img").off("error");
			$pro_main.find("img").error(function(e) {
				$(this).prop("src", getImagePath("", defaultImagePath));
			});

			if(typeof(queryFinish) == "function") {
				queryFinish();
			}
		};
	}

	var index = {
		"init": init
	};

	module.exports = index;
});