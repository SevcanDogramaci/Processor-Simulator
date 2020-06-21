package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class MemoryFile {

    public static final int STACK_START = 255;
    private Register stackPointer;
    private static byte data[][]; // two dimensional byte array used to reflect two aligned memory.

    public MemoryFile(){
        stackPointer = RegisterFile.getRegister("sp");
        data = new byte[(STACK_START+1) >> 1][2];
    }

    public static void resetData(){
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = 0;
            }
        }
    }

    // Do read or write operation if required according to flags.
    public int cycle(boolean read, boolean write, int index, int writeValue) throws Exception {
        System.out.println("inputs: " + read + " " + write + " " + index + " " + writeValue);

        if (read){
            if (index % 2 != 0)
                throw new Exception("Only aligned memory access allowed! " +
                        "\nMemory address " + index + " invalid!");
            return get(index);
        }
        else if (write){
            if (index % 2 != 0)
                throw new Exception("Only aligned memory access allowed! " +
                        "\nMemory address " + index + " invalid!");
            set(index, writeValue);
        }

        return 0;
    }

    // writes value to memory, (aligned)
    private void set(int index, int value){
        System.out.println("Memory File Set: index = " + (index>>1) );
        byte[] row = data[index>>1];

        row[1] = (byte) (value%(STACK_START+1));
        row[0] = (byte) ((value>>8)%(STACK_START+1));

        System.out.println("Memory File Set: value = " + row[0] + " : "  + row[1]);
    }

    // reads value from memory, (aligned)
    private int get(int index){
        byte[] row = data[index>>1];

        int value = 0;

        value += (row[0]<<8);
        value += (unsignedToBytes(row[1]));

        return value;
    }


    public ObservableList<Data> getMemoryData (){

        List<Data> memoryData = new ArrayList<>();

        for (int i = data.length - 1; i >= 0; i--) {

            StringBuilder val = new StringBuilder();
            byte[] row = data[i];
            for (int j = 0; j < row.length; j++) {
                val.append(String.format("%8s", Integer.toBinaryString(row[j] & 0xFF))
                        .replace(' ', '0')).append(" ");
            }

            Data datum = new Data(i << 1, val.toString());
            memoryData.add(datum);
        }

        return FXCollections.observableArrayList(memoryData);
    }

    public static int unsignedToBytes(byte b) {
        return b & 0xFF;
    }


}