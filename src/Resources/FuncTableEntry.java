package Resources;

import java.util.Arrays;

public class FuncTableEntry {

    private String funcString;
    private Vector2D translation;
    private double[] fRange;
    private double rotation;
    private double tRange;
    private String variable;

    public FuncTableEntry(String newFuncString, String newVar, Vector2D newTranslation, double newRotation, double newTRange, double[] newFRange) {

        funcString = newFuncString;
        translation = newTranslation;
        rotation = newRotation;
        tRange = newTRange;
        fRange = newFRange;
        variable = newVar;
    }

    public String getFunction() {

        return funcString;
    }

    public String getTranslation() {

        return (translation.toString());
    }

    public Vector2D getTranslationVar() {

        return translation;
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

    public String getVariable() {

        return variable;
    }

    public boolean equals(FuncTableEntry second) {

        return (getFunction().equals(second.getFunction())
                && getVariable().equals(second.getVariable())
                && getTranslation().equals(second.getTranslation())
                && getRotation().equals(second.getRotation())
                && getTRange().equals(second.getTRange())
                && getFuncRange().equals(second.getFuncRange()));
    }
}
