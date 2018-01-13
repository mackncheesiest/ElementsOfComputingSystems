package nand2tetris.assembler;

import java.io.FileOutputStream;
import java.util.HashMap;

public class Encoder {

    private static final HashMap<String, String> DEST_JMP_MAP = new HashMap<>();
    private static final HashMap<String, String> CMP_MAP = new HashMap<>();

    static {
        DEST_JMP_MAP.put(null, "000");
        DEST_JMP_MAP.put("M",  "001");
        DEST_JMP_MAP.put("D",  "010");
        DEST_JMP_MAP.put("MD", "011");
        DEST_JMP_MAP.put("A",  "100");
        DEST_JMP_MAP.put("AM", "101");
        DEST_JMP_MAP.put("AD", "110");
        DEST_JMP_MAP.put("AMD","111");

        DEST_JMP_MAP.put("JGT","001");
        DEST_JMP_MAP.put("JEQ","010");
        DEST_JMP_MAP.put("JGE","011");
        DEST_JMP_MAP.put("JLT","100");
        DEST_JMP_MAP.put("JNE","101");
        DEST_JMP_MAP.put("JLE","110");
        DEST_JMP_MAP.put("JMP","111");

        CMP_MAP.put("0",    "0101010");
        CMP_MAP.put("1",    "0111111");
        CMP_MAP.put("-1",   "0111010");
        CMP_MAP.put("D",    "0001100");
        CMP_MAP.put("A",    "0110000");
        CMP_MAP.put("!D",   "0001101");
        CMP_MAP.put("!A",   "0110001");
        CMP_MAP.put("-D",   "0001111");
        CMP_MAP.put("-A",   "0110011");
        CMP_MAP.put("D+1",  "0011111");
        CMP_MAP.put("A+1",  "0110111");
        CMP_MAP.put("D-1",  "0001110");
        CMP_MAP.put("A-1",  "0110010");
        CMP_MAP.put("D+A",  "0000010");
        CMP_MAP.put("D-A",  "0010011");
        CMP_MAP.put("A-D",  "0000111");
        CMP_MAP.put("D&A",  "0000000");
        CMP_MAP.put("D|A",  "0010101");

        CMP_MAP.put("M",    "1110000");
        CMP_MAP.put("!M",   "1110001");
        CMP_MAP.put("-M",   "1110011");
        CMP_MAP.put("M+1",  "1110111");
        CMP_MAP.put("M-1",  "1110010");
        CMP_MAP.put("D+M",  "1000010");
        CMP_MAP.put("D-M",  "1010011");
        CMP_MAP.put("M-D",  "1000111");
        CMP_MAP.put("D&M",  "1000000");
        CMP_MAP.put("D|M",  "1010101");

    }

    private Encoder() {}

    public static void addrToBin(Integer address, StringBuilder strBuf) {
        strBuf.append(String.format("%16s", Integer.toBinaryString(address)).replace(' ', '0'));
    }

    public static void dest(String command, StringBuilder strBuf) {
        strBuf.append(DEST_JMP_MAP.getOrDefault(command, "000"));
    }

    public static void comp(String command, StringBuilder strBuf) {
        strBuf.append(CMP_MAP.getOrDefault(command, "0000000"));
    }

    public static void jump(String command, StringBuilder strBuf) {
        strBuf.append(DEST_JMP_MAP.getOrDefault(command, "000"));
    }
}
