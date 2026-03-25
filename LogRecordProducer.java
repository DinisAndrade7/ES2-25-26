package logsystem.factory;

import logsystem.model.LogLevel;
import logsystem.model.LogRecord;

/**
 * M2 - Ponto de entrada para criação de registos.
 * Fornece acesso centralizado às fábricas concretas.
 */
public class LogRecordProducer {

    private static final LogRecordFactory INFO_FACTORY    = new InfoLogFactory();
    private static final LogRecordFactory WARNING_FACTORY = new WarningLogFactory();
    private static final LogRecordFactory ERROR_FACTORY   = new ErrorLogFactory();
    private static final LogRecordFactory DEBUG_FACTORY   = new DebugLogFactory();

    private LogRecordProducer() {}

    /**
     * Cria um LogRecord com base no nível indicado.
     */
    public static LogRecord create(LogLevel level, String message,
                                   String category, String source) {
        switch (level) {
            case INFO:    return INFO_FACTORY.createLogRecord(message, category, source);
            case WARNING: return WARNING_FACTORY.createLogRecord(message, category, source);
            case ERROR:   return ERROR_FACTORY.createLogRecord(message, category, source);
            case DEBUG:   return DEBUG_FACTORY.createLogRecord(message, category, source);
            default:      throw new IllegalArgumentException("Nível desconhecido: " + level);
        }
    }
}
