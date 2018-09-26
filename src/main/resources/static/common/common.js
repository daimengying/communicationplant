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


})($)

/**
 * 校验手机号
 * @param phoneNum
 * @returns {Object}
 */
$.validPhone= function (phoneNum) {
    var pattern = /^1[34578]\d{9}$/;
    return pattern.test(phoneNum);
}

/**
 * 谷歌图形验证码
 * @param vrifyCode
 */
$.imgvrifyKaptcha=function (vrifyCode) {
    var returnData;
    $.ajax({
        url: commonJs.root() + '/imgvrifyKaptcha?tt='+new Date().getTime(),
        type: "POST",
        cache: false,
        data:{"vrifyCode":vrifyCode},
        dataType:'json',
        async:false,  //此处必须同步，不然没有返回值
        success: function(data){
            returnData= data;
        }
    });
    return returnData;
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




