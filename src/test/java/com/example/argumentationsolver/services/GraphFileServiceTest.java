package com.example.argumentationsolver.services;

import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.Test;

import com.example.argumentationsolver.interfaces.services.IGraphFileService;

import guru.nidi.graphviz.model.MutableGraph;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GraphFileServiceTest {

    private String getResourcePath(String name) {
        URL resource = getClass().getClassLoader().getResource(name);
        String fileName;
        try {
            assert resource != null;
            fileName = resource.toURI().getPath();
        } catch (Exception e) {
            fileName = resource.getPath();
        }
        return fileName;
    }

    @Test
    void GraphFileService_Reads_Example_File11_DOT() throws IOException {
        IGraphFileService service = new GraphFileService();
        String fileName = getResourcePath("figure1-1.dot");
        MutableGraph graph = service.readFile(fileName);
        assertEquals(4, graph.nodes().size());
        assertEquals(4, graph.edges().size());
    }

    @Test
    void GraphFileService_Reads_Example_File11_TGF() throws IOException {
        IGraphFileService service = new GraphFileService();
        String fileName = getResourcePath("figure1-1.tgf");
        MutableGraph graph = service.readFile(fileName);
        assertEquals(4, graph.nodes().size());
        assertEquals(4, graph.edges().size());
    }

    @Test
    void GraphFileService_Reads_Example_File12_DOT() throws IOException {
        IGraphFileService service = new GraphFileService();
        String fileName = getResourcePath("figure1-2.dot");
        MutableGraph graph = service.readFile(fileName);
        assertEquals(4, graph.nodes().size());
        assertEquals(5, graph.edges().size());
    }

    @Test
    void GraphFileService_Reads_Example_File12_TGF() throws IOException {
        IGraphFileService service = new GraphFileService();
        String fileName = getResourcePath("figure1-2.tgf");
        MutableGraph graph = service.readFile(fileName);
        assertEquals(4, graph.nodes().size());
        assertEquals(5, graph.edges().size());
    }

    @Test
    void GraphFileService_Reads_Example_File13_DOT() throws IOException {
        IGraphFileService service = new GraphFileService();
        String fileName = getResourcePath("figure1-3.dot");
        MutableGraph graph = service.readFile(fileName);
        assertEquals(3, graph.nodes().size());
        assertEquals(3, graph.edges().size());
    }

    @Test
    void GraphFileService_Reads_Example_File13_TGF() throws IOException {
        IGraphFileService service = new GraphFileService();
        String fileName = getResourcePath("figure1-3.tgf");
        MutableGraph graph = service.readFile(fileName);
        assertEquals(3, graph.nodes().size());
        assertEquals(3, graph.edges().size());
    }

    @Test
    void GraphFileService_Reads_Example_File2_DOT() throws IOException {
        IGraphFileService service = new GraphFileService();
        String fileName = getResourcePath("figure2.dot");
        MutableGraph graph = service.readFile(fileName);
        assertEquals(5, graph.nodes().size());
        assertEquals(6, graph.edges().size());
    }

    @Test
    void GraphFileService_Reads_Example_File2_TGF() throws IOException {
        IGraphFileService service = new GraphFileService();
        String fileName = getResourcePath("figure2.tgf");
        MutableGraph graph = service.readFile(fileName);
        assertEquals(5, graph.nodes().size());
        assertEquals(6, graph.edges().size());
    }
}
