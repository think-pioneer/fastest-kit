package xyz.think.fastest.utils.string;

/**
 * 对StringUtils的补充
 * @author: aruba
 * @date: 2022-06-07
 */
public final class StringUtils extends org.apache.commons.lang3.StringUtils {
    private StringUtils(){}

    /**
     * 空格字符
     */
    private static final String SPACE_CHAR = "\\s|\\u00a0|\\u0020|\\u3000";

    /**
     * 替换字符串中所有指定的字符
     * 例如：将字符串“abcbe”中的b去除，stripAll("abcbe, b*, "")
     * @param content 原始字符串
     * @param pattern 待查找字符的正则
     * @param replacement 替换值
     * @return 替换后的字符串
     */
    public static String stripAll(CharSequence content, String pattern, String replacement){
        if(content == null){
            return null;
        }
        if(content.length() == 0){
            return (String) content;
        }
        return ((String) content).replaceAll(pattern, replacement);
    }

    /**
     * 去除字符串中所有的空格。包括nbsp。
     * {@link #stripAll(CharSequence, String, String)}
     * @param content 原始字符
     * @return 去除空格后的字符串。
     */
    public static String stripAllSpace(CharSequence content){
        return stripAll(content, "(?:" + SPACE_CHAR + ")", "");
    }

    /**
     * 替换字符串中的指定内容
     * @param content 原始字符串
     * @param pattern 查找正则
     * @param replacement 替换的内容
     * @return 替换后的字符串
     */
    public static String stripStartAndEnd(CharSequence content, String pattern, String replacement){
        if(content == null){
            return null;
        }
        if(content.length() == 0){
            return (String) content;
        }
        return ((String) content).replaceAll(pattern, replacement);
    }

    /**
     * 去除字符串首尾的空格，包括nbsp等。
     * {@link #stripStartAndEnd(CharSequence, String, String)}
     * @param content 原始字符串
     * @return 去除空格后的字符串
     */
    public static String stripStartAndEndSpace(CharSequence content){
        return stripStartAndEnd(content, "^(" + SPACE_CHAR + ")*|(" + SPACE_CHAR + ")*$", "");
    }

    /**
     * 判断字符串为空，会处理首尾的空格后在判断。
     * @param content 待处理的字符串
     * @return 判断结果。
     */
    public static boolean isEmpty(CharSequence content){
        if(content == null || content.length() == 0){
            return true;
        }
        return "".equals(stripStartAndEndSpace(content));
    }

    /**
     * 判断字符串内容不为空。
     * {@link #isEmpty(CharSequence)}
     * @param content 待处理字符串
     * @return 判断结果
     */
    public static boolean isNotEmpty(CharSequence content){
        return !isEmpty(content);
    }

    /**
     * 格式化字符串
     * format("a{0}c{1}", "b", "d") => "abcd"
     * @param pattern 字符串模板
     * @param arguments 待格式化参数
     * @return 格式化后的字符串
     */
    public static String format(String pattern, Object... arguments){
        return StringFormat.format(pattern, arguments);
    }

    /**
     * {@link #format(String, Object...)}
     * 和format相比，无需指定参数的位置坐标。书写更快捷
     * format2("a{}c{}", "b", "d") => "abcd"
     */
    public static String format2(String pattern, Object... arguments){
        return StringFormat.format2(pattern, arguments);
    }
}
