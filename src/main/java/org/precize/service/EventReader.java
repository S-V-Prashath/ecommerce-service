package org.precize.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.precize.dto.Event;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class EventReader {

    private final ObjectMapper objectMapper;

    public EventReader() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public List<Event> readEventsFromFile(Path filePath) {
        try (Stream<String> lines = Files.lines(filePath)) {
            return lines
                    .map(this::parseLineToEvent) // Convert each line of text into an Event object
                    .filter(Objects::nonNull)   // Filter out any lines that failed to parse (returned null)
                    .collect(Collectors.toList()); // Collect the valid events into a list
        } catch (IOException e) {
            System.err.println("Fatal: Could not read the event file at path: " + filePath);
            e.printStackTrace();
            return Collections.emptyList(); // Return an empty list to prevent downstream errors
        }
    }

    private Event parseLineToEvent(String jsonLine) {
        try {

            if (jsonLine.trim().isEmpty()) {
                return null; // Ignore empty lines
            }
            return objectMapper.readValue(jsonLine, Event.class);
        } catch (IOException e) {
            System.err.println("Warning: Failed to parse JSON line. Skipping. | Line: \"" + jsonLine + "\" | Error: " + e.getMessage());
            return null;
        }
    }
}
