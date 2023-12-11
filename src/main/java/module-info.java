module com.group.chipgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires org.json;
    requires javafx.graphics;


    opens com.group4.chipgame to javafx.fxml;
    exports com.group4.chipgame;
    exports com.group4.chipgame.ui;
    exports com.group4.chipgame.profile;
    exports com.group4.chipgame.Level.saving;
    exports com.group4.chipgame.entities.actors;
    exports com.group4.chipgame.entities.actors.collectibles;
    exports com.group4.chipgame.entities.actors.tiles;
    opens com.group4.chipgame.entities.actors to javafx.fxml;
    exports com.group4.chipgame.menu;
    opens com.group4.chipgame.menu to javafx.fxml;
    exports com.group4.chipgame.Level;
    opens com.group4.chipgame.Level to javafx.fxml;
}