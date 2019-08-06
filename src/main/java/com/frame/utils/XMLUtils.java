package com.frame.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.frame.codec.XMLMiddleBean;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @PackgeName:com.frame.utils
 * @ClassName:XmlUtils
 * @Auther: 马俊
 * @Date: 2019-08-04 19:43
 * @Description:
 *      XML工具类
 */
@Component
public class XMLUtils {
    @Autowired
    StringUtils stringUtils;
    public static Map<String, Namespace> namespaces = new HashMap<String, Namespace>();
    public XMLUtils(){
        namespaces.put("soap",new Namespace("soap","http://schemas.xmlsoap.org/soap/envelope/"));
        namespaces.put("s13",new Namespace("s13","http://geronimo.apache.org/xml/ns/attributes-1.1"));
        namespaces.put("d",new Namespace("d","http://geronimo.apache.org/xml/ns/attributes-1.1"));
    }
    /**
     * String 转 org.dom4j.Document
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static Document strToDocument(String xml) throws Exception{
        try {
            //加上xml标签是为了获取最外层的标签，如果不需要可以去掉

            // 创建saxReader对象
            SAXReader reader = new SAXReader();
            reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            return reader.read(new ByteArrayInputStream(xml.getBytes()));
        } catch (DocumentException e) {
            return null;
        }
    }

    /**
     * org.dom4j.Document 转  com.alibaba.fastjson.JSONObject
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static JSONObject documentToJSONObject(String xml) throws Exception{
        Document doc = strToDocument(xml);
        JSONObject result = new JSONObject(true);
        elementToJSONObject(doc.getRootElement(),result);
        return result;
    }

    /**
     * org.dom4j.Element 转  com.alibaba.fastjson.JSONObject
     * @param node
     * @return
     */
    public static void elementToJSONObject(Element node, JSONObject result) {
        // 当前节点的名称、文本内容和属性
        List<Attribute> listAttr = node.attributes();// 当前节点的所有属性的list
        for (Attribute attr : listAttr) {// 遍历当前节点的所有属性
            result.put(attr.getName(), attr.getValue());
        }
        // 递归遍历当前节点所有的子节点
        List<Element> listElement = node.elements();// 所有一级子节点的list
        if (!listElement.isEmpty()) {
            for (Element e : listElement) {// 遍历所有一级子节点
                if (e.attributes().isEmpty() && e.elements().isEmpty()) { // 判断一级节点是否有属性和子节点
                    result.put(e.getName(), e.getTextTrim());// 沒有则将当前节点作为上级节点的属性对待
                }else {
                    if (!result.containsKey(e.getName())) { // 判断父节点是否存在该一级节点名称的属性
                        result.put(e.getName(), new JSONObject(true));// 没有则创建
                    }
                    elementToJSONObject(e, result.getJSONObject(e.getName()));// 将该一级节点放入该节点名称的属性对应的值中

                }
            }
        }
    }
    public static void convertJsonToXml(Element node, JSONObject json){
        for (String key:json.keySet()){
            XMLMiddleBean xmb = null;
            Element n1 = null;
            if(json.get(key) instanceof JSONArray){
                for(int jsaIndex =0;jsaIndex<json.getJSONArray(key).size();jsaIndex++){
                    Element eachNode = createElement(node,key,null);
                    convertJsonToXml(eachNode,json.getJSONArray(key).getJSONObject(jsaIndex));
                    continue;
                }
                continue;
            }else if(json.get(key) instanceof XMLMiddleBean){
                xmb = (XMLMiddleBean)json.get(key);
                n1 = createElement(node,key,xmb);
            }else{
                if(key.equals("split")||key.equals("prefix")||!(json.get(key) instanceof JSONObject)) {
                    continue;
                }
                if(key.equals("value")&&json.get(key) instanceof JSONObject){
                    convertJsonToXml(node,json.getJSONObject(key));
                    continue;
                }else {
                    n1 = createElement(node, key, new XMLMiddleBean(null));
                }

            }
            if(null!=xmb&&xmb.getValue() instanceof JSONObject){
                convertJsonToXml(n1,json.getJSONObject(key));
            }else{
                JSONObject jo = json.getJSONObject(key);
                jo.remove("prefix");
                jo.remove("split");
                n1.addText(jo.getString("value"));

            }
        }
    }
    public static Element createElement(Element node,String elementName,XMLMiddleBean bean){
        if(StringUtils.isEmpty(bean.getPrefix())){
            return node.addElement(elementName);
        }else if(bean.isSplit()){
            return node.addElement(bean.getPrefix()+":"+elementName);
        }else{
            return node.addElement(bean.getPrefix()+elementName);
        }
    }
}
