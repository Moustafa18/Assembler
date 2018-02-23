import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

public class FirstPass {
    public static String statmentTable[] = new String[4];
    public static String statment;
    public static HashMap<String, String> symbolTable = new HashMap<String, String>();
    public static int LineCounter = 0;
    public static String startAddress;
    static String locationCounter = new String();
    static HashMap<String, String> opTable = Initialize.h;
    static String programLength = new String();
    static File fout;
    static FileOutputStream fos;
    static BufferedWriter bw;

    public static boolean firstPass(String statment1)
            throws Exception {
        statment = statment1;
        Initialize.init();
        setStatmentTable(statment);
        if (LineCounter == 0) {
            fout = new File("IntermediateFile.txt");

            fos = new FileOutputStream(fout);
            bw = new BufferedWriter(new OutputStreamWriter(fos));

        }

        String label = statmentTable[0];
        String opCode = statmentTable[1];
        String operand = statmentTable[2];
        label = label.trim();
        opCode = opCode.trim();
        operand = operand.trim();
        if (LineCounter == 0 && opCode.equals("START")) {

            startAddress = operand;
            locationCounter = startAddress;
            LineCounter++;

            bw.write(startAddress + "," + statment);
            bw.newLine();

            return false;
        }
        if (LineCounter != 0 && !opCode.equals("END")) {
            if (statment.charAt(0) != '.') {
                if (label.length() == 0
                        || searchSymbolTable(label) == null) {
                    symbolTable.put(label, locationCounter);

                    bw.write(locationCounter + "," + statment);
                    bw.newLine();

                } else {
                    bw.write(locationCounter + statment
                            + "(duplicated label)");
                    bw.newLine();
                    throwException();
                }
                String code = getsearchOpTable(opCode);
                if (code != null) {

                    locationCounter = increaseLocationCounter(
                            locationCounter, 3);

                } else if (opCode.equals("WORD")) {
                    locationCounter = increaseLocationCounter(
                            locationCounter, 3);
                } else if (opCode.equals("RESW")) {
                    locationCounter = increaseLocationCounter(
                            locationCounter,
                            3 * Integer.valueOf(operand));
                } else if (opCode.equals("RESB")) {
                    locationCounter = increaseLocationCounter(
                            locationCounter,
                            Integer.valueOf(operand));
                } else if (opCode.equals("BYTE")) {
                    countLengthOfBytes(operand);
                    locationCounter = increaseLocationCounter(
                            locationCounter,
                            3 * Integer.valueOf(operand));
                } else {

                    bw.write(locationCounter + statment.trim()
                            + " (Syntex Error @ opCode @ Line "
                            + LineCounter + ")");
                    bw.newLine();
                    throwException();

                }

            }

        } else {

            bw.write(locationCounter + "," + statment);
            bw.close();

            programLength = Integer.toHexString(
                    Integer.parseInt(locationCounter, 16)
                            - (Integer.decode(startAddress)));
            return true;
        }
        LineCounter++;
        return false;

    }

    private static int countLengthOfBytes(String operand)
            throws Exception {

        String temp = operand.substring(0, 1);
        if (temp.equals("X'")
                && operand.charAt(operand.length() - 1) == '\''
                && operand.length() <= 17
                && (operand.length() - 3) % 2 == 0) {
            return (operand.length() - 3) / 2; // we donot make sure of
                                               // hexadecimal is in betweeen
        } else if (temp.equals("C'")
                && operand.charAt(operand.length() - 1) == '\''
                && operand.length() <= 18) {
            return operand.length() - 3;
        } else {
            bw.write(locationCounter + statment.trim()
                    + " (Unexpected Operand @ line" + LineCounter
                    + ")");
            bw.newLine();
            throwException();
        }
        return 0;// doesn't make sense
    }

    public static String getsearchOpTable(String opCode) {

        return opTable.get(opCode.toLowerCase());
    }

    private static String increaseLocationCounter(
            String locationCounter, int incr) {
        String u = Integer
                .toHexString(Integer.parseInt(locationCounter,
                        16)
                        + Integer.valueOf(String.valueOf(incr),
                                16));
        return u;
    }

    public static String increaseAddress(String a, int incr) {
        String u = Integer.toHexString((Integer.decode(a))
                + Integer.valueOf(String.valueOf(incr), 16));
        return u;
    }

    public static String searchSymbolTable(String label) {
        return symbolTable.get(label);
    }

    public static void setStatmentTable(String statment)
            throws IOException {

        for (int i = 0; i < 100; i++) {
            statment = statment + " ";
        }
        if (statment.charAt(8) == ' '
                && statment.charAt(15) == ' '
                && statment.charAt(16) == ' '
                && statment.charAt(9) != ' '
                && statment.charAt(17) != ' ') {

            statmentTable[0] = statment.substring(0, 7);
            statmentTable[1] = statment.substring(9, 14);
            statmentTable[2] = statment.substring(17, 34);
            statmentTable[3] = statment.substring(35, 65);

        } else {
            bw.write(locationCounter + statment.trim()
                    + " (Syntex Error due to spacing @  "
                    + LineCounter + ")");
            bw.newLine();
            throwException();

        }

    }

    public static void throwException() {
        throw new RuntimeException();
    }
}
