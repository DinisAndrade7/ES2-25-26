package com.logsystem.composite;

/**
 * Interface Visitor para processamento externo de componentes de log.
 */
public interface LogVisitor {
    void visitLeaf(LogLeaf leaf);
    void visitCategory(LogCategory category);
}
