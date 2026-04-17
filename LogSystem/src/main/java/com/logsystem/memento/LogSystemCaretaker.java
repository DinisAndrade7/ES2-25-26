package com.logsystem.memento;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ArrayList;
import java.util.List;

/**
 * M6 - Caretaker: gere o histórico de Mementos.
 *
 * Padrão: MEMENTO
 * Guarda e recupera Mementos sem nunca inspecionar o seu conteúdo interno.
 * Suporta undo/redo através de duas pilhas (histórico e futuro).
 */
public class LogSystemCaretaker {

    private final LogSystemOriginator originator;
    private final Deque<LogSystemMemento> history; // pilha undo
    private final Deque<LogSystemMemento> future;  // pilha redo

    public LogSystemCaretaker(LogSystemOriginator originator) {
        this.originator = originator;
        this.history    = new ArrayDeque<>();
        this.future     = new ArrayDeque<>();
    }

    /**
     * Guarda o estado atual com um rótulo descritivo.
     */
    public void checkpoint(String label) {
        history.push(originator.save(label));
        future.clear(); // novo checkpoint invalida redo
        System.out.println("[CARETAKER] Checkpoint criado: '" + label + "' (histórico: " + history.size() + ")");
    }

    /**
     * Desfaz a última alteração (undo).
     */
    public boolean undo() {
        if (history.isEmpty()) {
            System.out.println("[CARETAKER] Sem histórico para desfazer.");
            return false;
        }
        // Guarda estado atual para redo
        future.push(originator.save("redo-state"));
        // Restaura estado anterior
        LogSystemMemento previous = history.pop();
        originator.restore(previous);
        System.out.println("[CARETAKER] Undo aplicado → " + previous.getLabel());
        return true;
    }

    /**
     * Refaz a última operação desfeita (redo).
     */
    public boolean redo() {
        if (future.isEmpty()) {
            System.out.println("[CARETAKER] Sem estados para refazer.");
            return false;
        }
        history.push(originator.save("undo-state"));
        LogSystemMemento next = future.pop();
        originator.restore(next);
        System.out.println("[CARETAKER] Redo aplicado → " + next.getLabel());
        return true;
    }

    /**
     * Lista todos os checkpoints guardados.
     */
    public List<LogSystemMemento> getHistory() {
        return new ArrayList<>(history);
    }

    public int getHistorySize() {
        return history.size();
    }
}
