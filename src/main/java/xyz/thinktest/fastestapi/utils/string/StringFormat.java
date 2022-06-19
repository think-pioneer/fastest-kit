package xyz.thinktest.fastestapi.utils.string;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: aruba
 * @date: 2022-05-28
 */
class StringFormat extends MessageFormat {
    private static final long serialVersionUID = 2578932158955202696L;

    public StringFormat(String pattern) {
        super(pattern);
    }

    public StringFormat(String pattern, Locale locale) {
        super(pattern, locale);
    }

    public static String format(String pattern, Object[] arguments){
        if(null == pattern || "".equals(pattern.trim())){
            return pattern;
        }
        return MessageFormat.format(pattern, arguments);
    }

    public static String format2(String pattern, Object[] arguments){
        return MessageFormat.format(buildPattern(pattern), arguments);
    }

    private static String buildPattern(String pattern){
        String tmpPattern = pattern.replaceAll("'\\{}'", "`#`");
        Matcher matcher = Pattern.compile("\\{}").matcher(tmpPattern);
        StringBuffer sb = new StringBuffer();
        int index = 0;
        while (matcher.find()){
            matcher.appendReplacement(sb, "{" + index + "}");
            index++;
        }
        matcher.appendTail(sb);
        tmpPattern = sb.toString().replaceAll("`#`", "'{}'");
        tmpPattern = tmpPattern.replaceAll("'", "'''");
        return tmpPattern;
    }
}
