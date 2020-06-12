package sample;

public class ProgramCounter {
    private int value;
    public ProgramCounter() {
        value = 0; // start from 0
    }

    public void set(int value) throws Exception {
        if (value%2 != 0) throw new Exception("Illegal address !");
        this.value = value;
    }

    public int get() {
        return value;
    }

    public void reset() {
        value = 0;
    }
}