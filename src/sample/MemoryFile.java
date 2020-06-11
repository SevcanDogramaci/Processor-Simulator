package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class MemoryFile {

    public static final int STACK_START = 4000;
    private Register stackPointer;
    private static byte data[][]; // two dimensional byte array used to reflect two aligned memory.

    public MemoryFile(){
        stackPointer = RegisterFile.getRegister("sp");
        data = new byte[STACK_START >> 2][4];
    }

    public static void resetData(){
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < 4; j++) {
                data[i][j] = 0;
            }
        }
    }

    // Do read or write operation if required according to flags.
    public int cycle(boolean read, boolean write, int index, int writeValue, int accessLength, boolean signed){

        if (read){
            return get(index, accessLength, signed);
        }
        else if (write){
            set(index, writeValue, accessLength);
        }

        return 0;
    }

    // writes value to memory, (aligned)
    private void set(int index, int value, int type){
        byte[] row = data[index >> 2];
        byte offset = (byte) (index % 4);

        int j = 0;
        for (int i = offset; i < offset + type; i++, j++) {
            row[i] = (byte) (value >> (type-1-j) * 8);
        }
    }

    // reads value from memory, (aligned)
    private int get(int index, int type, boolean signed){
        byte[] row = data[index >> 2];
        byte offset = (byte) (index % 4);
        int ret = 0;

        int j = 0;
        for (int i = offset; i < offset + type; i++, j++) {
            if(i == offset && signed)
                ret += (row[i]<< (type-1-i) * 8);
            else
                ret += (unsignedToBytes(row[i]) << (type-1-j) * 8);
        }

        return ret;
    }

    public ObservableList<Data> getMemoryData (){

        List<Data> memoryData = new ArrayList<>();

        for (int i = data.length - 1; i >= stackPointer.getRegValue() >> 2; i--) {

            String address = String.format("%6d", i << 2);
            StringBuilder val = new StringBuilder();
            byte[] row = data[i];
            for (int j = 0; j < row.length; j++) {
                val.append(String.format("%8s", Integer.toBinaryString(row[j] & 0xFF))
                        .replace(' ', '0')).append(" ");
            }

            Data datum = new Data(i << 2, val.toString());
            memoryData.add(datum);
        }

        return FXCollections.observableArrayList(memoryData);
    }

    public static int unsignedToBytes(byte b) {
        return b & 0xFF;
    }


}