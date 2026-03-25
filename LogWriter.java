package logsystem.bridge;

import logsystem.config.LogConfiguration;
import logsystem.model.LogRecord;

/**
 * M3 - Abstração do escritor de logs.
 * Padrão: Bridge — Abstraction
 *
 * Liga-se a um LogDestination (Implementor) e delega-lhe a escrita física.
 * Permite combinar qualquer abstração com qualquer destino em tempo de execução.
 */
public abstract class LogWriter {

    protected LogDestination destination;
    protected LogConfiguration config;

    protected LogWriter(LogDestination destination) {
        this.destination = destination;
        this.config = LogConfiguration.getInstance();
        this.destination.open();
    }

    /**
     * Formata e envia o registo para o destino.
     */
    public abstract void write(LogRecord record);

    /**
     * Formata a mensagem de acordo com as configurações globais.
     */
    protected String format(LogRecord record) {
        String fmt = config.getMessageFormat();
        fmt = fmt.replace("{timestamp}", config.isTimestampEnabled()
                ? record.getFormattedTimestamp() : "");
        fmt = fmt.replace("{level}",    record.getLevel().toString());
        fmt = fmt.replace("{category}", config.isCategoryEnabled()
                ? record.getCategory() : "");
        fmt = fmt.replace("{message}",  record.getMessage());
        return fmt;
    }

    public void close() {
        destination.close();
    }

    public LogDestination getDestination() {
        return destination;
    }

    /**
     * Permite trocar o destino em tempo de execução (Bridge).
     */
    public void setDestination(LogDestination newDestination) {
        this.destination.close();
        this.destination = newDestination;
        this.destination.open();
    }
}

// ── Escritor simples ──────────────────────────────────────────────────────────
class SimpleLogWriter extends LogWriter {

    public SimpleLogWriter(LogDestination destination) {
        super(destination);
    }

    @Override
    public void write(LogRecord record) {
        if (config.isLevelActive(record.getLevel())) {
            destination.write(record, format(record));
        }
    }
}

// ── Escritor com prefixo de aplicação ─────────────────────────────────────────
class ApplicationLogWriter extends LogWriter {

    private final String appName;

    public ApplicationLogWriter(LogDestination destination, String appName) {
        super(destination);
        this.appName = appName;
    }

    @Override
    public void write(LogRecord record) {
        if (config.isLevelActive(record.getLevel())) {
            String msg = "[" + appName + "] " + format(record);
            destination.write(record, msg);
        }
    }
}
