package logsystem.pool;

import logsystem.config.LogConfiguration;
import logsystem.model.LogRecord;



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
