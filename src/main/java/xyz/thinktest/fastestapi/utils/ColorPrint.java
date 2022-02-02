package xyz.thinktest.fastestapi.utils;

import java.io.PrintWriter;
import java.util.Arrays;

public enum ColorPrint {
    WHITE("255;255;255"){
        @Override
        public void println(String msg) {
            out(msg);
        }

        @Override
        public void print(String msg) {
            out(msg, false);
        }
    },
    RED("205;51;51") {
        @Override
        public void println(String msg) {
            out(msg);
        }

        @Override
        public void print(String msg) {
            out(msg, false);
        }
    },
    ORANGE("255;127;0"){
        @Override
        public void println(String msg) {
            out(msg);
        }

        @Override
        public void print(String msg) {
            out(msg, false);
        }
    },
    YELLOW("255;236;139") {
        @Override
        public void println(String msg) {
            out(msg);
        }

        @Override
        public void print(String msg) {
            out(msg, false);
        }
    },
    GREEN("0;255;127") {
        @Override
        public void println(String msg) {
            out(msg);
        }

        @Override
        public void print(String msg) {
            out(msg, false);
        }
    },
    CYAN("0;255;255") {
        @Override
        public void println(String msg) {
            out(msg);
        }

        @Override
        public void print(String msg) {
            out(msg, false);
        }
    },
    BLUE("0;0;255"){
        @Override
        public void println(String msg) {
            out(msg);
        }

        @Override
        public void print(String msg) {
            out(msg, false);
        }
    },
    PURPLE("139;0;255"){
        @Override
        public void println(String msg) {
            out(msg);
        }

        @Override
        public void print(String msg) {
            out(msg, false);
        }
    },
    DEFAULT("-1;-1;-1"){
        @Override
        public void println(String msg) {
            System.out.println(msg);
        }

        @Override
        public void print(String msg) {
            System.out.print(msg);
        }
    };
    protected final String colorCode;
    ColorPrint(String colorCode){
        this.colorCode = colorCode;
    }

    String getColorCode() {
        return colorCode;
    }

    public abstract void println(String msg);

    public abstract void print(String msg);

    protected String buildString(String msg, String colorCode){
        StringBuilder sb = new StringBuilder();
        sb.append("\033[38;2;").append(colorCode).append("m").append(msg).append("\033[0m");
        return sb.toString();
    }

    protected void out(String msg, boolean isNewLine){
        PrintWriter print = new PrintWriter(System.out);
        print.append(buildString(msg, this.colorCode));
        if(isNewLine){
            print.append("\n");
        }
        print.flush();
    }
    protected void out(String msg){
        out(msg, true);
    }

    public static ColorPrint color(String type){
        return Arrays.stream(ColorPrint.values()).filter(color -> color.name().equals(type.toUpperCase())).findFirst().orElse(DEFAULT);
    }

    public static ColorPrint code(String code){
        return Arrays.stream(ColorPrint.values()).filter(color -> color.getColorCode().equals(code.trim())).findFirst().orElse(DEFAULT);
    }
}
