package logsystem.bridge;

import logsystem.model.LogRecord;


public interface LogDestination {


    void write(LogRecord record, String formattedMessage);


    void open();


    void close();


    String getName();
}
