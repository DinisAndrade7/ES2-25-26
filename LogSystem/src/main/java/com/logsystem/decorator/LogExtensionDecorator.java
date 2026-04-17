package com.logsystem.decorator;

import com.logsystem.factory.LogRecord;

/**
 * M7 - Decorador Abstrato Base
 *
 * Padrão: DECORATOR
 * Envolve outra extensão e delega o processamento, permitindo encadeamento de decoradores.
 * Subclasses adicionam comportamento antes/depois da delegação.
 */
public abstract class LogExtensionDecorator implements LogExtension {

    protected final LogExtension wrapped;
    protected boolean enabled;

    protected LogExtensionDecorator(LogExtension wrapped) {
        this.wrapped = wrapped;
        this.enabled = true;
    }

    @Override
    public void process(LogRecord record, String formattedMessage) {
        if (enabled) {
            // Comportamento adicional antes (definido na subclasse)
            before(record, formattedMessage);
            // Delega ao componente envolvido
            wrapped.process(record, formattedMessage);
            // Comportamento adicional depois (definido na subclasse)
            after(record, formattedMessage);
        } else {
            wrapped.process(record, formattedMessage);
        }
    }

    protected void before(LogRecord record, String formattedMessage) {}
    protected void after(LogRecord record, String formattedMessage)  {}

    @Override public boolean isEnabled()                  { return enabled; }
    @Override public void setEnabled(boolean enabled)     { this.enabled = enabled; }
}
