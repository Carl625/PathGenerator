package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        Bounds rootBounds = root.getBoundsInLocal();

        primaryStage.setTitle("Hello World");
        Scene worldScene = new Scene(root, rootBounds.getWidth(), rootBounds.getHeight());
        primaryStage.setScene(worldScene);
        //primaryStage.setResizable(false);
        primaryStage.show();
        fixBounds(primaryStage, root);
    }

    private void fixBounds(Stage stage, Parent root) {
        double deltaW = stage.getWidth() - stage.getWidth();
        double deltaH = stage.getHeight() - stage.getHeight();

        Bounds prefBounds = getPrefBounds(root);

        stage.setMinWidth(prefBounds.getWidth() + deltaW);
        stage.setMinHeight(prefBounds.getHeight() + deltaH);
    }

    private Bounds getPrefBounds(Node node) {
        double prefWidth ;
        double prefHeight ;

        Orientation bias = node.getContentBias();
        if (bias == Orientation.HORIZONTAL) {
            prefWidth = node.prefWidth(-1);
            prefHeight = node.prefHeight(prefWidth);
        } else if (bias == Orientation.VERTICAL) {
            prefHeight = node.prefHeight(-1);
            prefWidth = node.prefWidth(prefHeight);
        } else {
            prefWidth = node.prefWidth(-1);
            prefHeight = node.prefHeight(-1);
        }
        return new BoundingBox(0, 0, prefWidth, prefHeight);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
