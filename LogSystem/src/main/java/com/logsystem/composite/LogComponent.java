package com.logsystem.composite;

import com.logsystem.factory.LogRecord;

/**
 * M4 - Módulo de Estruturação de Registos
 *
 * Padrão: COMPOSITE
 * Interface comum a registos individuais e grupos de registos.
 * Permite tratar uniformemente um único log e uma categoria inteira de logs.
 */
public interface LogComponent {

    /**
     * Nome deste componente (registo ou categoria).
     */
    String getName();

    /**
     * Processa (emite) este componente para os destinos configurados.
     */
    void process();

    /**
     * Retorna todos os registos contidos neste componente.
     */
    java.util.List<LogRecord> getRecords();

    /**
     * Aceita um visitante para processamento externo (ex: filtragem, análise).
     */
    void accept(LogVisitor visitor);
}
