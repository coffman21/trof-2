package com.company.app;

/**
 * Created by xk on 08.04.17.
 */
public class Main {
    public static void main(String[] args) {
        String filePath = "src/main/resources/Cars.xml";

        // 2. Выведите данные документа на консоль
        XmlObject obj = new XmlObject(filePath);
        obj.printRawXml();
        obj.printXml(2);

        obj.addNode("<car>\n" +
                "            <name>Toyota</name>\n" +
                "            <model>Mark II</model>\n" +
                "            <price>500000</price>\n" +
                "        </car>");
        obj.printRawXml();
        // 3. Создайте объект XmlDocument и начните работу с xml-документом

    }
}
