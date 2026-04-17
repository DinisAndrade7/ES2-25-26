package com.logsystem;

import com.logsystem.composite.LogCategory;
import com.logsystem.composite.LogLeaf;
import com.logsystem.config.LogConfiguration;
import com.logsystem.config.LogLevel;
import com.logsystem.decorator.LogExtension;
import com.logsystem.destination.LogDestination;
import com.logsystem.factory.LogFactory;
import com.logsystem.factory.LogRecord;
import com.logsystem.memento.LogSystemCaretaker;
import com.logsystem.memento.LogSystemOriginator;
import com.logsystem.pool.FormatterPool;
import com.logsystem.pool.MessageFormatter;

import java.util.*;

/**
 * LogManager — núcleo central do sistema de logs.
 *
 * Interliga todos os módulos:
 *   M1  LogConfiguration (Singleton)        → configuração global
 *   M2  LogFactory (Factory Method)          → criação de registos
 *   M3  LogDestination (Bridge/Strategy)     → destinos de saída
 *   M4  LogCategory / LogLeaf (Composite)    → estrutura em árvore
 *   M5  FormatterPool (Object Pool)          → reutilização de formatadores
 *   M6  Memento / Caretaker                  → guardar/restaurar estado
 *   M7  LogExtension (Decorator)             → extensões dinâmicas
 */
public class LogManager {

    // M1 — configuração singleton
    private final LogConfiguration config;

    // M3 — destinos registados
    private final Map<String, LogDestination> destinations;

    // M4 — categorias de log (árvore Composite)
    private final Map<String, LogCategory> categories;

    // M5 — pool de formatadores
    private final FormatterPool formatterPool;

    // M6 — Memento: guardar/restaurar estado
    private final LogSystemOriginator originator;
    private final LogSystemCaretaker  caretaker;

    // M7 — extensões decoradoras
    private LogExtension extensionPipeline;
    private boolean extensionsEnabled;

    // ─── Construtor ──────────────────────────────────────────────────────────

    public LogManager() {
        this.config           = LogConfiguration.getInstance();
        this.destinations     = new LinkedHashMap<>();
        this.categories       = new LinkedHashMap<>();
        this.formatterPool    = new FormatterPool(5);
        this.originator       = new LogSystemOriginator();
        this.caretaker        = new LogSystemCaretaker(originator);
        this.extensionsEnabled = false;

        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║       LogManager inicializado        ║");
        System.out.println("╚══════════════════════════════════════╝\n");
    }

    // ─── M3: Gestão de destinos ───────────────────────────────────────────────

    public void addDestination(LogDestination destination) {
        destinations.put(destination.getName(), destination);
        config.addDestination(destination.getName());
        System.out.println("[MANAGER] Destino registado: " + destination.getName());
    }

    public void removeDestination(String name) {
        destinations.remove(name);
        config.removeDestination(name);
    }

    public Collection<LogDestination> getDestinations() {
        return Collections.unmodifiableCollection(destinations.values());
    }

    // ─── M4: Gestão de categorias ─────────────────────────────────────────────

    public LogCategory getOrCreateCategory(String name) {
        return categories.computeIfAbsent(name, LogCategory::new);
    }

    public Map<String, LogCategory> getCategories() {
        return Collections.unmodifiableMap(categories);
    }

    // ─── M7: Extensões ────────────────────────────────────────────────────────

    public void setExtensionPipeline(LogExtension pipeline) {
        this.extensionPipeline = pipeline;
        this.extensionsEnabled = true;
        System.out.println("[MANAGER] Pipeline de extensões configurado: " + pipeline.getName());
    }

    public void disableExtensions() {
        this.extensionsEnabled = false;
    }

    // ─── M6: Memento ─────────────────────────────────────────────────────────

    public void checkpoint(String label) {
        caretaker.checkpoint(label);
    }

    public boolean undo() {
        return caretaker.undo();
    }

    public boolean redo() {
        return caretaker.redo();
    }

    public List<com.logsystem.memento.LogSystemMemento> getHistory() {
        return caretaker.getHistory();
    }

    // ─── Método principal de log ──────────────────────────────────────────────

    /**
     * Regista uma mensagem de log numa categoria, emitindo para todos os destinos
     * e passando pelo pipeline de extensões.
     *
     * Fluxo:
     *   1. M2  Cria o LogRecord via LogFactory
     *   2. M4  Adiciona o registo à categoria (Composite)
     *   3. M5  Obtém formatador do pool e formata a mensagem
     *   4. M3  Emite para todos os destinos registados
     *   5. M7  Passa pelo pipeline de extensões (Decorator)
     */
    public void log(LogLevel level, String message, String categoryName) {
        // 1. M2 — Factory Method: criar registo
        LogRecord record = LogFactory.create(level, message, categoryName);
        if (record == null) return; // nível inativo

        // 2. M4 — Composite: adicionar à categoria
        LogCategory category = getOrCreateCategory(categoryName);
        category.add(new LogLeaf(record));

        // 3. M5 — Object Pool: formatar mensagem
        MessageFormatter formatter = formatterPool.acquire();
        String formatted;
        try {
            formatted = formatter.format(record);
        } finally {
            formatterPool.release(formatter);
        }

        // 4. M3 — Bridge: emitir para cada destino disponível
        for (LogDestination dest : destinations.values()) {
            if (dest.isAvailable()) {
                dest.write(record, formatted);
            }
        }

        // 5. M7 — Decorator: processar extensões
        if (extensionsEnabled && extensionPipeline != null) {
            extensionPipeline.process(record, formatted);
        }
    }

    // ─── Métodos de conveniência ──────────────────────────────────────────────

    public void debug(String message, String category)   { log(LogLevel.DEBUG,   message, category); }
    public void info(String message, String category)    { log(LogLevel.INFO,    message, category); }
    public void warning(String message, String category) { log(LogLevel.WARNING, message, category); }
    public void error(String message, String category)   { log(LogLevel.ERROR,   message, category); }

    // ─── Relatório do estado ──────────────────────────────────────────────────

    public void printStatus() {
        System.out.println("\n─── Estado do LogManager ─────────────────────────");
        System.out.println("  Config      : " + config);
        System.out.println("  Destinos    : " + destinations.keySet());
        System.out.println("  Categorias  : " + categories.keySet());
        System.out.println("  Pool        : " + formatterPool.getAvailableCount() +
                " disponíveis / " + formatterPool.getTotalCreated() + " criados");
        System.out.println("  Histórico   : " + caretaker.getHistorySize() + " checkpoint(s)");
        System.out.println("  Extensões   : " + (extensionsEnabled ? "ATIVAS" : "INATIVAS"));
        System.out.println("──────────────────────────────────────────────────\n");
    }

    /**
     * Liberta todos os recursos (destinos abertos).
     */
    public void shutdown() {
        System.out.println("\n[MANAGER] A encerrar LogManager...");
        destinations.values().forEach(LogDestination::close);
        destinations.clear();
        System.out.println("[MANAGER] LogManager encerrado.");
    }
}
