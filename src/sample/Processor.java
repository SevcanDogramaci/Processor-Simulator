package sample;

import javafx.collections.ObservableList;

import java.util.List;

public class Processor {

    private ProgramCounter pc;
    private RegisterFile registerFile;
    private InstructionMemoryFile instructionMemoryFile;
    private MemoryFile memory;
    private ALU alu;
    private int changedMemIdx;

    public Processor() { // Initialize components that are connected to processor.
        pc = new ProgramCounter();
        registerFile = new RegisterFile();
        instructionMemoryFile = new InstructionMemoryFile();
        memory = new MemoryFile();
        alu = new ALU();
    }

    // Load extracted instructions to instruction memory.
    public void loadInstructionsToMemory(List<Instruction> instructions) {
        this.instructionMemoryFile.load(instructions);
        reset();
    }

    private void reset() {
        pc.reset();
        RegisterFile.resetData();
        memory.resetData();
    }

    public void step() throws Exception {

        if(isDone()) {
            return;
        }

        Instruction instruction;
        int alu_out = 0, data_out = 0, regData1 = 0, regData2 = 0,
                new_pc = pc.get(), write_data;
        boolean alu_zero;
        changedMemIdx = -1;

        // fetch instruction
        instruction = instructionMemoryFile.fetch(pc);


        // send instruction to control unit
        ControlUnit controlUnit = new ControlUnit(instruction);


        // extract registers' data that will be used
        Register sourceReg = instruction.getSourceReg(),
                targetReg = instruction.getTargetReg(),
                destinationReg = instruction.getDestinationReg();


        Register writeReg1 = (Register) mux(targetReg, destinationReg , controlUnit.isRegDst());
        Register writeReg2 = RegisterFile.getRegister("7"); // ra register
        Register writeReg = (Register) mux(writeReg1, writeReg2, controlUnit.isJump());

        registerFile.setRegisters(sourceReg, targetReg, writeReg);
        regData1 = registerFile.readData1();
        regData2 = registerFile.readData2();

        LCDDisplay.setValue(controlUnit.isCall(), regData1, instruction);
        // ALU performs operation
        alu.setOperation(
                controlUnit.getALUOp(),
                (int)mux(regData2, instruction.getImmediate(), controlUnit.isALUsrc()),
                (int)mux(regData1, 8, controlUnit.isShiftReg()));
        alu_out = alu.getOut();
        alu_zero = alu.isZero();


        data_out = memory.cycle(controlUnit.isMemRead(), controlUnit.isMemWrite(), alu_out, regData2);
        if(controlUnit.isMemRead() || controlUnit.isMemWrite())
            changedMemIdx = alu_out;


        // writeback
        write_data = (int)mux(alu_out, data_out, controlUnit.isMemRead());
        write_data = (int)mux(write_data, new_pc + 2, controlUnit.isJump());
        registerFile.write(controlUnit.isRegWrite(), write_data);


        // update pc 
        updatePc(instruction, new_pc, regData1, alu_zero, controlUnit);

    }
    private void updatePc(Instruction instruction, int new_pc, int jr_pc, boolean alu_zero, ControlUnit controlUnit) throws Exception {
        new_pc += 2;

        int branch_pc = new_pc + (instruction.getImmediate() << 1); // branch address

        boolean is_branch = ((controlUnit.isBranch() && alu_zero) || // beq
                            (controlUnit.isBranch() && controlUnit.isRegDst() && !alu_zero)); // bne

        // update pc if branching or jumping exists
        new_pc = (int)mux(new_pc, branch_pc, is_branch); // branch
        new_pc = (int)mux(new_pc, instruction.getImmediate(), controlUnit.isJump()); // jump
        new_pc = (int)mux(new_pc, jr_pc, controlUnit.isJumpReg()); // jr
        System.out.println("new pc: " + new_pc);
        pc.set(new_pc);
    }

    // 2to1 Multiplexer.
    private Object mux (Object value1, Object value2, boolean getSecond) {
        if(getSecond) {
            return value2;
        }
        return value1;
    }

    public boolean isDone() {
        return pc.get() >= instructionMemoryFile.length();
    }

    public int getIndex() {
        return pc.get()/2;
    }

    public ObservableList<Data> getStackData(){
        return memory.getMemoryData();
    }

    public int getChangedMemIdx(){return changedMemIdx;}

}