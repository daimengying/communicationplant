$(function(){

})

var FlowCharge= {
    toCharge : function () {
        var jsonData={
            "orderDetail":$("#orderDetail").val(),
            "userInfo":$("#userInfo").val(),
            "commodity":$("#commodity").val(),
        };

        $.ajax({
            url: commonJs.root() + '/charge/invokeCharge?tt='+new Date().getTime(),
            type: "POST",
            cache: false,
            data:JSON.stringify(jsonData),
            contentType : 'application/json;charset=utf-8',
            dataType:'json',
            async:false,
            success: function(data){
                if(data){
                    if(data.success) {
                        var payResponse=data.payResponse;
                        //发起微信支付
                        pay(payResponse, commonJs.root()+"/charge/result?orderId="+data.orderId);
                    }else {
                        layer.alert('服务器异常', {icon: 5});
                    }
                }
            }
        });
    }



}