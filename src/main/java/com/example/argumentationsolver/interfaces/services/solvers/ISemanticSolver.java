package com.example.argumentationsolver.interfaces.services.solvers;

import java.util.Collection;
import java.util.Set;

import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

/**
 * Represents the methods to crate and verify an extension from a semantic
 */
public interface ISemanticSolver {
    /**
     * Generate the possible labellings for the Semantic with the given ArgumentationFramework AF
     * @param af
     * @return
     */
    Collection<Set<MutableNode>> compute(MutableGraph af);

    /**
     * Verify if the the given extension is a valid extension for the semantic in this Argumentation Framework
     * @param extension
     * @param af
     * @return
     */
    boolean verify(Collection<MutableNode> extension, MutableGraph af);
}
