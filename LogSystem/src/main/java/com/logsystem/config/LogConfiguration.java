package com.logsystem.config;

import java.util.HashSet;
import java.util.Set;

/**
 * M1 - Módulo de Configuração Centralizada
 *
 * Padrão: SINGLETON
 * Garante um único ponto de acesso às configurações globais do sistema de logs.
 * Evita a criação de múltiplas instâncias inconsistentes ao longo da aplicação.
 */
public class LogConfiguration {

    // --- Singleton: instância única e volátil para thread-safety ---
    private static volatile LogConfiguration instance;

    // Configurações globais
    private LogLevel minimumLevel;
    private String messageFormat;
    private Set<String> activeDestinations;
    private Set<String> activeFilters;
    private boolean timestampEnabled;
    private String dateFormat;

    /**
     * Construtor privado — impede instanciação externa.
     */
    private LogConfiguration() {
        // Valores por defeito
        this.minimumLevel = LogLevel.DEBUG;
        this.messageFormat = "[%level] [%timestamp] [%category] %message";
        this.activeDestinations = new HashSet<>();
        this.activeFilters = new HashSet<>();
        this.timestampEnabled = true;
        this.dateFormat = "yyyy-MM-dd HH:mm:ss";

        // Destinos por defeito
        this.activeDestinations.add("CONSOLE");
    }

    /**
     * Ponto de acesso único à instância (Double-Checked Locking).
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

    // --- Getters e Setters ---

    public LogLevel getMinimumLevel() {
        return minimumLevel;
    }

    public void setMinimumLevel(LogLevel minimumLevel) {
        this.minimumLevel = minimumLevel;
        System.out.println("[CONFIG] Nível mínimo alterado para: " + minimumLevel);
    }

    public String getMessageFormat() {
        return messageFormat;
    }

    public void setMessageFormat(String messageFormat) {
        this.messageFormat = messageFormat;
    }

    public Set<String> getActiveDestinations() {
        return new HashSet<>(activeDestinations);
    }

    public void addDestination(String destination) {
        this.activeDestinations.add(destination.toUpperCase());
        System.out.println("[CONFIG] Destino adicionado: " + destination);
    }

    public void removeDestination(String destination) {
        this.activeDestinations.remove(destination.toUpperCase());
    }

    public Set<String> getActiveFilters() {
        return new HashSet<>(activeFilters);
    }

    public void addFilter(String filter) {
        this.activeFilters.add(filter);
    }

    public void removeFilter(String filter) {
        this.activeFilters.remove(filter);
    }

    public boolean isTimestampEnabled() {
        return timestampEnabled;
    }

    public void setTimestampEnabled(boolean timestampEnabled) {
        this.timestampEnabled = timestampEnabled;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * Verifica se um nível de log está ativo com base no nível mínimo configurado.
     */
    public boolean isLevelActive(LogLevel level) {
        return level.getPriority() >= this.minimumLevel.getPriority();
    }

    @Override
    public String toString() {
        return "LogConfiguration{" +
                "minimumLevel=" + minimumLevel +
                ", messageFormat='" + messageFormat + '\'' +
                ", activeDestinations=" + activeDestinations +
                ", activeFilters=" + activeFilters +
                ", timestampEnabled=" + timestampEnabled +
                '}';
    }
}
