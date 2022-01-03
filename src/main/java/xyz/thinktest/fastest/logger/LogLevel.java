package xyz.thinktest.fastest.logger;

enum LogLevel {
    INFO(3, "INFO"),
    DEBUG(2, "DEBUG"),
    ERROR(5, "ERROR"),
    TRACE(1, "TRACE"),
    WARN(4, "WARN");
    public final int id;
    public final String type;

    LogLevel(int id, String type) {
        this.id = id;
        this.type = type;
    }
}
