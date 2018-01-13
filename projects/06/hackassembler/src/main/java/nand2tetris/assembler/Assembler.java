package nand2tetris.assembler;

import java.io.*;

public class Assembler {

    public static void main(String args[]) throws IOException {
        if (args.length != 1) {
            for (String str : args) {
                System.out.println(str);
            }
            printUsage();
            System.exit(1);
        }

        SymbolTable symbolTable = new SymbolTable();

        //First, parse just for labels
        try (InputStream inputStream = new FileInputStream(new File(args[0]))) {
            System.out.println("First pass: label initialization...");
            int pcAddress = 0;
            Parser parser = new Parser(inputStream);

            while (parser.hasMoreCommands()) {
                parser.advance();
                if (parser.commandType() == CommandType.WHITESPACE_COMMAND) {
                    continue;
                }
                if (parser.commandType() == CommandType.L_COMMAND) {
                    if (!symbolTable.contains(parser.symbol())) {
                        symbolTable.addEntry(parser.symbol(), pcAddress);
                    } else {
                        System.err.println("Duplicate Label Declaration: Label (" + parser.symbol() + ") redeclared");
                    }
                } else {
                    pcAddress++;
                }
            }
        }

        //Then, parse for variables
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(new File(args[0])))) {
            System.out.println("Second pass: variable initialization...");
            int variableAddress = 16;
            Parser parser = new Parser(inputStream);

            while (parser.hasMoreCommands()) {
                parser.advance();
                if (parser.commandType() == CommandType.WHITESPACE_COMMAND) {
                    continue;
                }
                if (parser.commandType() == CommandType.A_COMMAND) {
                    if (!parser.symbol().matches("\\d+")) {
                        if (!symbolTable.contains(parser.symbol())) {
                            symbolTable.addEntry(parser.symbol(), variableAddress++);
                        }
                    }
                }
            }
        }

        //Finally, perform full conversion with finished symbol table
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(new File(args[0])))) {
            System.out.println("Third pass: final file conversion and output...");
            Parser parser = new Parser(inputStream);
            StringBuilder outputBuilder = new StringBuilder();

            while(parser.hasMoreCommands()) {
                parser.advance();
                if (parser.commandType() == CommandType.WHITESPACE_COMMAND) {
                    continue;
                }
                if (parser.commandType() == CommandType.A_COMMAND) {
                    if (parser.symbol().matches("\\d+")) {
                        Encoder.addrToBin(Integer.parseInt(parser.symbol()), outputBuilder);
                    } else {
                        Encoder.addrToBin(symbolTable.getAddress(parser.symbol()), outputBuilder);
                    }
                    outputBuilder.append(System.lineSeparator());
                } else if (parser.commandType() == CommandType.C_COMMAND) {
                    outputBuilder.append("111");
                    Encoder.comp(parser.comp(), outputBuilder);
                    Encoder.dest(parser.dest(), outputBuilder);
                    Encoder.jump(parser.jump(), outputBuilder);
                    outputBuilder.append(System.lineSeparator());
                }
            }

            try (FileOutputStream fileOutputStream = new FileOutputStream(new File(args[0].substring(0, args[0].indexOf('.')) + ".hack"))) {
                fileOutputStream.write(outputBuilder.toString().getBytes());
            }
        }
    }

    private static void printUsage() {
        System.out.println("Usage: java Assembler filename.asm\nOutput is saved in filename.hack");
    }

}
