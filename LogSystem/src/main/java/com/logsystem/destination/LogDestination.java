package com.logsystem.destination;

import com.logsystem.factory.LogRecord;

/**
 * M3 - Módulo de Abstração de Destino de Logs
 *
 * Padrão: BRIDGE (Strategy para destinos)
 * Interface que desacopla a abstração "como enviar" da implementação concreta.
 * Novos destinos são adicionados implementando esta interface — sem tocar no restante sistema.
 */
public interface LogDestination {

    /**
     * Envia um registo de log para este destino.
     */
    void write(LogRecord record, String formattedMessage);

    /**
     * Nome identificador do destino.
     */
    String getName();

    /**
     * Indica se o destino está disponível/aberto.
     */
    boolean isAvailable();

    /**
     * Liberta recursos associados ao destino (ficheiro, ligação, etc.).
     */
    void close();
}
