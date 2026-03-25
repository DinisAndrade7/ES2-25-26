package logsystem.composite;

import logsystem.bridge.LogWriter;
import logsystem.model.LogRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * M4 - Nó folha: representa um único registo de log.
 * Padrão: Composite — Leaf
 */
public class LogEntry implements LogComponent {

    private final LogRecord record;
    private final LogWriter writer;

    public LogEntry(LogRecord record, LogWriter writer) {
        this.record = record;
        this.writer = writer;
    }

    @Override
    public void process() {
        writer.write(record);
    }

    @Override
    public String getName() {
        return record.getLevel() + ":" + record.getMessage();
    }

    @Override
    public List<LogRecord> getAllRecords() {
        return Collections.singletonList(record);
    }

    public LogRecord getRecord() {
        return record;
    }
}
