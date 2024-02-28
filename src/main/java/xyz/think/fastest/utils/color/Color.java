package xyz.think.fastest.utils.color;

import java.io.PrintWriter;

/**
 * @author: aruba
 * @date: 2022-02-03
 */
public class Color {
    private final String colorCode;
    Color(String colorCode){
        this.colorCode = colorCode;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void println(String msg){
        out(msg);
    }

    public void print(String msg){
        out(msg, false);
    }

    private String buildString(String msg, String colorCode){
        StringBuilder sb = new StringBuilder();
        sb.append("\033[38;2;").append(colorCode).append("m").append(msg).append("\033[0m");
        return sb.toString();
    }

    private void out(String msg, boolean isNewLine){
        PrintWriter print = new PrintWriter(System.out);
        print.append(buildString(msg, this.colorCode));
        if(isNewLine){
            print.append("\n");
        }
        print.flush();
    }
    private void out(String msg){
        out(msg, true);
    }
}
