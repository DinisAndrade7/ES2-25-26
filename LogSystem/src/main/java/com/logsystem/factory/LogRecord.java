package com.logsystem.factory;

import com.logsystem.config.LogLevel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa um registo de log criado pelo sistema.
 * É o produto gerado pelos criadores da Factory Method.
 */
public class LogRecord {

    private final LogLevel level;
    private final String message;
    private final String category;
    private final LocalDateTime timestamp;
    private final String threadName;

    public LogRecord(LogLevel level, String message, String category) {
        this.level = level;
        this.message = message;
        this.category = category;
        this.timestamp = LocalDateTime.now();
        this.threadName = Thread.currentThread().getName();
    }

    public LogLevel getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public String getCategory() {
        return category;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getThreadName() {
        return threadName;
    }

    /**
     * Formata o registo de acordo com o padrão de formato fornecido.
     */
    public String format(String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return pattern
                .replace("%level",     "[" + level.getLabel() + "]")
                .replace("%timestamp", timestamp.format(dtf))
                .replace("%category",  category)
                .replace("%message",   message)
                .replace("%thread",    threadName);
    }

    @Override
    public String toString() {
        return format("[%level] [%timestamp] [%category] %message");
    }
}
