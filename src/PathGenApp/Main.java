package PathGenApp;

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        Parent root = loader.load();
        Bounds rootBounds = root.getBoundsInLocal();

        //set up the data inside the controller
        WindowController mainController = loader.getController();
        mainController.initialize();

        primaryStage.setTitle("Path Generator");
        Scene worldScene = new Scene(root, rootBounds.getWidth(), rootBounds.getHeight());
        primaryStage.setScene(worldScene);
        primaryStage.setResizable(false);
        primaryStage.show();
        fixBounds(primaryStage, root);
    }

    private void fixBounds(Stage stage, Parent root) {

        Bounds rootBounds = root.getBoundsInLocal();
        Bounds prefBounds = getPrefBounds(root);

        double deltaW = stage.getWidth() - rootBounds.getWidth();
        double deltaH = stage.getHeight() - rootBounds.getHeight();

//        System.out.println(deltaW);
//        System.out.println(deltaH);

        stage.setMinWidth(prefBounds.getWidth() + deltaW);
        stage.setMinHeight(prefBounds.getHeight() + deltaH);
        stage.setMaxWidth(prefBounds.getWidth() + deltaW);
        stage.setMaxHeight(prefBounds.getHeight() + deltaH);
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
