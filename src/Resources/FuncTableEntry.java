package Resources;

import java.util.Arrays;

public class FuncTableEntry {

    private String funcString;
    private String variable;
    private double[] fRange;
    private double rotation;
    private double tRange;
    private double scaleX;
    private double scaleY;

    public FuncTableEntry(String newFuncString, String newVar, double newScaleX, double newScaleY, double newRotation, double newTRange, double[] newFRange) {

        funcString = newFuncString;
        variable = newVar;
        rotation = newRotation;
        tRange = newTRange;
        fRange = newFRange;
        scaleX = newScaleX;
        scaleY = newScaleY;
    }

    public String getFunction() {

        return funcString;
    }

    public String getVariable() {

        return variable;
    }

    public String getRotation() {

        return (String.valueOf(rotation));
    }

    public double getRotationVar() {

        return rotation;
    }

    public String getTRange() {

        return String.valueOf(tRange);
    }

    public double getTRangeVar() {

        return tRange;
    }

    public String getFuncRange() {

        return Arrays.toString(fRange);
    }

    public double[] getFuncRangeVar() {

        return fRange;
    }

    public String getScale() {

        return "[" + scaleX + ":" + scaleY + "]";
    }

    public Pair<Double, Double> getScaleVar() {

        return new Pair<Double, Double>(scaleX, scaleY);
    }

    public boolean equals(FuncTableEntry second) {

        return (getFunction().equals(second.getFunction())
                && getScale().equals(second.getScale())
                && getRotation().equals(second.getRotation())
                && getTRange().equals(second.getTRange())
                && getFuncRange().equals(second.getFuncRange()));
    }
}
