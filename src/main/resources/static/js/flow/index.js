$(function(){
    var mySwiper = new Swiper(".swiper-container", {});
// 流量和话费充值切换
    $(".tab").on("tap click", "li", function () {
        var index = $(this).index();
        $(this).addClass("tabActive").siblings("li").removeClass("tabActive");
        mySwiper.slideTo(index, 1000, false);
        if(index==0){
            $(".msg").show();
        }else{
            $(".msg").hide();
        }

    })

// 选择所需充值的流量
    $(".content").on("tap click", "li", function () {
        $(this).addClass("liActive").parent("div").siblings("div").children("li").removeClass("liActive");
        $("#commodityId").val($(this).attr("commodityId"));
    })

// 点击充值
    $(".pay-btn").on("tap click", function () {
        if(!$.validPhone($("#phoneNum").val())){
            layer.msg("请输入手机号");
            return false;
        }

        //流量
        if ($(".tabActive").index() == 0) {
            if (!$("#flow").find(".liActive").length) {
                layer.msg("请选择流量包");
                return false;
            }
            $("#formSubmit").submit();
        }
        //话费
        else {
            layer.msg("敬请期待！");
            return false;
            // if (!$('#phonefare').find('.liActive').length) {
            //     layer.alert("请选择充值面值");
            //     return;
            // }
        }
        return true;
    })

// 折叠
    $("#msg").on("tap click",function () {
        var num=$(this).attr("data-num");
        if(num==0){
            $(this).attr("data-num",1);
            $(this).siblings("ul").slideUp();
            $(this).find("img:last-child").attr("src","/common/imgs/down.png");
        }else{
            $(this).attr("data-num",0);
            $(this).siblings("ul").slideDown();
            $(this).find("img:last-child").attr("src","/common/imgs/right.png");
        }
    })
})

