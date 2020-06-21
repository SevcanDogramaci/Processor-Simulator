package sample;

public class ALU {

    public static final short ADD = 0;
    public static final short SUB = 1;
    public static final short AND = 2;
    public static final short OR = 3;
    public static final short MUL = 4;
    public static final short SLL = 5;
    public static final short SRL = 6;
    public static final short SLT = 7;

    private int out;
    private boolean zero;

    public void setOperation(short control, int srcv, int rsv) {
        System.out.println("srcv : " + srcv);
        System.out.println("rsv : " + rsv);
        zero = false;
        out = 0;

        // perform operation with function, source values and shift amount
        switch(control) {
            case ADD:
                out =  rsv + srcv;
                break;

            case SUB:
                out = rsv - srcv;
                if(out == 0) {
                    zero = true;
                }
                break;

            case AND:
                out = rsv & srcv;
                break;

            case OR:
                out = rsv | srcv;
                break;

            case SLT:
                out = rsv < srcv ? 1 : 0;
                break;

            case SLL:
                out = rsv << srcv;
                break;

            case SRL:
                out = rsv >>> srcv;
                break;

            case MUL:
                out = srcv * rsv; // to be changed according to multiplication implementation
        }
    }

    // get result of operation
    public int getOut() {
        return out;
    }

    // get zero value
    public boolean isZero() {
        return zero;
    }
}
