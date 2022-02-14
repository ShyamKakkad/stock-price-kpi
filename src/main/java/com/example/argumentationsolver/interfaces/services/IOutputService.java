package com.example.argumentationsolver.interfaces.services;

import java.util.List;

/**
 * Abstraction of output service to provide a way to give output of the application other than logging
 */
public interface IOutputService {
    /**
     * Write to the output followed by a newline.
     * @param line
     */
    void writeLine(String line);

    /**
     * Write a formatted list of data
     * @param list
     */
    void writeList(List<String> list);
}
