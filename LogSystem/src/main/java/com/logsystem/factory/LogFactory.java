package com.logsystem.factory;

import com.logsystem.config.LogLevel;

/**
 * M2 - Fachada da Factory
 *
 * Ponto de entrada unificado para criação de registos de log.
 * Oculta os criadores concretos do resto da aplicação.
 */
public class LogFactory {

    private static final LogCreator debugCreator   = new DebugLogCreator();
    private static final LogCreator infoCreator    = new InfoLogCreator();
    private static final LogCreator warningCreator = new WarningLogCreator();
    private static final LogCreator errorCreator   = new ErrorLogCreator();

    private LogFactory() {}

    /**
     * Cria um registo de log do tipo indicado.
     * Retorna null se o nível estiver inativo na configuração atual.
     */
    public static LogRecord create(LogLevel level, String message, String category) {
        return switch (level) {
            case DEBUG   -> debugCreator.buildLog(message, category);
            case INFO    -> infoCreator.buildLog(message, category);
            case WARNING -> warningCreator.buildLog(message, category);
            case ERROR   -> errorCreator.buildLog(message, category);
        };
    }

    public static LogRecord debug(String message, String category) {
        return create(LogLevel.DEBUG, message, category);
    }

    public static LogRecord info(String message, String category) {
        return create(LogLevel.INFO, message, category);
    }

    public static LogRecord warning(String message, String category) {
        return create(LogLevel.WARNING, message, category);
    }

    public static LogRecord error(String message, String category) {
        return create(LogLevel.ERROR, message, category);
    }
}
