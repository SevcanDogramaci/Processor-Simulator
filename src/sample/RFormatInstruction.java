package sample;

import java.util.HashMap;
import java.util.Map;


public class RFormatInstruction extends Instruction {
    private static final Map<String, String> instructionMap;

    public RFormatInstruction(String line, int i) throws Exception {
        index = i;
        this.opcode = 0;
        this.line = line;

        parseInstruction(line);
        value = getMachineCode();
    }

    public static boolean checkFormat(String functionName) {
        return instructionMap.containsKey(functionName);
    }

    @Override
    void parseInstruction(String line) throws Exception {
        // split instruction and extract function.
        String[] instruction = line.split(",");
        functionName = instruction[0].split(" ")[0];

        String code = instructionMap.get(functionName);

        opcode = Short.parseShort(code.substring(0, 3), 2);
        is_imm = (code.charAt(4) == '1');
        is_jump = (code.charAt(3) == '1');

        String[] temp = instruction[0].split(" ");
        System.out.println(1);
        if (temp.length <= 1) // syscall or exit.
            return;

        instruction[0] = temp[1].trim();
        System.out.println(2);
        try {
            if (is_jump) {
                try{
                    sourceReg = RegisterFile.getRegister(Register.extractRegisterName(instruction[0]));
                    System.out.println(3);
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                destinationReg = RegisterFile.getRegister(Register.extractRegisterName(instruction[0]));
                sourceReg = RegisterFile.getRegister(Register.extractRegisterName(instruction[1].trim()));
                targetReg = RegisterFile.getRegister(Register.extractRegisterName(instruction[2].trim()));
            }
        }catch (Exception e){
            throw new Exception("Error occurred while parsing instruction!\nCheck instruction format : " + line);
        }
    }

    @Override
    public String getMachineCode() throws Exception {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(instructionMap.get(functionName));
            if (is_jump){
                sb.append(fillWithZero(Integer.toBinaryString(sourceReg.getNo()), 3));
                sb.append(fillWithZero("", 8));
            } else {
                sb.append(fillWithZero(Integer.toBinaryString(sourceReg.getNo()), 3));
                sb.append(fillWithZero(Integer.toBinaryString(targetReg.getNo()), 3));
                sb.append(fillWithZero(Integer.toBinaryString(destinationReg.getNo()), 3));
                sb.append(fillWithZero("", 2));
            }
        } catch (Exception e){
            throw new Exception("Error occurred while generating machine code!\nCheck instruction format : " + line);
        }

        return sb.toString();
    }

    static {
        instructionMap = new HashMap<>();

        // put instructions
        instructionMap.put("add", "00000");   // +
        instructionMap.put("sub", "00100");   // +

        instructionMap.put("and", "01000");   // +
        instructionMap.put("or", "01100");    // +

        instructionMap.put("jr", "00110");    // +

        instructionMap.put("sll", "10100");   // +
        instructionMap.put("slt", "11100");   // +
        instructionMap.put("srl", "11000");   // +

        instructionMap.put("mul", "10000");  // +
    }
}
