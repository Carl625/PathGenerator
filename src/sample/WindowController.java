package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;

public class WindowController {
    public Label helloWorld;
    public ImageView field;

    public void sayHelloWorld(ActionEvent actionEvent) {

        helloWorld.setText("Hello World!");
    }

    public void updateField() {

        // TODO: figure out how to make JavaFX Images to pass to the ImageView
    }
}
