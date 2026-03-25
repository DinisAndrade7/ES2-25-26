package logsystem.factory;

import logsystem.model.LogLevel;
import logsystem.model.LogRecord;

/**
 * M2 - Fábricas concretas para cada tipo de log.
 * Padrão: Factory Method — cada classe cria um tipo específico de LogRecord.
 *
 * Para adicionar um novo tipo basta criar uma nova subclasse,
 * sem alterar o código existente (princípio Open/Closed).
 */

// ── INFO ──────────────────────────────────────────────────────────────────────
class InfoLogFactory extends LogRecordFactory {
    @Override
    public LogRecord createLogRecord(String message, String category, String source) {
        return new LogRecord(LogLevel.INFO, message, category, source);
    }
}

// ── WARNING ───────────────────────────────────────────────────────────────────
class WarningLogFactory extends LogRecordFactory {
    @Override
    public LogRecord createLogRecord(String message, String category, String source) {
        return new LogRecord(LogLevel.WARNING, "[ATENÇÃO] " + message, category, source);
    }
}

// ── ERROR ─────────────────────────────────────────────────────────────────────
class ErrorLogFactory extends LogRecordFactory {
    @Override
    public LogRecord createLogRecord(String message, String category, String source) {
        return new LogRecord(LogLevel.ERROR, "[ERRO CRÍTICO] " + message, category, source);
    }
}

// ── DEBUG ─────────────────────────────────────────────────────────────────────
class DebugLogFactory extends LogRecordFactory {
    @Override
    public LogRecord createLogRecord(String message, String category, String source) {
        return new LogRecord(LogLevel.DEBUG, "[DBG] " + message, category, source);
    }
}
