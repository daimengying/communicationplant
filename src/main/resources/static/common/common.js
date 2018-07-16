(function($) {
    var _this = {};
    _this.root = function(){
        var curWwwPath = window.document.location.href;
        var pathName = window.document.location.pathname;
        var pos = curWwwPath.indexOf(pathName);
        var localhostPath = curWwwPath.substring(0,pos);
        var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);
        return (localhostPath);
    }
    commonJs=_this;

    if (typeof WeixinJSBridge == "undefined"){
        if( document.addEventListener ){
            document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
        }else if (document.attachEvent){
            document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
            document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
        }
    }else{
        onBridgeReady();
    }
})($)

//发起微信支付
function onBridgeReady(payResponse,returnUrl){
    WeixinJSBridge.invoke(
        'getBrandWCPayRequest', {
            "appId":payResponse.appId,     //公众号名称，由商户传入
            "timeStamp":payResponse.timeStamp,         //时间戳，自1970年以来的秒数
            "nonceStr":payResponse.nonceStr, //随机串
            "package":payResponse.packAge,
            "signType":"MD5",         //微信签名方式
            "paySign":payResponse.paySign //微信签名
        },
        function(res){
            // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
            if(res.err_msg != "get_brand_wcpay_request:ok" ) {
                // window.location.href=returnUrl;
            }
        }

    );
}



/** 拼接字符串 **/
function StringBuffer(str){
    this._string_ = new Array();
    this._string_.push(str);
};
StringBuffer.prototype.append = function(str){
    this._string_.push(str);
};
StringBuffer.prototype.toString = function(){
    return this._string_.join('');
};


$.ajaxAlert = function(options){
    $.ajax({
        url: options.ajaxUrl,
        type: 'get',
        success: function (data) {
            // 清空表单
            $('.close').on('hidden.bs.modal', options.fun);
            $('#close').on('hidden.bs.modal', options.fun);
            $(".modalBody").html(data);
            $('#modal-form').modal('show');
            $('#myModalTitle').text(options.title);
        }
    });

}


