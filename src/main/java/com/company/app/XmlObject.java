package com.company.app;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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

    public void printTags(Node nodes, int deep, int currDeep) {
        if (nodes.hasChildNodes()) {
            currDeep++;
            if (currDeep > deep) {
                System.out.println(nodes.getNodeName() + " : " + nodes.getTextContent());
            }
            NodeList nl = nodes.getChildNodes();
            for (int j=0 ; j < nl.getLength() ; j++) {
                printTags(nl.item(j), deep, currDeep);
            }
        }
    }

    /*
     * @printXml() prints xml nodes if its deep was reached.
     * @deep = 1 equals to root XML node
     */

    public void printXml(int deep){
        System.out.println("XML file content:");
        for (int i = 0; i < nodeList.getLength(); i++) {
            printTags(nodeList.item(i), deep, 1);
        }
    }


    private void paste(NodeList nl, Node toAdd) {
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeName().equals(toAdd.getNodeName())) {
                nl.item(i).getParentNode().appendChild(toAdd); // WRONG_DOCUMENT_ERR
                return;
            }
            paste(nl.item(i).getChildNodes(), toAdd);
        }
    }
    public void addNode(String rawXml) {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(rawXml));
            Document d = db.parse(is);
            Node toAdd = d.getDocumentElement().getParentNode();
            paste(nodeList, toAdd.getFirstChild());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
