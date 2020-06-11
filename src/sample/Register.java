package sample;


import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Register implements ObservableValue {

    private String name;
    private int no;
    private int regValue;
    ChangeListener changeListener;

    public Register(int no, String name) {
        this.no = no;
        this.name = name;

        if (name.equals("sp")) // start sp from stack start.
            this.regValue = MemoryFile.STACK_START;
        else
            this.regValue = 0;
    }

    public void setRegValue(int regValue) {
        changeListener.changed(this, getRegValue(), regValue);
        if (!name.equalsIgnoreCase("zero"))
            this.regValue = regValue;
    }

    public int getNo() { return no; }

    public String getName() { return name; }

    @Override
    public void addListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    @Override
    public void removeListener(ChangeListener changeListener) {

    }

    @Override
    public Object getValue() {
        return null;
    }


    public int getRegValue() { return regValue; }

    public static String extractRegisterName(String name) {
        if (name.contains("$"))
            name = name.trim().replace("$", "");
        return name;
    }

    @Override
    public void addListener(InvalidationListener ınvalidationListener) {

    }

    @Override
    public void removeListener(InvalidationListener ınvalidationListener) {

    }
}