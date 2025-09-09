package org.precize;



import org.precize.dto.Event;
import org.precize.observer.AlertObserver;
import org.precize.observer.LoggerObserver;
import org.precize.repository.OrderRepository;
import org.precize.service.EventProcessor;
import org.precize.service.EventReader;
import org.precize.util.CommonUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        try {
            // 1. Initialization
            OrderRepository repository = new OrderRepository();
            EventProcessor processor = new EventProcessor(repository);
            EventReader reader = new EventReader();
            CommonUtil commonUtil = new CommonUtil();

            // 2. Register Observers
            processor.addObserver(new LoggerObserver());
            processor.addObserver(new AlertObserver());

            // 3. Load input file path from resources folder
            Path inputFilePath = Paths.get(
                    Objects.requireNonNull(
                            ClassLoader.getSystemResource("events.txt")
                    ).toURI()
            );

            // 4. Read and process events
            System.out.println("--- Starting Event Processing ---");
            List<Event> events = reader.readEventsFromFile(inputFilePath);
            for (Event event : events) {
                processor.processEvent(event);
            }
            System.out.println("--- Event Processing Finished ---");

            // Optional: Print final state of orders for verification
            System.out.println("\n--- Final Customer Order States ---");
            System.out.println(commonUtil.prettyPrintHistory(repository.getAll()));



        } catch ( Exception e) {
            System.err.println("Error running application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Path getPathFromResources(String fileName) throws URISyntaxException {
        return Paths.get(ClassLoader.getSystemResource(fileName).toURI());
    }
}