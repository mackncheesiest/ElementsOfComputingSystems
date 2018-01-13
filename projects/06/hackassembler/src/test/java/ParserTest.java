import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import nand2tetris.assembler.CommandType;
import nand2tetris.assembler.Parser;
import org.junit.Test;
import org.junit.Before;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ParserTest {

    Parser parser;

    @Before
    public void setup() {
    }

    @Test
    public void testC_CommandParsing() throws IOException {
        String sampleCommand = "D=M;JGT\n";
        parser = new Parser(new ByteArrayInputStream(sampleCommand.getBytes(StandardCharsets.UTF_8)));
        parser.advance();
        assertThat(parser.commandType(), is(CommandType.C_COMMAND));
        assertThat(parser.comp(), is("M"));
        assertThat(parser.dest(), is("D"));
        assertThat(parser.jump(), is("JGT"));
        assertThat(parser.symbol(), is(""));
        assertThat(parser.hasMoreCommands(), is(false));
    }

    @Test
    public void testA_CommandParsing() throws IOException {
        String sampleCommand = "@12345\n";
        parser = new Parser(new ByteArrayInputStream(sampleCommand.getBytes(StandardCharsets.UTF_8)));
        parser.advance();
        assertThat(parser.commandType(), is(CommandType.A_COMMAND));
        assertThat(parser.comp(), is(""));
        assertThat(parser.dest(), is(""));
        assertThat(parser.jump(), is(""));
        assertThat(parser.symbol(), is("12345"));
        assertThat(parser.hasMoreCommands(), is(false));

        sampleCommand = "@SOMELABEL5";
        parser = new Parser(new ByteArrayInputStream(sampleCommand.getBytes(StandardCharsets.UTF_8)));
        parser.advance();
        assertThat(parser.commandType(), is(CommandType.A_COMMAND));
        assertThat(parser.comp(), is(""));
        assertThat(parser.dest(), is(""));
        assertThat(parser.jump(), is(""));
        assertThat(parser.symbol(), is("SOMELABEL5"));
        assertThat(parser.hasMoreCommands(), is(false));
    }

    @Test
    public void testL_CommandParsing() throws IOException {
        String sampleCommand = "(LOOP):\n";
        parser = new Parser(new ByteArrayInputStream(sampleCommand.getBytes(StandardCharsets.UTF_8)));
        parser.advance();
        assertThat(parser.commandType(), is(CommandType.L_COMMAND));
        assertThat(parser.comp(), is(""));
        assertThat(parser.dest(), is(""));
        assertThat(parser.jump(), is(""));
        assertThat(parser.symbol(), is("LOOP"));
        assertThat(parser.hasMoreCommands(), is(false));

        sampleCommand = "(OUTPUT_FIRST)\n";
        parser = new Parser(new ByteArrayInputStream(sampleCommand.getBytes(StandardCharsets.UTF_8)));
        parser.advance();
        assertThat(parser.commandType(), is(CommandType.L_COMMAND));
        assertThat(parser.comp(), is(""));
        assertThat(parser.dest(), is(""));
        assertThat(parser.jump(), is(""));
        assertThat(parser.symbol(), is("OUTPUT_FIRST"));
        assertThat(parser.hasMoreCommands(), is(false));
    }

    @Test
    public void testWHITESPACE_CommandParsing() throws IOException {
        String sampleCommand = "// This is a comment\n";
        parser = new Parser(new ByteArrayInputStream(sampleCommand.getBytes(StandardCharsets.UTF_8)));
        parser.advance();
        assertThat(parser.commandType(), is(CommandType.WHITESPACE_COMMAND));
        assertThat(parser.comp(), is(""));
        assertThat(parser.dest(), is(""));
        assertThat(parser.jump(), is(""));
        assertThat(parser.symbol(), is(""));
        assertThat(parser.hasMoreCommands(), is(false));

        sampleCommand = "  \n";
        parser = new Parser(new ByteArrayInputStream(sampleCommand.getBytes(StandardCharsets.UTF_8)));
        parser.advance();
        assertThat(parser.commandType(), is(CommandType.WHITESPACE_COMMAND));
        assertThat(parser.comp(), is(""));
        assertThat(parser.dest(), is(""));
        assertThat(parser.jump(), is(""));
        assertThat(parser.symbol(), is(""));
        assertThat(parser.hasMoreCommands(), is(false));

        assertThat("@2".matches("@\\d+"), is(true));
    }

}
