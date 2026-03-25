package logsystem.composite;

import logsystem.model.LogLevel;
import logsystem.model.LogRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * M4 - Nó composto: representa uma categoria de logs (ex.: AUTH, DB, NETWORK).
 * Padrão: Composite — Composite
 *
 * Pode conter LogEntry (folhas) ou outras LogCategory (sub-categorias).
 * A operação process() propaga-se automaticamente a todos os filhos.
 */
public class LogCategory implements LogComponent {

    private final String name;
    private final List<LogComponent> children = new ArrayList<>();

    public LogCategory(String name) {
        this.name = name;
    }

    @Override
    public void add(LogComponent component) {
        children.add(component);
    }

    @Override
    public void remove(LogComponent component) {
        children.remove(component);
    }

    @Override
    public List<LogComponent> getChildren() {
        return new ArrayList<>(children);
    }

    /**
     * Propaga process() a todos os filhos (folhas e sub-categorias).
     */
    @Override
    public void process() {
        System.out.println("\n── Categoria: " + name + " (" + children.size() + " registos) ──");
        for (LogComponent child : children) {
            child.process();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Recolhe todos os LogRecord desta categoria e das sub-categorias.
     */
    @Override
    public List<LogRecord> getAllRecords() {
        List<LogRecord> all = new ArrayList<>();
        for (LogComponent child : children) {
            all.addAll(child.getAllRecords());
        }
        return all;
    }

    /**
     * Filtra registos por nível nesta categoria.
     */
    public List<LogRecord> filterByLevel(LogLevel level) {
        List<LogRecord> filtered = new ArrayList<>();
        for (LogRecord r : getAllRecords()) {
            if (r.getLevel() == level) {
                filtered.add(r);
            }
        }
        return filtered;
    }

    public int size() {
        return children.size();
    }
}
