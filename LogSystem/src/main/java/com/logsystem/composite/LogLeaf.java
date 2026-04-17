package com.logsystem.composite;

import com.logsystem.factory.LogRecord;

import java.util.Collections;
import java.util.List;

/**
 * M4 - Folha do Composite
 *
 * Representa um registo de log individual.
 * Não tem filhos — é o elemento atómico da estrutura.
 */
public class LogLeaf implements LogComponent {

    private final LogRecord record;

    public LogLeaf(LogRecord record) {
        this.record = record;
    }

    public LogRecord getRecord() {
        return record;
    }

    @Override
    public String getName() {
        return record.getLevel() + ":" + record.getMessage().substring(0, Math.min(30, record.getMessage().length()));
    }

    @Override
    public void process() {
        System.out.println("  [LEAF] " + record);
    }

    @Override
    public List<LogRecord> getRecords() {
        return Collections.singletonList(record);
    }

    @Override
    public void accept(LogVisitor visitor) {
        visitor.visitLeaf(this);
    }
}
