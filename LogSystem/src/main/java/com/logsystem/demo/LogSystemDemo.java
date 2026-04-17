package com.logsystem.demo;

import com.logsystem.LogManager;
import com.logsystem.composite.LogCategory;
import com.logsystem.config.LogConfiguration;
import com.logsystem.config.LogLevel;
import com.logsystem.decorator.ExtensionBuilder;
import com.logsystem.decorator.LogExtension;
import com.logsystem.destination.DestinationFactory;
import com.logsystem.factory.LogRecord;

import java.util.List;

/**
 * Demonstração completa do sistema de logs.
 * Exercita todos os 7 módulos e os padrões de desenho implementados.
 */
public class LogSystemDemo {

    public static void main(String[] args) {

        separator("DEMO DO SISTEMA DE LOGS");

        // ══════════════════════════════════════════════════════════════
        // M1 — Singleton: configuração centralizada
        // ══════════════════════════════════════════════════════════════
        separator("M1 — Singleton: Configuração Centralizada");

        LogConfiguration config = LogConfiguration.getInstance();
        LogConfiguration config2 = LogConfiguration.getInstance();
        System.out.println("Mesma instância? " + (config == config2));  // true

        config.setMinimumLevel(LogLevel.DEBUG);
        config.setMessageFormat("[%level] [%timestamp] [%category] %message");
        System.out.println("Configuração: " + config);

        // ══════════════════════════════════════════════════════════════
        // Inicializar LogManager (núcleo central)
        // ══════════════════════════════════════════════════════════════
        LogManager manager = new LogManager();

        // ══════════════════════════════════════════════════════════════
        // M3 — Bridge: múltiplos destinos
        // ══════════════════════════════════════════════════════════════
        separator("M3 — Bridge: Destinos de Log");

        manager.addDestination(DestinationFactory.console());
        manager.addDestination(DestinationFactory.file("logs/sistema.log"));
        manager.addDestination(DestinationFactory.database("jdbc:h2:mem:logsdb"));
        manager.addDestination(DestinationFactory.remote("https://logs.empresa.pt/api/ingest"));

        // ══════════════════════════════════════════════════════════════
        // M6 — Memento: guardar estado inicial
        // ══════════════════════════════════════════════════════════════
        separator("M6 — Memento: Checkpoint inicial");

        manager.checkpoint("estado-inicial");

        // ══════════════════════════════════════════════════════════════
        // M7 — Decorator: pipeline de extensões
        // ══════════════════════════════════════════════════════════════
        separator("M7 — Decorator: Pipeline de Extensões");

        LogExtension pipeline = new ExtensionBuilder()
                .withTimestampEnricher()
                .withMonitoring("http://grafana:3000/metrics")
                .withAdminAlerts(LogLevel.ERROR, "admin@empresa.pt")
                .withErrorPatternAnalysis(3)
                .build();

        manager.setExtensionPipeline(pipeline);

        // ══════════════════════════════════════════════════════════════
        // M2 — Factory Method: criação de registos + M4 Composite
        // ══════════════════════════════════════════════════════════════
        separator("M2 + M4 — Factory Method + Composite: Registar logs");

        System.out.println("\n--- Categoria: AUTENTICAÇÃO ---");
        manager.info("Utilizador 'alice' autenticado com sucesso", "AUTH");
        manager.warning("Tentativa de login falhada para utilizador 'bob'", "AUTH");
        manager.error("Token JWT expirado para sessão #4521", "AUTH");

        System.out.println("\n--- Categoria: BASE DE DADOS ---");
        manager.debug("Query executada: SELECT * FROM users WHERE id=1", "DATABASE");
        manager.info("Ligação ao pool de BD estabelecida (5/10 ligações ativas)", "DATABASE");
        manager.error("Timeout na ligação à base de dados após 30s", "DATABASE");

        System.out.println("\n--- Categoria: REDE ---");
        manager.info("Pedido HTTP GET /api/users [200 OK] em 45ms", "NETWORK");
        manager.warning("Latência elevada detetada: 1850ms para POST /api/orders", "NETWORK");
        manager.error("Serviço externo de pagamento inacessível", "NETWORK");

        System.out.println("\n--- Categoria: INTERFACE ---");
        manager.debug("Componente Dashboard renderizado em 12ms", "UI");
        manager.info("Utilizador navegou para /dashboard/analytics", "UI");

        // ══════════════════════════════════════════════════════════════
        // M4 — Composite: análise por categoria
        // ══════════════════════════════════════════════════════════════
        separator("M4 — Composite: Análise por Categoria");

        LogCategory authCategory = manager.getOrCreateCategory("AUTH");
        System.out.println("Total de logs AUTH: " + authCategory.getRecords().size());

        List<LogRecord> authErrors = authCategory.filterByLevel(LogLevel.ERROR);
        System.out.println("Erros em AUTH: " + authErrors.size());
        authErrors.forEach(r -> System.out.println("  → " + r));

        LogCategory dbCategory = manager.getOrCreateCategory("DATABASE");
        System.out.println("\nTotal de logs DATABASE: " + dbCategory.getRecords().size());

        // Processa toda a categoria de rede de forma uniforme
        System.out.println("\nProcessar categoria NETWORK:");
        manager.getOrCreateCategory("NETWORK").process();

        // ══════════════════════════════════════════════════════════════
        // M6 — Memento: alterar configuração e guardar novo estado
        // ══════════════════════════════════════════════════════════════
        separator("M6 — Memento: Alterar estado e fazer Undo/Redo");

        System.out.println("Nível atual: " + config.getMinimumLevel());
        manager.checkpoint("antes-de-alterar-nivel");

        config.setMinimumLevel(LogLevel.WARNING);
        System.out.println("Nível após alteração: " + config.getMinimumLevel());

        // Este debug não será registado (abaixo de WARNING)
        manager.debug("Este debug NÃO deve aparecer", "DATABASE");
        manager.warning("Este warning DEVE aparecer", "DATABASE");

        // Undo: volta ao nível DEBUG
        System.out.println("\n-- UNDO --");
        manager.undo();
        System.out.println("Nível após undo: " + config.getMinimumLevel());
        manager.debug("Este debug VOLTA a aparecer", "DATABASE");

        // Redo: volta a WARNING
        System.out.println("\n-- REDO --");
        manager.redo();
        System.out.println("Nível após redo: " + config.getMinimumLevel());

        // ══════════════════════════════════════════════════════════════
        // M5 — Object Pool: relatório de reutilização
        // ══════════════════════════════════════════════════════════════
        separator("M5 — Object Pool: Estatísticas de Formatadores");

        // As estatísticas refletem o uso durante toda a demo
        manager.printStatus();

        // ══════════════════════════════════════════════════════════════
        // M7 — Decorator: testar análise de padrões de erro
        // (3 erros consecutivos disparam o analisador)
        // ══════════════════════════════════════════════════════════════
        separator("M7 — Decorator: Análise de Padrões de Erro");

        config.setMinimumLevel(LogLevel.DEBUG); // repõe nível
        manager.error("Erro crítico #1 no módulo de pagamento", "PAYMENT");
        manager.error("Erro crítico #2 no módulo de pagamento", "PAYMENT");
        manager.error("Erro crítico #3 — DISPARA ALERTA DE PADRÃO", "PAYMENT");

        // ══════════════════════════════════════════════════════════════
        // Encerramento
        // ══════════════════════════════════════════════════════════════
        separator("Encerramento");
        manager.shutdown();

        System.out.println("\nDemo concluída. Todos os módulos foram exercitados.\n");
    }

    private static void separator(String title) {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.printf( "║  %-52s║%n", title);
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }
}
