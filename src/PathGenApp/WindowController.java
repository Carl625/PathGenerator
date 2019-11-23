package PathGenApp;

import Resources.*;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class WindowController {

    // panes
    public Pane mainPane;

    // display elements
    enum displayState {display, dragStarted, dragging, dragFinished,};

    public Pane telemetryPane;

    public ListView telemetryList;

    public TableView funcInfoTable;

    public Canvas fieldDisplay;
    public GraphicsContext graphics;
    public Image field;

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

    private int funcSelected = -1;

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

        try {
            FileInputStream f = new FileInputStream("../PathGenerator/src/Resources/Images/topviewfield.png");
            field = new Image(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        graphics = fieldDisplay.getGraphicsContext2D();
        resetField();

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
            rotation = Math.toRadians(Double.parseDouble(rotationInput.getText()));
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

        
    }

    public void dragDetected(MouseEvent mouseEvent) {

    }

    public void dragDropped(DragEvent dragEvent) {

    }

    /* ---------- Data Methods ----------*/

    private void resetField() {
        graphics.drawImage(field, 0, 0);
    }

    private void regenFunctions() {

        Function[] orderedFunctions = functions.toArray(new Function[0]);
        double[] newAngles = rotations.stream().mapToDouble(d -> d).toArray();

        displayedFunctions.addAll(Arrays.asList(ParametricPath.parametrize(orderedFunctions, newAngles)));
    }

    private void updateDisplay() {
        // variables
        Function[] orderedFunctions = functions.toArray(new Function[0]);
        Vector2D[] newTranslations = translations.toArray(new Vector2D[0]);
        double[] newTRanges = tRanges.stream().mapToDouble(d -> d).toArray();
        double[][] newDefRanges = new ArrayList<double[]>(definedFunctionRanges).toArray(new double[definedFunctionRanges.size()][]);
        double[] newAngles = rotations.stream().mapToDouble(d -> d).toArray();

        // draw
        resetField();
        regenFunctions();

        for (int p = 0; p < displayedFunctions.size(); p++) {

            // prepare the canvas
            double[] funcBoundingBox = displayedFunctions.get(p).findBounds(newDefRanges[p]);
            double xRange = funcBoundingBox[2] - funcBoundingBox[0];
            double yRange = funcBoundingBox[3] - funcBoundingBox[1];
            BufferedImage func = new BufferedImage((int) xRange, (int) yRange, BufferedImage.TYPE_INT_RGB);

            // draw the function
            drawFunction(displayedFunctions.get(p), new Vector2D(funcBoundingBox[0], funcBoundingBox[1]), newDefRanges[p], func);

            // scale the canvas to the right size
            double currentScale = (xRange * yRange) / (fieldDisplay.getBoundsInLocal().getWidth() * fieldDisplay.getBoundsInLocal().getHeight());
            double calculatedScale = scales.get(p) / currentScale;

            // draw the frickin function my dudes
            draw(fieldDisplay, func, (int) newTranslations[p].getComponent(0).doubleValue(), (int) newTranslations[p].getComponent(1).doubleValue(), calculatedScale);
        }

        // making sure it's good to export or not
        boolean exportReady = ParametricPath.isValid(ParametricPath.parametrize(orderedFunctions, newAngles), newTranslations, newTRanges, newDefRanges);

        if (exportReady) {
            exportFunction.setDisable(false);
        } else {
            exportFunction.setDisable(true);
        }

    }

    private void drawFunction(ParametricFunction2D p, Vector2D offset, double[] funcRange, BufferedImage canvas) {

        for (double t = funcRange[0]; t <= funcRange[1]; t++) {

            Vector2D point = p.output(t);
            point.add(offset);

            if ((point.getComponent(0) > 0 && point.getComponent(0) < canvas.getWidth()) && (point.getComponent(1) > 0 && point.getComponent(1) < canvas.getHeight())) {

                canvas.setRGB((int) point.getComponent(0).doubleValue(), (int) point.getComponent(1).doubleValue(), java.awt.Color.green.getRGB());
            }
        }
    }

    public void draw(Canvas c, BufferedImage image) {

        draw(c, image, 0, 0);
    }

    public void draw(Canvas c, BufferedImage image, int xOffset, int yOffset) {

        draw(c, image, xOffset, yOffset, 1.0);
    }

    public void draw(Canvas c, BufferedImage image, int xOffset, int yOffset, double scale) {

        PixelWriter drawInterface = c.getGraphicsContext2D().getPixelWriter();

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                int RGB = image.getRGB(x, y);
                java.awt.Color newColor = new java.awt.Color(RGB);
                int xPixel = (int) ((x * scale) + xOffset);
                int yPixel = (int) ((y * scale) + yOffset);

                if ((xPixel >= 0 && xPixel < c.getWidth()) && (yPixel >= 0 && yPixel < c.getHeight()) ) {
                    drawInterface.setColor(xPixel, yPixel, new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), newColor.getAlpha()));
                }
            }
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
    public Image draw(ParametricFunction2D newFunc, double deltaT, double[] fRange , boolean polarity) {

        Pair<Function, Function> process;
        double[] funcBounds = newFunc.findBounds(fRange);
        Vector2D offset = new Vector2D(funcBounds[0], funcBounds[1]);

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
