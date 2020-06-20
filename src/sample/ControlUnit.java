package sample;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ControlUnit {

    // control lines
    private boolean RegDst;     // rt or rd for write
    private boolean Branch;     // branch
    private boolean Jump;       // jump
    private boolean JumpReg;    // jump to address in register
    private boolean MemRead;    // memory read
    private boolean MemWrite;   // memory write
    private boolean ALUsrc;     // reg2 or immediate
    private boolean RegWrite;   // register write
    private boolean ShiftReg;
    private boolean Call;
    private short ALUOp = 0;

    public ControlUnit(Instruction instruction) {

        // specify control lines for R format instructions
        if (instruction.isRFormat()){

            if (instruction.isJump()) { // jr
                if (instruction.opcode == 5){
                    Call = true;
                    return;
                }
                JumpReg = true;
                ShiftReg = true;
            }
            else {
                switch (instruction.opcode) {
                    case 1: // sub
                        ALUOp = 1;
                        break;
                    case 2: // and
                        ALUOp = 2;
                        break;
                    case 3: // or
                        ALUOp = 3;
                        break;
                    case 4: // mul
                        ALUOp = 4;
                        break;
                    case 5: // sll
                        ALUOp = 5;
                        break;
                    case 6: // srl
                        ALUOp = 6;
                        break;
                    case 7: // slt
                        ALUOp = 7;
                        break;
                }
            }
            RegDst = true;
            RegWrite = true;
        }

        // specify control lines for I format instructions
        else if (instruction.isIFormat()){
            RegWrite = true;
            ALUsrc = true;
            switch (instruction.opcode) {
                case 0: // sw
                    MemWrite = true;
                    RegWrite = false;
                    break;
                case 1: // beq
                    Branch = true;
                    RegWrite = false;
                    ALUsrc = false;
                    ALUOp = 1;
                    break;
                case 2: // lw
                    MemRead = true;
                    break;
                case 3: // bne
                    Branch = true;
                    RegWrite = false;
                    ALUsrc = false;
                    RegDst = true;
                    ALUOp = 1;
                    break;
                case 4: // muli
                    ALUOp = 4;
                    break;
                case 5: // lui
                    ShiftReg = true;
                    ALUOp = 5;
                    break;
                case 7: // slti
                    ALUOp = 7;
                    break;
            }

        }

        // specify control lines for J format instructions
        else if (instruction.isJFormat()){
            Jump = true;

            if (instruction.opcode == 1){ // jal
                RegWrite = true;
                System.out.println(true);
            }
        }
    }

    public short getALUOp() {
        return ALUOp;
    }

    public boolean isRegDst() {
        return RegDst;
    }

    public boolean isBranch() {
        return Branch;
    }

    public boolean isJump() {
        return Jump;
    }

    public boolean isJumpReg() {
        return JumpReg;
    }

    public boolean isMemRead() {
        return MemRead;
    }

    public boolean isMemWrite() {
        return MemWrite;
    }

    public boolean isALUsrc() {
        return ALUsrc;
    }

    public boolean isRegWrite() {
        return RegWrite;
    }

    public boolean isShiftReg() {
        return ShiftReg;
    }

    public boolean isCall() {
        return Call;
    }

    public Map<String, Boolean> getControlSignal(){
        Map<String, Boolean> controlSignals = new TreeMap<>();

        controlSignals.put("RegDst", RegDst);
        controlSignals.put("Branch", Branch);
        controlSignals.put("Jump", Jump);
        controlSignals.put("JumpReg", JumpReg);
        controlSignals.put("MemRead", MemRead);
        controlSignals.put("MemWrite", MemWrite);
        controlSignals.put("ALUsrc", ALUsrc);
        controlSignals.put("RegWrite", RegWrite);
        controlSignals.put("ShiftReg", ShiftReg);
        controlSignals.put("Call", Call);

        return controlSignals;
    }

    public static Map<String, Boolean> getControlSignalFalse(){
        Map<String, Boolean> controlSignals = new TreeMap<>();

        controlSignals.put("RegDst", false);
        controlSignals.put("Branch", false);
        controlSignals.put("Jump", false);
        controlSignals.put("JumpReg", false);
        controlSignals.put("MemRead", false);
        controlSignals.put("MemWrite", false);
        controlSignals.put("ALUsrc", false);
        controlSignals.put("RegWrite", false);
        controlSignals.put("ShiftReg", false);
        controlSignals.put("Call", false);

        return controlSignals;
    }
}