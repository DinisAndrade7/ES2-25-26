package logsystem.memento;

import logsystem.model.LogLevel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * M6 - Módulo de Armazenamento de Estado
 * Padrão: Memento — Memento
 *
 * Snapshot imutável do estado do sistema de logs.
 * Não expõe os detalhes internos da implementação ao exterior.
 */
public final class LogSystemMemento {

    // Estado capturado — imutável após criação
    private final LogLevel minimumLevel;
    private final String messageFormat;
    private final boolean timestampEnabled;
    private final boolean categoryEnabled;
    private final List<String> activeDestinations;
    private final LocalDateTime savedAt;
    private final String label;

    LogSystemMemento(LogLevel minimumLevel,
                     String messageFormat,
                     boolean timestampEnabled,
                     boolean categoryEnabled,
                     List<String> activeDestinations,
                     String label) {
        this.minimumLevel      = minimumLevel;
        this.messageFormat     = messageFormat;
        this.timestampEnabled  = timestampEnabled;
        this.categoryEnabled   = categoryEnabled;
        this.activeDestinations = Collections.unmodifiableList(new ArrayList<>(activeDestinations));
        this.savedAt           = LocalDateTime.now();
        this.label             = label;
    }

    // Apenas getters — sem setters (imutável)
    LogLevel getMinimumLevel()         { return minimumLevel; }
    String getMessageFormat()          { return messageFormat; }
    boolean isTimestampEnabled()       { return timestampEnabled; }
    boolean isCategoryEnabled()        { return categoryEnabled; }
    List<String> getActiveDestinations() { return activeDestinations; }

    public LocalDateTime getSavedAt() { return savedAt; }
    public String getLabel()          { return label; }

    @Override
    public String toString() {
        return "Memento['" + label + "' @ " + savedAt +
                " | level=" + minimumLevel +
                " | destinos=" + activeDestinations + "]";
    }
}
