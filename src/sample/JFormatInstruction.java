package sample;

import java.util.HashMap;
import java.util.Map;

public class JFormatInstruction extends Instruction {

    private static final Map<String, String> instructionMap;
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
        String offset;
        functionName = instruction[0];
        offset = instruction[1];

        String code = instructionMap.get(functionName);

        opcode = Short.parseShort(code.substring(0, 3), 2);
        is_imm = (code.charAt(3) == '1');
        is_jump = (code.charAt(4) == '1');

        // specify instruction fields
        this.immediate = calculateLabel(offset);
        this.targetReg = RegisterFile.getRegister("ra");
    }

    @Override
    public String getMachineCode() throws Exception {
        StringBuilder sb = new StringBuilder();

        try{
        sb.append(instructionMap.get(functionName))
                .append(" ");
        String imm = fillWithZero(Integer.toBinaryString(immediate), 11);
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
        instructionMap.put("j", "00010");  // +
        instructionMap.put("jal", "00110");// +
    }
}
