package sample;

public class LCDDisplay {
    public static String displayString = "";

    public static void setValue(boolean call, int value, Instruction instruction){
        if(call){
            displayString = String.valueOf(value);
        } else {
            String opcode = Integer.toBinaryString(instruction.opcode);
            String jump = instruction.is_jump ? "1" : "0";
            String imm = instruction.is_imm ? "1" : "0";
            displayString = "op:"+ fillWithZero(opcode, 3) + " j:" + jump + " im:"+ imm;
        }

    }

    private static String fillWithZero(String s, int expectedLen){
        StringBuilder sBuilder = new StringBuilder(s);
        for (int i = expectedLen - sBuilder.length(); i > 0; i--){
            sBuilder.insert(0, "0");
        }

        return sBuilder.toString();
    }
}
