module com.group.chipgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires org.json;
    requires javafx.graphics;


    opens com.group4.chipgame to javafx.fxml;
    exports com.group4.chipgame;
    exports com.group4.chipgame.actors;
    exports com.group4.chipgame.collectibles;
    exports com.group4.chipgame.tiles;
    opens com.group4.chipgame.actors to javafx.fxml;
    exports com.group4.chipgame.menu;
    opens com.group4.chipgame.menu to javafx.fxml;
}