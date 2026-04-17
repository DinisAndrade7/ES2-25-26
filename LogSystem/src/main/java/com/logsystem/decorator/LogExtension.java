package com.logsystem.decorator;

import com.logsystem.factory.LogRecord;

/**
 * M7 - Módulo de Extensão de Funcionalidades
 *
 * Padrão: DECORATOR
 * Interface base para extensões dinâmicas do sistema de logs.
 * Permite adicionar funcionalidades (alertas, monitorização, análise) sem
 * modificar a estrutura principal do sistema.
 */
public interface LogExtension {

    /**
     * Nome identificador da extensão.
     */
    String getName();

    /**
     * Processa um registo de log — a extensão pode decorar, filtrar ou reagir.
     */
    void process(LogRecord record, String formattedMessage);

    /**
     * Indica se a extensão está ativa.
     */
    boolean isEnabled();

    /**
     * Ativa ou desativa a extensão em runtime.
     */
    void setEnabled(boolean enabled);
}
