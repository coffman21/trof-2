package com.company.app;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * Created by xk on 08.04.17.
 */
public class XmlObject {
    private String path;
    private Document document;
    private NodeList nodeList;

    XmlObject(String path) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            this.path = path;
            document = documentBuilder.parse(path);
            nodeList = document.getDocumentElement().getChildNodes();

        } catch (ParserConfigurationException ex) {
            ex.printStackTrace(System.out);
        } catch (SAXException ex) {
            ex.printStackTrace(System.out);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    public NodeList getNodeList() {
        return this.nodeList;
    }

    public void printRawXml() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.path));
            String line = null;
            while ((line = br.readLine()) != null) System.out.println(line);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * @printXml() prints xml nodes if its deep was reached.
     * @deep = 1 equals to root XML node
     */

    public void printXml(int deep){
        System.out.println("XML file content:");
        for (int i = 0; i < nodeList.getLength(); i++) {
            printNode(nodeList.item(i), deep, 1);
        }
    }

    public void printNode(Node nodes, int deep, int currDeep) {
        if (nodes.hasChildNodes()) {
            currDeep++;
            if (currDeep > deep) {
                System.out.println(nodes.getNodeName() + " : " + nodes.getTextContent());
            }
            NodeList nl = nodes.getChildNodes();
            for (int j=0 ; j < nl.getLength() ; j++) {
                printNode(nl.item(j), deep, currDeep);
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

    public void addNode(String tag, String name, String model, String price) {
        Element dataTag = document.getDocumentElement();

        Element carsTag =  (Element) dataTag.getElementsByTagName(tag+"s").item(0);

        Element newVehicle = document.createElement(tag);

        Element setName = document.createElement("name");
        setName.setTextContent(name);

        Element setModel = document.createElement("model");
        setModel.setTextContent(model);

        Element setPrice = document.createElement("price");
        setPrice.setTextContent(price);

        newVehicle.appendChild(setName);
        newVehicle.appendChild(setModel);
        newVehicle.appendChild(setPrice);
        carsTag.appendChild(newVehicle);

        nodeList = document.getDocumentElement().getChildNodes();
    }

    /*
     * @nl in this method takes \n values somehow.
      * reduce it by deleting newline characters from .xml file :)
     */

    public void deleteNode(int index, String category) {
        Node n = document.getElementsByTagName(category).item(0);
        NodeList nl = n.getChildNodes();
        document.getElementsByTagName(category).item(0).removeChild(nl.item(index));

        nodeList = document.getDocumentElement().getChildNodes();

    }
    
    @Override
    public String toString() {
        try {
            DOMSource source = new DOMSource(document);
            StringWriter w = new StringWriter();
            StreamResult sr = new StreamResult(w);
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            transformer.transform(source, sr);
            return w.toString();

        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return "";
    }
    public void printAttribute(String attr) {
        for(int k=0;k<nodeList.getLength();k++){
                printTags((Node)nodeList.item(k), attr);
            }
        }
    public void printTags(Node nodes, String attr){
        if (nodes.getNodeName().equals(attr)) {
            System.out.println(nodes.getNodeName() + " : " + nodes.getTextContent());
        }
        if (nodes.hasChildNodes()) {
            NodeList nl=nodes.getChildNodes();
                for (int j=0;j<nl.getLength();j++) {
                    printTags(nl.item(j), attr);
                }
            }
        }
    }

