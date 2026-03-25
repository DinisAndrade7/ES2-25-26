package logsystem.bridge;

/**
 * M3 - Fábrica de destinos e escritores.
 * Ponto de acesso para criar combinações de LogWriter + LogDestination.
 */
public class DestinationFactory {

    private DestinationFactory() {}

    public static LogDestination createConsole() {
        return new ConsoleDestination();
    }

    public static LogDestination createFile(String path) {
        return new FileDestination(path);
    }

    public static LogDestination createDatabase(String connectionString) {
        return new DatabaseDestination(connectionString);
    }

    public static LogDestination createRemote(String url) {
        return new RemoteServiceDestination(url);
    }

    public static LogWriter createSimpleWriter(LogDestination destination) {
        return new SimpleLogWriter(destination);
    }

    public static LogWriter createApplicationWriter(LogDestination destination, String appName) {
        return new ApplicationLogWriter(destination, appName);
    }
}
