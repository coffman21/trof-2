package com.company.app;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;

/**
 * Created by xk on 08.04.17.
 */
public class Main {
    public static void printNode(Node nodes, int deep, int currDeep) {
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
    public static void printTags(Node nodes, String attr){
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
    public static void main(String[] args) {
        String filePath = "src/main/resources/Cars.xml";
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(filePath);
            NodeList nodeList = document.getDocumentElement().getChildNodes();

            System.out.println("XML file content:");

            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    System.out.println(nodeList.item(i).getNodeName() + ":");
                    NodeList nodes = nodeList.item(i).getChildNodes();
                    for (int j = 0; j < nodes.getLength(); j++) {
                        if (nodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            Node n = nodes.item(j);
                            System.out.println(n.getNodeName() + " : " + n.getTextContent());
                        }
                    }
                }
            }
            // 4. Добавьте в документ 2 новых элемента
            Element dataTag = document.getDocumentElement();
            Element vehicleTag =  (Element) dataTag.getElementsByTagName("cars").item(0);
            Element newVehicle = document.createElement("car");

            Element setName = document.createElement("name");
            setName.setTextContent("Toyota");

            Element setModel = document.createElement("model");
            setModel.setTextContent("Mark II");

            Element setPrice = document.createElement("price");
            setPrice.setTextContent("500000");

            newVehicle.appendChild(setName);
            newVehicle.appendChild(setModel);
            newVehicle.appendChild(setPrice);
            vehicleTag.appendChild(newVehicle);

            vehicleTag =  (Element) dataTag.getElementsByTagName("bikes").item(0);
            newVehicle = document.createElement("bike");

            setName.setTextContent("Wild Thing");
            setModel.setTextContent("2000");
            setPrice.setTextContent("0");

            newVehicle.appendChild(setName);
            newVehicle.appendChild(setModel);
            newVehicle.appendChild(setPrice);
            vehicleTag.appendChild(newVehicle);

            // 5. Удалите из документа первый узел
            Node n = document.getElementsByTagName("cars").item(0);
            NodeList nl = n.getChildNodes();
            document.getElementsByTagName("cars").item(0).removeChild(nl.item(1));
            nodeList = document.getDocumentElement().getChildNodes();

            // 6. Выведите на консоль значения какого-либо атрибута у всех элементов
            for(int k=0;k<nodeList.getLength();k++){
                printTags((Node)nodeList.item(k), "name");
            }

            XPath xpath = XPathFactory.newInstance().newXPath();

            // 2. Выберите все узлы корневого элемента
            NodeList nodeList2_2 = document.getDocumentElement().getChildNodes();

            // 3. Выберите те элементы, атрибуты которых имеют только определенное значение
            System.out.println("--");
            XPathExpression expr = xpath.compile("./cars[car/name = 'Lada']");
            NodeList nodeList2_3 = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
            for (int i = 0; i < nodeList2_3.getLength(); i++) {
                System.out.println(nodeList2_3.item(i).getNodeName() + " : " +
                        nodeList2_3.item(i).getNodeValue());
            }


        } catch (ParserConfigurationException ex) {
            ex.printStackTrace(System.out);
        } catch (SAXException ex) {
            ex.printStackTrace(System.out);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }
}
