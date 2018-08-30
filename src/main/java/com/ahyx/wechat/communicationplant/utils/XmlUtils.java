package com.ahyx.wechat.communicationplant.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * @Author: daimengying
 * @Date: 2018/8/8 17:14
 * @Description:XML工具类
 */
public class XmlUtils {

    private static Logger _logger = LoggerFactory.getLogger(XmlUtils.class);

    /**
     * XML格式字符串转换为Map
     * @param xml
     * @return
     */
    public static Map<String,Object> readStringXmlOut(String xml) {
        Map<String, Object> data = new HashMap<>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
            }

        } catch (Exception e) {
            _logger.error("xml解析错误："+e.getMessage());
        }
        return data;
    }

    public static String mapToXmlString(Map<String, Object> map) {
        String xmlResult = "";

        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (String key : map.keySet()) {

            String value = "<![CDATA[" + map.get(key) + "]]>";
            sb.append("<" + key + ">" + value + "</" + key + ">");
        }
        sb.append("</xml>");
        xmlResult = sb.toString();

        return xmlResult;
    }

    public static void main(String[] args) {
        String str="<xml><return_code><![CDATA[FAIL]]></return_code>\n" +
                "<return_msg><![CDATA[缺少参数]]></return_msg>\n" +
                "</xml>";
        Map<String,Object>result=readStringXmlOut(str);
        System.out.println(result.toString());
        System.out.println(mapToXmlString(result));
    }
}
