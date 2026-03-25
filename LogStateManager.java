package logsystem.memento;

/**
 * M6 - Fachada pública para o módulo Memento.
 * Expõe apenas o necessário ao resto do sistema.
 */
public class LogStateManager {

    private final LogSystemOriginator originator;
    private final LogStateCaretaker caretaker;

    // Singleton
    private static volatile LogStateManager instance;

    private LogStateManager() {
        this.originator = new LogSystemOriginator();
        this.caretaker  = new LogStateCaretaker(originator);
    }

    public static LogStateManager getInstance() {
        if (instance == null) {
            synchronized (LogStateManager.class) {
                if (instance == null) {
                    instance = new LogStateManager();
                }
            }
        }
        return instance;
    }

    public void backup(String label)  { caretaker.backup(label); }
    public void undo()                { caretaker.undo(); }
    public void listHistory()         { caretaker.listHistory(); }
    public boolean hasHistory()       { return caretaker.hasHistory(); }
}
