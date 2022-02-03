package xyz.thinktest.fastestapi.core.internal.enhance.fieldhelper;

import org.apache.commons.lang3.StringUtils;
import xyz.thinktest.fastestapi.core.annotations.Pointcut;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.Target;
import xyz.thinktest.fastestapi.core.enhance.joinpoint.field.JoinPoint;
import xyz.thinktest.fastestapi.utils.ObjectUtil;
import xyz.thinktest.fastestapi.common.exceptions.ValueException;
import xyz.thinktest.fastestapi.core.annotations.Value;
import xyz.thinktest.fastestapi.utils.files.PropertyUtil;
import xyz.thinktest.fastestapi.utils.reflects.FieldHelper;
import xyz.thinktest.fastestapi.utils.reflects.ReflectUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Date: 2021/10/31
 */
@Pointcut(annotation = Value.class)
public class ValueAnnotationProcess extends AbstractFieldProcess {

    @Override
    public void process(JoinPoint joinPoint) {
        this.exec(joinPoint);
    }

    private void exec(JoinPoint joinPoint){
        Field field = joinPoint.getField();
        Value value = (Value) joinPoint.getAnnotation();
        Class<?> fieldType = field.getType();
        String fileName = value.file().trim();
        String key;
        String originKey = value.key().trim();
        String aliasKey = value.value().trim();

        if(StringUtils.isEmpty(originKey) && StringUtils.isNotEmpty(aliasKey)){
            key = aliasKey;
        } else if(StringUtils.isEmpty(aliasKey) && StringUtils.isNotEmpty(originKey)){
            key = aliasKey;
        } else if (StringUtils.isEmpty(originKey) && StringUtils.isEmpty(aliasKey)){
            key = field.getName();
        } else if(StringUtils.isNotEmpty(originKey) && StringUtils.isNotEmpty(aliasKey)){
            key = originKey;
        } else {
            key = aliasKey;
        }
        String val;
        if(StringUtils.isEmpty(fileName)){
            val = PropertyUtil.getProperty(key);
        } else {
            val = PropertyUtil.getProperty(fileName, key);
        }
        if(Objects.nonNull(val)){
            setField(field, fieldType, val, joinPoint.getTarget());
        }
    }

    public static void setField(Field field, Class<?> fieldType, String value, Target target){
        try {
            String canonicalName = fieldType.getCanonicalName();
            if (fieldType.isArray()) {
                Array array = Array.create(fieldType.getCanonicalName(), value.split(","), target.getInstance(), field);
                if(canonicalName.startsWith("java.lang")) {
                    array.parseWrapperArray();
                } else {
                    array.parseBaseArray();
                }
            } else if(fieldType.getName().endsWith("List")){
                Class<?> elementType = ReflectUtil.getCollectionGenericRealType(field.getGenericType());
                Array array = Array.create(elementType.getName(), value.split(","), target.getInstance(), field);
                array.parseWrapperList();
            }
            else {
                Parse parse = Parse.create(fieldType.getName(), value, target.getInstance(), field);
                parse.parse();
            }
        }catch (IllegalArgumentException e){
            throw new ValueException(ObjectUtil.format("set value error: {} {} {} expect type {}, value {}",field.getDeclaringClass().getName(), field.getType().getName(), field.getName(), fieldType, value), e.getCause());
        }
    }

    static class Parse{
        private final String type;
        private final String value;
        private final FieldHelper<?> fieldHelper;

        private Parse(String type, String value, Object instance, Field field){
            this.type = type;
            this.value = value;
            this.fieldHelper = FieldHelper.getInstance(instance, field);
        }

        public void parse(){
            Object _value;
            if("java.lang.Integer".equals(type) || "int".equals(type)) {
                _value = Integer.parseInt(value);
            } else if ("java.lang.Long".equals(type) || "long".equals(type)) {
                _value = Long.parseLong(value);
            } else if ("java.lang.Float".equals(type) || "float".equals(type)) {
                _value = Float.parseFloat(value);
            } else if ("java.lang.Double".equals(type) || "double".equals(type)) {
                _value = Double.parseDouble(value);
            } else if ("java.lang.Boolean".equals(type) || "boolean".equals(type)) {
                _value = Boolean.parseBoolean(value);
            } else if ("java.lang.Short".equals(type) || "short".equals(type)) {
                _value = Short.parseShort(value);
            } else if ("java.lang.Byte".equals(type) || "byte".equals(type)) {
                _value = Byte.parseByte(value);
            } else {
                _value = value;
            }
            this.fieldHelper.set(_value);
        }

        public static Parse create(String type, String value, Object instance, Field field){
            return new Parse(type, value, instance, field);
        }
    }

    static class Array{
        private final String type;
        private final int length;
        private final String[] value;
        private final FieldHelper<?> fieldHelper;

