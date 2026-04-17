package com.logsystem.memento;

import com.logsystem.config.LogConfiguration;
import com.logsystem.config.LogLevel;

/**
 * M6 - Originator: cria e restaura Mementos do estado do sistema de logs.
 *
 * Padrão: MEMENTO
 * Tem acesso ao estado interno do LogConfiguration e sabe como guardá-lo e restaurá-lo.
 */
public class LogSystemOriginator {

    private final LogConfiguration config;

    public LogSystemOriginator() {
        this.config = LogConfiguration.getInstance();
    }

    /**
     * Guarda o estado atual do sistema num Memento.
     */
    public LogSystemMemento save(String label) {
        LogSystemMemento memento = new LogSystemMemento(
                config.getMinimumLevel(),
                config.getActiveDestinations(),
                config.getActiveFilters(),
                config.getMessageFormat(),
                config.isTimestampEnabled(),
                label
        );
        System.out.println("[MEMENTO] Estado guardado: " + memento);
        return memento;
    }

    /**
     * Restaura o estado do sistema a partir de um Memento.
     */
    public void restore(LogSystemMemento memento) {
        System.out.println("[MEMENTO] A restaurar estado: " + memento);

        config.setMinimumLevel(memento.getMinimumLevel());
        config.setMessageFormat(memento.getMessageFormat());
        config.setTimestampEnabled(memento.isTimestampEnabled());

        // Limpa e repõe destinos
        for (String dest : config.getActiveDestinations()) {
            config.removeDestination(dest);
        }
        for (String dest : memento.getActiveDestinations()) {
            config.addDestination(dest);
        }

        // Limpa e repõe filtros
        for (String filter : config.getActiveFilters()) {
            config.removeFilter(filter);
        }
        for (String filter : memento.getActiveFilters()) {
            config.addFilter(filter);
        }

        System.out.println("[MEMENTO] Estado restaurado com sucesso.");
    }

    public LogLevel getCurrentLevel() {
        return config.getMinimumLevel();
    }
}
