package com.logsystem.composite;

import com.logsystem.config.LogLevel;
import com.logsystem.factory.LogRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * M4 - Nó Composto do Composite
 *
 * Representa uma categoria/componente da aplicação (ex: AUTH, DATABASE, NETWORK).
 * Pode conter registos individuais (LogLeaf) e outras sub-categorias (LogCategory).
 * Permite filtragem e análise em grupo de forma uniforme.
 */
public class LogCategory implements LogComponent {

    private final String name;
    private final List<LogComponent> children;

    public LogCategory(String name) {
        this.name     = name;
        this.children = new ArrayList<>();
    }

    public void add(LogComponent component) {
        children.add(component);
    }

    public void remove(LogComponent component) {
        children.remove(component);
    }

    public List<LogComponent> getChildren() {
        return new ArrayList<>(children);
    }

    /**
     * Filtra registos desta categoria por nível.
     */
    public List<LogRecord> filterByLevel(LogLevel level) {
        List<LogRecord> result = new ArrayList<>();
        for (LogRecord r : getRecords()) {
            if (r.getLevel() == level) {
                result.add(r);
            }
        }
        return result;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void process() {
        System.out.println("[CATEGORY: " + name + "] — " + children.size() + " componente(s)");
        for (LogComponent child : children) {
            child.process();
        }
    }

    @Override
    public List<LogRecord> getRecords() {
        List<LogRecord> all = new ArrayList<>();
        for (LogComponent child : children) {
            all.addAll(child.getRecords());
        }
        return all;
    }

    @Override
    public void accept(LogVisitor visitor) {
        visitor.visitCategory(this);
        for (LogComponent child : children) {
            child.accept(visitor);
        }
    }

    @Override
    public String toString() {
        return "LogCategory{name='" + name + "', children=" + children.size() + "}";
    }
}
