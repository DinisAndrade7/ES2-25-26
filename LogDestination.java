package logsystem.bridge;

import logsystem.model.LogRecord;

/**
 * M3 - Módulo de Abstração de Destino de Logs
 * Padrão: Bridge — Implementor
 *
 * Define a interface de baixo nível para escrever logs num destino concreto.
 * Desacopla a abstração (LogWriter) da implementação (destino físico).
 */
public interface LogDestination {

    /**
     * Escreve o registo de log no destino específico.
     */
    void write(LogRecord record, String formattedMessage);

    /**
     * Abre / inicializa o destino (ex.: abre ficheiro, conecta à BD).
     */
    void open();

    /**
     * Fecha / liberta recursos do destino.
     */
    void close();

    /**
     * Devolve o nome identificativo do destino.
     */
    String getName();
}
