package logsystem;

import logsystem.config.LogConfiguration;
import logsystem.factory.LogRecordProducer;
import logsystem.bridge.*;
import logsystem.composite.*;
import logsystem.pool.FormatterPool;
import logsystem.pool.MessageFormatter;
import logsystem.memento.LogStateManager;
import logsystem.decorator.*;
import logsystem.model.LogLevel;

/**
 * Demonstração completa do Sistema de Logs com 7 padrões de desenho.
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║     SISTEMA DE LOGS COM PADRÕES DE DESENHO DE SOFTWARE       ║");
        System.out.println("║                                                              ║");
        System.out.println("║ M1: Singleton (LogConfiguration)                             ║");
        System.out.println("║ M2: Factory (LogRecordProducer)                              ║");
        System.out.println("║ M3: Bridge (LogWriter + LogDestination)                      ║");
        System.out.println("║ M4: Composite (LogCategory + LogEntry)                       ║");
        System.out.println("║ M5: Object Pool (FormatterPool)                              ║");
        System.out.println("║ M6: Memento (LogStateManager)                                ║");
        System.out.println("║ M7: Decorator (LogExtension)                                 ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
        
        // M1: Singleton - Configuração Centralizada
        demonstrateSingleton();
        
        // M2: Factory - Criação de Registos
        demonstrateFactory();
        
        // M3: Bridge - Abstração de Destinos
        demonstrateBridge();
        
        // M4: Composite - Estruturação de Registos
        demonstrateComposite();
        
        // M5: Object Pool - Otimização de Recursos
        demonstrateObjectPool();
        
        // M6: Memento - Armazenamento de Estado
        demonstrateMemento();
        
        // M7: Decorator - Extensão de Funcionalidades
        demonstrateDecorator();
    }
    
    private static void demonstrateSingleton() {
        System.out.println("\n========== M1: SINGLETON (LogConfiguration) ==========");
        System.out.println();
        LogConfiguration config1 = LogConfiguration.getInstance();
        LogConfiguration config2 = LogConfiguration.getInstance();
        
        System.out.println("Instância 1 == Instância 2? " + (config1 == config2));
        System.out.println("Configuração: " + config1);
        
        config1.setMinimumLevel(LogLevel.INFO);
        System.out.println("Após mudar config1: " + config2.getMinimumLevel());
    }
    
    private static void demonstrateFactory() {
        System.out.println("\n========== M2: FACTORY (LogRecordProducer) ==========");
        System.out.println();
        var infoLog = LogRecordProducer.create(LogLevel.INFO, \n            "Aplicação iniciada com sucesso", "SYSTEM", "Main");
        var warningLog = LogRecordProducer.create(LogLevel.WARNING, \n            "Memória acima de 80%", "MEMORY", "Main");
        var errorLog = LogRecordProducer.create(LogLevel.ERROR, \n            "Falha ao conectar à base de dados", "DATABASE", "Main");
        
        System.out.println("INFO: " + infoLog);
        System.out.println("WARNING: " + warningLog);
        System.out.println("ERROR: " + errorLog);
    }
    
    private static void demonstrateBridge() {
        System.out.println("\n========== M3: BRIDGE (LogWriter + LogDestination) ==========");
        System.out.println();
        
        // Cria destinos
        LogDestination console = DestinationFactory.createConsole();
        LogDestination file = DestinationFactory.createFile("logs.txt");
        LogDestination database = DestinationFactory.createDatabase("jdbc:mysql://localhost");
        
        // Cria escritores
        LogWriter consoleWriter = DestinationFactory.createSimpleWriter(console);
        LogWriter fileWriter = DestinationFactory.createApplicationWriter(file, "MyApp");
        LogWriter dbWriter = DestinationFactory.createSimpleWriter(database);
        
        // Escreve logs em diferentes destinos
        var record = LogRecordProducer.create(LogLevel.INFO, \n            "Teste de Bridge", "NETWORK", "Main");
        
        consoleWriter.write(record);
        fileWriter.write(record);
        dbWriter.write(record);
        
        // Demonstra mudança dinâmica de destino
        System.out.println("\n[BRIDGE] Mudando destino em tempo de execução...");
        consoleWriter.setDestination(file);
        consoleWriter.write(record);
    }
    
    private static void demonstrateComposite() {
        System.out.println("\n========== M4: COMPOSITE (LogCategory + LogEntry) ==========");
        System.out.println();
        
        LogConfiguration config = LogConfiguration.getInstance();
        LogWriter writer = DestinationFactory.createSimpleWriter(
            DestinationFactory.createConsole()
        );
        
        // Cria estrutura hierárquica
        LogCategory root = new LogCategory("ROOT");
        
        LogCategory auth = new LogCategory("AUTHENTICATION");
        LogCategory database = new LogCategory("DATABASE");
        LogCategory network = new LogCategory("NETWORK");
        
        root.add(auth);
        root.add(database);
        root.add(network);
        
        // Adiciona logs às categorias
        auth.add(new LogEntry(
            LogRecordProducer.create(LogLevel.INFO, "User login successful", "AUTH", "Main"),
            writer
        ));
        auth.add(new LogEntry(
            LogRecordProducer.create(LogLevel.ERROR, "Failed login attempt", "AUTH", "Main"),
            writer
        ));
        
        database.add(new LogEntry(
            LogRecordProducer.create(LogLevel.INFO, "Query executed", "DB", "Main"),
            writer
        ));
        
        network.add(new LogEntry(
            LogRecordProducer.create(LogLevel.WARNING, "Slow response time", "NET", "Main"),
            writer
        ));
        
        // Processa toda a árvore
        root.process();
        
        // Filtragem
        System.out.println("\n[COMPOSITE] Logs de AUTH com nível ERROR:");
        auth.filterByLevel(LogLevel.ERROR).forEach(System.out::println);
    }
    
    private static void demonstrateObjectPool() {
        System.out.println("\n========== M5: OBJECT POOL (FormatterPool) ==========");
        System.out.println();
        
        FormatterPool pool = FormatterPool.getInstance();
        
        // Adquire formatadores
        MessageFormatter fmt1 = pool.acquire();
        MessageFormatter fmt2 = pool.acquire();
        MessageFormatter fmt3 = pool.acquire();
        
        var record = LogRecordProducer.create(LogLevel.INFO, \n            "Teste de Pool", "SYSTEM", "Main");
        
        // Usa formatadores
        System.out.println("Fmt1: " + fmt1.format(record));
        System.out.println("Fmt2: " + fmt2.format(record));
        System.out.println("Fmt3: " + fmt3.format(record));
        
        // Devolve ao pool
        pool.release(fmt1);
        pool.release(fmt2);
        pool.release(fmt3);
        
        System.out.println("\nFormatadores disponíveis: " + pool.availableCount());
        System.out.println("Formatadores em uso: " + pool.inUseCount());
    }
    
    private static void demonstrateMemento() {
        System.out.println("\n========== M6: MEMENTO (LogStateManager) ==========");
        System.out.println();
        
        LogStateManager stateManager = LogStateManager.getInstance();
        LogConfiguration config = LogConfiguration.getInstance();
        
        // Estado 1
        config.setMinimumLevel(LogLevel.DEBUG);
        config.addDestination("CONSOLE");
        stateManager.backup("Estado Inicial");
        System.out.println("Backup 1: " + config.getMinimumLevel() + " | Destinos: " + config.getActiveDestinations());
        
        // Muda configuração
        config.setMinimumLevel(LogLevel.WARNING);
        config.addDestination("FILE");
        stateManager.backup("Após mudanças");
        System.out.println("Backup 2: " + config.getMinimumLevel() + " | Destinos: " + config.getActiveDestinations());
        
        // Mais mudanças
        config.setMinimumLevel(LogLevel.ERROR);
        System.out.println("Config agora: " + config.getMinimumLevel());
        
        // Restaura
        stateManager.undo();
        System.out.println("Após undo: " + config.getMinimumLevel());
        
        stateManager.listHistory();
    }
    
    private static void demonstrateDecorator() {
        System.out.println("\n========== M7: DECORATOR (LogExtension) ==========");
        System.out.println();
        
        // Cria extensões
        AdminAlertDecorator adminAlert = new AdminAlertDecorator(
            "admin@example.com", LogLevel.ERROR
        );
        
        MonitoringDecorator monitoring = new MonitoringDecorator(
            "http://prometheus:9090"
        );
        
        ErrorPatternAnalysisDecorator patternAnalysis = new ErrorPatternAnalysisDecorator(3);
        
        ThresholdCrossedDecorator threshold = new ThresholdCrossedDecorator(10);
        
        System.out.println("Extensões registadas:");
        System.out.println("1. " + adminAlert.getName());
        System.out.println("2. " + monitoring.getName());
        System.out.println("3. " + patternAnalysis.getName());
        System.out.println("4. " + threshold.getName());
        
        // Processa logs com as extensões
        System.out.println("\n[DECORATOR] Processando logs com todas as extensões:");
        
        var infoLog = LogRecordProducer.create(LogLevel.INFO, "Sistema operacional", "SYSTEM", "Main");
        var errorLog1 = LogRecordProducer.create(LogLevel.ERROR, "Falha crítica na conexão", "DATABASE", "Main");
        var errorLog2 = LogRecordProducer.create(LogLevel.ERROR, "Falha crítica na conexão", "DATABASE", "Main");
        var errorLog3 = LogRecordProducer.create(LogLevel.ERROR, "Falha crítica na conexão", "DATABASE", "Main");
        
        procesarWithExtensions(infoLog, adminAlert, monitoring, patternAnalysis, threshold);
        procesarWithExtensions(errorLog1, adminAlert, monitoring, patternAnalysis, threshold);
        procesarWithExtensions(errorLog2, adminAlert, monitoring, patternAnalysis, threshold);
        procesarWithExtensions(errorLog3, adminAlert, monitoring, patternAnalysis, threshold);
        
        // Mostrar estatísticas
        monitoring.printStats();
        patternAnalysis.printPatternAnalysis();
        threshold.printThresholdStats();
    }
    
    private static void procesarWithExtensions(var record, LogExtension... extensions) {
        String formatted = "[" + record.getLevel() + "] " + record.getMessage();
        for (LogExtension ext : extensions) {
            ext.process(record, formatted);
        }
    }
}