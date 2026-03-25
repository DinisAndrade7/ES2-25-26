package logsystem.memento;

import logsystem.config.LogConfiguration;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * M6 - Originator: sabe criar e restaurar snapshots da LogConfiguration.
 * Padrão: Memento — Originator
 */
public class LogSystemOriginator {

    private final LogConfiguration config;

    public LogSystemOriginator() {
        this.config = LogConfiguration.getInstance();
    }

    /**
     * Cria e devolve um Memento com o estado atual da configuração.
     */
    public LogSystemMemento save(String label) {
        LogSystemMemento memento = new LogSystemMemento(
                config.getMinimumLevel(),
                config.getMessageFormat(),
                config.isTimestampEnabled(),
                config.isCategoryEnabled(),
                config.getActiveDestinations(),
                label
        );
        System.out.println("[MEMENTO] Estado guardado: " + memento);
        return memento;
    }

    /**
     * Restaura a configuração a partir de um Memento.
     */
    public void restore(LogSystemMemento memento) {
        config.applySnapshot(
                memento.getMinimumLevel(),
                memento.getMessageFormat(),
                memento.isTimestampEnabled(),
                memento.isCategoryEnabled(),
                memento.getActiveDestinations()
        );
        System.out.println("[MEMENTO] Estado restaurado: " + memento);
    }
}

// ─────────────────────────────────────────────────────────────────────────────

/**
 * M6 - Caretaker: guarda o histórico de Mementos sem aceder ao seu conteúdo.
 * Padrão: Memento — Caretaker
 */
class LogStateCaretaker {

    private final Deque<LogSystemMemento> history = new ArrayDeque<>();
    private final LogSystemOriginator originator;

    public LogStateCaretaker(LogSystemOriginator originator) {
        this.originator = originator;
    }

    /**
     * Guarda o estado atual com uma etiqueta descritiva.
     */
    public void backup(String label) {
        history.push(originator.save(label));
    }

    /**
     * Desfaz a última alteração (undo).
     */
    public void undo() {
        if (history.isEmpty()) {
            System.out.println("[MEMENTO] Sem estados anteriores para restaurar.");
            return;
        }
        LogSystemMemento memento = history.pop();
        originator.restore(memento);
    }

    /**
     * Lista todos os snapshots guardados.
     */
    public void listHistory() {
        System.out.println("[MEMENTO] Histórico (" + history.size() + " estados):");
        List<LogSystemMemento> list = new ArrayList<>(history);
        for (int i = 0; i < list.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + list.get(i));
        }
    }

    public boolean hasHistory() {
        return !history.isEmpty();
    }
}
