package com.logsystem.decorator;

import com.logsystem.config.LogLevel;

/**
 * M7 - Builder fluente para encadear extensões via Decorator.
 *
 * Permite construir pipelines de extensões de forma legível:
 *   LogExtension pipeline = new ExtensionBuilder()
 *       .withAdminAlerts(LogLevel.ERROR, "admin@empresa.pt")
 *       .withMonitoring("http://grafana:3000")
 *       .withErrorPatternAnalysis(5)
 *       .build();
 */
public class ExtensionBuilder {

    private LogExtension current;

    public ExtensionBuilder() {
        this.current = new BaseLogExtension();
    }

    public ExtensionBuilder withAdminAlerts(LogLevel threshold, String adminContact) {
        current = new AdminAlertDecorator(current, threshold, adminContact);
        return this;
    }

    public ExtensionBuilder withMonitoring(String endpoint) {
        current = new MonitoringDecorator(current, endpoint);
        return this;
    }

    public ExtensionBuilder withErrorPatternAnalysis(int threshold) {
        current = new ErrorPatternAnalyzerDecorator(current, threshold);
        return this;
    }

    public ExtensionBuilder withTimestampEnricher() {
        current = new TimestampEnricherDecorator(current);
        return this;
    }

    public LogExtension build() {
        return current;
    }
}
