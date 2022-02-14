package com.example.argumentationsolver.services;

import org.apache.commons.cli.Options;
import org.springframework.stereotype.Component;

import com.example.argumentationsolver.interfaces.services.ICommandOptionsConfig;

/**
 * Insance of commandline options for ICCMA 2021 Rules
 */
@Component
public class Iccma2021Options implements ICommandOptionsConfig {
    @Override public Options configure(Options options) {
        options.addOption("formats", "formats", false, "Prints the supported formats of the solver in the form\n"
                        + "[supportedFormat1,...,supportedFormatN]\n");
        options.addOption("problems", "problems", false, "Prints the supported computational problems in the form\n"
                        + "[supportedProblem1,...,supportedProblemN]\n");
        options.addOption("p", "problem", true, "Problem to solve");
        options.addOption("f", "file", true, "Input filename");
        options.addOption("fo", "format", true, "Input file format");
        options.addOption("a", "additional", true, "Additional parameter required for the function");
        options.addOption("?", "help", false, "Print usage instructions");
        return options;
    }
}
