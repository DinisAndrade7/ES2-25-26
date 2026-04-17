package com.logsystem.pool;

import com.logsystem.factory.LogRecord;

/**
 * M5 - Objeto gerido pelo Object Pool.
 *
 * Padrão: STRATEGY (para formatação)
 * Encapsula a lógica de formatação de mensagens de log.
 * Pode ser reutilizado após reset() sem necessidade de recriação.
 */
public class MessageFormatter {

    private String pattern;
    private int usageCount;

    public MessageFormatter(String pattern) {
        this.pattern    = pattern;
        this.usageCount = 0;
    }

    /**
     * Formata um LogRecord de acordo com o padrão configurado.
     */
    public String format(LogRecord record) {
        usageCount++;
        return record.format(pattern);
    }

    /**
     * Altera o padrão de formatação em runtime (Strategy).
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    /**
     * Repõe o estado do formatador para reutilização pelo pool.
     */
    public void reset() {
        // Mantém o padrão base da configuração
        this.usageCount = 0;
    }

    public int getUsageCount() {
        return usageCount;
    }

    @Override
    public String toString() {
        return "MessageFormatter{pattern='" + pattern + "', usages=" + usageCount + "}";
    }
}
