var click = false;
var prizeLevel,alertContent;

window.onload = function() {
    getPrizeList();//加载奖品九宫格

};

var lottery = {
	index: -1, //当前转动到哪个位置，起点位置
	count: 0, //总共有多少个位置
	timer: 0, //setTimeout的ID，用clearTimeout清除
	speed: 20, //初始转动速度
	times: 0, //转动次数
	cycle: 50, //转动基本次数：即至少需要转动多少次再进入抽奖环节
	prize: -1, //中奖位置
	init: function(id) {
		if($('#' + id).find('.lottery-unit').length > 0) {
			$lottery = $('#' + id);
			$units = $lottery.find('.lottery-unit');
			this.obj = $lottery;
			this.count = $units.length;
			$lottery.find('.lottery-unit.lottery-unit-' + this.index).addClass('active');
		};
	},
	roll: function() {
		var index = this.index;
		var count = this.count;
		var lottery = this.obj;
		$(lottery).find('.lottery-unit.lottery-unit-' + index).removeClass('active');
		index += 1;
		if(index > count - 1) {
			index = 0;
		};
		$(lottery).find('.lottery-unit.lottery-unit-' + index).addClass('active');
		this.index = index;
		return false;
	},
	stop: function(index) {
		this.prize = index;
		return false;
	}
};

/**
 * 后台获取九宫格奖品列表
 */
function getPrizeList() {
    $.ajax({
        url: commonJs.root() + '/active/prizeList?tt='+new Date().getTime(),
        type: "POST",
        cache: false,
        data:{"type":1},
        // contentType : 'application/json;charset=utf-8',
        dataType:'json',
        success: function(data){
            var tbody="";
            if(data!=null){
            	for(var i=0;i<data.length;i++){
            		if(i==0||i==3||i==5){
                        tbody+="<tr>";
					}
                    tbody+="<td class='item lottery-unit lottery-unit-"+i+"'>" +
                        "<div class='img'>" +
                        "<img src='/imgs/active/raffle/img"+(i+1)+".png'  alt=''>" +
                        "</div>" +
                        "<span class='name margin' >"+data[i].name+"</span>" +
                        "</td>" +
                        "<td class='gap'></td>";
            		if(i==3){
            			tbody+="<td class=''>" +
                            "<a class='draw-btn' href='javascript:' onclick='raffle();return false;' ></a>" +
                            "</td>"+"<td class='gap'></td>";
					}
                    if(i==2||i==4){
                        tbody+="</tr><tr>" +
                            "<td class='gap-2' colspan='5'></td>" +
                            "</tr>";
                    }
				}
                tbody+="</tr>";
			}
			$("table").append(tbody);
            lottery.init('lottery');
        }
    });

}

/**
 * 跳转到中奖记录页
 */
function record() {
    window.open(commonJs.root() +'/active/raffleRecord?mobile='+$("#phone").val());
}

/**
 * 抽奖事件
 * @returns {boolean}
 */
function raffle() {
    var mobile=$("#phone").val();
    if(click) { //click控制一次抽奖过程中不能重复点击抽奖按钮，后面的点击不响应
        return false;
    }
    click = true; //一次抽奖完成后，设置click为true，可继续抽奖
    if(!mobile){
        layer.msg("请先输入手机号码");
        return false;
    }
    // if(!$("#vrifyCode").val()){
    //     layer.msg("请先输入验证码");
    //     return false;
    // }
    //手机号校验
    if(!$.validPhone(mobile)){
        layer.msg("手机号格式不正确");
        return false;
    }

    //验证码校验
    // var vrifyCode=$.imgvrifyKaptcha($("#vrifyCode").val());
    // if(!vrifyCode.success){
    //     layer.msg("验证码错误");
    //     return false;
    // }
    lottery.speed = 100;
    $.ajax({
        url: commonJs.root() + '/active/raffleLogic?tt='+new Date().getTime(),
        type: "POST",
        cache: false,
        data:{"mobile":mobile},
        dataType:'json',
        // async:false,
        success: function(data){
            if(!data.success){
                if(data.code=="-1001"){
                    layer.msg("您今日已参加过抽奖");
                }
                click=false;
            }else{
                var content="恭喜您获得"+data.prizeName;
                if(data.prizeLevel==8){
                    content="很遗憾，您未能中奖"
                }
                roll(); //转圈过程不响应click事件，会将click置为false
                prizeLevel=data.prizeLevel;
                alertContent=content;
            }

        }
    });
    return false;
}

function roll() {
	lottery.times += 1;
	lottery.roll(); //转动过程调用的是lottery的roll方法，这里是第一次调用初始化

	if(lottery.times > lottery.cycle + 10 && lottery.prize == lottery.index) {
		clearTimeout(lottery.timer);
		layer.msg(alertContent, {
		    time: 3000 //3s后自动关闭
		});
		lottery.prize = prizeLevel-1;
		lottery.times = 0;
		click = false;
	} else {
		if(lottery.times < lottery.cycle) {
			lottery.speed -= 10;
		} else if(lottery.times == lottery.cycle) {
            lottery.prize = prizeLevel-1
		} else {
			if(lottery.times > lottery.cycle + 10 && ((lottery.prize == 0 && lottery.index == 7) || lottery.prize == lottery.index + 1)) {
				lottery.speed += 110;
			} else {
				lottery.speed += 20;
			}
		}
		if(lottery.speed < 40) {
			lottery.speed = 40;
		};
		lottery.timer = setTimeout(roll, lottery.speed); //循环调用
	}
	return false;
}

//中奖名单
function f1() {
    $(function() {
        var height = $('.ul2').height()
        $('.ul2').animate({
            'top': -height
        }, 5000, 'linear', function() {
            $('.ul2').css('top', '0px')
        })
    })
}
window.setInterval('f1()', 0);


