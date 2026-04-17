package com.logsystem.pool;

import com.logsystem.config.LogConfiguration;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * M5 - Módulo de Otimização de Recursos
 *
 * Padrão: OBJECT POOL
 * Reutiliza instâncias de MessageFormatter para evitar criação repetida de objetos pesados.
 * Útil quando a formatação envolve recursos (parsers, compiladores de padrões, etc.).
 */
public class FormatterPool {

    private final ConcurrentLinkedQueue<MessageFormatter> available;
    private final int maxSize;
    private int totalCreated;

    public FormatterPool(int maxSize) {
        this.maxSize      = maxSize;
        this.available    = new ConcurrentLinkedQueue<>();
        this.totalCreated = 0;
        // Pré-aquece o pool com alguns formatadores
        for (int i = 0; i < Math.min(2, maxSize); i++) {
            available.offer(createNew());
        }
        System.out.println("[POOL] FormatterPool inicializado com " + available.size() + " instâncias.");
    }

    /**
     * Obtém um formatador do pool (ou cria novo se não houver disponível e o limite não foi atingido).
     */
    public MessageFormatter acquire() {
        MessageFormatter formatter = available.poll();
        if (formatter == null) {
            if (totalCreated < maxSize) {
                formatter = createNew();
                System.out.println("[POOL] Nova instância criada (total: " + totalCreated + "/" + maxSize + ")");
            } else {
                // Pool esgotado: aguarda ou usa temporário
                System.out.println("[POOL] Pool esgotado — criando instância temporária.");
                return createNew();
            }
        }
        return formatter;
    }

    /**
     * Devolve um formatador ao pool para reutilização.
     */
    public void release(MessageFormatter formatter) {
        if (formatter != null && available.size() < maxSize) {
            formatter.reset();
            available.offer(formatter);
        }
    }

    private MessageFormatter createNew() {
        totalCreated++;
        return new MessageFormatter(LogConfiguration.getInstance().getMessageFormat());
    }

    public int getAvailableCount() {
        return available.size();
    }

    public int getTotalCreated() {
        return totalCreated;
    }
}
