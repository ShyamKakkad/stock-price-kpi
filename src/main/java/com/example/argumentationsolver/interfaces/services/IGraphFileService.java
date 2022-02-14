package com.example.argumentationsolver.interfaces.services;

import java.io.IOException;
import java.util.List;

import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.MutableGraph;

/**
 * Abstraction for a DOT file reader
 */
public interface IGraphFileService {
    /**
     * Read a file from the filesystem into an Immutable Graph
     *
     * @param fileName
     * @return
     */
    MutableGraph readFile(String fileName) throws IOException;

    /**
     * String list of formats supported by the reader
     * @return
     */
    List<String> formats();

    void writeFile(Graph g);
}
