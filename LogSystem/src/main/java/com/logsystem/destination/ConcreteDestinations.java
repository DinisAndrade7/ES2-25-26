package com.logsystem.destination;

import com.logsystem.factory.LogRecord;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * M3 - Implementações concretas dos destinos de log.
 *
 * Cada classe implementa LogDestination de forma independente.
 * O restante sistema depende apenas da interface — nunca das classes concretas.
 */

// ─── CONSOLA ─────────────────────────────────────────────────────────────────
class ConsoleDestination implements LogDestination {

    @Override
    public void write(LogRecord record, String formattedMessage) {
        // Diferencia erros e avisos com saída de erro padrão
        switch (record.getLevel()) {
            case ERROR, WARNING -> System.err.println(formattedMessage);
            default             -> System.out.println(formattedMessage);
        }
    }

    @Override public String getName()      { return "CONSOLE"; }
    @Override public boolean isAvailable() { return true; }
    @Override public void close()          { /* Consola não necessita fechar */ }
}

// ─── FICHEIRO ─────────────────────────────────────────────────────────────────
class FileDestination implements LogDestination {

    private final String filePath;
    private PrintWriter writer;
    private boolean available;

    FileDestination(String filePath) {
        this.filePath = filePath;
        try {
            this.writer    = new PrintWriter(new FileWriter(filePath, true));
            this.available = true;
            System.out.println("[FILE_DEST] Ficheiro de log aberto: " + filePath);
        } catch (IOException e) {
            this.available = false;
            System.err.println("[FILE_DEST] Erro ao abrir ficheiro: " + e.getMessage());
        }
    }

    @Override
    public void write(LogRecord record, String formattedMessage) {
        if (available && writer != null) {
            writer.println(formattedMessage);
            writer.flush();
        }
    }

    @Override public String getName()      { return "FILE:" + filePath; }
    @Override public boolean isAvailable() { return available; }

    @Override
    public void close() {
        if (writer != null) {
            writer.close();
            available = false;
        }
    }
}

// ─── BASE DE DADOS (simulada) ─────────────────────────────────────────────────
class DatabaseDestination implements LogDestination {

    private final String connectionString;
    private boolean connected;
    private final List<String> buffer; // simula tabela de logs

    DatabaseDestination(String connectionString) {
        this.connectionString = connectionString;
        this.buffer           = new ArrayList<>();
        // Simula ligação à BD
        this.connected = true;
        System.out.println("[DB_DEST] Ligação simulada à BD: " + connectionString);
    }

    @Override
    public void write(LogRecord record, String formattedMessage) {
        if (connected) {
            // Simula INSERT INTO logs ...
            String entry = String.format("INSERT INTO logs (level, category, message, ts) VALUES ('%s','%s','%s','%s')",
                    record.getLevel(), record.getCategory(),
                    record.getMessage().replace("'", "''"),
                    record.getTimestamp());
            buffer.add(entry);
            System.out.println("[DB_DEST] " + entry);
        }
    }

    public List<String> getBuffer() {
        return Collections.unmodifiableList(buffer);
    }

    @Override public String getName()      { return "DATABASE:" + connectionString; }
    @Override public boolean isAvailable() { return connected; }

    @Override
    public void close() {
        connected = false;
        System.out.println("[DB_DEST] Ligação encerrada.");
    }
}

// ─── SERVIÇO REMOTO (simulado) ────────────────────────────────────────────────
class RemoteServiceDestination implements LogDestination {

    private final String endpoint;
    private boolean available;

    RemoteServiceDestination(String endpoint) {
        this.endpoint  = endpoint;
        this.available = true;
        System.out.println("[REMOTE_DEST] Destino remoto configurado: " + endpoint);
    }

    @Override
    public void write(LogRecord record, String formattedMessage) {
        if (available) {
            // Simula envio HTTP POST
            System.out.println("[REMOTE_DEST] POST " + endpoint + " → " + formattedMessage);
        }
    }

    @Override public String getName()      { return "REMOTE:" + endpoint; }
    @Override public boolean isAvailable() { return available; }
    @Override public void close()          { available = false; }
}
