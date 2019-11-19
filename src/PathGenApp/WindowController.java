package PathGenApp;

import Resources.*;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class WindowController {

    // display elements
    enum displayState {display, dragStarted, dragging, dragFinished,};

    public Pane telemetryPane;

    public ListView telemetryList;

    public TableView funcInfoTable;

    public ImageView fieldDisplay;

    public TextField funcStrInput;
    public TextField pathRangeInput;
    public TextField fRangeInit;
    public TextField fRangeEnd;
    public TextField funcVarInput;
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
    public Label funcVarLabel;
    public Label transXLabel;
    public Label transYLabel;
    public Label rotationLabel;

    public TableColumn funcColumn;
    public TableColumn transColumn;
    public TableColumn rotColumn;
    public TableColumn tRangeColumn;
    public TableColumn fRangeColumn;

    //display data
    private ArrayList<ParametricFunction2D> displayedFunctions;

    // Path storage
    private ArrayList<Function> functions;
    private ArrayList<Vector2D> translations;
    private ArrayList<Double> rotations;
    private ArrayList<double[]> definedFunctionRanges;
    private ArrayList<double[]> tRanges;
    private ArrayList<Double> maxSpeed;

    private int funcSelected = -1; // add the listener for the selection model

    public void initialize() {

        //table

        funcColumn.setCellValueFactory(new PropertyValueFactory<>("Function"));
        transColumn.setCellValueFactory(new PropertyValueFactory<>("Translation"));
        rotColumn.setCellValueFactory(new PropertyValueFactory<>("Rotation"));
        tRangeColumn.setCellValueFactory(new PropertyValueFactory<>("TRange"));
        fRangeColumn.setCellValueFactory(new PropertyValueFactory<>("FuncRange"));

        funcInfoTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> rowSelected(getRow((FuncTableEntry) observable.getValue())));

        // data
        displayedFunctions = new ArrayList<ParametricFunction2D>();

        functions = new ArrayList<Function>();
        translations = new ArrayList<Vector2D>();
        rotations = new ArrayList<Double>();

        tRanges = new ArrayList<double[]>();
        definedFunctionRanges = new ArrayList<double[]>();

        maxSpeed = new ArrayList<Double>();

        Image field = null;

        try {
            FileInputStream f = new FileInputStream("../PathGenerator/src/Resources/Images/topviewfield.png");
            field = new Image(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        fieldDisplay.setImage(field);
    }

    /* ---------- Component Functions ----------*/

    // buttons
    public void deleteFunc(ActionEvent actionEvent) {


    }

    public void exportFunc(ActionEvent actionEvent) {

    }

    public void genFunc(ActionEvent actionEvent) {

        boolean properlyFormatted = true;

        if (funcSelected >= 0) {

            //TODO: remember to make the generate button change to a modify button when a function is selected
        }

        Function newFunc = null;

        try {
            newFunc = Function.simplify(new Function(funcStrInput.getText(), funcVarInput.getText(), new HashMap<String, Double>()));
        } catch (Exception e) {
            properlyFormatted = false;
        }

        double xComp = 0.0;
        double yComp = 0.0;
        try {
             xComp = Double.parseDouble(transXInput.getText());
             yComp = Double.parseDouble(transYInput.getText());
        } catch (Exception e) {

            properlyFormatted = false;
        }

        Vector2D translation = new Vector2D(xComp, yComp);

        double rotation = 0.0;

        try {
            rotation = Double.parseDouble(rotationInput.getText());
        } catch (Exception e) {
            properlyFormatted = false;
        }

        double tRange = 0.0;

        try {
            tRange = Double.parseDouble(pathRangeInput.getText());
        } catch (Exception e) {
            properlyFormatted = false;
        }

        double[] fRange = new double[2];

        try {
            fRange[0] = Double.parseDouble(fRangeInit.getText());
            fRange[1] = Double.parseDouble(fRangeEnd.getText());
        } catch (Exception e) {
            properlyFormatted = false;
        }

        if (properlyFormatted) {

            FuncTableEntry newRow = new FuncTableEntry(newFunc.toString(), newFunc.getVariable(), translation, rotation, tRange, fRange);

            if (funcSelected >= 0) {
                funcInfoTable.getItems().set(funcSelected, newRow); // overwrite the selected row
            } else {
                funcInfoTable.getItems().add(newRow); // add a new row
            }
        } else {
            // make a window pop up
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText("Format Error");
            error.setContentText("There was an error parsing the function parameters input");
            error.showAndWait();
        }
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

    public void setFuncVar(ActionEvent actionEvent) {

    }

    public void setTransX(ActionEvent actionEvent) {

    }

    public void setTransY(ActionEvent actionEvent) {

    }

    public void setRotation(ActionEvent actionEvent) {

    }

    // table
    public void rowSelected(int row) { // whatever it's gonna be called

        funcSelected = row;
        // set funcSelected equal to something
        loadRow(funcSelected); // loads the row to be modified
    }

    // image view
    public void fieldClicked(MouseEvent mouseEvent) {

        // TODO: implement cool file functions to load and save images
    }

    public void dragDetected(MouseEvent mouseEvent) {

    }

    public void dragDropped(DragEvent dragEvent) {

    }

    /* ---------- Data Methods ----------*/

    public int getRow(FuncTableEntry tableEntry) {

        for (Object f: funcInfoTable.getItems()) {

            FuncTableEntry current = (FuncTableEntry) f;

            if (current.equals(tableEntry)) { // do a deep check


            }
        }

        return 0; // fix
    }

    private void loadRow(int rowNum) {

        FuncTableEntry f = (FuncTableEntry) funcInfoTable.getItems().get(rowNum);

        funcStrInput.setText(f.getFunction());
        pathRangeInput.setText(f.getTRange());
        fRangeInit.setText(String.valueOf(f.getFuncRangeVar()[0]));
        fRangeEnd.setText(String.valueOf(f.getFuncRangeVar()[1]));
        funcVarInput.setText(f.getVariable());
        transXInput.setText(String.valueOf(f.getTranslationVar().getComponents()[0]));
        transYInput.setText(String.valueOf(f.getTranslationVar().getComponents()[1]));
        rotationInput.setText(f.getRotation());
    }

}
