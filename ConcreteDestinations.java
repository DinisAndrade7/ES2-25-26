package logsystem.bridge;

import logsystem.model.LogRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * M3 - Implementações concretas de destinos de log.
 * Padrão: Bridge — ConcreteImplementors
 *
 * Cada destino pode ser adicionado/removido independentemente
 * sem alterar a abstração LogWriter nem os restantes destinos.
 */

// ── CONSOLA ───────────────────────────────────────────────────────────────────
class ConsoleDestination implements LogDestination {

    @Override
    public void write(LogRecord record, String formattedMessage) {
        // Cores ANSI por nível
        String color;
        switch (record.getLevel()) {
            case DEBUG:   color = "\u001B[36m"; break; // Ciano
            case INFO:    color = "\u001B[32m"; break; // Verde
            case WARNING: color = "\u001B[33m"; break; // Amarelo
            case ERROR:   color = "\u001B[31m"; break; // Vermelho
            default:      color = "\u001B[0m";
        }
        System.out.println(color + formattedMessage + "\u001B[0m");
    }

    @Override public void open()  { /* Consola sempre disponível */ }
    @Override public void close() { /* Nada a fechar */ }
    @Override public String getName() { return "CONSOLE"; }
}

// ── FICHEIRO ──────────────────────────────────────────────────────────────────
class FileDestination implements LogDestination {

    private final String filePath;
    private final List<String> buffer = new ArrayList<>();
    private boolean isOpen = false;

    FileDestination(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void write(LogRecord record, String formattedMessage) {
        if (isOpen) {
            buffer.add(formattedMessage);
            // Simulação: em produção usaria FileWriter
            System.out.println("[FICHEIRO → " + filePath + "] " + formattedMessage);
        }
    }

    @Override
    public void open() {
        isOpen = true;
        System.out.println("[FICHEIRO] Aberto: " + filePath);
    }

    @Override
    public void close() {
        System.out.println("[FICHEIRO] Fechado. Linhas escritas: " + buffer.size());
        isOpen = false;
    }

    @Override
    public String getName() { return "FILE:" + filePath; }

    public List<String> getBuffer() { return new ArrayList<>(buffer); }
}

// ── BASE DE DADOS ─────────────────────────────────────────────────────────────
class DatabaseDestination implements LogDestination {

    private final String connectionString;
    private boolean connected = false;
    private int recordsInserted = 0;

    DatabaseDestination(String connectionString) {
        this.connectionString = connectionString;
    }

    @Override
    public void write(LogRecord record, String formattedMessage) {
        if (connected) {
            recordsInserted++;
            // Simulação de INSERT
            System.out.println("[BD] INSERT log #" + recordsInserted +
                    " level=" + record.getLevel() +
                    " category=" + record.getCategory() +
                    " msg=" + record.getMessage());
        }
    }

    @Override
    public void open() {
        connected = true;
        System.out.println("[BD] Conectado: " + connectionString);
    }

    @Override
    public void close() {
        System.out.println("[BD] Desconectado. Registos inseridos: " + recordsInserted);
        connected = false;
    }

    @Override
    public String getName() { return "DATABASE"; }
}

// ── SERVIÇO REMOTO ────────────────────────────────────────────────────────────
class RemoteServiceDestination implements LogDestination {

    private final String endpointUrl;
    private boolean connected = false;

    RemoteServiceDestination(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    @Override
    public void write(LogRecord record, String formattedMessage) {
        if (connected) {
            // Simulação de HTTP POST
            System.out.println("[REMOTO → " + endpointUrl + "] POST: " + formattedMessage);
        }
    }

    @Override
    public void open() {
        connected = true;
        System.out.println("[REMOTO] Conectado a: " + endpointUrl);
    }

    @Override
    public void close() {
        System.out.println("[REMOTO] Ligação encerrada: " + endpointUrl);
        connected = false;
    }

    @Override
    public String getName() { return "REMOTE:" + endpointUrl; }
}
