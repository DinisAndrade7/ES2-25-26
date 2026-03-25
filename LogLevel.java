package logsystem.model;

/**
 * Enumeração dos níveis de log suportados pelo sistema.
 */
public enum LogLevel {
    DEBUG(1),
    INFO(2),
    WARNING(3),
    ERROR(4);

    private final int priority;

    LogLevel(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
