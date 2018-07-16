$(function(){

})

var FlowCharge= {
    toCharge : function () {
        $.ajax({
            type: "POST",
            url: commonJs.root() + '/charge/create?tt='+new Date().getTime(),
            cache: false,
            data:{price:$("#price").val()},
            dataType:'json',
            async:false,
            success: function(data){
                if(data.success) {
                    //发起微信支付
                    onBridgeReady(data.payResponse, commonJs.root() + '/charge/flowOrderDetail?id='+data.orderId);
                }else {
                    layer.alert('服务器异常', {icon: 5});
                }

            }
        });
    }



}