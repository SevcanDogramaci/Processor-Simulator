package sample;

import java.util.HashMap;
import java.util.Map;


public class IFormatInstruction extends Instruction {

    private Parser parser;
    private static final Map<String, String> instructionMap;

    public IFormatInstruction(String line, int i, Parser parser) throws Exception {
        this.parser = parser;
        index = i;
        this.line = line;

        // extract opcode, registers and immediate values
        parseInstruction(line);

        // produce machine code
        value = getMachineCode();
    }

    @Override
    void parseInstruction(String line) throws Exception {

        String[] instruction = line.split(",");
        functionName = instruction[0].split(" ")[0];

        String code = instructionMap.get(functionName);

        opcode = Short.parseShort(code.substring(0, 3), 2);
        is_imm = (code.charAt(4) == '1');
        is_jump = (code.charAt(3) == '1');

        // source or target register
        instruction[0] = instruction[0].split(" ")[1].trim();

        if (functionName.startsWith("b")) {
            sourceReg = RegisterFile.getRegister(Register.extractRegisterName(instruction[0]));
            // beq, bne
            targetReg = RegisterFile.getRegister(Register.extractRegisterName(instruction[1]));

            // assign label address to immediate
            try {
                immediate = Integer.parseInt(instruction[2].trim());
            } catch (Exception e) {
                immediate = calculateLabel(instruction[2]);
            }
        }
        else {
            targetReg = RegisterFile.getRegister(Register.extractRegisterName(instruction[0]));

            // load and store instructions
            if (instruction.length == 2) {

                String ins = instruction[1].trim();

                if (!ins.contains("(")){ // lui
                    immediate = Integer.parseInt(ins);
                    sourceReg = targetReg; // !!!!
                    return;
                }

                // extract base register and offset value
                immediate = Integer.parseInt(ins.substring(0, ins.indexOf("(")));
                sourceReg = RegisterFile.getRegister(
                        Register.extractRegisterName(ins.substring(ins.indexOf("(") + 1, ins.indexOf(")"))));
            }
            else {
                // extract source register and immediate value
                sourceReg = RegisterFile.getRegister(Register.extractRegisterName(instruction[1]));
                immediate = Integer.parseInt(instruction[2].trim());
            }
        }

    }

    private int calculateLabel(String label) throws Exception {
        // get label address from label address map
        return parser.getLabelAddress(label.trim()) - index - 1;
    }

    public int getImmediate() { return immediate; }

    public static boolean checkFormat(String functionName) {
        // check if instruction map contains the function
        return instructionMap.containsKey(functionName);
    }

    @Override
    public String getMachineCode() throws Exception {
        StringBuilder sb = new StringBuilder();

        try {
            sb.append(instructionMap.get(functionName)).append(" ")
                    .append(fillWithZero(Integer.toBinaryString(sourceReg.getNo()), 5)).append(" ")
                    .append(fillWithZero(Integer.toBinaryString(targetReg.getNo()), 5)).append(" ");

            String imm = fillWithZero(Integer.toBinaryString(immediate), 5);
            if (imm.length() > 5)
                imm = imm.substring(imm.length() - 5);
            sb.append(imm);
        }catch (Exception e){
            throw new Exception("Error occurred while generating machine code!\nCheck instruction format : " + line);
        }

        return sb.toString();
    }

    static {
        instructionMap = new HashMap<>();

        // put instructions
        instructionMap.put("lui", "10101"); // +

        instructionMap.put("slti", "11101");// +

        instructionMap.put("muli", "10001");// +
        instructionMap.put("beq", "00101"); // +

        instructionMap.put("bne", "01101"); // +

        instructionMap.put("lw", "01001");  // +
        instructionMap.put("sw", "00001");  // +
    }
}
