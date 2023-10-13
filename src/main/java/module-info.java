module com.group4.chipgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires org.json;
    requires javafx.graphics;


    opens com.group4.chipgame to javafx.fxml;
    exports com.group4.chipgame;
    exports com.group4.chipgame.actors;
    opens com.group4.chipgame.actors to javafx.fxml;
}