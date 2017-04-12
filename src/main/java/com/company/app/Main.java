package com.company.app;

/**
 * Created by xk on 08.04.17.
 */
public class Main {
    public static void main(String[] args) {
        String filePath = "src/main/resources/music_catalogue.xml";

        // 2. Выведите данные документа на консоль
        XmlDomObj obj = new XmlDomObj(filePath);
        //obj.printXml();

        // 3. Создайте объект XmlDocument и начните работу с xml-документом
        // 4. Добавьте в документ 2 новых элемента
        obj.addNode(new String[]{"Artist"}, "Name=Coffman_21");
        obj.addNode(new String[]{"Album", "Artist", "Name=Coffman_21"}, "Title=Demo","Release_date=2015","Type=Single");
        obj.addNode(new String[]{"Song", "Album", "Title=Demo"}, "No=1","Title=I Am Awesome","Length=4:20","Music=Xenon Krypton","Genre=Club");
        obj.printXml();

        // 5. Удалите из документа первый узел
        obj.deleteNode("Artist","Name=Metallica");
        System.out.println(obj.toString());

        // 6. Выведите на консоль значения какого-либо атрибута у всех элементов
        obj.printAttribute("Genre");

        // 3. Выберите те элементы, атрибуты которых имеют только определенное значение
        obj.printAttribute("Genre", "Cloud Rap");

        // 4. Осуществите выборку вниз по иерархии элементов
        obj.printXml();



    }
}
