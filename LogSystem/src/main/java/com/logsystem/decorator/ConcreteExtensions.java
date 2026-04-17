package com.logsystem.decorator;

import com.logsystem.config.LogLevel;
import com.logsystem.factory.LogRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * M7 - Extensões concretas do sistema de logs.
 *
 * Cada extensão acrescenta uma funcionalidade independente.
 * São encadeáveis via Decorator sem alterar a estrutura principal.
 */

// ─── BASE (extensão nula — ponto de partida da cadeia) ────────────────────────
class BaseLogExtension implements LogExtension {

    @Override public String getName()                      { return "BASE"; }
    @Override public void process(LogRecord r, String m)   { /* nada — base da cadeia */ }
    @Override public boolean isEnabled()                   { return true; }
    @Override public void setEnabled(boolean e)            { /* sempre ativa */ }
}

// ─── ALERTA DE ADMINISTRADOR ──────────────────────────────────────────────────
class AdminAlertDecorator extends LogExtensionDecorator {

    private final LogLevel threshold;
    private final String adminContact;
    private int alertsSent;

    AdminAlertDecorator(LogExtension wrapped, LogLevel threshold, String adminContact) {
        super(wrapped);
        this.threshold    = threshold;
        this.adminContact = adminContact;
        this.alertsSent   = 0;
    }

    @Override
    protected void before(LogRecord record, String formattedMessage) {
        if (record.getLevel().getPriority() >= threshold.getPriority()) {
            alertsSent++;
            System.out.println("[ALERT] *** ALERTA ENVIADO PARA " + adminContact +
                    " *** | " + record.getLevel() + ": " + record.getMessage());
        }
    }

    @Override public String getName() { return "ADMIN_ALERT(>" + threshold + ")"; }
    public int getAlertsSent()        { return alertsSent; }
}

// ─── INTEGRAÇÃO COM SISTEMA DE MONITORIZAÇÃO ─────────────────────────────────
class MonitoringDecorator extends LogExtensionDecorator {

    private final Map<LogLevel, Integer> counters;
    private final String monitoringEndpoint;

    MonitoringDecorator(LogExtension wrapped, String monitoringEndpoint) {
        super(wrapped);
        this.monitoringEndpoint = monitoringEndpoint;
        this.counters           = new HashMap<>();
        for (LogLevel l : LogLevel.values()) counters.put(l, 0);
    }

    @Override
    protected void after(LogRecord record, String formattedMessage) {
        counters.merge(record.getLevel(), 1, Integer::sum);
        // Simula envio de métrica
        System.out.println("[MONITOR] Métrica enviada para " + monitoringEndpoint +
                " | " + record.getLevel() + "=" + counters.get(record.getLevel()));
    }

    @Override public String getName() { return "MONITORING(" + monitoringEndpoint + ")"; }

    public Map<LogLevel, Integer> getCounters() { return new HashMap<>(counters); }
}

// ─── ANÁLISE AUTOMÁTICA DE PADRÕES DE ERRO ────────────────────────────────────
class ErrorPatternAnalyzerDecorator extends LogExtensionDecorator {

    private final List<String> errorMessages;
    private final int alertThreshold;

    ErrorPatternAnalyzerDecorator(LogExtension wrapped, int alertThreshold) {
        super(wrapped);
        this.errorMessages  = new ArrayList<>();
        this.alertThreshold = alertThreshold;
    }

    @Override
    protected void before(LogRecord record, String formattedMessage) {
        if (record.getLevel() == LogLevel.ERROR) {
            errorMessages.add(record.getMessage());
            if (errorMessages.size() >= alertThreshold) {
                System.out.println("[ANALYZER] *** PADRÃO DETECTADO: " +
                        errorMessages.size() + " erros acumulados! ***");
                System.out.println("[ANALYZER] Últimos erros: " +
                        errorMessages.subList(Math.max(0, errorMessages.size() - 3), errorMessages.size()));
                errorMessages.clear(); // reset após alerta
            }
        }
    }

    @Override public String getName()             { return "ERROR_ANALYZER(t=" + alertThreshold + ")"; }
    public List<String> getErrorMessages()        { return new ArrayList<>(errorMessages); }
}

// ─── TIMESTAMP ENRICHER ───────────────────────────────────────────────────────
class TimestampEnricherDecorator extends LogExtensionDecorator {

    TimestampEnricherDecorator(LogExtension wrapped) {
        super(wrapped);
    }

    @Override
    protected void before(LogRecord record, String formattedMessage) {
        // Simula enriquecimento com metadados adicionais de tempo
        System.out.println("[ENRICHER] Timestamp UTC enriquecido: " + record.getTimestamp());
    }

    @Override public String getName() { return "TIMESTAMP_ENRICHER"; }
}
