package logsystem.factory;

import logsystem.model.LogRecord;

/**
 * M2 - Módulo de Criação de Registos de Log
 * Padrão: Factory Method
 *
 * Define o contrato para criação de registos de log.
 * Subclasses concretas implementam a lógica específica de cada tipo.
 */
public abstract class LogRecordFactory {

    /**
     * Factory Method — cria um registo de log.
     * Cada subclasse define o tipo e comportamento específico.
     *
     * @param message  Mensagem do log
     * @param category Categoria/componente de origem
     * @param source   Classe ou módulo que gerou o log
     * @return LogRecord pronto a ser processado
     */
    public abstract LogRecord createLogRecord(String message, String category, String source);

    /**
     * Template method — cria e imprime imediatamente o log.
     */
    public LogRecord createAndPrint(String message, String category, String source) {
        LogRecord record = createLogRecord(message, category, source);
        System.out.println("[FACTORY] Criado: " + record);
        return record;
    }
}
