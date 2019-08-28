$(function(){
    // 按钮操作
    $('.header .btn').on('click',function(){
        var $this = $(this);
        if(!!$this.hasClass('lock')){
            $this.attr('class','btn unlock');
            $this.text('解锁');
            // 锁定
            dropload.lock();
            $('.dropload-down').hide();
        }else{
            $this.attr('class','btn lock');
            $this.text('锁定');
            // 解锁
            dropload.unlock();
            $('.dropload-down').show();
        }
    });

    // dropload
    var dropload = $('.inner').dropload({
        domUp : {
            domClass   : 'dropload-up',//定义加载段外层div的class。可修改，写样式
            domRefresh : '<div class="dropload-refresh">↓下拉刷新-自定义内容</div>',
            domUpdate  : '<div class="dropload-update">↑释放更新-自定义内容</div>',
            domLoad    : '<div class="dropload-load"><span class="loading"></span>加载中-自定义内容...</div>'
        },
        domDown : {
            domClass   : 'dropload-down',//定义加载段外层div的class。可修改，写样式
            domRefresh : '<div class="dropload-refresh">↑上拉加载更多-自定义内容</div>',
            domLoad    : '<div class="dropload-load"><span class="loading"></span>加载中-自定义内容...</div>',
            domNoData  : '<div class="dropload-noData">暂无数据-自定义内容</div>'
        },
        loadUpFn : function(me){
			
			console.log("下拉刷新");
            var result = '';
                    result = '<a class="item" href="#"> <img src="http://d6.yihaodianimg.com/N02/M0B/81/5A/CgQCsVMhX_mAAvXsAAJDE3K2zh485900_80x80.jpg" alt=""><h3>10文字描述文字描述文字描述文字描述文字描述</h3><span class="date">2014-14-14</span></a>'+
						'<a class="item" href="#"> <img src="http://d6.yihaodianimg.com/N02/M0B/81/5A/CgQCsVMhX_mAAvXsAAJDE3K2zh485900_80x80.jpg" alt=""><h3>10文字描述文字描述文字描述文字描述文字描述</h3><span class="date">2014-14-14</span></a>'+
						'<a class="item" href="#"> <img src="http://d6.yihaodianimg.com/N02/M0B/81/5A/CgQCsVMhX_mAAvXsAAJDE3K2zh485900_80x80.jpg" alt=""><h3>10文字描述文字描述文字描述文字描述文字描述</h3><span class="date">2014-14-14</span></a>'+
						'<a class="item" href="#"> <img src="http://d6.yihaodianimg.com/N02/M0B/81/5A/CgQCsVMhX_mAAvXsAAJDE3K2zh485900_80x80.jpg" alt=""><h3>10文字描述文字描述文字描述文字描述文字描述</h3><span class="date">2014-14-14</span></a>'+
						'<a class="item" href="#"> <img src="http://d6.yihaodianimg.com/N02/M0B/81/5A/CgQCsVMhX_mAAvXsAAJDE3K2zh485900_80x80.jpg" alt=""><h3>10文字描述文字描述文字描述文字描述文字描述</h3><span class="date">2014-14-14</span></a>'+
						'<a class="item" href="#"> <img src="http://d6.yihaodianimg.com/N02/M0B/81/5A/CgQCsVMhX_mAAvXsAAJDE3K2zh485900_80x80.jpg" alt=""><h3>10文字描述文字描述文字描述文字描述文字描述</h3><span class="date">2014-14-14</span></a>'+
						'<a class="item" href="#"> <img src="http://d6.yihaodianimg.com/N02/M0B/81/5A/CgQCsVMhX_mAAvXsAAJDE3K2zh485900_80x80.jpg" alt=""><h3>10文字描述文字描述文字描述文字描述文字描述</h3><span class="date">2014-14-14</span></a>';

                    // 为了测试，延迟1秒加载
                    setTimeout(function(){
                        $('.lists').html(result);
                        // 每次数据加载完，必须重置
                        me.resetload();
                        // 重置页数，重新获取loadDownFn的数据
                        page = 0;
                        // 解锁loadDownFn里锁定的情况
                        me.unlock();
                        me.noData(false);
                    },1000);
        },
        loadDownFn : function(me){
		console.log("上拉加载下一页");
            //page++;
            // 拼接HTML
            var result = '';
			var arrLen = 1;//data.length;
                    if(arrLen > 0){
						result = '<a class="item" href="#"> <img src="http://d6.yihaodianimg.com/N02/M0B/81/5A/CgQCsVMhX_mAAvXsAAJDE3K2zh485900_80x80.jpg" alt=""><h3>10文字描述文字描述文字描述文字描述文字描述</h3><span class="date">2014-14-14</span></a>'+
						'<a class="item" href="#"> <img src="http://d6.yihaodianimg.com/N02/M0B/81/5A/CgQCsVMhX_mAAvXsAAJDE3K2zh485900_80x80.jpg" alt=""><h3>10文字描述文字描述文字描述文字描述文字描述</h3><span class="date">2014-14-14</span></a>'+
						'<a class="item" href="#"> <img src="http://d6.yihaodianimg.com/N02/M0B/81/5A/CgQCsVMhX_mAAvXsAAJDE3K2zh485900_80x80.jpg" alt=""><h3>10文字描述文字描述文字描述文字描述文字描述</h3><span class="date">2014-14-14</span></a>'+
						'<a class="item" href="#"> <img src="http://d6.yihaodianimg.com/N02/M0B/81/5A/CgQCsVMhX_mAAvXsAAJDE3K2zh485900_80x80.jpg" alt=""><h3>10文字描述文字描述文字描述文字描述文字描述</h3><span class="date">2014-14-14</span></a>'+
						'<a class="item" href="#"> <img src="http://d6.yihaodianimg.com/N02/M0B/81/5A/CgQCsVMhX_mAAvXsAAJDE3K2zh485900_80x80.jpg" alt=""><h3>10文字描述文字描述文字描述文字描述文字描述</h3><span class="date">2014-14-14</span></a>'+
						'<a class="item" href="#"> <img src="http://d6.yihaodianimg.com/N02/M0B/81/5A/CgQCsVMhX_mAAvXsAAJDE3K2zh485900_80x80.jpg" alt=""><h3>10文字描述文字描述文字描述文字描述文字描述</h3><span class="date">2014-14-14</span></a>'+
						'<a class="item" href="#"> <img src="http://d6.yihaodianimg.com/N02/M0B/81/5A/CgQCsVMhX_mAAvXsAAJDE3K2zh485900_80x80.jpg" alt=""><h3>10文字描述文字描述文字描述文字描述文字描述</h3><span class="date">2014-14-14</span></a>';
                    // 如果没有数据
                    }else{
                        // 锁定
                        me.lock();
                        // 无数据
                        me.noData();
                    }
                    // 为了测试，延迟1秒加载
                    setTimeout(function(){
                        // 插入数据到页面，放到最后面
                        $('.lists').append(result);
                        // 每次数据插入，必须重置
                        me.resetload();
                    },1000);
        },
        threshold : 50
    });
});