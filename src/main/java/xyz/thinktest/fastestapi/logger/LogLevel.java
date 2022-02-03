package xyz.thinktest.fastestapi.logger;

import xyz.thinktest.fastestapi.utils.color.ColorPrint;

enum LogLevel {
    TRACE(1, "TRACE") {
        @Override
        protected void println(String msg) {
            ColorPrint.WHITE.println(msg);
        }
    },
    DEBUG(2, "DEBUG") {
        @Override
        protected void println(String msg) {
            ColorPrint.CYAN.println(msg);
        }
    },
    INFO(3, "INFO"){
        @Override
        protected void println(String msg) {
            ColorPrint.GREEN.println(msg);
        }
    },
    WARN(4, "WARN") {
        @Override
        protected void println(String msg) {
            ColorPrint.YELLOW.println(msg);
        }
    },
    ERROR(5, "ERROR") {
        @Override
        protected void println(String msg) {
            ColorPrint.RED.println(msg);
        }
    };
    public final int id;
    public final String type;

    LogLevel(int id, String type) {
        this.id = id;
        this.type = type;
    }

    protected abstract void println(String msg);
}
