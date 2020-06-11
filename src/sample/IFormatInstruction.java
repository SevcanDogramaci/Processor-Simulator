package sample;

import java.util.HashMap;
import java.util.Map;

public class IFormatInstruction extends Instruction {

    // access length for memory operations
    public enum AccessLength {
        BYTE(1),
        HALF_WORD(2),
        WORD(4);

        private final int byteNum;

        AccessLength(int byteNum) {
            this.byteNum = byteNum;
        }

        public int getByteNum() {
            return this.byteNum;
        }
    }

    private Parser parser;
    private static final Map<String, Short> instructionMap;

    public IFormatInstruction(String line, int i, Parser parser) throws Exception {
        this.parser = parser;
        index = i;
        this.line = line;

        // extract opcode, registers and immediate values
        parseInstruction(line);

        // produce machine code
        value = getMachineCode();
    }

    public int getImmediate() { return immediate; }

    @Override
    public String getMachineCode() throws Exception {
        StringBuilder sb = new StringBuilder();

        try {
            sb.append(fillWithZero(Integer.toBinaryString(opcode), 6)).append(" ")
                    .append(fillWithZero(Integer.toBinaryString(sourceReg.getNo()), 5)).append(" ")
                    .append(fillWithZero(Integer.toBinaryString(targetReg.getNo()), 5)).append(" ");

            String imm = fillWithZero(Integer.toBinaryString(immediate), 16);
            if (imm.length() > 16)
                imm = imm.substring(imm.length() - 16);
            sb.append(imm);
        }catch (Exception e){
            throw new Exception("Error occurred while generating machine code!\nCheck instruction format : " + line);
        }

        return sb.toString();
    }

    public static boolean checkFormat(String functionName) {
        // check if instruction map contains the function
        return instructionMap.containsKey(functionName);
    }

    @Override
    void parseInstruction(String line) throws Exception {

        String[] instruction = line.split(",");
        String functionName = instruction[0].split(" ")[0];

        opcode = instructionMap.get(functionName);

        // source or target register
        instruction[0] = instruction[0].split(" ")[1].trim();

        if (functionName.startsWith("b")) {
            sourceReg = RegisterFile.getRegister(extractRegisterName(instruction[0]));

            // bgez, blez, bgtz, bltz
            if (instruction.length == 2) {
                targetReg = RegisterFile.getRegister("at");

                if (functionName.equalsIgnoreCase("bgez")) {
                    // shift amount 1 to distinguish from bltz
                    shiftAmount = 1;
                }

                // assign label address to immediate
                immediate = calculateLabel(instruction[1]);
            }
            // beq, bne
            else {
                targetReg = RegisterFile.getRegister(extractRegisterName(instruction[1]));

                // assign label address to immediate
                try {
                    immediate = Integer.parseInt(instruction[2].trim());
                } catch (Exception e) {
                    immediate = calculateLabel(instruction[2]);
                }
            }

        }
        else {
            targetReg = RegisterFile.getRegister(extractRegisterName(instruction[0]));

            // load and store instructions
            if (instruction.length == 2) {

                String ins = instruction[1].trim();

                if (!ins.contains("(")){ // lui
                    immediate = Integer.parseInt(ins);
                    shiftAmount = 16;
                    sourceReg = targetReg;
                    return;
                }

                // extract base register and offset value
                immediate = Integer.parseInt(ins.substring(0, ins.indexOf("(")));
                sourceReg = RegisterFile.getRegister(
                        extractRegisterName(ins.substring(ins.indexOf("(") + 1, ins.indexOf(")"))));
            }
            else {
                // extract source register and immediate value
                sourceReg = RegisterFile.getRegister(extractRegisterName(instruction[1]));
                immediate = Integer.parseInt(instruction[2].trim());
            }
        }

    }

    private int calculateLabel(String label) throws Exception {
        // get label address from label address map
        return parser.getLabelAddress(label.trim()) - index - 1;
    }

    private String extractRegisterName(String name) {
        // get only register name by removing $
        if (name.contains("$"))
            name = name.trim().replace("$", "");
        return name;
    }

    public short getAccessLength() {
        switch (this.opcode & 3) {
            case 0:
                return (short) AccessLength.BYTE.getByteNum();
            case 1:
                return (short) AccessLength.HALF_WORD.getByteNum();
            case 3:
                return (short) AccessLength.WORD.getByteNum();
        }
        return 0;
    }

    static {
        instructionMap = new HashMap<>();

        // put instructions
        instructionMap.put("addi", (short) 8);   // +
        instructionMap.put("addiu", (short) 9);  // +

        instructionMap.put("andi", (short) 12);  // +
        instructionMap.put("ori", (short) 11);   // +
        instructionMap.put("xori", (short) 14);  // +

        instructionMap.put("beq", (short) 4);    // +
        instructionMap.put("bgez", (short) 1);   // +
        instructionMap.put("bgtz", (short) 7);   // +
        instructionMap.put("blez", (short) 6);   // +
        instructionMap.put("bltz", (short) 1);   // +
        instructionMap.put("bne", (short) 5);    // +

        instructionMap.put("lb", (short) 32);    // +
        instructionMap.put("lbu", (short) 36);   // +
        instructionMap.put("lh", (short) 33);    // +
        instructionMap.put("lhu", (short) 37);   // +
        instructionMap.put("lui", (short) 15);   // +
        instructionMap.put("lw", (short) 35);    // +

        instructionMap.put("sb", (short) 40);    // +
        instructionMap.put("slti", (short) 10);  // +
        instructionMap.put("sh", (short) 41);    // +
        instructionMap.put("sw", (short) 43);    // +

        // excluded instructions
        instructionMap.put("swc1", (short) 57);  // -
        instructionMap.put("lwc1", (short) 49);  // -
    }
}