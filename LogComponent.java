package logsystem.composite;

import logsystem.model.LogRecord;

import java.util.List;

/**
 * M4 - Módulo de Estruturação de Registos
 * Padrão: Composite — Component
 *
 * Interface comum para registos individuais e grupos de registos.
 * Permite tratar de forma uniforme um único log ou uma categoria inteira.
 */
public interface LogComponent {

    /**
     * Processa (publica/escreve) o registo ou grupo de registos.
     */
    void process();

    /**
     * Devolve o nome do componente.
     */
    String getName();

    /**
     * Adiciona um sub-componente (apenas em grupos).
     */
    default void add(LogComponent component) {
        throw new UnsupportedOperationException("Operação não suportada neste componente.");
    }

    /**
     * Remove um sub-componente (apenas em grupos).
     */
    default void remove(LogComponent component) {
        throw new UnsupportedOperationException("Operação não suportada neste componente.");
    }

    /**
     * Devolve os sub-componentes (apenas em grupos).
     */
    default List<LogComponent> getChildren() {
        throw new UnsupportedOperationException("Operação não suportada neste componente.");
    }

    /**
     * Recolhe todos os LogRecord folha deste nó e seus descendentes.
     */
    List<LogRecord> getAllRecords();
}
