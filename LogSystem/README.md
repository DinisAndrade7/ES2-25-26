# Sistema de Logs — Java Design Patterns

## Estrutura do Projeto

```
src/main/java/com/logsystem/
├── config/
│   ├── LogLevel.java              # Enum de níveis de log
│   └── LogConfiguration.java     # M1 — Singleton
├── factory/
│   ├── LogRecord.java             # Produto da factory
│   ├── LogCreator.java            # M2 — Factory Method (abstrato)
│   ├── ConcreteLogCreators.java   # M2 — Criadores concretos
│   └── LogFactory.java            # M2 — Fachada da factory
├── destination/
│   ├── LogDestination.java        # M3 — Interface Bridge
│   ├── ConcreteDestinations.java  # M3 — Console, File, Database, Remote
│   └── DestinationFactory.java    # M3 — Fábrica de destinos
├── composite/
│   ├── LogComponent.java          # M4 — Interface Composite
│   ├── LogLeaf.java               # M4 — Folha (registo individual)
│   ├── LogCategory.java           # M4 — Nó composto (categoria)
│   └── LogVisitor.java            # M4 — Interface Visitor
├── pool/
│   ├── FormatterPool.java         # M5 — Object Pool
│   └── MessageFormatter.java      # M5 — Objeto gerido (Strategy)
├── memento/
│   ├── LogSystemMemento.java      # M6 — Memento (snapshot)
│   ├── LogSystemOriginator.java   # M6 — Originator
│   └── LogSystemCaretaker.java    # M6 — Caretaker (undo/redo)
├── decorator/
│   ├── LogExtension.java          # M7 — Interface Decorator
│   ├── LogExtensionDecorator.java # M7 — Decorador abstrato
│   ├── ConcreteExtensions.java    # M7 — Alertas, Monitorização, Análise
│   └── ExtensionBuilder.java      # M7 — Builder fluente de pipeline
├── LogManager.java                # Núcleo central (interliga todos os módulos)
└── demo/
    └── LogSystemDemo.java         # Demo completa de todos os módulos
```

## Padrões de Desenho Implementados

| Módulo | Padrão         | Classe(s) Principal(is)                          |
|--------|----------------|--------------------------------------------------|
| M1     | Singleton      | LogConfiguration                                 |
| M2     | Factory Method | LogCreator, ConcreteLogCreators, LogFactory      |
| M3     | Bridge         | LogDestination, ConcreteDestinations             |
| M4     | Composite      | LogComponent, LogLeaf, LogCategory               |
| M5     | Object Pool    | FormatterPool, MessageFormatter (+ Strategy)     |
| M6     | Memento        | LogSystemMemento, Originator, Caretaker          |
| M7     | Decorator      | LogExtension, LogExtensionDecorator, Extensions  |

## Como Compilar e Executar

```bash
# Compilar
mkdir out
find src -name "*.java" | xargs javac --enable-preview --release 21 -d out

# Executar a demo
java --enable-preview -cp out com.logsystem.demo.LogSystemDemo
```

## Fluxo de um Log (LogManager.log)

```
log(level, message, category)
   │
   ├─ M2 LogFactory.create()        → LogRecord
   ├─ M4 LogCategory.add(LogLeaf)   → estrutura em árvore
   ├─ M5 FormatterPool.acquire()    → MessageFormatter (reutilizado)
   ├─ M3 LogDestination.write()     → Console / File / DB / Remote
   └─ M7 LogExtension.process()    → Alert / Monitor / Analyze
```
