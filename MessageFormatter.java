package logsystem.pool;

import logsystem.config.LogConfiguration;
import logsystem.model.LogRecord;

/**
 * M5 - Objeto gerido pelo pool.
 * Formata mensagens de log aplicando o template da configuração global.
 *
 * É um objeto "caro" de criar em produção porque pode carregar
 * templates externos, compilar expressões regulares, etc.
 */
public class MessageFormatter {

    private final String id;
    private int usageCount = 0;
    private final LogConfiguration config;

    // Simulação de recurso "pesado" (em produção: regex compilada, template carregado, etc.)
    private String cachedTemplate;

    public MessageFormatter(String id) {
        this.id = id;
        this.config = LogConfiguration.getInstance();
        // Simula inicialização cara
        this.cachedTemplate = config.getMessageFormat();
    }

    /**
     * Formata um LogRecord aplicando o template atual.
     */
    public String format(LogRecord record) {
        usageCount++;
        // Usa o template em cache (evita re-leitura em cada uso)
        String result = cachedTemplate;
        result = result.replace("{timestamp}", config.isTimestampEnabled()
                ? record.getFormattedTimestamp() : "");
        result = result.replace("{level}",    record.getLevel().toString());
        result = result.replace("{category}", config.isCategoryEnabled()
                ? record.getCategory() : "");
        result = result.replace("{message}",  record.getMessage());
        return result;
    }

    /**
     * Reinicia o estado do formatador antes de devolver ao pool.
     */
    public void reset() {
        // Recarrega o template (pode ter mudado na configuração)
        this.cachedTemplate = config.getMessageFormat();
    }

    public String getId()      { return id; }
    public int getUsageCount() { return usageCount; }

    @Override
    public String toString() {
        return "MessageFormatter{id='" + id + "', uses=" + usageCount + "}";
    }
}
