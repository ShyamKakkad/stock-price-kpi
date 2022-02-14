package com.example.argumentationsolver.services.solvers;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.example.argumentationsolver.interfaces.services.solvers.ISemanticSolver;

import guru.nidi.graphviz.model.MutableNode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

public class GroundedSemanticSolverTest {
    @Test
    public void GroundedSemanticSolver_IsConstructed() {
        GroundedSemanticSolver groundedSolver = new GroundedSemanticSolver();
        assertNotNull(groundedSolver);
        assertTrue(groundedSolver instanceof ISemanticSolver);
    }

    @Test
    public void GroundedSemanticSolver_SolvesTrivialArgument() {
        MutableNode argumentA = mutNode("a");
        MutableNode argumentB = mutNode("b");
        MutableNode argumentC = mutNode("c");

        var af = mutGraph("example1").setDirected(true).use((gr, ctx) -> {
            argumentA.addLink(argumentB);
            argumentB.addLink(argumentC);

            gr.add(argumentA);
            gr.add(argumentB);
            gr.add(argumentC);
        });

        GroundedSemanticSolver groundedSolver = new GroundedSemanticSolver();
        var expectedGroundedExtension = Set.of(argumentA, argumentC);
        var groundedExtension = groundedSolver.compute(af);
        assertEquals(1, groundedExtension.size());
        assertEquals(expectedGroundedExtension, groundedExtension.iterator().next());
    }
}
