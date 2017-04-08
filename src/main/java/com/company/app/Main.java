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

        // 3. Создайте объект XmlDocument и начните работу с xml-документом
        // 4. Добавьте в документ 2 новых элемента
        obj.addNode("car", "Toyota", "Mark II", "500000");
        obj.addNode("bike", "Wild Thing", "2000", "0");

        // 5. Удалите из документа первый узел
        obj.deleteNode(1, "cars");
        System.out.println(obj.toString());

        // 6. Выведите на консоль значения какого-либо атрибута у всех элементов
        obj.printAttribute("name");


    }
}
