package com.example.argumentationsolver.services.solvers;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.argumentationsolver.interfaces.models.Labelling;
import com.example.argumentationsolver.interfaces.services.solvers.ISemanticSolver;

import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.LinkTarget;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

/**
 * Implementation of Nofal et al 2021 Grounded Semantic solver using recursive algorithm
 */
@Service("gr")
public class GroundedSemanticSolver implements ISemanticSolver {

    private class State {
        public Map<MutableNode, Labelling> labels;
        public Map<String, MutableNode> nodesByName;
        public Map<LinkTarget, Long> und_pre;
    }

    @Override
    public Collection<Set<MutableNode>> compute(MutableGraph af) {
        var s = new State();

        // Label all nodes as UNDECIDED
        s.labels = af.nodes().parallelStream().collect(Collectors.toMap(Function.identity(), node -> Labelling.UNDECIDED));

        // Collect all nodes by name since edges to not directly reference them
        s.nodesByName = af.nodes().parallelStream().collect(Collectors.toMap(n -> n.name().value(), Function.identity()));

        // Count all edges by target node, as referenced from above
        s.und_pre = af.edges().parallelStream()
                    .collect(Collectors.groupingBy(link -> s.nodesByName.get(link.to().name().value()), Collectors.counting()));

        // Collect all nodes with no parents (these are not attacked)
        var no_pre = s.labels.keySet().parallelStream()
                             .filter(y -> !s.und_pre.containsKey(y))
                             .collect(Collectors.toSet());

        // include() all nodes with no parents: label IN and children OUT, etc
        no_pre.forEach(x -> include(s, x));

        // return the extension set of all nodes that are IN
        return Set.of(s.labels.entrySet().parallelStream()
                              .filter(e -> e.getValue() == Labelling.IN)
                              .map(Map.Entry::getKey)
                              .collect(Collectors.toSet()));
    }


    private State include(State s, MutableNode x) {
        //Label x as IN
        s.labels.put(x, Labelling.IN);

        // Collect all children that are not OUT
        var y_out = x.links().parallelStream()
                     .map(link -> s.nodesByName.get(link.to().name().value()))
                     .filter(y -> !s.labels.get(y).equals(Labelling.OUT))
                     .collect(Collectors.toSet());
        // Label all children as OUT and collect all UNDECIDED grandchildren
        var y_out_undecided_children = y_out.stream().flatMap(y -> {
            s.labels.put(y, Labelling.OUT);
            return y.links().parallelStream()
                    .map(link -> s.nodesByName.get(link.to().name().value()))
                    .filter(z -> s.labels.get(z).equals(Labelling.UNDECIDED));
        }).collect(Collectors.toSet());

        // include() all UNDECIDED grandchildren: recursive call mark them IN, and children OUT and so on
        y_out_undecided_children.forEach(z -> {
            var und_pre_z = s.und_pre.get(z);
            if (und_pre_z >= 0) {
                und_pre_z--;
                s.und_pre.put(z, und_pre_z);
                if (und_pre_z == 0) {
                    include(s, z);
                }
            }
        });
        return s;
    }


    @Override
    public boolean verify(Collection<MutableNode> extension, MutableGraph af) {
        return false;
    }
}
