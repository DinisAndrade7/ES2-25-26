package com.logsystem.memento;

import com.logsystem.config.LogLevel;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * M6 - Memento: guarda um snapshot do estado do sistema de logs.
 *
 * Padrão: MEMENTO
 * O Memento é opaco — não expõe os seus detalhes internos ao exterior.
 * Apenas o LogSystemCaretaker e o LogSystemOriginator acedem ao estado guardado.
 */
public class LogSystemMemento {

    // Estado encapsulado — imutável após criação
    private final LogLevel minimumLevel;
    private final Set<String> activeDestinations;
    private final Set<String> activeFilters;
    private final String messageFormat;
    private final boolean timestampEnabled;
    private final LocalDateTime savedAt;
    private final String label;

    // Construtor package-private: só o Originator pode criar Mementos
    LogSystemMemento(LogLevel minimumLevel,
                     Set<String> activeDestinations,
                     Set<String> activeFilters,
                     String messageFormat,
                     boolean timestampEnabled,
                     String label) {
        this.minimumLevel       = minimumLevel;
        this.activeDestinations = Set.copyOf(activeDestinations);
        this.activeFilters      = Set.copyOf(activeFilters);
        this.messageFormat      = messageFormat;
        this.timestampEnabled   = timestampEnabled;
        this.savedAt            = LocalDateTime.now();
        this.label              = label;
    }

    // Package-private getters: acessíveis ao Originator, não ao exterior
    LogLevel getMinimumLevel()           { return minimumLevel; }
    Set<String> getActiveDestinations()  { return activeDestinations; }
    Set<String> getActiveFilters()       { return activeFilters; }
    String getMessageFormat()            { return messageFormat; }
    boolean isTimestampEnabled()         { return timestampEnabled; }

    // Público: apenas metadados descritivos
    public LocalDateTime getSavedAt() { return savedAt; }
    public String getLabel()          { return label; }

    @Override
    public String toString() {
        return "Memento['" + label + "' @ " + savedAt + " | level=" + minimumLevel + "]";
    }
}
