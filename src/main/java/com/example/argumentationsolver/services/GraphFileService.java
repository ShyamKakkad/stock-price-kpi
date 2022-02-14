package com.example.argumentationsolver.services;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.argumentationsolver.interfaces.services.IGraphFileService;

import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.parse.Parser;

import static guru.nidi.graphviz.model.Factory.mutGraph;

@Service
public class GraphFileService implements IGraphFileService {

    private Logger log = LoggerFactory.getLogger(GraphFileService.class);

    private String stackTrace(Exception e) {
        return Arrays.stream(e.getStackTrace())
                     .map(StackTraceElement::toString)
                     .collect(Collectors.joining("\n"));
    }

    private void addAttackFromLine(Map<String, MutableNode> nodes, String line) {
        try {
            String[] attackParts = line.trim().split("\\s", 2);
            MutableNode node = nodes.get(attackParts[0]);
            synchronized (node) {
                node.addLink(nodes.get(attackParts[1]));
                log.trace("att({},{})", node.name().toString(), attackParts[1]);
            }
        } catch (Exception e) {
            log.warn("Ignoring line '{}' since it does not parse as a valid attack", line);
        }
    }

    MutableGraph readAfFile(String fileName) throws IOException {
        log.debug("Attempting to read {}", fileName);
        try (InputStream af = new FileInputStream(fileName)) {
            ArrayList<String> argumentsStr = new ArrayList<>();
            ArrayList<String> attacksStr = new ArrayList<>();

            BufferedReader inputFileReader = new BufferedReader(new InputStreamReader(af));
            String line = inputFileReader.readLine();
            //Read nodes
            while (line != null && !line.equals("#")) {
                argumentsStr.add(line);
                log.trace("a({})", line);
                line = inputFileReader.readLine();
            }
            //Skip the divider line
            if ("#".equals(line)) {
                line = inputFileReader.readLine();
            }
            //Read edges
            while (line != null) {
                attacksStr.add(line);
                line = inputFileReader.readLine();
            }
            //Create and index arguments (nodes) by label
            ConcurrentMap<String, MutableNode> arguments = argumentsStr.parallelStream()
                                                                       .collect(Collectors.toConcurrentMap(Function.identity(), Factory::mutNode));
            //Add attacks to the existing arguments
            attacksStr.parallelStream().distinct().forEach(attackLine -> addAttackFromLine(arguments, attackLine));

            return mutGraph().add(new ArrayList<>(arguments.values())).setDirected(true);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.trace(stackTrace(e));
            throw e;
        }
    }

    MutableGraph readDotFile(String fileName) throws IOException {
        log.debug("Attempting to read {}", fileName);
        try (InputStream dot = new FileInputStream(fileName)) {
            MutableGraph g = new Parser().read(dot);
            log.debug("Read {} with {} arguments and {} attacks", fileName, g.nodes().size(), g.edges().size());
            return g;
        } catch (IOException e) {
            log.error(e.getMessage());
            log.trace(stackTrace(e));
            throw e;
        }
    }

    @Override
    public List<String> formats() {
        return List.of("tgf");
    }

    @Override
    public MutableGraph readFile(String fileName) throws IOException {
        if (fileName.toLowerCase().endsWith(".tgf")) {
            log.info("Reading AF file");
            return readAfFile(fileName);
        }
        else if (fileName.toLowerCase().endsWith(".dot")) {
            log.info("Reading DOT file");
            return readDotFile(fileName);
        }
        else {
            throw new IllegalArgumentException("Not supported file format");
        }
    }

    @Override
    public void writeFile(Graph g) {
        log.error("Not yet implemented");
    }
}
