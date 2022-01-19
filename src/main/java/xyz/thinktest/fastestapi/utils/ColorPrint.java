package xyz.thinktest.fastestapi.utils;

import java.io.BufferedWriter;
import java.io.PrintStream;
import java.io.PrintWriter;

public enum ColorPrint {
    WHITE{
        @Override
        public void println(String msg) {
            out(msg, "255;255;255");
        }
    },
    CYAN {
        @Override
        public void println(String msg) {
            out(msg, "0;255;255");
        }
    },
    GREEN {
        @Override
        public void println(String msg) {
            out(msg, "0;255;127");
        }
    },
    YELLOW {
        @Override
        public void println(String msg) {
            out(msg, "255;236;139");
        }
    },
    RED {
        @Override
        public void println(String msg) {
            out(msg, "205;51;51");
        }
    };

    public abstract void println(String msg);

    protected String buildString(String msg, String colorCode){
        StringBuilder sb = new StringBuilder();
        sb.append("\033[38;2;").append(colorCode).append("m").append(msg).append("\033[0m");
        return sb.toString();
    }

    protected void out(String msg, String colorCode){
        PrintWriter print = new PrintWriter(System.out);
        print.append(buildString(msg, colorCode)).append("\n");
        print.flush();
    }
}
