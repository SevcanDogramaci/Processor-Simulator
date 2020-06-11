package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class InstructionMemoryFile {
    private static Instruction[] instructions;

    public void load(List<Instruction> instructions) {
        this.instructions = new Instruction[instructions.size()];

        for(int i = 0; i <instructions.size(); i++){
            // set memory address for instructions
            instructions.get(i).setAddress(i);
        }

        // load instructions into instruction memory
        this.instructions = instructions.toArray(this.instructions);
    }

    // fetch instruction from instruction memory with pc
    public Instruction fetch(ProgramCounter pc) {
        return instructions[pc.get()/4];
    }

    public int length() {
        return instructions.length * 4;
    }

    public static ObservableList<Data> getInstructions() throws Exception {
        List<Data> memoryData = new ArrayList<>();
        for (int i = 0; i < instructions.length; i++) {
            memoryData.add(new Data(i << 2, instructions[i].getMachineCode()));
        }
        return FXCollections.observableArrayList(memoryData);
    }
}
