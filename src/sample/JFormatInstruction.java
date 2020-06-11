package sample;

import java.util.HashMap;
import java.util.Map;

public class JFormatInstruction extends Instruction {

    private static final Map<String, Short> instructionMap;
    private Parser parser;

    public JFormatInstruction(String line, int i, Parser parser) throws Exception {
        this.parser = parser;
        index = i;
        this.line = line;

        parseInstruction(line);
        value = getMachineCode();
    }

    public static boolean checkFormat(String functionName) {
        // check if instruction map contains the function
        return instructionMap.containsKey(functionName);
    }

    @Override
    void parseInstruction(String line) throws Exception {
        String[] instruction = line.split(" ");

        if (instruction.length > 2)
            throw new Exception("Check instruction format : " + line);

        // extract function name and offset
        String functionName, offset;
        functionName = instruction[0];
        offset = instruction[1];

        // specify instruction fields
        this.opcode = instructionMap.get(functionName);
        this.immediate = calculateLabel(offset);
        this.targetReg = RegisterFile.getRegister("ra");
    }

    @Override
    public String getMachineCode() throws Exception {
        StringBuilder sb = new StringBuilder();

        try{
        sb.append(fillWithZero(Integer.toBinaryString(opcode), 6))
                .append(" ");
        String imm = fillWithZero(Integer.toBinaryString(immediate), 26);
        if (imm.length() > 26)
                imm = imm.substring(imm.length() - 26);
            sb.append(imm);
        }

        catch (Exception exception){
            throw new Exception("Error occurred while generating machine code!\nCheck instruction format : " + line);
        }

        return sb.toString();
    }

    private int calculateLabel(String s) throws Exception {
        // get label address from label address map
        return parser.getLabelAddress(s.trim()) - index - 1;
    }

    static {
        instructionMap = new HashMap<>();

        // put instructions
        instructionMap.put("j", (short)2);  // +
        instructionMap.put("jal", (short)3);// +
    }
}
