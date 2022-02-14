package com.example.argumentationsolver.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.argumentationsolver.interfaces.services.IOutputService;

import static java.lang.System.*;

@Service
public class StdOutOutputService implements IOutputService {
    Logger log = LoggerFactory.getLogger(StdOutOutputService.class);
    @Override
    public void writeLine(String line) {
        log.debug("Output: {}", line);
        out.println(line);
    }

    @Override
    public void writeList(List<String> list) {
        writeLine("[" + String.join(", ", list) + "]");
    }

}
