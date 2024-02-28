package xyz.think.fastest.common.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Date: 2021/10/31
 */
public class CaptureException extends FastestBasicException {
    private final static String[] excludes = {"6f72672e746573746e672e6c697374656e65722e54657374436173654c697374656e6572", "6f72672e656173792e66617374657374", "6e65742e73662e63676c6962", "2424456e68616e636572427943474c49422424"};

    public CaptureException(Throwable cause){
        super(cause);
        this.exclude(this);
    }

    public CaptureException(String msg, Throwable cause){
        super(msg, cause);
        this.exclude(this);
    }


    public void exclude(Throwable cause){
        List<StackTraceElement> stackTraceElementList = new ArrayList<>();
        for(StackTraceElement element: cause.getStackTrace()){
            if(!checkStackTraceElement(element.toString())){
                stackTraceElementList.add(element);
            }

        }
        cause.setStackTrace(stackTraceElementList.toArray(new StackTraceElement[0]));
        if(Objects.nonNull(cause.getCause())){
            exclude(cause.getCause());
        }
    }

    private boolean checkStackTraceElement(String className){
        for(String name:excludes){
            String stackClassName = bytesToHexString(className.getBytes());
            if(stackClassName.contains(name)){
                return true;
            }
        }
        return false;
    }

    private String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return "";
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    private byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private byte toByte(char c) {
        byte b = (byte) "0123456789abcdef".indexOf(c);
        return b;
    }
}
