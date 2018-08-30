package com.ahyx.wechat.communicationplant.vo;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import lombok.Data;

/**
 * @Author: daimengying
 * @Date: 2018/8/8 15:02
 * @Description:
 */
@Data
public class UnifiedRefundRequest {
    private String appid;//公众账号ID

    private String mch_id;//微信支付分配的商户号

    private String nonce_str;//随机字符串，不长于32位

    private String out_refund_no;//系统退款单号

    private String sign;//md5签名

    private String out_trade_no;//系统订单号

    private int total_fee;//订单总金额，单位为分

    private int refund_fee;//退款总金额，单位为分

//    @XStreamAlias("aa_aa");
//    private int totalFee;

//    private Boolean loveDmy = true;

    public String toXml(UnifiedRefundRequest req){
        //创建xStream对象
        XStream xStream = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-","_")));
        xStream.alias("xml", UnifiedRefundRequest.class);

        return xStream.toXML(req);
    }
}
