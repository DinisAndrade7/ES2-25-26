package logsystem.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa um registo de log com todos os seus atributos.
 */
public class LogRecord {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final LogLevel level;
    private final String message;
    private final String category;
    private final LocalDateTime timestamp;
    private final String source;

    public LogRecord(LogLevel level, String message, String category, String source) {
        this.level = level;
        this.message = message;
        this.category = category;
        this.source = source;
        this.timestamp = LocalDateTime.now();
    }

    public LogLevel getLevel() { return level; }
    public String getMessage() { return message; }
    public String getCategory() { return category; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getSource() { return source; }

    public String getFormattedTimestamp() {
        return timestamp.format(FORMATTER);
    }

    @Override
    public String toString() {
        return "[" + getFormattedTimestamp() + "] [" + level + "] [" + category + "] " + message;
    }
}
