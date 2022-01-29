package xyz.thinktest.fastestapi.utils;

import java.io.PrintWriter;
import java.util.Arrays;

public enum ColorPrint {
    WHITE{
        @Override
        public void println(String msg) {
            out(msg, "255;255;255");
        }

        @Override
        public void print(String msg) {
            out(msg, "255;255;255", false);
        }
    },
    CYAN {
        @Override
        public void println(String msg) {
            out(msg, "0;255;255");
        }

        @Override
        public void print(String msg) {
            out(msg, "0;255;255", false);
        }
    },
    GREEN {
        @Override
        public void println(String msg) {
            out(msg, "0;255;127");
        }

        @Override
        public void print(String msg) {
            out(msg, "0;255;127", false);
        }
    },
    YELLOW {
        @Override
        public void println(String msg) {
            out(msg, "255;236;139");
        }

        @Override
        public void print(String msg) {
            out(msg, "255;236;139", false);
        }
    },
    RED {
        @Override
        public void println(String msg) {
            out(msg, "205;51;51");
        }

        @Override
        public void print(String msg) {
            out(msg, "205;51;51", false);
        }
    },
    DEFAULT{
        @Override
        public void println(String msg) {
            System.out.println(msg);
        }

        @Override
        public void print(String msg) {
            System.out.print(msg);
        }
    };

    public abstract void println(String msg);

    public abstract void print(String msg);

    protected String buildString(String msg, String colorCode){
        StringBuilder sb = new StringBuilder();
        sb.append("\033[38;2;").append(colorCode).append("m").append(msg).append("\033[0m");
        return sb.toString();
    }

    protected void out(String msg, String colorCode, boolean isNewLine){
        PrintWriter print = new PrintWriter(System.out);
        print.append(buildString(msg, colorCode));
        if(isNewLine){
            print.append("\n");
        }
        print.flush();
    }
    protected void out(String msg, String colorCode){
        out(msg, colorCode, true);
    }

    public static ColorPrint color(String type){
        return Arrays.stream(ColorPrint.values()).filter(color -> color.name().equals(type.toUpperCase())).findFirst().orElse(DEFAULT);
    }
}
