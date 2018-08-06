$(function(){

})

var FlowCharge= {
    toCharge : function () {
        var orderResultStr=$("#orderResult").val();
        var orderResult=JSON.parse(orderResultStr);
        var jsonData={
            "name":orderResult.name,
            "price":orderResult.price,
            "userInfo":$("#userInfo").val()
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
                        //发起微信支付
                        pay(data.payResponse, commonJs.root() + '/charge/wxNotify');
                    }else {
                        layer.alert('服务器异常', {icon: 5});
                    }
                }
            }
        });
    }



}