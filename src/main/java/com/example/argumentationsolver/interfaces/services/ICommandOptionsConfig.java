package com.example.argumentationsolver.interfaces.services;

import org.apache.commons.cli.Options;

/**
 * Application command options for use with Apache Commons CLI
 */
public interface ICommandOptionsConfig {
    /**
     * Add the required configuration to the given Options object
     * @param options
     * @return
     */
    Options configure(Options options);
}
