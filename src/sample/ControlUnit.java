package sample;

public class ControlUnit {

    // control lines
    private boolean RegDst;     // rt or rd for write
    private boolean Branch;     // branch
    private boolean Jump;       // jump
    private boolean JumpReg;    // jump to address in register
    private boolean MemRead;    // memory read
    private boolean MemtoReg;   // memory to register
    private boolean MemWrite;   // memory write
    private boolean ALUsrc;     // reg2 or immediate
    private boolean RegWrite;   // register write
    private boolean ALUOp0;     // ALU operation LSB
    private boolean ALUOp1;     // ALU operation Middle
    private boolean ALUOp2;     // ALU operation MSB
    private boolean ShiftReg;

    //private boolean BranchNotEqual;
    //private int branchCode = 0;

    public ControlUnit(Instruction instruction) {

        // specify control lines for R format instructions
        if (instruction.isRFormat()){

            if (instruction.isJump()) { // jr
                JumpReg = true;
                ShiftReg = true;
            }
            else {
                switch (instruction.opcode) {
                    case 1: // sub
                        ALUOp0 = true;
                        break;
                    case 2: // or
                        ALUOp1 = true;
                        ALUOp0 = true;
                        break;
                    case 3: // and
                        ALUOp1 = true;
                        break;
                    case 4: // mul
                        ALUOp2 = true;
                        break;
                    case 5: // sll
                        ALUOp2 = true;
                        ALUOp0 = true;
                        break;
                    case 6: // srl
                        ALUOp2 = true;
                        ALUOp1 = true;
                        break;
                    case 7: // slt
                        ALUOp2 = true;
                        ALUOp1 = true;
                        ALUOp0 = true;
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
                    ALUOp0 = true;
                    break;
                case 2: // lw
                    MemRead = true;
                    break;
                case 3: // bne
                    Branch = true;
                    RegWrite = false;
                    ALUsrc = false;
                    RegDst = true;
                    ALUOp0 = true;
                    ALUOp1 = true;
                    ALUOp0 = true;
                    break;
                case 4: // muli
                    ALUOp2 = true;
                    break;
                case 5: // lui
                    ShiftReg = true;
                    ALUOp2 = true;
                    ALUOp0 = true;
                    break;
                case 7: // slti
                    ALUOp2 = true;
                    ALUOp1 = true;
                    ALUOp0 = true;
                    break;
            }

        }

        // specify control lines for J format instructions
        else if (instruction.isJFormat()){
            Jump = true;

            if (instruction.opcode == 0){ // jal
                RegWrite = true;
            }
        }
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

    public boolean isMemtoReg() {
        return MemtoReg;
    }

    public boolean isALUOp2() {
        return ALUOp2;
    }

    public boolean isALUOp1() {
        return ALUOp1;
    }

    public boolean isALUOp0() {
        return ALUOp0;
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

    /*
    public boolean isBranchNotEqual() {
        return BranchNotEqual;
    }

    public boolean isSignExtend() {
        return SignExtend;
    }

    public int getBranchCode() {
        return branchCode;
    }
    */

}