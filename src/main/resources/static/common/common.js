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




