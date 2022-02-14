package com.example.argumentationsolver;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import com.example.argumentationsolver.interfaces.services.ICommandOptionsConfig;
import com.example.argumentationsolver.interfaces.services.IGraphFileService;
import com.example.argumentationsolver.interfaces.services.IOutputService;

import guru.nidi.graphviz.model.MutableGraph;

@SpringBootApplication
public class ArgumentationSolverApplication implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(ArgumentationSolverApplication.class);

    private final IGraphFileService graphFileService;

    private final IOutputService outputService;

    private final Environment environment;

    private final ICommandOptionsConfig optionsConfig;

    private final Options options = new Options();

    public ArgumentationSolverApplication(IGraphFileService graphFileService,
                    IOutputService outputService, Environment environment, ICommandOptionsConfig optionsConfig) {
        this.graphFileService = graphFileService;
        this.outputService = outputService;
        this.environment = environment;
        this.optionsConfig = optionsConfig;
    }

    public static void main(String[] args) {
        SpringApplication.run(ArgumentationSolverApplication.class, args);
    }

    @Override
    public void run(String... args) {
        CommandLineParser parser = new DefaultParser();
        CommandLine line;
        try {
            // parse the command line arguments
            line = parser.parse(optionsConfig.configure(options), args);
        } catch (ParseException e) {
            logger.error(e.getMessage());
            return;
        }
        if (line.getOptions().length == 0) {
            logger.debug("Printing version and author");
            //Default case - output version and number.
            outputService.writeLine(environment.getProperty("application.nameAndVersion"));
            outputService.writeLine(environment.getProperty("application.author"));
            return;
        }
        if (line.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();
            String jarName = "argumentation-solver";
            try {
                String jarPath = ArgumentationSolverApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                jarName = jarPath.substring(jarPath.lastIndexOf("/") + 1);
            } catch (Exception e) {
                logger.error("Can't get jar name: {}", e.getMessage());
            }
            formatter.printHelp(String.format("java -jar %s", jarName), options);
            return;
        }
        if (line.hasOption("formats")) {
            logger.debug("Listing formats");
            outputService.writeList(graphFileService.formats());
            return;
        }

        if (line.hasOption("f")) {
            logger.info("Read file: {}", args[0]);
            MutableGraph af;
            try {
                String fileName = line.getOptionValue("f");
                af = graphFileService.readFile(fileName);
            } catch (Exception e) {
                logger.error("Can't read input file, unable to continue: {}", e.getMessage());
                outputService.writeLine("Error: " + e.getMessage());
                return;
            }
            if (af != null) {
                outputService.writeLine(
                                String.format("Argumentation Framework has %d arguments and %d attacks", af.nodes().size(), af.edges().size()));
            }
        }
    }

}