        public Array(String type, String[] value, Object instance, Field field){
            this.type = type;
            this.length = value.length;
            this.value = value;
            this.fieldHelper = FieldHelper.getInstance(instance, field);
        }

        public static Array create(String type, String[] value, Object instance, Field field){
            return new Array(type, value, instance, field);
        }

        public void parseWrapperArray(){
            Object[] _value;
            switch (type){
                case ("java.lang.Integer[]"):
                    _value = new Integer[length];
                    for(int i=0; i < length;i++){
                        _value[i] = Integer.parseInt(value[i]);
                    }
                    break;
                case "java.lang.Long[]":
                    _value= new Long[length];
                    for(int i=0;i < length;i++){
                        _value[i] = Long.parseLong(value[i]);
                    }
                    break;
                case "java.lang.Float[]":
                    _value = new Float[length];
                    for(int i=0; i<length;i++){
                        _value[i] = Float.parseFloat(value[i]);
                    }
                    break;
                case "java.lang.Double[]":
                    _value = new Double[length];
                    for(int i=0; i<length;i++){
                        _value[i] = Double.parseDouble(value[i]);
                    }
                    break;
                case "java.lang.Boolean[]":
                    _value = new Boolean[length];
                    for(int i = 0;i < length;i++){
                        _value[i] = Boolean.parseBoolean(value[i]);
                    }
                    break;
                case "java.lang.Short[]":
                    _value = new Short[length];
                    for(int i=0;i<length;i++){
                        _value[i] = Short.parseShort(value[i]);
                    }
                    break;
                case "java.lang.Byte[]":
                    _value = new Byte[length];
                    for(int i=0;i<length;i++){
                        _value[i] = Byte.parseByte(value[i]);
                    }
                    break;
                default:
                    _value = value;
                    break;
            }
            this.fieldHelper.set(_value);
        }

        public void parseWrapperList(){
            List<Object> _value = new ArrayList<>();
            switch (type){
                case ("java.lang.Integer"):
                    for(int i=0; i < length;i++){
                        _value.add(Integer.parseInt(value[i]));
                    }
                    break;
                case "java.lang.Long":
                    for(int i=0;i < length;i++){
                        _value.add(Long.parseLong(value[i]));
                    }
                    break;
                case "java.lang.Float":
                    for(int i=0; i<length;i++){
                        _value.add(Float.parseFloat(value[i]));
                    }
                    break;
                case "java.lang.Double[]":
                    for(int i=0; i<length;i++){
                        _value.add(Double.parseDouble(value[i]));
                    }
                    break;
                case "java.lang.Boolean":
                    for(int i = 0;i < length;i++){
                        _value.add(Boolean.parseBoolean(value[i]));
                    }
                    break;
                case "java.lang.Short[]":
                    for(int i=0;i<length;i++){
                        _value.add(Short.parseShort(value[i]));
                    }
                    break;
                case "java.lang.Byte":
                    for(int i=0;i<length;i++){
                        _value.add(Byte.parseByte(value[i]));
                    }
                    break;
                default:
                    _value.add(new ArrayList<>(Arrays.asList(value)));
            }
            this.fieldHelper.set(_value);
        }

        public void parseBaseArray(){
            switch (type){
                case "int[]":
                    int[] integers = new int[length];
                    for(int i=0; i < length;i++){
                        integers[i] = Integer.parseInt(value[i]);
                    }
                    this.fieldHelper.set(integers);
                    break;
                case "long[]":
                    long[] longs = new long[length];
                    for(int i=0;i < length;i++){
                        longs[i] = Long.parseLong(value[i]);
                    }
                    this.fieldHelper.set(longs);
                    break;
                case "float[]":
                    float[] floats = new float[length];
                    for(int i=0; i<length;i++){
                        floats[i] = Float.parseFloat(value[i]);
                    }
                    this.fieldHelper.set(floats);
                    break;
                case "java.lang.Double[]":
                    double[] doubles = new double[length];
                    for(int i=0; i<length;i++){
                        doubles[i] = Double.parseDouble(value[i]);
                    }
                    this.fieldHelper.set(doubles);
                    break;
                case "java.lang.Boolean[]":
                    boolean[] booleans = new boolean[length];
                    for(int i = 0;i < length;i++){
                        booleans[i] = Boolean.parseBoolean(value[i]);
                    }
                    this.fieldHelper.set(booleans);
                    break;
                case "java.lang.Short[]":
                    short[] shorts = new short[length];
                    for(int i=0;i<length;i++){
                        shorts[i] = Short.parseShort(value[i]);
                    }
                    this.fieldHelper.set(shorts);
                    break;
                case "java.lang.Byte[]":
                    byte[] bytes = new byte[length];
                    for(int i=0;i<length;i++){
                        bytes[i] = Byte.parseByte(value[i]);
                    }
                    this.fieldHelper.set(bytes);
                    break;
                default:
                    this.fieldHelper.set(value);
            }
        }
    }
}
