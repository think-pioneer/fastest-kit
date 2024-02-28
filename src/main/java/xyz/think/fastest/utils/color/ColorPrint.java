package xyz.think.fastest.utils.color;

import java.util.Arrays;

public enum ColorPrint {
    WHITE("255;255;255"){},
    RED("205;51;51") {},
    ORANGE("255;127;0"){},
    YELLOW("255;236;139") {},
    GREEN("0;255;127") {},
    CYAN("0;255;255") {},
    BLUE("0;0;255"){},
    PURPLE("139;0;255"){},
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
    private final Color color;
    ColorPrint(String colorCode){
        this.color = new Color(colorCode);
    }


    public void println(String msg){
        this.color.println(msg);
    }

    public void print(String msg){
        this.color.print(msg);
    }

    public static ColorPrint color(String type){
        return Arrays.stream(ColorPrint.values()).filter(color -> color.name().equals(type.toUpperCase())).findFirst().orElse(DEFAULT);
    }

    public static Color code(String code){
        return new Color(code);
    }
}
