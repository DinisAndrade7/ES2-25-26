package com.logsystem.factory;

import com.logsystem.config.LogLevel;

/**
 * M2 - Criadores Concretos (Factory Method)
 *
 * Cada classe concreta sabe criar registos do seu tipo específico.
 * Para adicionar um novo tipo, basta criar nova subclasse — sem alterar código existente.
 */

// ─── DEBUG ───────────────────────────────────────────────────────────────────
class DebugLogCreator extends LogCreator {

    @Override
    public LogRecord createLog(String message, String category) {
        return new LogRecord(LogLevel.DEBUG, "[DBG] " + message, category);
    }

    @Override
    public LogLevel getLevel() {
        return LogLevel.DEBUG;
    }
}

// ─── INFO ────────────────────────────────────────────────────────────────────
class InfoLogCreator extends LogCreator {

    @Override
    public LogRecord createLog(String message, String category) {
        return new LogRecord(LogLevel.INFO, message, category);
    }

    @Override
    public LogLevel getLevel() {
        return LogLevel.INFO;
    }
}

// ─── WARNING ─────────────────────────────────────────────────────────────────
class WarningLogCreator extends LogCreator {

    @Override
    public LogRecord createLog(String message, String category) {
        return new LogRecord(LogLevel.WARNING, "⚠ " + message, category);
    }

    @Override
    public LogLevel getLevel() {
        return LogLevel.WARNING;
    }
}

// ─── ERROR ───────────────────────────────────────────────────────────────────
class ErrorLogCreator extends LogCreator {

    @Override
    public LogRecord createLog(String message, String category) {
        return new LogRecord(LogLevel.ERROR, "✖ " + message, category);
    }

    @Override
    public LogLevel getLevel() {
        return LogLevel.ERROR;
    }
}
