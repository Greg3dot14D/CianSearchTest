package ru.greg3d.helpers.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.greg3d.annotations.Xpath;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sbt-ivanov-iva on 27/05/2016.
 *
 */
public class XmlUnit {
    private static final Logger LOG = LoggerFactory.getLogger(XmlUnit.class);

    private Document doc = null;
    private String filePath = "";
    private Map<String, String> parameters;

    public XmlUnit(){}

    public XmlUnit(String xmlFileName){
        this.setDocumentFromFile(xmlFileName);
    }

    private void setDocumentFromFile(String xmlFileName){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            File file = new File(xmlFileName);
            this.setDocument(docBuilder.parse(file));
        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public XmlUnit setXml(String xml){
        this.setDocument(serializeStringToXmlDocument(xml));
        return this;
    }

    public String getXml(){
        return serializXmlDocumentToString(this.doc);
    }

    public void setDocument(Document doc){
        this.doc = doc;
    }

    public Document getDocument(){
        return this.doc;
    }

    public XmlUnit replaceAttributeValue(String nodeXpathString, String attributeName, String value){
        try {
            this.setDocument(setValueToAttributeByXpath(this.getDocument(), nodeXpathString, attributeName, value));
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e.toString());
        }
        return this;
    }

    //@Step
    public XmlUnit substitutionXmlWithParametersFromDocument(Document doc, Map<String, String> parameters) {
        if(parameters.size() == 0)
            parameters.put("fake","fake");
        try {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                LOG.debug(entry.getKey() + " = " + entry.getValue());
                if (entry.getKey().startsWith("//")) {
                    if(!"".equals(entry.getValue()))
                        doc = setValueByXpath(doc, entry.getKey(), entry.getValue());
                    else
                        doc = setValueByXpath(doc, entry.getKey(), "");
                }
                String xml = serializXmlDocumentToString(doc);

                for(String key:parameters.keySet()){
                    if(key.startsWith("##"))
                        xml = xml.replace(key, parameters.get(key));
                }
                this.setDocument(serializeStringToXmlDocument(xml));
            }
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e.toString());
        }
        return this;
    }

    public static XmlUnit parametriseXml(String xml, Object object) {
        return new XmlUnit().setXml(xml).substitutionXmlWithParametersFromDocument(object);
    }

    private XmlUnit substitutionXmlWithParametersFromDocument(Object object){
        return substitutionXmlWithParametersFromDocument(this.getDocument(), object);
    }

    // TODO - добавить ч/з рефлекшн
    public XmlUnit substitutionXmlWithParametersFromDocument(Document doc, Object object) {

        Field[] fields = object.getClass().getFields();

        for(Field f: fields) {
            if(f.isAnnotationPresent(Xpath.class))
            try {
                try {
                    doc = setValueByXpath(doc, f.getAnnotation(Xpath.class).value(), f.get(object).toString());
                } catch (XPathExpressionException | IllegalAccessException e) {
                    throw new RuntimeException(e.toString());
                }
            }catch(NullPointerException e){
                //throw new RuntimeException(String.format("Can't find element by xpath[%s]", f.getAnnotation(Xpath.class).value()));
                System.err.println(String.format("Can't find element by xpath[%s]\n%s", f.getAnnotation(Xpath.class).value(), e.getMessage()));
            }
        }
        this.setDocument(doc);
        return this;
    }

    /*
    ** переводим XML Document в String, и подставляем "левый" namespace
    */
    private String serializXmlDocumentToString(Node doc){
//            try {
//                return serializXmlDocumentToStringImpl(doc);
//            } catch (TransformerException e) {
//                try {
//                    //Node o = doc.getFirstChild();
//                    Node o = doc;
//                    ((Element)o).setAttribute(String.format("xmlns:%s", getFakeNs(e.getMessage())),"");
//                    return serializXmlDocumentToStringImpl(doc);
//                }catch(TransformerException e1){
//                    throw new RuntimeException("TransformerException ->>" + e1.getMessage());
//                }
//            }
        try {
            return serializXmlDocumentToStringImpl(doc);
        }catch(TransformerException e){
            throw new RuntimeException(e.toString());
        }
    }


    /*
    **  переводим XML Document в String
    */
    private String serializXmlDocumentToStringImpl(Node doc) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.getBuffer().toString();
    }

    private String getFakeNs(String errorText){
        String xmlnsPattern = "Namespace for prefix '\\w{1,}' has not been declared";
        Pattern p = Pattern.compile(xmlnsPattern);
        Matcher m = p.matcher(errorText);
        List<String> nslist = new ArrayList<>();
        while(m.find())
            nslist.add(m.group().replaceAll("Namespace for prefix '","").replaceAll("' has not been declared",""));
        return nslist.get(0).toString();
    }


    // переводим String в XML Document
    private Document serializeStringToXmlDocument(String string){
        Document doc = null;
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.parse(new ByteArrayInputStream(string.getBytes()));
        }catch (ParserConfigurationException | IOException | SAXException e){
            throw new RuntimeException(e.toString());
        }
        return doc;
    }

    public XmlUnit serializeFromString(String xml){
        this.setDocument(serializeStringToXmlDocument(xml));
        return this;
    }

    /**
     * Возвращает список текстовых xml-ек описывающий каждый тэг с именем tagName
     *
     * @param tagName - имя тэгов, которые ищутся в xml-ке
     * @return - список текстовых xml-ек описывающий каждый тэг с именем tagName
     * @throws TransformerException
     */
    public List<String> getStringListOfNodesByTagName(String tagName) throws TransformerException {
        NodeList nodeList = getDocument().getElementsByTagName(tagName);
        List<String> stringNodeList = new ArrayList<>();
        for(int i = 0; i < nodeList.getLength(); i ++)
            stringNodeList.add(serializXmlDocumentToString(nodeList.item(i)));
        return stringNodeList;
    }


    public List<String> getStringListOfNodeNamesByTagName(String tagName) throws TransformerException {
        NodeList nodeList = getDocument().getElementsByTagName(tagName).item(0).getChildNodes();
        List<String> stringNodeList = new ArrayList<>();
        for(int i = 0; i < nodeList.getLength(); i ++) {
            Node node = nodeList.item(i);
            if(!"#text".equals(node.getNodeName()))
                stringNodeList.add(node.getNodeName());
        }
        return stringNodeList;
    }

    private String saveXml(Document doc) {

        removeEmptyNodes(doc);

        String fileName = String.format("out_%s_out.xml", Long.toString(new Date().getTime()));
        try {
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(new File("out/"+fileName)));
            this.saveXmlAttach(fileName);
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e.toString());
        } catch (TransformerException e) {
            throw new RuntimeException(e.toString());
        }
        return fileName;
    }

