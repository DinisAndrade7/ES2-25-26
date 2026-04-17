package logsystem.pool;

import logsystem.config.LogConfiguration;
import logsystem.model.LogRecord;

import java.util.concurrent.ConcurrentLinkedQueue;

public class FormatterPool {

    private static final int POOL_SIZE = 5;

    // Pool thread-safe de formatadores disponíveis
    private final ConcurrentLinkedQueue<MessageFormatter> available = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<MessageFormatter> inUse     = new ConcurrentLinkedQueue<>();

    // Singleton do pool
    private static volatile FormatterPool instance;

    private FormatterPool() {
        // Pré-popula o pool
        for (int i = 0; i < POOL_SIZE; i++) {
            available.add(new MessageFormatter("Formatter-" + (i + 1)));
        }
        System.out.println("[POOL] Inicializado com " + POOL_SIZE + " formatadores.");
    }

    public static FormatterPool getInstance() {
        if (instance == null) {
            synchronized (FormatterPool.class) {
                if (instance == null) {
                    instance = new FormatterPool();
                }
            }
        }
        return instance;
    }

   
    public MessageFormatter acquire() {
        MessageFormatter formatter = available.poll();
        if (formatter == null) {
            // Pool esgotado: cria temporariamente (evita bloqueio)
            formatter = new MessageFormatter("Formatter-temp-" + System.currentTimeMillis());
            System.out.println("[POOL] Pool esgotado — criado formatador temporário.");
        }
        inUse.add(formatter);
        System.out.println("[POOL] Adquirido: " + formatter.getId() +
                " | Disponíveis: " + available.size() + " | Em uso: " + inUse.size());
        return formatter;
    }

   
    public void release(MessageFormatter formatter) {
        inUse.remove(formatter);
        formatter.reset();
        available.add(formatter);
        System.out.println("[POOL] Devolvido: " + formatter.getId() +
                " | Disponíveis: " + available.size() + " | Em uso: " + inUse.size());
    }

    public int availableCount() { return available.size(); }
    public int inUseCount()     { return inUse.size(); }
}
