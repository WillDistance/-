/**
 *
 * 2017-07-017 13:35:55
 * 资讯文章详情页面
 */
define('fts/scripts/discover/prize.js', function (require, exports, module) {
    var pageId = '#prize ';
    var pageCode = 'discover/prize';
    var commService = require("commService").getInstance();//公共服务接口类
    var nativePluginService = require("nativePluginService");
    var constants = require("constants");
    var shareType = 0;
    var canmove = true;

    //JS接口集合
    var apiList = ['checkJsApi', 'onMenuShareTimeline', 'onMenuShareAppMessage', 'onMenuShareQQ', 'onMenuShareWeibo',
        'hideMenuItems', 'showMenuItems', 'hideAllNonBaseMenuItem', 'showAllNonBaseMenuItem', 'translateVoice',
        'startRecord', 'stopRecord', 'onVoiceRecordEnd', 'playVoice', 'pauseVoice', 'stopVoice', 'uploadVoice',
        'downloadVoice', 'chooseImage', 'previewImage', 'uploadImage', 'downloadImage', 'getNetworkType',
        'openLocation', 'getLocation', 'hideOptionMenu', 'showOptionMenu', 'closeWindow', 'scanQRCode',
        'chooseWXPay', 'openProductSpecificView', 'addCard', 'chooseCard', 'openCard'
    ];

    /**
     * 初始化方法
     */
    function init() {
        initView();

        //微信jssdk初始化
        weixinInit();
    }

    /*
     * 页面事件绑定
     */
    function bindPageEvent() {
        //返回事件
        $.bindEvent($(pageId + ".icon_back"), function () {
            var param = {};
            param["moduleName"] = "prize";
            nativePluginService.function50114(param);
        }, "click");

        //分享
        $.bindEvent($(pageId + '.icon_share'), function () {
            var titleA = "一“触”即中，2000份话费任你赢";

            // var news_briefs = "活动期间，在期赢天下APP新开户且绑定期货账户的客户或在活动期间新绑定期货账户的存量客户，即可赢取10~500元话费。";
            var news_briefs = "活动期间，在期赢天下APP首次绑定期货账户可参与抽奖，最高赢500元话费！";
            var link = $.config.global.shareLink + "discover/prize.html?shareType=1";
            var invokeParam = {
                moduleName: "fts", // String	目标模块名	N
                source: "fts", // String	源模块	Y
                funcNo: "50230",
                shareTypeList: "22,23,24,1",
                link: link,
                title: titleA,
                content: news_briefs,
                imgUrl: "https://htqhapp.htfc.com/m/fts/images/prize_share_logo.jpg"
            };
            nativePluginService.function50230(invokeParam);
        }, 'click');

        //点击抽奖
        $.bindEvent($(pageId + ".click_area"), function () {
            //先判断是否是app内的点击事件行为
            var version = nativePluginService.function50010();
            if (JSON.stringify(version) == "{}" || version == null || version == "") {
                //APP外的点击事件行为
                submitFn();
            } else {
                //APP内的点击事件行为
                var userinfo = $.getSStorageInfo("com.thinkive.zh.userInfo");
                if (userinfo == "" || userinfo == null) {
                    //跳转到登录页面，未登录客户点击领取，提示一下
                    $.toast("活动期间，注册且绑定期货账户即可抽奖，等您回来！", 3.5, function () {
                                 gotoLogin("")
                            });
                   
                    return;
                }

                /**接口查询当前用户是否有期货账户已经是否绑定期货账户*/
                var paramA = {
                    "v_mobile_no": userinfo.mobile
                };
                var ctrlParam = {
                    isShowWait: false
                };
                var callBack = function (data) {
                    if (data.error_no == "0") {
                        var result = data.results[0];
                        if (result.v_flag == "RESULT A") {//没有开过期货账户，跳转期货开户页面
                            $.toast("活动期间，注册且绑定期货账户即可抽奖，等您回来！", 3.5, function () {
                                jumpToOpenAccount()
                            });

                        } else if (result.v_flag == "RESULT B") {//开过户，没有生成财富号,切没有绑定期货账户
                            $.toast("活动期间，注册且绑定期货账户即可抽奖，等您回来！", 3.5, function () {
                                jumpToApplyFortuneAccount()
                            });
                        } else if (result.v_flag == "RESULT C") {
                            //开过户，已经生成了财富号，但是没有绑定期货账户
                            $.toast("活动期间，注册且绑定期货账户即可抽奖，等您回来！", 3.5, function () {
                                jumpToBindFutureAccount();
                            });

                        } else if (result.v_flag == "RESULT D") {
                            //判断该绑定结果是否在活动有效期内
                            var startTime = "2018-05-21";
                            var endTime = "2019-01-30";
                            var start = new Date(startTime);
                            var end = new Date(endTime);
                            var bindTime = new Date(result.bind_date);
                            if (bindTime >= start && bindTime <= end) {
                                //跳转抽奖活动,使用app内部打开
                                var url = result.url;
                                var param = {};
                                param["moduleName"] = "fts";
                                param["url"] = url;
                                param["isShoeBackBtn"] = "1";
                                param["statusColor"] = "#1A4CED";
                                param["title"] = "一“触”即中，2000份话费任你赢";
                                param["supportback"] = "1";
                                param["isUseSwipingBack"] = "1";
                                nativePluginService.function50115(param);
                            } else {
                                //非活动期间绑定的用户只能提示不能参加活动
                                $.toast("活动期间首次绑定期货账户，</br>才能参与本活动。感谢关注", 3.5, function () {
                                });
                            }
                        } else {
                            $.toast(data.error_info, 1.5, function () {
                            });
                        }
                    } else {
                        $.toast(data.error_info, 1.5, function () {
                        });

                    }
                };
                commService.isCurrentUserHasCFId(paramA, callBack, ctrlParam);
            }

        }, 'click');

        //禁止浮层滑动
        // document.body.addEventListener("touchmove",function(event){
        //     if (!canmove) {
        //         event.preventDefault();
        //     };
        // },{passive:false});
    }

    /**
     * 判断app是否安装，没有安装的跳转下载页面
     */
    function submitFn() {
        //判断浏览器
        var u = navigator.userAgent;
        // if (/MicroMessenger/gi.test(u)) {
        //     // 引导用户在浏览器中打开
        //     alert('请在手机浏览器中打开');
        //     return;
        // }
        var d = new Date();
        var t0 = d.getTime();
        if (u.indexOf('Android') > -1 || u.indexOf('Linux') > -1) {
            //Android
            if (openApp('qytxapp://com.htfc.qytx/openwith')) {
                openApp('qytxapp://com.htfc.qytx/openwith');
            } else {
                //由于打开需要1～2秒，利用这个时间差来处理－－打开app后，返回h5页面会出现页面变成app下载页面，影响用户体验
                //判断是否是微信
                if (u.toLowerCase().match(/MicroMessenger/i) == 'micromessenger') {
                    //alert("请点击右上方的按钮，选择在浏览器中打开");
                    //替换图片
                    canmove = false;
                    // var indicator = document.getElementById("indicator");
                    // indicator.style.display = 'block';
                    // //显示出来之后屏蔽掉底下图片的点击事件
                    // var background = document.getElementById("background");
                    // background.style.pointerEvents = "none";
                    // //禁止底部图片的滑动
                    // // $('html').setAttribute("style","overflow:hidden;");
                    // // $('body').setAttribute("style","overflow:hidden;");
                    // //设置div的高度
                    // // var height = document.documentElement.clientHeight;
                    // $(pageId + '.indicator').style.height = height;
                    // var container = document.getElementById("container");
                    // container.scrollTop = 0;
                    var background = document.getElementById("background");
                    background.style.pointerEvents = "none";

                    var indicator = document.getElementById("indicator");
                    indicator.style.pointerEvents = "none";
                    var click = document.getElementById("clickme");
                    indicator.style.display = 'block';
                    click.style.display = 'block';

                    $("#clickme").on("click", function () {
                        //隐藏浮层，把下层的事件弄出来
                        indicator.style.display = 'none';
                        click.style.display = 'none';
                        background.style.pointerEvents = "auto";

                    });


                } else {
                    var delay = setInterval(function () {
                        var d = new Date();
                        var t1 = d.getTime();
                        if (t1 - t0 < 3000 && t1 - t0 > 2000) {
                            $.toast("请下载并安装新版本期赢天下", 3, function () {
                                window.location.href = "https://weixin.htfc.com/m/wx/views/service/handRiches.html";
                            });
                        }
                        if (t1 - t0 >= 3000) {
                            clearInterval(delay);
                        }
                    }, 1000);
                }


            }
        } else if (u.indexOf('iPhone') > -1) {
            //IOS
            //var isSafari = /^(?=.Safari)(?!.Chrome)/.test(navigator.userAgent);
            if (u.toLowerCase().indexOf('safari') > -1) {
                window.location.href = 'htfcApp://';
                //判断Ios版本
                var version = navigator.userAgent.match(/os\s+(\d+)/i)[1]-0;
                // version = parseInt(ver[1],10);
                if(version>=10){
                    setTimeout(function () {
                    // $.toast("请下载并安装新版本期赢天下", 3, function () {
                    //             window.location.href = "https://weixin.htfc.com/m/wx/views/service/handRiches.html";
                    //         });
                        window.location.href = 'https://itunes.apple.com/cn/app/%E6%9C%9F%E8%B5%A2%E5%A4%A9%E4%B8%8B-%E6%9C%9F%E8%B4%A7%E8%A1%8C%E4%B8%9A%E9%A2%86%E5%85%88%E7%9A%84%E6%8E%8C%E4%B8%8A%E7%90%86%E8%B4%A2%E7%A5%9E%E5%99%A8/id1284436837';
                    }, 500);
                }else{
                    setTimeout(function () {
                    // $.toast("请下载并安装新版本期赢天下", 3, function () {
                    //             window.location.href = "https://weixin.htfc.com/m/wx/views/service/handRiches.html";
                    //         });
                    window.location.href = 'https://itunes.apple.com/cn/app/%E6%9C%9F%E8%B5%A2%E5%A4%A9%E4%B8%8B-%E6%9C%9F%E8%B4%A7%E8%A1%8C%E4%B8%9A%E9%A2%86%E5%85%88%E7%9A%84%E6%8E%8C%E4%B8%8A%E7%90%86%E8%B4%A2%E7%A5%9E%E5%99%A8/id1284436837';
                }, 3000);

                }


                
                // setTimeout(function () {
                //     location.reload();
                // }, 4000);
                // var ifr = document.createElement('iframe');
                // ifr.src = src;
                // ifr.style.display = 'none';
                // document.body.appendChild(ifr);
                // window.setTimeout(function () {
                //     document.body.removeChild(ifr);
                //     window.location.href = 'https://weixin.htfc.com/m/wx/views/service/handRiches.html';
                // }, 4000);
            } else {
                //替换图片
                //显示出来之后屏蔽掉底下图片的点击事件
                var background = document.getElementById("background");
                // var container = document.getElementById("container");
                // var img = document.getElementById("backimg");

                background.style.pointerEvents = "none";

                var indicator = document.getElementById("indicator");
                var click = document.getElementById("clickme");
                indicator.style.display = 'block';
                click.style.display = 'block';

                $("#clickme").on("click", function () {
                    //隐藏浮层，把下层的事件弄出来
                    indicator.style.display = 'none';
                    click.style.display = 'none';
                    background.style.pointerEvents = "auto";

                });


                //获取顶部导航距离
                // var top = $(document).scrollTop();
                // if(top > 0){
                //     $("#container").css({"top":"0","position":"fixed"});
                // }
                // container.scrollTop = 0;
                // indicator.scrollTop = 0;
                // background.scrollTop = 0;
                //  canmove = false;
                // container.setAttribute("style","overflow:hidden;");
                // background.style.height = window.innerHeight;
                // indicator.style.height = window.innerHeight;
                // container.style.height = window.innerHeight;
                // $('html,body').css('height','100%').css('overflow','hidden');

            }

            //location.href = "https://itunes.apple.com/cn/app/%E6%9C%9F%E8%B5%A2%E5%A4%A9%E4%B8%8B-%E6%9C%9F%E8%B4%A7%E8%A1%8C%E4%B8%9A%E9%A2%86%E5%85%88%E7%9A%84%E6%8E%8C%E4%B8%8A%E7%90%86%E8%B4%A2%E7%A5%9E%E5%99%A8/id1284436837";
        }
    }

    function openApp(src) {
// 通过iframe的方式试图打开APP，如果能正常打开，会直接切换到APP，并自动阻止a标签的默认行为
// 否则打开a标签的href链接
        var ifr = document.createElement('iframe');
        ifr.src = src;
        ifr.style.display = 'none';
        document.body.appendChild(ifr);
        window.setTimeout(function () {
            document.body.removeChild(ifr);
        }, 2000);
    }


    /**
     * 跳转期货开户
     */
    function jumpToOpenAccount() {
        var paramMap = {};
        paramMap["param"] = {};
        paramMap["msgNo"] = '005';
        paramMap["action"] = '4';
        paramMap["sourceModule"] = 'fts';
        paramMap["targetModule"] = "systemDispatcher";
        nativePluginService.function50500(paramMap);
    }

    /**
     * 跳转绑定期货账户页面
     */
    function jumpToBindFutureAccount() {
        var random = (Math.random() + "").substring(2, 6) + '';
        var url = "www/m/mall/views/userCenter/futureAccount.html?from=app&random=" + random;
        var param = {};
        param["moduleName"] = "account";
        param["url"] = url;
        param["statusColor"] = "#F82C3C";
        param["statusStyle"] = "1";
        nativePluginService.function50115(param);
    }

    /**
     * 跳转申请财富号页面
     */
    function jumpToApplyFortuneAccount() {
        var url = "www/m/account/views/userCenter/checkAccountPwd.html";
        var param = {};
        param["moduleName"] = "account";
        param["url"] = url;
        param["statusColor"] = "#F82C3C";
        param["statusStyle"] = "1";
        nativePluginService.function50115(param);

    }


    /**
     * 登录
     * @param topage
     * @param param
     */
    function gotoLogin(topage, param) {
        /*********防止连续多次弹出登录页面begin**********/
        if (window.globalIsLogin) {
            return;
        }
        window.globalIsLogin = true;
        setTimeout(function () {
            window.globalIsLogin = null;
        }, 2000);
        /*********防止连续多次弹出登录页面begin**********/

        // 走原生socket协议时，跳转到原生登录页面
        param = param || {};
        topage = topage || "";
        // 如果是点击广告跳转页面的。登录成功后进入指定页面。
        if (param.actionFrom && param.actionFrom === "AD") {
            topage = param.toPage;
        }
        if ($.config.isFordHttpReq === 1 && $.config.platform != 0) {
            var paramMap = {
                "moduleName": "sso",
                "params": {
                    "moduleName": "fts"
                }
            };
            nativePluginService.function50101(paramMap);
        } else {
//			$.pageInit($.getCurrentPageObj().pageCode, "login/userLogin", param);
        }
    }

    function weixinInit() {
        //JS接口列表，所有JS接口列表见附录2（http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html）
        var jsApiJson = {
            "share": [apiList[1], apiList[2], apiList[3], apiList[4]]
        };
        var jsApiList = ["checkJsApi"];// 必填，需要使用的JS接口列表
        // if (param["apiArr"] != undefined) {
        //     //默认划分的接口列表
        //     for (var i = 0; i < param["apiArr"].length; i++) {
        //         jsApiList = jsApiList.concat(jsApiJson[param["apiArr"][i]]);
        //     }
        // } else if (param["myApiArr"] != undefined) {
        //     //自定义接口列表
        //     for (var i = 0; i < param["myApiArr"].length; i++) {
        //         jsApiList.push(apiList[param["myApiArr"][i]]);
        //     }
        // }
        //1.步骤一：绑定域名 【设置 ——> 公众号设置 ——> 功能设置 ——>  JS安全域名（必须为ICP备案域名）】

        //2.步骤二：引入JS文件 【在index.html 在框架前引入】

        //3.步骤三：通过config接口注入权限验证配置
//		var pageParam = $.getPageParam();
//		var curUrl = window.location.href;
//		curUrl = curUrl.indexOf("?") == -1 ? curUrl : curUrl.substring(0, curUrl.indexOf("?"));

        //消除干扰
        modifyURLParam();

        var configParam = getWeiXinParams();
        /*所有需要使用JS-SDK的页面必须先注入配置信息，
        否则将无法调用（同一个url仅需调用一次，对于变化url的SPA的web app可在每次url变化时进行调用）。*/
        wx.config(configParam);

        console.log("配置信息处理成功");

        //4.步骤四：通过ready接口处理成功验证
        wx.ready(function () {
            check(jsApiList);
//			callBack();
            weixinShare();
            console.log("准备好了");
        });

        //5.步骤五：通过error接口处理失败验证
        wx.error(function (res) {
            $.alert(res.errMsg);
            console.log("没有错误信息");
        });


    }

    function weixinShare() {
        var share_title = '一“触”即中，2000份话费任你赢';
        var share_desc = '活动期间，在期赢天下APP首次绑定期货账户可参与抽奖，最高赢500元话费！';
        var share_link = window.location.href;
        var share_imgUrl = 'https://htqhapp.htfc.com/m/fts/images/prize_share_logo.jpg';
        var share_type = 'link';

        //分享：
        wx.onMenuShareTimeline({
                title: share_title, // 分享标题
                link: share_link, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                imgUrl: share_imgUrl, // 分享图标
                success: function () {
                    console.log("分享成功");

                }
            }
        );

        wx.onMenuShareAppMessage({
            title: share_title, // 分享标题
            desc: share_desc, // 分享描述
            link: share_link, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: share_imgUrl, // 分享图标
            type: share_type, // 分享类型,music、video或link，不填默认为link
            dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
            success: function () {
                console.log("分享成功");

            }

        });

        wx.onMenuShareWeibo({
            title: share_title, // 分享标题
            desc: share_desc, // 分享描述
            link: share_link, // 分享链接
            imgUrl: share_imgUrl, // 分享图标
            success: function () {
                // 用户确认分享后执行的回调函数
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
            }
        });

        wx.onMenuShareQZone({
            title: share_title, // 分享标题
            desc: share_desc, // 分享描述
            link: share_link, // 分享链接
            imgUrl: share_imgUrl, // 分享图标
            success: function () {
                // 用户确认分享后执行的回调函数
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
            }
        });
    }


    function getWeiXinParams() {
        //获取验签的数据,发起ajax请求，获取参数
        var result = "";
        var shareUrl = window.location.href;
        $.ajax({
            url: "https://weixin.htfc.com/servlet/json?funcNo=1000003&weixinpk=gh_112ad1bd3c36&url=" + encodeURIComponent(shareUrl),
            type: "post",
            async: false,
            success: function (data) {
                var datas = JSON.parse(data);

                if (datas.error_no == "0") {
                    //封装成数据给微信调用
                    var results = datas.results[0];
                    // var returns = {
                    //     timestamp:results.timestamp,
                    //     appid:results.appid,
                    //     nonceStr:results.nonceStr,
                    //     jsapi_ticket:results.jsapi_ticket,
                    //     signature:results.signature,
                    //     url:results.url
                    // }
                    result = {
                        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                        appId: results.appid, // 必填，公众号的唯一标识
                        timestamp: results.timestamp, // 必填，生成签名的时间戳
                        nonceStr: results.nonceStr, // 必填，生成签名的随机串
                        signature: results.signature,// 必填，签名，见附录1
                        jsApiList: apiList// 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                    };
                    return result;
                } else {
                    $.alert(data.error_info);
                }
            }
        });

        return result;
    }

    /**
     *  解决分享参数干扰
     */
    function modifyURLParam() {
        var url = window.location.href;//访问页面参数
        var flag = false;//是否存在干扰
        if (url.indexOf("&from=singlemessage") > 0) {
            url = url.replace("&from=singlemessage", "");
            flag = true;
        }
        if (url.indexOf("?from=singlemessage") > 0) {
            url = url.replace("?from=singlemessage", "");
            flag = true;
        }
        if (url.indexOf("&isappinstalled=0") > 0) {
            url = url.replace("&isappinstalled=0", "");
            flag = true;
        }
        if (url.indexOf("?isappinstalled=0") > 0) {
            url = url.replace("?isappinstalled=0", "");
            flag = true;
        }
        //存在干扰重定向
        if (flag) {
            window.location.href = url;
        }
    }


    /*******************************初始化结束********************************************/

    /*******************************检测接口开始********************************************/
    var check = function (jsApiList, func) {
        /**
         * 检测当前设备是否支持使用你要使用的接口
         * 一版不能使用的原因是微信版本低于6.1版本
         * @param func <function> 建议按你选择的接口单独处理问题
         */
        wx.checkJsApi({
            jsApiList: jsApiList,
            success: function (res) {
                if (func) {
                    func(res);
                } else {
                    for (var i = 0; i < jsApiList.length; i++) {
                        if (res["checkResult"][jsApiList[i]] == false) {
                            $.alert("当前微信版本过低，请升级到最新版本，不然会影响您的体验哦！");
                            break;
                        }
                    }
                }
            }
        });
    };
    /*******************************检测接口结束********************************************/


    /*******************************分享接口开始********************************************/

    /**
     * 分享接口集合
     */
    var share = {
        AppMessage: function (param, clickFuc, hasFuc, cancelFuc, failFuc) {
            //2.1 监听“分享给朋友”，按钮点击、自定义分享内容及分享结果接口
            wx.onMenuShareAppMessage(
                commonShareJson(param, clickFuc, hasFuc, cancelFuc, failFuc)
            );
        },
        Timeline: function (param, clickFuc, hasFuc, cancelFuc, failFuc) {
            // 2.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
            wx.onMenuShareTimeline(
                commonShareJson(param, clickFuc, hasFuc, cancelFuc, failFuc)
            );
        },
        QQ: function (param, clickFuc, hasFuc, cancelFuc, failFuc) {
            // 2.3 监听“分享到QQ”按钮点击、自定义分享内容及分享结果接口
            wx.onMenuShareQQ(
                commonShareJson(param, clickFuc, hasFuc, cancelFuc, failFuc)
            );
        },
        Weibo: function (param, clickFuc, hasFuc, cancelFuc, failFuc) {
            // 2.4 监听“分享到微博”按钮点击、自定义分享内容及分享结果接口
            wx.onMenuShareWeibo(
                commonShareJson(param, clickFuc, hasFuc, cancelFuc, failFuc)
            );
        }
    };

    /**
     * 获取所有分享
     * @param param {jsApiList,imgUrl,link,title,desc}
     * @param clickFuc 用户点击发送给朋友callBack
     * @param hasFuc 已分享callBack
     * @param cancelFuc 已取消callBak
     * @param failFuc 分享失败callBak
     */
    function shareAll(param, clickFuc, hasFuc, cancelFuc, failFuc) {
        //详情见分享集合 share
        share.AppMessage(param, clickFuc, hasFuc, cancelFuc, failFuc);
        share.Timeline(param, clickFuc, hasFuc, cancelFuc, failFuc);
        share.QQ(param, clickFuc, hasFuc, cancelFuc, failFuc);
        share.Weibo(param, clickFuc, hasFuc, cancelFuc, failFuc);
    }

    /**
     * @param param {title,desc,link,imgUrl}
     * &_share=1 后面拼接用于区分是否来自分享，也是为了取参数避免取到腾讯加到的后缀
     * @param clickFuc 用户点击发送给朋友callBack
     * @param hasFuc 已分享callBack
     * @param cancelFuc 已取消callBak
     * @param failFuc 分享失败callBak
     *
     * @returns {} 初始化分享内容
     */
    function commonShareJson(param, clickFuc, hasFuc, cancelFuc, failFuc) {
        //分享标识，用于判断当前页面是否是分享过来的
//		var shareFlag = param["title"].indexOf("?") > -1 ? "&_share=1" : "?_share=1";
        return {
            title: param["title"],// 分享标题
            desc: param["desc"],// 分享描述
            link: param["link"],// 分享链接  + shareFlag
            imgUrl: param["imgUrl"],// 分享图标
            trigger: function (res) {
                if (clickFuc)
                    clickFuc();
            },
            success: function (res) {
                if (hasFuc)
                    hasFuc();
                // layer.iMsg("分享成功");
                $("#nativeShare_global").remove();
            },
            cancel: function (res) {
                if (cancelFuc)
                    cancelFuc();
                $("#nativeShare_global").remove();
            },
            fail: function (res) {
                if (failFuc)
                    failFuc();
                // layer.iMsg("分享失败");
                $("#nativeShare_global").remove();
            }
        };
    }


    /**
     * ******************************* 自定义方法 star
     */
    /**
     * 页面初始化方法
     */
    function initView() {
        shareType = $.getPageParam("shareType");
        if (!shareType) {
            shareType = 0;//app内访问
            $(pageId + ".icon_back").show();
            $(pageId + ".icon_share").show();
        }
        else {
            shareType = 1;//分享访问
            $(pageId + ".icon_share").hide();
            $(pageId + ".icon_back").hide();
        }

    }


    /**
     * ******************************* 自定义方法 end
     */

    /**
     * 页面跳转后销毁方法
     */
    function destroy() {

    }

    /**
     * 页面返回方法
     */
    function pageBack() {
    }

    /**
     * 页面效果执行完毕方法
     *
     */
    function load() {

    }

    var prize = {
        init: init,
        bindPageEvent: bindPageEvent,
        destroy: destroy,
        pageBack: pageBack,
        load: load
    };

    module.exports = prize;
});  