//    public String saveXml() {
//
//        removeEmptyNodes(doc);
//
//        TransformerFactory transformerFactory = TransformerFactory.newInstance();
//        Transformer transformer = null;
//        Date date = new Date();
//        String nameFile = null;
//        try {
//            transformer = transformerFactory.newTransformer();
//            DOMSource source = new DOMSource(doc);
//            nameFile = "out_"+Long.toString(date.getTime())+"_"+ Utils.getRandomNumber(4)+".xml";
//            String f = "out/"+nameFile;
//            StreamResult result = new StreamResult(new File(f));
//            transformer.transform(source, result);
//            LOG.info("Xml generated -> " + f);
//            saveXmlAttach(nameFile);
//        } catch (TransformerConfigurationException e) {
//            e.printStackTrace();
//        } catch (TransformerException e) {
//            e.printStackTrace();
//        }
//        return nameFile;
//    }

    public String saveXml() {
        String nameFile = String.format("out_%s.xml", Long.toString(new Date().getTime()));
        this.saveXml(this.filePath + nameFile);
        return nameFile;
    }

    public void saveXml(String fileName) {
        try {
            TransformerFactory.newInstance().newTransformer()
                    .transform(new DOMSource(this.getDocument()), new StreamResult(new File(fileName)));
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e.toString());
        } catch (TransformerException e) {
            throw new RuntimeException(e.toString());
        }
    }


    private Node getNodeByXPath(Document doc, String xPathString) throws XPathExpressionException {
        return (Node) XPathFactory.newInstance().newXPath().compile(xPathString).evaluate(doc, XPathConstants.NODE);
    }

    public Document setValueToAttributeByXpath(String xPathString, String attributeName, String value) throws XPathExpressionException {
        return setValueToAttributeByXpath(this.getDocument(), xPathString, attributeName, value);
    }

    // подменяем в аттрибуте attributeName в ноде с xPath = xPathString значением value
    private Document setValueToAttributeByXpath(Document doc, String xPathString, String attributeName, String value) throws XPathExpressionException {
        this.getNodeByXPath(doc, xPathString)
                .getAttributes()
                .getNamedItem(attributeName)
                .setTextContent(value);
        return doc;
    }

    public String getValueOfAttributeByXpath(String xPathString, String attributeName) throws XPathExpressionException {
        return getValueOfAttributeByXpath(this.getDocument(), xPathString + "[1]", attributeName);
    }

    // возвращает значение аттрибутав ноде с xPath = xPathString
    private String getValueOfAttributeByXpath(Document doc, String xPathString, String attributeName) throws XPathExpressionException {
        return this.getNodeByXPath(doc, xPathString)
                .getAttributes()
                .getNamedItem(attributeName)
                .getTextContent();
    }


    public Document setValueByXpath(String xPathString, String value) throws XPathExpressionException {
        return this.setValueByXpath(this.getDocument(), xPathString, value);
    }

    public Document setValueByXpath(Document doc, String xPathString, String value) throws XPathExpressionException {
        this.getNodeByXPath(doc, xPathString)
                .setTextContent(value);
        return doc;
    }

    public String getValueByXpath(Document doc, String xPathString) throws XPathExpressionException {
        return this.getNodeByXPath(doc, xPathString).getTextContent();
    }

    public String getValueByXpath(String xPathString) throws XPathExpressionException {
        return getValueByXpath(this.getDocument(), xPathString + "[1]");
    }

    // Находим Node по Xpath и возвращаем в виде стринга
    public String getNodeToStringByXpath(String xPathString) throws XPathExpressionException, TransformerException {
        return serializXmlDocumentToStringImpl(this.getNodeByXPath(this.getDocument(), xPathString + "[1]"));
    }

    // Находим List Node-ов по Xpath и возвращаем в виде списка стрингов
    public List<String> getListOfNodesToListOfStringsByXpath(String xPathString) throws XPathExpressionException, TransformerException {
        XPath xPath = XPathFactory.newInstance().newXPath();
//        NodeList nodeList = (NodeList) xPath.compile(xPathString + "[1]").evaluate(this.getDocument(), XPathConstants.NODESET);
//        List<String> resultList = new ArrayList<>();
//        for(int i = 0; i < nodeList.getLength(); i ++)
//            resultList.add(serializXmlDocumentToString(nodeList.item(i)));

        List<String> resultList = new ArrayList<>();
        int i = 1;
        while(1==1){
            NodeList nodeList = (NodeList) xPath.compile(xPathString + "[" + (i++) + "]").evaluate(this.getDocument(), XPathConstants.NODESET);
            if(nodeList.getLength() == 0)
                break;
            else
                resultList.add(serializXmlDocumentToString(nodeList.item(0)));
        }
        return resultList;
    }


    public List<String> getListOfValuesByXpath(String xPathString) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        //this.setNamespaceContext(xPath);
        NodeList nodeList = (NodeList) xPath.compile(xPathString).evaluate(this.getDocument(), XPathConstants.NODESET);
        List<String> resultList = new ArrayList<>();
        for(int i =0 ; i < nodeList.getLength(); i ++){
            resultList.add(nodeList.item(i).getTextContent());
        }
        return resultList;
    }

    //@Attachment(value = "{0}", type = "text/xml")
    public byte[] saveXmlAttach(String attachName) {
        try {
            return toByteArray(new File("out/"+attachName).getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private static byte[] toByteArray(File file) throws IOException {
        return Files.readAllBytes(Paths.get(file.getPath()));
    }


    public XmlUnit removeEmptyNodes(){
        removeEmptyNodes(this.getDocument());
        return this;
    }

    public XmlUnit removeNodesByXpath(String xPathString) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodeList = (NodeList) xPath.compile(xPathString).evaluate(this.getDocument(), XPathConstants.NODESET);
        for (int i = 0 ; i < nodeList.getLength(); i++)
            nodeList.item(i).getParentNode().removeChild(nodeList.item(i));
        return this;
    }

    public static void removeEmptyNodes(Node node) {

        NodeList list = node.getChildNodes();
        List<Node> nodesToRecursivelyCall = new LinkedList();

        for (int i = 0; i < list.getLength(); i++) {
            nodesToRecursivelyCall.add(list.item(i));
        }

        for(Node tempNode : nodesToRecursivelyCall) {
            removeEmptyNodes(tempNode);
        }

        boolean emptyElement = node.getNodeType() == Node.ELEMENT_NODE
                && node.getChildNodes().getLength() == 0;
        boolean emptyText = node.getNodeType() == Node.TEXT_NODE
                && node.getNodeValue().trim().isEmpty();

        if (emptyElement || emptyText) {
            if(!node.hasAttributes()) {
                node.getParentNode().removeChild(node);
            }
        }

    }

    public static <T> T deserialize(String xml, T object){
        Field[] fields = object.getClass().getFields();

        XmlUnit x = new XmlUnit().setXml(xml);

        StringBuilder exceptionMessage = new StringBuilder();

        for(Field f: fields) {
            if(f.isAnnotationPresent(Xpath.class))
                try {
                    f.set(object,  x.getValueByXpath(f.getAnnotation(Xpath.class).value()));
                    // TODO - обработать событие NullPointerException, когда в файле нет искомого нода (сейчас xml должен содержать ВСЕ ноды модели)
                } catch (XPathExpressionException | IllegalAccessException e) {
                    throw new RuntimeException(e.toString());
                } catch (NullPointerException e){
                    //throw new NullPointerException(String.format("Can't find node by xpath ['%s']\n%s", f.getAnnotation(Xpath.class).value(), e.getMessage()));
                    if("".equals(exceptionMessage.toString()))
                        exceptionMessage.append("Can't find node by xpath :\n");
                    exceptionMessage.append(f.getAnnotation(Xpath.class).value()).append("\n");
                }
        }
        return object;
    }

    public static <T> T deserialize(String xml, Class<T> clazz){
        T object;
        try {
            object = clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e.toString());
        }
        return deserialize(xml, object);
    }


    /*
    **
    ** Набор карявых методов, которые рука не поднимается удалить, потому, что многие НЕ мои !!!
    **
    */

    //@Step
    public String getValueFromXmlByXpath(String xml, String xpath) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(stream);
        String value = getValueByXpath(doc, xpath);
        return value;
    }

    //@Step
    public String substitutionXmlWithParameters(String inputXml, Map<String, String> inParameters) {
        //parameters.put("//*[local-name()='RqTm']", Esb.generateRqTime());
        this.parameters = inParameters;
        Document doc = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            LOG.info(inputXml + " = " + classLoader.getResource(inputXml));

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            File file = new File(classLoader.getResource(inputXml).getFile());
            LOG.info(file.getAbsolutePath());
            doc = docBuilder.parse(file);
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                LOG.debug(entry.getKey() + " = " + entry.getValue());
                if (entry.getKey().startsWith("//")) {
                    if(!"".equals(entry.getValue()))
                        doc = setValueByXpath(doc, entry.getKey(), entry.getValue());
                    else
                        doc = setValueByXpath(doc, entry.getKey(), "");
                }
                //else if (entry.getKey().startsWith("##")) {
                //    doc = setValueByMatch(doc, entry.getKey(), entry.getValue());
                //}
            }
        } catch (XPathExpressionException | SAXException | IOException | ParserConfigurationException e) {
            throw new RuntimeException(e.toString());
        }
        return saveXml(doc);
    }

    //@Step
    public XmlUnit substitutionXmlWithParametersFromFile(String xmlFileName, Map<String, String> inParameters) {
        this.parameters = inParameters;
        Document doc = null;
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            File file = new File(xmlFileName);
            this.filePath = file.getParentFile().getPath() + "\\out\\";
            new File(this.filePath).mkdir();
            doc = docBuilder.parse(file);
            return substitutionXmlWithParametersFromDocument(doc,parameters);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new RuntimeException(e.toString());
        }
        //return this;
    }

    public XmlUnit substitutionXmlWithParametersFromFile(String xmlFileName, Object object) {
        Document doc = null;
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            File file = new File(xmlFileName);
            this.filePath = file.getParentFile().getPath() + "\\out\\";
            new File(this.filePath).mkdir();
            doc = docBuilder.parse(file);
            return substitutionXmlWithParametersFromDocument(doc, object);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public boolean validateAgainstXSD(String xmlFileName, String xsdFileName)
    {
//        try {
//            // 1. Поиск и создание экземпляра фабрики для языка XML Schema
//            SchemaFactory factory =
//                    SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
//
//            // 2. Компиляция схемы
//            // Схема загружается в объект типа java.io.File, но вы также можете использовать
//            // классы java.net.URL и javax.xml.transform.Source
//            File schemaLocation = new File(xsdFileName);
//            Schema schema = factory.newSchema(schemaLocation);
//
//            // 3. Создание валидатора для схемы
//            Validator validator = schema.newValidator();
//
//            // 4. Разбор проверяемого документа
//            Source source = new StreamSource(xmlFileName);
//
//            // 5. Валидация документа
//            try {
//                validator.validate(source);
//                //System.out.println(args[0] + " is valid.");
//                return true;
//            } catch (SAXException ex) {
//                //System.out.println(args[0] + " is not valid because ");
//                //System.out.println(ex.getMessage());
//                ex.printStackTrace();
//                return false;
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//            return false;
//        }
        try{
            return validateAgainstXSD(new FileInputStream(xmlFileName), new FileInputStream(xsdFileName));
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }

    static boolean validateAgainstXSD(InputStream xml, InputStream xsd)
    {
        try
        {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            //SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schema = factory.newSchema(new StreamSource(xsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xml));
            return true;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    //    private void setNamespaceContext(XPath xPath) {
//        xPath.setNamespaceContext(new NamespaceContext() {
//            @Override
//            public String getNamespaceURI(String prefix) {
//                if("saw".equals(prefix))
//                    //return "com.siebel.analytics.web/report/v1.1";
//                    return "saw";
//                else if("xsi".equals(prefix))
//                    //return "http://www.w3.org/2001/XMLSchema-instance";
//                    return "xsi";
////                else
////                    return null;
//                else
//                    return "";
//            }
//
//            @Override
//            public String getPrefix(String namespaceURI) {
//                return null;
//            }
//
//            @Override
//            public Iterator getPrefixes(String namespaceURI) {
//                return null;
//            }
//        });
//    }

//    public Document setValueByMatch(Document d, String matchString, String value) throws XPathExpressionException {
//        matchString = "//*[text()='"+matchString+"']";
//        XPath xPath = XPathFactory.newInstance().newXPath();
//        NodeList myNodeList = (NodeList) xPath.compile(matchString).evaluate(d, XPathConstants.NODESET);
//        myNodeList.item(0).getFirstChild().setNodeValue(value);
//        return d;
//    }
    // Хрень полная, но удалить - рука не поднимается
    public static String removeAllXmlNamespace(String xmlData)
    {
        String xmlnsPattern = "xmlns(\\s{0,}:\\s{0,}\\w{1,})\\s{0,}=";
        Pattern p = Pattern.compile(xmlnsPattern);
        Matcher m = p.matcher(xmlData);
        String removeRegexp = "(xmlns:)";
        while(m.find())
            removeRegexp += String.format("|(%s:)", m.group().replaceAll("xmlns(\\s{0,}:\\s{0,})","").replaceAll("\\s{0,}=",""));
        return xmlData.replaceAll(removeRegexp, "");
    }

//    public String getValueByXpathOrNull(String xPathString) throws XPathExpressionException {
//        try {
//            return getValueByXpath(xPathString);
//        }catch(NullPointerException e){ return null; }
//    }

//    /**
//     * Возвращает список текстовых xml-ек описывающий каждый тэг с именем tagName
//     *
//     * @param tagName - имя тэгов, которые ищутся в xml-ке
//     * @return - список текстовых xml-ек описывающий каждый тэг с именем tagName
//     * @throws TransformerException
//     */
//    public List<String> getStringListOfNodesByTagName(String tagName, List<String> nodeNamesList) throws TransformerException {
//        NodeList nodeList = getDocument().getElementsByTagName(tagName);
//
//        List<String> stringNodeList = new ArrayList<>();
//
//
//        for(int i = 0; i < nodeList.getLength(); i ++) {
//            Node node = nodeList.item(i);
//
//            System.out.println(node.getNodeName());
//
//
//            NodeList childNodeList = node.getChildNodes();
//
//            for(int j =0; j < childNodeList.getLength(); j ++){
//
//                Node child = childNodeList.item(j);
//
//                System.out.println(String.format( "child = [%s]",child.getNodeName()));
//                //if(!nodeNamesList.contains(child.getNodeName()) && !"#text".equals(child.getNodeName())) {
//                if(!nodeNamesList.contains(child.getNodeName())) {
//                    System.out.println(String.format( "child to remove-> [%s]",child.getNodeName()));
//                    node.removeChild(child);
//                    j --;
//                }
//            }
//            stringNodeList.add(serializXmlDocumentToString(node));
//        }
//        return stringNodeList;
//    }
}