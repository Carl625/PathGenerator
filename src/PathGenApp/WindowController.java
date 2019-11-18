package PathGenApp;

import Resources.FileFunctions;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class WindowController {

    enum displayState {display, dragStarted, dragging, dragFinished,};

    public Pane telemetryPane;

    public ListView telemetryList;

    public TableView funcInfoTable;

    public ImageView fieldDisplay;

    public TextField funcStrInput;
    public TextField pathRangeInput;
    public TextField fRangeInit;
    public TextField fRangeEnd;
    public TextField transXInput;
    public TextField transYInput;
    public TextField rotationInput;

    public Button deleteFunction;
    public Button exportFunction;
    public Button genFunction;

    public Label functionStrLabel;
    public Label tRangeLabel;
    public Label fRangeInitLabel;
    public Label fRangeEndLabel;
    public Label transXLabel;
    public Label transYLabel;
    public Label rotationLabel;

    /* ---------- Component Functions ----------*/

    // buttons
    public void deleteFunc(ActionEvent actionEvent) {

    }

    public void exportFunc(ActionEvent actionEvent) {

    }

    public void genFunc(ActionEvent actionEvent) {

    }

    // text fields
    public void setFuncString(ActionEvent actionEvent) {

    }

    public void setPathRange(ActionEvent actionEvent) {

    }

    public void setInitFuncLimit(ActionEvent actionEvent) {

    }

    public void setEndFuncLimit(ActionEvent actionEvent) {

    }

    public void setTransX(ActionEvent actionEvent) {

    }

    public void setTransY(ActionEvent actionEvent) {

    }

    public void setRotation(ActionEvent actionEvent) {

    }

    // image view
    public void fieldClicked(MouseEvent mouseEvent) {

        // TODO: implement cool file functions to load and save images
    }

    public void dragDetected(MouseEvent mouseEvent) {

    }

    public void dragDropped(DragEvent dragEvent) {

    }
}
