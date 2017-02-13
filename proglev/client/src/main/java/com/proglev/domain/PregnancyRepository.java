package com.proglev.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class PregnancyRepository {
    private final Map<Long, Pregnancy> data = new HashMap<>();
    private long nextId = 0L;

    @Value("file:${APPDATA}/proglev/data.db")
    private Resource eventsLog;

    @javax.annotation.Resource
    private ObjectMapper objectMapper;

    private BufferedWriter eventsLogWriter;
    private boolean loggingEnabled = false;

    @PostConstruct
    public void init() throws IOException {
        File logFile = eventsLog.getFile();
        if (!logFile.exists()){
            logFile.getParentFile().mkdirs();
            logFile.createNewFile();
        }
        Files.lines(logFile.toPath()).forEach(line -> {
            String[] splitted = line.split("\\|");
            if (splitted.length < 2)
                return;
            String eventType = splitted[0];
            String serializedData = splitted[1];
            try {
                switch (eventType) {
                    case "CREATE":
                        create(objectMapper.readValue(serializedData, Pregnancy.class));
                        break;
                    case "UPDATE":
                        update(objectMapper.readValue(serializedData, Pregnancy.class));
                        break;
                    case "DELETE":
                        delete(objectMapper.readValue(serializedData, Long.class));
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        eventsLogWriter = new BufferedWriter(new FileWriter(logFile, true));
        loggingEnabled = true;
    }

    @PreDestroy
    public void destroy() throws IOException {
        eventsLogWriter.close();
    }

    public Pregnancy create(Pregnancy pregnancy) throws IOException {
        validate(pregnancy);
        pregnancy.setId(nextId++);
        data.put(pregnancy.getId(), pregnancy);
        log("CREATE", pregnancy);
        return pregnancy;
    }

    private void log(String eventType, Object eventData) throws IOException {
        if (loggingEnabled) {
            eventsLogWriter.write(eventType + "|" + objectMapper.writeValueAsString(eventData));
            eventsLogWriter.newLine();
            eventsLogWriter.flush();
        }
    }

    public void update(Pregnancy pregnancy) throws IOException {
        assertNotNull(pregnancy.getId(), "Pregnancy should have an id");
        validate(pregnancy);
        data.put(pregnancy.getId(), pregnancy);
        log("UPDATE", pregnancy);
    }

    public Pregnancy get(Long id) {
        return data.get(id);
    }

    public Pregnancy delete(Long id) throws IOException {
        Pregnancy removed = data.remove(id);
        log("DELETE", id);
        return removed;
    }

    public Collection<Pregnancy> getAll() {
        return new ArrayList<>(data.values());
    }

    private void validate(Pregnancy pregnancy) {

    }

    private void assertNotNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
