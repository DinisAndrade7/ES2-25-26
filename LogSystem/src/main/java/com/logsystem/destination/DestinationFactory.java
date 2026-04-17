package com.logsystem.destination;

/**
 * M3 - Fábrica de Destinos de Log
 *
 * Cria instâncias concretas de LogDestination com base no tipo pedido.
 * Encapsula a lógica de instanciação — o cliente só conhece a interface.
 */
public class DestinationFactory {

    private DestinationFactory() {}

    public static LogDestination create(String type, String param) {
        return switch (type.toUpperCase()) {
            case "CONSOLE"   -> new ConsoleDestination();
            case "FILE"      -> new FileDestination(param);
            case "DATABASE"  -> new DatabaseDestination(param);
            case "REMOTE"    -> new RemoteServiceDestination(param);
            default -> throw new IllegalArgumentException("Destino desconhecido: " + type);
        };
    }

    public static LogDestination console() {
        return new ConsoleDestination();
    }

    public static LogDestination file(String path) {
        return new FileDestination(path);
    }

    public static LogDestination database(String connectionString) {
        return new DatabaseDestination(connectionString);
    }

    public static LogDestination remote(String endpoint) {
        return new RemoteServiceDestination(endpoint);
    }
}
