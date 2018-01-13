package nand2tetris.assembler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Parses an input .asm HACK program for conversion to a .hack binary file
 */
public class Parser {

    private Scanner fileScanner;
    private String currLine;
    private CommandType commandType;

    private static final Pattern A_COMMAND_MATCHER = Pattern.compile("@[\\dA-Za-z_.$]+");
    private static final Pattern L_COMMAND_MATCHER = Pattern.compile("\\([\\dA-Za-z_.$]+\\)");
    private static final Pattern WHITESPACE_MATCHER = Pattern.compile("(^[/]{2}.*[\r\n]*$)|(^\\s*$)");

    public Parser(InputStream inputFile) {
        this.fileScanner = new Scanner(inputFile);
        this.currLine = "";
        this.commandType = null;
    }

    public boolean hasMoreCommands() throws IOException {
        return fileScanner.hasNextLine();
    }

    public void advance() throws IOException {
        currLine = fileScanner.nextLine();
        if (A_COMMAND_MATCHER.asPredicate().test(currLine)) {
            commandType = CommandType.A_COMMAND;
        } else if (L_COMMAND_MATCHER.asPredicate().test(currLine)) {
            commandType = CommandType.L_COMMAND;
        } else if (WHITESPACE_MATCHER.asPredicate().test(currLine)) {
            commandType = CommandType.WHITESPACE_COMMAND;
        } else {
            commandType = CommandType.C_COMMAND;
        }
    }

    public CommandType commandType() {
        return commandType;
    }

    public String symbol() {
        if (commandType == CommandType.A_COMMAND) {
            return currLine.trim().substring(1);
        } else if (commandType == CommandType.L_COMMAND) {
            return currLine.trim().substring(1, currLine.indexOf(')'));
        } else {
            return "";
        }
    }

    public String dest() {
        if (commandType != CommandType.C_COMMAND) {
            return "";
        }
        if (currLine.contains("=")) {
            return currLine.substring(0, currLine.indexOf('=')).trim();
        }
        return "";
    }

    public String comp() {
        if (commandType != CommandType.C_COMMAND) {
            return "";
        }
        if (currLine.contains("=")) {
            if (currLine.contains(";")) {
                return currLine.substring(currLine.indexOf('=')+1, currLine.indexOf(';')).trim();
            } else {
                if (currLine.contains("//")) {
                    return currLine.substring(currLine.indexOf('=')+1, currLine.indexOf('/')).trim();
                } else {
                    return currLine.substring(currLine.indexOf('=')+1).trim();
                }
            }
        } else {
            if (currLine.contains(";")) {
                return currLine.substring(0, currLine.indexOf(';')).trim();
            }
        }
        return "";
    }

    public String jump() {
        if (commandType != CommandType.C_COMMAND) {
            return "";
        }
        if (currLine.contains(";")) {
            if (currLine.contains("//")) {
                return currLine.substring(currLine.indexOf(';')+1, currLine.indexOf('/')).trim();
            } else {
                return currLine.substring(currLine.indexOf(';')+1).trim();
            }
        }
        return "";
    }

}
