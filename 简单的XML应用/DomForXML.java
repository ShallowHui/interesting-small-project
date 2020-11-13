import java.io.*;
import org.w3c.dom.*;

import javax.tools.StandardJavaFileManager.PathFactory;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;

public class DomForXML {
    public static void main(String[] args) throws XPathExpressionException {
        Document doc = XMLtoDocument("Alibrary.xml");
        getNodeByXpath(doc , "/child::library/child::book/child::name");
        System.out.println("*************************************");
        getNodeByXpath(doc, "/child::library/child::book/attribute::language");
        System.out.println("*************************************");
        getNodeByXpath(doc, "/library/book[3]/name");
    }
    //通过XPath获取XML文档中的元素
    public static void getNodeByXpath(Document doc,String xpath) throws XPathExpressionException {
        //初始化工厂
        XPathFactory xpathFactory = XPathFactory.newInstance();
        //获取XPath对象
        XPath xPath = xpathFactory.newXPath();
        //创建XPath表达式对象
        XPathExpression expression = xPath.compile(xpath);
        //执行解析XPath表达式
        NodeList nodes = (NodeList)expression.evaluate(doc, XPathConstants.NODESET);
        //输出获取到的元素节点
        for(int i = 0; i<nodes.getLength();i++){
            Node node=nodes.item(i);
            System.out.println(node.getNodeName()+"——>"+node.getTextContent());
        }
    }
    public static Document XMLtoDocument(String pathName)
    {
        Document doc = null;
        try{
            //读入XML文件
            File XMLFile = new File(pathName);
            //将XML文件通过工厂转换为Document对象
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(XMLFile);
        }
        catch (Exception e) {
            System.out.println("文件读取异常");
        }
        return doc;
    }
    //遍历打印输出XML文档
    public static void toStringXML(Document doc){
        NodeList allNode = doc.getChildNodes();
        for(int m=0;m<allNode.getLength();m++){
            Node temp = allNode.item(m);
            if(temp.getNodeType()==Node.COMMENT_NODE){
                System.out.println("<!--"+temp.getNodeValue()+"-->");
            }
            else if(temp.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE){
                System.out.println("<?style-sheet"+temp.getNodeValue()+"?>");
            }
            else if(temp.getNodeType() == Node.ELEMENT_NODE){
                parse(temp);
            }
        }
    }
    private static void parse(Node node){
        if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.CDATA_SECTION_NODE) {
            System.out.print(node.getNodeValue());
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE) {
            System.out.print("<" + node.getNodeName() + " ");
            NamedNodeMap map = node.getAttributes();
            for (int t = 0; t < map.getLength(); t++) {
                Node aTemp = map.item(t);
                System.out.print(aTemp.getNodeName()+"="+aTemp.getNodeValue());
            }
            System.out.print(">");

            NodeList nodelist = node.getChildNodes();
            for (int i = 0; i < nodelist.getLength(); i++) {
                Node next = nodelist.item(i);
                // 递归调用自身
                parse(next);
            }
            System.out.print("</" + node.getNodeName() + ">");
        }
    }
}
    //添加元素
    /*private static Node AddChildElement(Document doc,String TagName){
        Element element = doc.createElement(TagName);
        doc.appendChild(element);
        return doc.getLastChild();
    }
    //删除元素
    private static void deleteElement(Document doc, String TagName) {
        NodeList nodeList = doc.getElementsByTagName(TagName);
        int length = nodeList.getLength();
        for(int i=0; i<length;i++) {
            nodeList.item(0).getParentNode().removeChild(nodeList.item(0));
        }
    }
    //修改元素
    private static void modifyElement(Node node, String Value){
        node.setTextContent(Value);
    }
}*/
