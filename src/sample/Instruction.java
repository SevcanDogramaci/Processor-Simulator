package sample;

public abstract class Instruction extends Data{

    protected short opcode;
    protected int index, immediate;
    protected Register sourceReg, targetReg, destinationReg;
    protected String line;
    protected boolean is_imm, is_jump;
    protected String functionName = "";

    public static Instruction createInstruction(String line, int i, Parser parser) throws Exception {

        String funcName = line.split(" ")[0].replace("\n", "");

        // create instruction according to function name
        if (RFormatInstruction.checkFormat(funcName))
            return new RFormatInstruction(line, i);
        else if (IFormatInstruction.checkFormat(funcName))
            return new IFormatInstruction(line, i, parser);
        else if (JFormatInstruction.checkFormat(funcName))
            return new JFormatInstruction(line, i, parser);

        return null;
    }

    // extract opcode or functioncode, registers and fields
    abstract void parseInstruction (String line) throws Exception;

    // flags
    public boolean isRegSource() {
        return sourceReg != null;
    }

    public boolean isRegTarget() {
        return targetReg != null;
    }

    public boolean isRegDest () {
        return destinationReg != null;
    }

    public boolean isRFormat(){ return this instanceof RFormatInstruction; }

    public boolean isIFormat(){
        return this instanceof IFormatInstruction;
    }

    public boolean isJFormat(){
        return this instanceof JFormatInstruction;
    }

    // setters
    //public void setAddress(short address) { this.address = address; }

    // getters
    public Register getDestinationReg() { return destinationReg; }

    public Register getSourceReg() { return sourceReg; }

    public Register getTargetReg() { return targetReg; }

    public int getImmediate() { return immediate; }

    public short getFunction() { return opcode; }

    public String getLine() {
        return line;
    }

    public abstract String getMachineCode() throws Exception;

    // helper function for machine code generation
    protected String fillWithZero(String s, int expectedLen){
        StringBuilder sBuilder = new StringBuilder(s);
        for (int i = expectedLen - sBuilder.length(); i > 0; i--){
            sBuilder.insert(0, "0");
        }

        return sBuilder.toString();
    }


    public boolean isImm() {
        return is_imm;
    }

    public boolean isJump() {
        return is_jump;
    }
}
