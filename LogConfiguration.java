package logsystem.config;

import logsystem.model.LogLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * M1 - Módulo de Configuração Centralizada
 * Padrão: Singleton
 *
 * Garante um único ponto de acesso às configurações globais do sistema de logs.
 * Evita instâncias inconsistentes em toda a aplicação.
 */
public class LogConfiguration {

    // Instância única (Singleton)
    private static volatile LogConfiguration instance;

    // Configurações globais
    private LogLevel minimumLevel;
    private String messageFormat;
    private boolean timestampEnabled;
    private boolean categoryEnabled;
    private List<String> activeDestinations;

    /**
     * Construtor privado — impede instanciação externa.
     */
    private LogConfiguration() {
        // Valores por defeito
        this.minimumLevel = LogLevel.DEBUG;
        this.messageFormat = "[{timestamp}] [{level}] [{category}] {message}";
        this.timestampEnabled = true;
        this.categoryEnabled = true;
        this.activeDestinations = new ArrayList<>();
        this.activeDestinations.add("CONSOLE");
    }

    /**
     * Devolve a instância única da configuração (thread-safe com double-checked locking).
     */
    public static LogConfiguration getInstance() {
        if (instance == null) {
            synchronized (LogConfiguration.class) {
                if (instance == null) {
                    instance = new LogConfiguration();
                }
            }
        }
        return instance;
    }

    // ── Getters e Setters ──────────────────────────────────────────────────────

    public LogLevel getMinimumLevel() {
        return minimumLevel;
    }

    public void setMinimumLevel(LogLevel minimumLevel) {
        this.minimumLevel = minimumLevel;
    }

    public String getMessageFormat() {
        return messageFormat;
    }

    public void setMessageFormat(String messageFormat) {
        this.messageFormat = messageFormat;
    }

    public boolean isTimestampEnabled() {
        return timestampEnabled;
    }

    public void setTimestampEnabled(boolean timestampEnabled) {
        this.timestampEnabled = timestampEnabled;
    }

    public boolean isCategoryEnabled() {
        return categoryEnabled;
    }

    public void setCategoryEnabled(boolean categoryEnabled) {
        this.categoryEnabled = categoryEnabled;
    }

    public List<String> getActiveDestinations() {
        return new ArrayList<>(activeDestinations);
    }

    public void addDestination(String destination) {
        if (!activeDestinations.contains(destination)) {
            activeDestinations.add(destination);
        }
    }

    public void removeDestination(String destination) {
        activeDestinations.remove(destination);
    }

    /**
     * Verifica se um determinado nível está ativo segundo a configuração atual.
     */
    public boolean isLevelActive(LogLevel level) {
        return level.getPriority() >= minimumLevel.getPriority();
    }

    /**
     * Aplica um snapshot de configuração (usado pelo Memento).
     */
    public void applySnapshot(LogLevel level, String format, boolean timestamp,
                               boolean category, List<String> destinations) {
        this.minimumLevel = level;
        this.messageFormat = format;
        this.timestampEnabled = timestamp;
        this.categoryEnabled = category;
        this.activeDestinations = new ArrayList<>(destinations);
    }

    @Override
    public String toString() {
        return "LogConfiguration{" +
                "minimumLevel=" + minimumLevel +
                ", messageFormat='" + messageFormat + '\'' +
                ", timestampEnabled=" + timestampEnabled +
                ", categoryEnabled=" + categoryEnabled +
                ", activeDestinations=" + activeDestinations +
                '}';
    }
}
