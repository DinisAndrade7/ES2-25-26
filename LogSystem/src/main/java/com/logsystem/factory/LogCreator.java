package com.logsystem.factory;

import com.logsystem.config.LogConfiguration;
import com.logsystem.config.LogLevel;

/**
 * M2 - Módulo de Criação de Registos de Log
 *
 * Padrão: FACTORY METHOD
 * Classe abstrata que define o contrato para criação de registos de log.
 * Cada subclasse concreta cria registos do seu tipo específico.
 * Permite adicionar novos tipos de registo sem modificar código existente.
 */
public abstract class LogCreator {

    protected final LogConfiguration config;

    protected LogCreator() {
        this.config = LogConfiguration.getInstance();
    }

    /**
     * Factory Method — a ser implementado pelas subclasses concretas.
     * Define como criar o LogRecord para cada tipo específico.
     */
    public abstract LogRecord createLog(String message, String category);

    /**
     * Retorna o nível de log associado a este criador.
     */
    public abstract LogLevel getLevel();

    /**
     * Método template: verifica configuração e delega criação ao Factory Method.
     * Garante que apenas logs com nível ativo são criados.
     */
    public LogRecord buildLog(String message, String category) {
        if (!config.isLevelActive(getLevel())) {
            return null; // nível inativo, não cria registo
        }
        return createLog(message, category);
    }
}
