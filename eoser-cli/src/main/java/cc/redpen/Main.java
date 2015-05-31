package cc.redpen;

import cc.redpen.config.SymbolTable;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final String PROGRAM = "eoser";

    public static void main(String... args) throws EOSerException {
        System.exit(run(args));
    }

    @SuppressWarnings("static-access")
    public static int run(String... args) throws EOSerException {
        Options options = new Options();
        options.addOption("h", "help", false, "Displays this help information and exits");

        options.addOption(OptionBuilder.withLongOpt("version")
                .withDescription("Displays version information and exits")
                .create("v"));

        CommandLineParser commandLineParser = new BasicParser();
        CommandLine commandLine;
        try {
            commandLine = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            LOG.error("Error occurred in parsing command line options ");
            printHelp(options);
            return -1;
        }

        if (commandLine.hasOption("h")) {
            printHelp(options);
            return 0;
        }
        if (commandLine.hasOption("v")) {
            System.out.println(SentenceExtractor.VERSION);
            return 0;
        }

        String[] inputFileNames = commandLine.getArgs();
        Path[] inputFiles = new Path[inputFileNames.length];
        for (int i = 0; i < inputFileNames.length; i++) {
            inputFiles[i] = Paths.get(inputFileNames[i]);
        }

        SentenceExtractor extractor = new SentenceExtractor(new SymbolTable("en",
                Optional.<String>empty(), new ArrayList<>()));

        for (Path inputFile : inputFiles) {
            try (BufferedReader br = Files.newBufferedReader(inputFile)) {
                String line;
                while((line  = br.readLine()) != null) {
                    System.out.println("line: " + line);
                }
            } catch (IOException e) {
                LOG.error("An error was reported: " + e.getMessage());
            }
        }

        return 0;
    }

    private static void printHelp(Options opt) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(100);
        PrintWriter pw = new PrintWriter(System.err);
        formatter.printHelp(pw, 80, PROGRAM + " [<INPUT FILE>]", null, opt, 1, 3, "");
        pw.flush();
    }

}
