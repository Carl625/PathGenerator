package PathGenApp;

import Resources.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
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
    public TextField funcScaleXInput;
    public TextField funcScaleYInput;
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
    public Label funcScaleXLabel;
    public Label funcScaleYLabel;
    public Label funcVarLabel;
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
    private ArrayList<Pair<Double, Double>> scales;
    private ArrayList<Double> tRanges;
    private ArrayList<double[]> definedFunctionRanges;

    private int funcSelectedTable = -1;
    private int funcSelectedField = -1;

    private java.awt.Color ignoreDrawColor = new java.awt.Color(230, 230, 230);

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
        scales = new ArrayList<Pair<Double, Double>>();
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
        FuncTableEntry f = new FuncTableEntry("(x + 2)", "x",  0.25, 0.5, new Vector2D(0, 0), 0, 3, new double[] {-20, 12});
        loadRow(f);
    }

    /* ---------- Component Functions ----------*/

    // pane
    public void mainPaneClicked(MouseEvent mouseEvent) {

        Vector2D clickedPoint = new Vector2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());

        if (funcSelectedTable != -1) {

            funcSelectedTable = -1;
            clearInputs();
            funcInfoTable.getSelectionModel().clearSelection();
            deleteFunction.setDisable(true);
            genFunction.setText("Generate");
        }

        Vector2D fieldDisplayOffset = new Vector2D(fieldDisplay.getLayoutX(), fieldDisplay.getLayoutY());
        clickedPoint.sub(fieldDisplayOffset);

        if (!fieldDisplay.contains(Vector2D.convert(clickedPoint))) {

            funcSelectedField = -1;
            System.out.println("Reset selected Function!");
        }
    }

    // buttons
    public void deleteFunc(ActionEvent actionEvent) {


        // change the internal data
        functions.remove(funcSelectedTable);
        translations.remove(funcSelectedTable);
        rotations.remove(funcSelectedTable);
        scales.remove(funcSelectedTable);
        tRanges.remove(funcSelectedTable);
        definedFunctionRanges.remove(funcSelectedTable);

        funcInfoTable.getItems().remove(funcSelectedTable); // overwrite the selected row

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

        String variable = funcVarInput.getText();

        if (variable.contains(" ") || "+-*/^".contains(variable)) {

            properlyFormatted = false;
        }

        Function newFunc = null;

        if (properlyFormatted) {
            try {
                newFunc = Function.constSimplify(new Function(funcStrInput.getText(), variable, new HashMap<String, Double>()));
            } catch (Exception e) {
                properlyFormatted = false;
            }
        }

        double xComp = 0.0;
        double yComp = 0.0;
        try {
             xComp = Double.parseDouble(transXInput.getText());
             yComp = Double.parseDouble(transYInput.getText());
        } catch (Exception e) {

            properlyFormatted = false;
        }

        double scaleX = 0.0;
        double scaleY = 0.0;

        try {
            scaleX = Double.parseDouble(funcScaleXInput.getText());
            scaleY = Double.parseDouble(funcScaleYInput.getText());
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

            FuncTableEntry newRow = new FuncTableEntry(newFunc.toString(), variable, scaleX, scaleY, translation, rotation, tRange, fRange);

            if (funcSelectedTable >= 0) {

                // change the internal data
                functions.set(funcSelectedTable, newFunc);
                translations.set(funcSelectedTable, translation);
                rotations.set(funcSelectedTable, rotation);
                scales.set(funcSelectedTable, new Pair<Double, Double>(scaleX, scaleY));
                tRanges.set(funcSelectedTable, tRange);
                definedFunctionRanges.set(funcSelectedTable, fRange);

                funcInfoTable.getItems().set(funcSelectedTable, newRow); // overwrite the selected row

                // clear the modify state
                clearInputs();
                deleteFunction.setDisable(true);
                genFunction.setText("Generate");
            } else {

                // add to internal data
                functions.add(newFunc);
                translations.add(translation);
                rotations.add(rotation);
                scales.add(new Pair<Double, Double>(scaleX, scaleY));
                tRanges.add(tRange);
                definedFunctionRanges.add(fRange);

                funcInfoTable.getItems().add(newRow); // add a new row
            }

            updateDisplay();
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

        funcSelectedTable = funcInfoTable.getSelectionModel().getSelectedIndex();
        System.out.println("row Selected: " + funcSelectedTable);
        // set funcSelected equal to something
        if (funcSelectedTable != -1) {
            loadRow((FuncTableEntry) funcInfoTable.getSelectionModel().getSelectedItem()); // loads the row to be modified
            deleteFunction.setDisable(false);
            genFunction.setText("Modify");
        }
    }

    // image view
    public void fieldClicked(MouseEvent mouseEvent) {
        Vector2D mouseClick = new Vector2D(mouseEvent.getSceneX() - fieldDisplay.getLayoutX(), mouseEvent.getSceneY() - fieldDisplay.getLayoutY());

        if (funcSelectedField == -1) { // picking a function up

            funcSelectedField = findSelectedFunc(mouseClick);
        } else { // placing a function down

            double[] bounds = getDisplayBoundingBox(funcSelectedField);
            Vector2D centerDisp = new Vector2D((bounds[0] - bounds[2]) / 2.0, (bounds[1] - bounds[3]) / 2.0);
            Vector2D boundOffset = Vector2D.add(mouseClick, centerDisp);
            System.out.println("Function: " + funcSelectedField + " New Translation: " + boundOffset);

            translations.set(funcSelectedField, boundOffset);
            updateDisplay();
        }
    }

    private int findSelectedFunc(Vector2D clickedPoint) {

        if (displayedFunctions.size() > 0) {

            double min = Double.MAX_VALUE;
            int maxIndex = 0;
            boolean foundBox = false;

            for (int f = 0; f < displayedFunctions.size(); f++) {

                ParametricFunction2D p = displayedFunctions.get(f);
                double[] boundingBox = getDisplayBoundingBox(f);
                System.out.println(Arrays.toString(boundingBox));

                if ((clickedPoint.getComponent(0) > boundingBox[0] && clickedPoint.getComponent(0) < boundingBox[2]) && (clickedPoint.getComponent(1) > boundingBox[1] && clickedPoint.getComponent(1) < boundingBox[3])) {
                    System.out.println("found function box!");
                    Vector2D distance = new Vector2D((boundingBox[2] - boundingBox[0]) / 2.0, (boundingBox[3] - boundingBox[1]) / 2.0);
                    distance.sub(clickedPoint);

                    if (distance.getMag() < min) {

                        min = distance.getMag();
                        maxIndex = f;
                    }

                    foundBox = true;
                }
            }

            if (foundBox) {
                return maxIndex;
            } else {
                return -1;
            }
        }

        return -1;
    }


    public void dragDetected(MouseEvent mouseEvent) {

    }

    public void dragDropped(DragEvent dragEvent) {

    }

    /* ---------- Data Methods ----------*/

    private double[] getDisplayBoundingBox(int func) {

        ParametricFunction2D p = displayedFunctions.get(func);
        double[] boundingBox = p.findBounds(definedFunctionRanges.get(func));
        Pair<Double, Double> scale = scales.get(func);
        Vector2D trans = translations.get(func);

        double xRange = boundingBox[2] - boundingBox[0];
        double yRange = boundingBox[3] - boundingBox[1];

        double currentScaleX = xRange / fieldDisplay.getBoundsInLocal().getWidth();
        double calcScaleX = scale.get1() / currentScaleX;

        double currentScaleY = yRange / fieldDisplay.getBoundsInLocal().getHeight();
        double calcScaleY = scale.get2() / currentScaleY;

        System.out.println(scale);

        boundingBox[2] -= boundingBox[0];
        boundingBox[0] -= boundingBox[0];
        boundingBox[3] -= boundingBox[1];
        boundingBox[1] -= boundingBox[1];

        boundingBox[0] += trans.getComponent(0);
        boundingBox[2] *= calcScaleX;
        boundingBox[2] += trans.getComponent(0);

        boundingBox[1] += trans.getComponent(1);
        boundingBox[3] *= calcScaleY;
        boundingBox[3] += trans.getComponent(1);

        return boundingBox;
    }

    private void resetField() {
        graphics.drawImage(field, 0, 0);
    }

    private void regenFunctions() {
        Function[] orderedFunctions = functions.toArray(new Function[0]);
        double[] newAngles = rotations.stream().mapToDouble(d -> d).toArray();

        displayedFunctions.clear();
        displayedFunctions.addAll(Arrays.asList(ParametricPath.parametrize(orderedFunctions, newAngles)));
    }

    @FXML
    protected void updateDisplay() {
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
//            System.out.println(displayedFunctions.get(p));
//            System.out.println(Arrays.toString(newDefRanges[p]));
            double[] funcBoundingBox = displayedFunctions.get(p).findBounds(newDefRanges[p]);
            double xRange = funcBoundingBox[2] - funcBoundingBox[0];
            double yRange = funcBoundingBox[3] - funcBoundingBox[1];
            //System.out.println("Bounding Box: " + Arrays.toString(funcBoundingBox));

            // scale the canvas to the right size
            double currentScaleX = xRange / fieldDisplay.getBoundsInLocal().getWidth();
            double calcScaleX = scales.get(p).get1() / currentScaleX;

            double currentScaleY = yRange / fieldDisplay.getBoundsInLocal().getHeight();
            double calcScaleY = scales.get(p).get2() / currentScaleY;

            BufferedImage func = new BufferedImage((int) (xRange * calcScaleX), (int) (yRange * calcScaleY), BufferedImage.TYPE_INT_RGB);;
            fill(func, ignoreDrawColor);

            // draw the function

            drawFunction(displayedFunctions.get(p), new Vector2D(funcBoundingBox[0], funcBoundingBox[1]), newDefRanges[p], func, calcScaleX, calcScaleY);

            // draw the frickin function my dudes
            draw(fieldDisplay, func, (int) newTranslations[p].getComponent(0).doubleValue(), (int) newTranslations[p].getComponent(1).doubleValue());
            //fill(fieldDisplay, Color.GREEN);
        }

        // making sure it's good to export or not
        boolean exportReady = ParametricPath.isValid(ParametricPath.parametrize(orderedFunctions, newAngles), newTranslations, newTRanges, newDefRanges);

        if (exportReady) {
            exportFunction.setDisable(false);
        } else {
            exportFunction.setDisable(true);
        }
    }

    private void drawFunction(ParametricFunction2D p, Vector2D offset, double[] funcRange, BufferedImage canvas, double scaleX, double scaleY) {

//        System.out.println("Image Height: " + canvas.getHeight());
//        System.out.println("Image Width: " + canvas.getWidth());
//        System.out.println("Offset: " + offset);

        Vector2D previousPoint = null;
        Graphics g = canvas.getGraphics();
        g.setColor(java.awt.Color.green);

        for (double t = funcRange[0]; t <= funcRange[1]; t += 0.1) {

            Vector2D point = p.output(t);
            point.sub(offset);
            point.scale(0, scaleX);
            point.scale(1, scaleY);
            //System.out.println("func Point: " + point);

            if ((point.getComponent(0) > 0 && point.getComponent(0) < canvas.getWidth()) && (point.getComponent(1) > 0 && point.getComponent(1) < canvas.getHeight())) {

                canvas.setRGB((int) point.getComponent(0).doubleValue(), (int) point.getComponent(1).doubleValue(), java.awt.Color.green.getRGB());

                if (previousPoint != null) {
                    g.drawLine((int) previousPoint.getComponent(0).doubleValue(), (int) previousPoint.getComponent(1).doubleValue(), (int) point.getComponent(0).doubleValue(), (int) point.getComponent(1).doubleValue());
                }

                //System.out.println(new java.awt.Color(canvas.getRGB((int) point.getComponent(0).doubleValue(), (int) point.getComponent(1).doubleValue())));
            }

            previousPoint = point;
        }
    }

    public void fill(Canvas c, Color fillColor) {

        PixelWriter p = c.getGraphicsContext2D().getPixelWriter();

        for (int x = 0; x < c.getWidth(); x++) {
            for (int y = 0; y < c.getHeight(); y++) {

                p.setColor(x, y, fillColor);
            }
        }
    }

    public void fill(BufferedImage image, Color fillColor) {

        fill(image, convert(fillColor));
    }

    public void fill(BufferedImage image, java.awt.Color fillColor) {

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                image.setRGB(x, y, fillColor.getRGB());
            }
        }
    }

    public void draw(Canvas c, BufferedImage image) {

        draw(c, image, 0, 0);
    }

    public void draw(Canvas c, BufferedImage image, int xOffset, int yOffset) {

        PixelWriter drawInterface = c.getGraphicsContext2D().getPixelWriter();

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                int RGB = image.getRGB(x, y);
                java.awt.Color newColor = new java.awt.Color(RGB);
                int xPixel = x + xOffset;
                int yPixel = y + yOffset;

                if ((xPixel >= 0 && xPixel < c.getWidth()) && (yPixel >= 0 && yPixel < c.getHeight())) {
//                    System.out.println("xPoint: " + xPixel + ", yPoint: " + yPixel);
//                    System.out.println("Color: " + newColor);
                    if (newColor.getRGB() != ignoreDrawColor.getRGB()) {

                        drawInterface.setColor(xPixel, yPixel, convert(newColor));
                    }
                }
            }
        }
    }

    private BufferedImage scaleImage(BufferedImage image, double scale) {

        return scaleImage(image, scale, scale);
    }

    private BufferedImage scaleImage(BufferedImage image, double scaleX, double scaleY) {

        BufferedImage scaledImage = new BufferedImage((int) (image.getWidth() * scaleX), (int) (image.getHeight() * scaleY), BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < scaledImage.getWidth(); x++) {
            for (int y = 0; y < scaledImage.getHeight(); y++) {
                scaledImage.setRGB(x, y, image.getRGB((int)(x / scaleX), (int)(y / scaleY)));
            }
        }

        return scaledImage;
    }

    public Color convert(java.awt.Color newColor) {

        return (Color.color((newColor.getRed() / 255.0), (newColor.getGreen() / 255.0), (newColor.getBlue() / 255.0), (newColor.getAlpha() / 255.0)));
    }

    public java.awt.Color convert(Color newColor) {

        return (new java.awt.Color((int) (newColor.getRed() * 255), (int) (newColor.getBlue() * 255), (int) (newColor.getGreen() * 255), (int) (newColor.getOpacity() * 255)));
    }

    public void add(double[] array, double offset) {

        for (int i = 0; i < array.length; i++) {

            array[i] += offset;
        }
    }

    public void scale(double[] array, double scale) {

        for (int i = 0; i < array.length; i++) {

            array[i] *= scale;
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
        funcScaleXInput.setText(String.valueOf(f.getScaleVar().get1()));
        funcScaleYInput.setText(String.valueOf(f.getScaleVar().get2()));
        funcVarInput.setText(f.getVariable());
        transXInput.setText(String.valueOf(f.getTranslationVar().getComponents()[0]));
        transYInput.setText(String.valueOf(f.getTranslationVar().getComponents()[1]));
        rotationInput.setText(String.valueOf(Math.toDegrees(f.getRotationVar())));
    }

    private void clearInputs() {

        funcStrInput.clear();
        pathRangeInput.clear();
        fRangeInit.clear();
        fRangeEnd.clear();
        funcScaleXInput.clear();
        funcScaleYInput.clear();
        funcVarInput.clear();
        transXInput.clear();
        transYInput.clear();
        rotationInput.clear();
    }
}
