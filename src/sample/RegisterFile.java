package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class RegisterFile {

    private Register rr1, rr2, wr;
    private static ArrayList<Register> registers = new ArrayList<>();

    // pc is excluded
    private static String[] registerNames = {
            "r0",
            "r1",
            "r2",
            "r3",
            "a0",
            "v0",
            "sp",
            "ra"
        };

    static {
        int registerNo = 0;
        for (String registerName : registerNames) {
            registers.add(new Register(registerNo ++, registerName));
        }
    }

    public static void setRegisterData(int index, int dataValue){
        registers.get(index).setRegValue(dataValue);
    }

    public static ObservableList<Register> getRegisters(){
        return FXCollections.observableArrayList(registers);
    }

    public static void resetData(){
        for(Register register : registers) {
            register.setRegValue(0);
            if (register.getName().equals("sp")) register.setRegValue(MemoryFile.STACK_START);
        }
    }

    // Set current instruction's registers.
    public void setRegisters(Register rr1, Register rr2, Register wr) {
        this.rr1 = rr1;
        this.rr2 = rr2;
        this.wr = wr;
    }

    public int readData1() { return  rr1 == null ? 0 : rr1.getRegValue(); }

    public int readData2() { return  rr2 == null ? 0 : rr2.getRegValue(); }

    // Regwrite
    public void write(boolean regWrite, int dataValue) {
        if(regWrite) {
            if(wr != null)
                setRegisterData(wr.getNo(), dataValue);
        }
    }

    // get register with respect to name or register number.
    public static Register getRegister(String name) {
        for (Register r: registers) {
            try {
                if (r.getNo() == Integer.parseInt(name))
                    return r;
            } catch (Exception e) {
                if (r.getName().equalsIgnoreCase(name))
                    return r;
            }
        }
        return null;
    }
}
