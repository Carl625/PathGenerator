package PathGenApp;

import Resources.*;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class WindowController {

    // panes
    public Pane mainPane;

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
    public TextField funcScaleInput;
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
    public Label funcScaleLabel;
    public Label transXLabel;
    public Label transYLabel;
    public Label rotationLabel;

    public TableColumn funcColumn;
    public TableColumn transColumn;
    public TableColumn rotColumn;
    public TableColumn scaleColumn;
    public TableColumn tRangeColumn;
    public TableColumn fRangeColumn;

    //display data
    private ArrayList<ParametricFunction2D> displayedFunctions;

    // Path storage
    private ArrayList<Function> functions;
    private ArrayList<Vector2D> translations;
    private ArrayList<Double> rotations;
    private ArrayList<Double> scales;
    private ArrayList<Double> tRanges;
    private ArrayList<double[]> definedFunctionRanges;

    private int funcSelected = -1; // add the listener for the selection model
    private boolean modifyFinished = true;

    public void initialize() {

        //table

        funcColumn.setCellValueFactory(new PropertyValueFactory<>("Function"));
        transColumn.setCellValueFactory(new PropertyValueFactory<>("Translation"));
        rotColumn.setCellValueFactory(new PropertyValueFactory<>("Rotation"));
        scaleColumn.setCellValueFactory(new PropertyValueFactory<>("Scale"));
        tRangeColumn.setCellValueFactory(new PropertyValueFactory<>("TRange"));
        fRangeColumn.setCellValueFactory(new PropertyValueFactory<>("FuncRange"));

        funcInfoTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> rowSelected());

        // buttons
        deleteFunction.setDisable(true);
        exportFunction.setDisable(true);

        // data
        displayedFunctions = new ArrayList<ParametricFunction2D>();

        functions = new ArrayList<Function>();
        translations = new ArrayList<Vector2D>();
        rotations = new ArrayList<Double>();
        scales = new ArrayList<Double>();
        tRanges = new ArrayList<Double>();
        definedFunctionRanges = new ArrayList<double[]>();

        Image field = null;

        try {
            FileInputStream f = new FileInputStream("../PathGenerator/src/Resources/Images/topviewfield.png");
            field = new Image(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        fieldDisplay.setImage(field);

        // debug
        FuncTableEntry f = new FuncTableEntry("(x + 2)", 0.25, new Vector2D(0, 0), 0, 3, new double[] {-20, 12});
        loadRow(f);
    }

    /* ---------- Component Functions ----------*/

    // pane
    public void mainPaneClicked(MouseEvent mouseEvent) {

        if (funcSelected != -1) {

            funcSelected = -1;
            clearInputs();
            funcInfoTable.getSelectionModel().clearSelection();
            deleteFunction.setDisable(true);
            genFunction.setText("Generate");
        }
    }

    // buttons
    public void deleteFunc(ActionEvent actionEvent) {


        // change the internal data
        functions.remove(funcSelected);
        translations.remove(funcSelected);
        rotations.remove(funcSelected);
        scales.remove(funcSelected);
        tRanges.remove(funcSelected);
        definedFunctionRanges.remove(funcSelected);

        funcInfoTable.getItems().remove(funcSelected); // overwrite the selected row

        clearInputs();
        funcInfoTable.getSelectionModel().clearSelection();

        if (funcInfoTable.getItems().size() == 0) {

            // clear the modify state
            deleteFunction.setDisable(true);
            genFunction.setText("Generate");
        }
    }

    public void exportFunc(ActionEvent actionEvent) {

        // run method that exports current path
    }

    public void genFunc(ActionEvent actionEvent) {

        boolean properlyFormatted = true;

        Function newFunc = null;

        try {
            newFunc = Function.constSimplify(new Function(funcStrInput.getText(), "x", new HashMap<String, Double>()));
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

        double scale = 0.0;

        try {
            scale = Double.parseDouble(funcScaleInput.getText());
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

            FuncTableEntry newRow = new FuncTableEntry(newFunc.toString(), scale, translation, rotation, tRange, fRange);

            if (funcSelected >= 0) {

                // change the internal data
                functions.set(funcSelected, newFunc);
                translations.set(funcSelected, translation);
                rotations.set(funcSelected, rotation);
                scales.set(funcSelected, scale);
                tRanges.set(funcSelected, tRange);
                definedFunctionRanges.set(funcSelected, fRange);

                funcInfoTable.getItems().set(funcSelected, newRow); // overwrite the selected row

                // clear the modify state
                clearInputs();
                deleteFunction.setDisable(true);
                genFunction.setText("Generate");
            } else {

                // add to internal data
                functions.add(newFunc);
                translations.add(translation);
                rotations.add(rotation);
                scales.add(scale);
                tRanges.add(tRange);
                definedFunctionRanges.add(fRange);

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
    public void rowSelected() {

        funcSelected = funcInfoTable.getSelectionModel().getSelectedIndex();
        System.out.println("row Selected: " + funcSelected);
        // set funcSelected equal to something
        if (funcSelected != -1) {
            loadRow((FuncTableEntry) funcInfoTable.getSelectionModel().getSelectedItem()); // loads the row to be modified
            deleteFunction.setDisable(false);
            genFunction.setText("Modify");
        }
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

    private void updateDisplay() {

        // draw



        // making sure it's good to export or not
        boolean exportReady = true;

        Function[] orderedFunctions = functions.toArray(new Function[0]);
        Vector2D[] newTranslations = translations.toArray(new Vector2D[0]);
        double[] newTRanges = tRanges.stream().mapToDouble(d -> d).toArray();
        double[][] newDefRanges = new ArrayList<double[]>(definedFunctionRanges).toArray(new double[definedFunctionRanges.size()][]);
        double[] newAngles = rotations.stream().mapToDouble(d -> d).toArray();

        ParametricPath path = null;

        try {
            path = new ParametricPath(orderedFunctions, newTranslations, newTRanges, newDefRanges, newAngles);
        } catch (FunctionFormatException e) {
            exportReady = false;
        }

        if (exportReady) {

            exportFunction.setDisable(false);
        } else {

            exportFunction.setDisable(true);
        }

    }

    private void loadRow(int rowNum) {

        FuncTableEntry f = (FuncTableEntry) funcInfoTable.getItems().get(rowNum);

        loadRow(f);
    }

    private void loadRow(FuncTableEntry f) {

        funcStrInput.setText(f.getFunction());
        pathRangeInput.setText(f.getTRange());
        fRangeInit.setText(String.valueOf(f.getFuncRangeVar()[0]));
        fRangeEnd.setText(String.valueOf(f.getFuncRangeVar()[1]));
        funcScaleInput.setText(f.getScale());
        transXInput.setText(String.valueOf(f.getTranslationVar().getComponents()[0]));
        transYInput.setText(String.valueOf(f.getTranslationVar().getComponents()[1]));
        rotationInput.setText(f.getRotation());
    }

    private void clearInputs() {

        funcStrInput.clear();
        pathRangeInput.clear();
        fRangeInit.clear();
        fRangeEnd.clear();
        funcScaleInput.clear();
        transXInput.clear();
        transYInput.clear();
        rotationInput.clear();
    }


    // drawing algorithms
    public Image draw(ParametricFunction2D newFunc, double initT, double deltaT, double fRange , boolean polarity) {

        Pair<Function, Function> process;
        double xOffset = 0;
        double yOffset = 0;

        if (polarity) {
            process = newFunc.getPolarComponents();
            double r = process.get1().output(initT);
            double theta = process.get2().output(initT);

            xOffset = r * Math.cos(theta);
            yOffset = r * Math.sin(theta);
        } else {
            process = newFunc.getRectangularComponents();
            xOffset = process.get1().output(initT);
            yOffset = process.get2().output(initT);
        }

        ArrayList<Pair<Double, Double>> points = new ArrayList<Pair<Double, Double>>();

//        Coordinate lastCoord = null; // TODO: IMPLEMENT GOOD THING
//
//        int pointNum = 0;
//        Coordinate point = getPoint(pointNum);
//
//        while (point != null) {
//
//            double xPixel = pixelStart[0] + (point.getX() - displayRange.get1().getX()) * (graphWidth / xRange);
//            double yPixel = pixelStart[1] + (displayRange.get1().getY() + yRange - point.getY()) * (graphHeight / yRange);
//
//            if (polarity) {
//
//                xPixel = pixelStart[0] + ((point.getX() * Math.cos(point.getY())) - displayRange.get1().getX()) * (graphWidth / xRange);
//                yPixel = pixelStart[1] + (displayRange.get1().getY() + yRange - (point.getX() * Math.sin(point.getY()))) * (graphHeight / yRange);
//            }
//
//            if (lastCoord != null) {
//                if (point.drawTo() && lastCoord.drawFrom()) {
//                    g.drawLine((int) lastCoord.getX(), (int) lastCoord.getY(), (int) xPixel, (int) yPixel);
//                }
//            }
//
//
//            lastCoord = new Coordinate(xPixel, yPixel, point.drawFrom(), point.drawTo());
//            pointNum++;
//            point = getPoint(pointNum);
//        }

        Image function = null;

        return function;
    }
}
