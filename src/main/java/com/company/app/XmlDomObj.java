package com.company.app;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by xk on 08.04.17.
 */
public class XmlDomObj {
    private Document document;
    private NodeList nodeList;

    private void trimWhitespace(Node node) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.TEXT_NODE) {
                child.setTextContent(child.getTextContent().trim());
            }
            trimWhitespace(child);
        }
    }

    XmlDomObj(String path) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = documentBuilder.parse(path);
            nodeList = document.getDocumentElement().getChildNodes();
            trimWhitespace(nodeList.item(0).getParentNode());


        } catch (ParserConfigurationException ex) {
            ex.printStackTrace(System.out);
        } catch (SAXException ex) {
            ex.printStackTrace(System.out);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    public void printXml(){
        System.out.println("XML file content:");
        for (int i = 0; i < nodeList.getLength(); i++) {
            printNode(nodeList.item(i));
        }
    }

    public void printNode(Node nodes) {
        if (nodes.getNodeType() != Node.TEXT_NODE) {
            System.out.println(nodes.getNodeName() + " : " + nodes.getTextContent());
            NamedNodeMap attrMap = nodes.getAttributes();
            for (int i = 0; i < attrMap.getLength(); ++i)
            {
                Node attr = attrMap.item(i);
                System.out.println(attr.getNodeName() + " = \"" + attr.getNodeValue() + "\"");
            }
        }
        if (nodes.hasChildNodes()) {
            NodeList nl = nodes.getChildNodes();
            for (int j=0 ; j < nl.getLength() ; j++) {
                printNode(nl.item(j));
            }
        }
    }


    public void paste(NodeList nl, Node toAdd) {
        for(int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeName().equals(toAdd.getNodeName())) {
                Node n = document.importNode(toAdd, true);
                nodeList = document.getDocumentElement().getChildNodes();
                nodeList.item(i).appendChild(n);
                return;

            }
        }
        paste(nl, toAdd);
    }

    /*
    * What is actually going on is trying to append an element to document.
    * @tags is two- or one-valued array of Strings, where
    * @tag[0] is the name of element which is appended,
    * @tag[1] is the name of parent node, and
    * @tag[1] is separated pair of parent's attribute name and its value.
    * @attrs are varargs which contain separated value of appending element attributes.
    * */

    private String[] spl (String s) {
        return s.split("=");
    }

    public void addNode(String[] tags, String ... attrs) {
        Element dataTag = document.getDocumentElement();

        ArrayList<String> attrsNames = new ArrayList<String>();
        ArrayList<String> attrsValues = new ArrayList<String>();
        for (String a : attrs) {
            String[] s = spl(a);
            attrsNames.add(s[0]);
            attrsValues.add(s[1]);
        }

        Element parent = dataTag;
        String elemName = tags[0];
        if (tags.length == 3) {
            elemName = tags[0];
            String[] parentsNameNValue = spl(tags[2]);

            NodeList nl = document.getElementsByTagName(parentsNameNValue[1]);
            for (int i = 0; i < nl.getLength(); i++) {
                Element e = (Element) nl.item(i);
                if (e.hasAttribute(parentsNameNValue[1])) {
                    parent = e;
                }
            }
        }
        // else parent is a root node, therefore appending to it.

        Element child = document.createElement(elemName);
        for (int i = 0; i < attrsNames.size(); i++) {
            child.setAttribute(attrsNames.get(i), attrsValues.get(i));
        }

        parent.appendChild(child);
        nodeList = document.getDocumentElement().getChildNodes();
    }

    public void deleteNode(String name, String attrNvalue) {
        String attr = spl(attrNvalue)[0];
        String value = spl(attrNvalue)[1];
        NodeList nl = document.getElementsByTagName(name);
        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element) nl.item(i);
            if (e.getAttribute(attr).equals(value)) {
                nl.item(0).getParentNode().removeChild(e);
            }
        }
    }

    @Override
    public String toString() {
        try {
            DOMSource source = new DOMSource(document);
            StringWriter w = new StringWriter();
            StreamResult sr = new StreamResult(w);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, sr);
            return w.toString();

        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return "Casting was not successful";
    }
    public void printAttribute(String attr) {
        for(int k=0;k<nodeList.getLength();k++){
                printTags(nodeList.item(k), attr);
            }
        }

    //overload
    public void printAttribute(String attr, String value) {
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xpath.compile("//*[@"+ attr + "='"+value+"']");
            NodeList nl = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
            for (int i = 0; i < nl.getLength(); i++) {
                Element e = (Element) nl.item(i);
                NamedNodeMap attrMap = e.getAttributes();
                for (int j = 0; j < attrMap.getLength(); j++)
                {
                    Node n = attrMap.item(j);
                    System.out.println(n.getNodeName() + " = \"" + n.getNodeValue() + "\"");
                }
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }
    private void printTags(Node nodes, String attr){
        Element e = null;
        if (nodes.getNodeType() == Node.ELEMENT_NODE) {
            e = (Element) nodes;
        }
        else return;
        if (e.hasAttribute(attr)) {
            System.out.println(attr + " : " + e.getAttribute(attr));
        }
        if (nodes.hasChildNodes()) {
            NodeList nl=nodes.getChildNodes();
                for (int j=0;j<nl.getLength();j++) {
                    printTags(nl.item(j), attr);
                }
            }
        }
    }