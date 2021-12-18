package org.fastest.core.internal.enhance.fieldhelper;

import org.apache.commons.lang3.StringUtils;
import org.fastest.common.exceptions.ValueException;
import org.fastest.core.annotations.Value;
import org.fastest.core.aspect.field.JoinPoint;
import org.fastest.core.internal.ReflectTool;
import org.fastest.core.internal.enhance.FieldTool;
import org.fastest.utils.ObjectUtil;
import org.fastest.utils.YamlUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Date: 2021/10/31
 */
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
            val = YamlUtil.getString(key);
        } else {
            val = YamlUtil.getString(fileName, key);
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
                Class<?> elementType = ReflectTool.getCollectionGenericRealType(field.getGenericType());
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
        private final Field field;
        private final Object instance;

        private Parse(String type, String value, Object instance, Field field){
            this.type = type;
            this.value = value;
            this.instance = instance;
            this.field = field;
        }

        public void parse(){

            if("java.lang.Integer".equals(type) || "int".equals(type)) {
                FieldTool.set(field, instance, Integer.parseInt(value));
            } else if ("java.lang.Long".equals(type) || "long".equals(type)) {
                FieldTool.set(field, instance, Long.parseLong(value));
            } else if ("java.lang.Float".equals(type) || "float".equals(type)) {
                FieldTool.set(field, instance, Float.parseFloat(value));
            } else if ("java.lang.Double".equals(type) || "double".equals(type)) {
                FieldTool.set(field, instance, Double.parseDouble(value));
            } else if ("java.lang.Boolean".equals(type) || "boolean".equals(type)) {
                FieldTool.set(field, instance, Boolean.parseBoolean(value));
            } else if ("java.lang.Short".equals(type) || "short".equals(type)) {
                FieldTool.set(field, instance, Short.parseShort(value));
            } else if ("java.lang.Byte".equals(type) || "byte".equals(type)) {
                FieldTool.set(field, instance, Byte.parseByte(value));
            } else {
                FieldTool.set(field, instance, value);
            }
        }

        public static Parse create(String type, String value, Object instance, Field field){
            return new Parse(type, value, instance, field);
        }
    }

    static class Array{
        private final String type;
        private final int length;
        private final Object instance;
        private final Field field;
        private final String[] value;

        public Array(String type, String[] value, Object instance, Field field){
            this.type = type;
            this.length = value.length;
            this.value = value;
            this.instance = instance;
            this.field = field;
        }

        public static Array create(String type, String[] value, Object instance, Field field){
            return new Array(type, value, instance, field);
        }

        public void parseWrapperArray(){
            switch (type){
                case ("java.lang.Integer[]"):
                    Integer[] integers = new Integer[length];
                    for(int i=0; i < length;i++){
                        integers[i] = Integer.parseInt(value[i]);
                    }
                    FieldTool.set(field, instance, integers);
                    break;
                case "java.lang.Long[]":
                    Long[] longs = new Long[length];
                    for(int i=0;i < length;i++){
                        longs[i] = Long.parseLong(value[i]);
                    }
                    FieldTool.set(field, instance, longs);
                    break;
                case "java.lang.Float[]":
                    Float[] floats = new Float[length];
                    for(int i=0; i<length;i++){
                        floats[i] = Float.parseFloat(value[i]);
                    }
                    FieldTool.set(field, instance, floats);
                    break;
                case "java.lang.Double[]":
                    Double[] doubles = new Double[length];
                    for(int i=0; i<length;i++){
                        doubles[i] = Double.parseDouble(value[i]);
                    }
                    FieldTool.set(field, instance, doubles);
                    break;
                case "java.lang.Boolean[]":
                    Boolean[] booleans = new Boolean[length];
                    for(int i = 0;i < length;i++){
                        booleans[i] = Boolean.parseBoolean(value[i]);
                    }
                    FieldTool.set(field, instance, booleans);
                    break;
                case "java.lang.Short[]":
                    Short[] shorts = new Short[length];
                    for(int i=0;i<length;i++){
                        shorts[i] = Short.parseShort(value[i]);
                    }
                    FieldTool.set(field, instance, shorts);
                    break;
                case "java.lang.Byte[]":
                    Byte[] bytes = new Byte[length];
                    for(int i=0;i<length;i++){
                        bytes[i] = Byte.parseByte(value[i]);
                    }
                    FieldTool.set(field, instance, bytes);
                    break;
                default:
                    FieldTool.set(field, instance, value);

            }
        }

        public void parseWrapperList(){
            switch (type){
                case ("java.lang.Integer"):
                    List<Integer> integerList = new ArrayList<>();
                    for(int i=0; i < length;i++){
                        integerList.add(Integer.parseInt(value[i]));
                    }
                    FieldTool.set(field, instance, integerList);
                    break;
                case "java.lang.Long":
                    List<Long> longList = new ArrayList<>();
                    for(int i=0;i < length;i++){
                        longList.add(Long.parseLong(value[i]));
                    }
                    FieldTool.set(field, instance, longList);
                    break;
                case "java.lang.Float":
                    List<Float> floatList = new ArrayList<>();
                    for(int i=0; i<length;i++){
                        floatList.add(Float.parseFloat(value[i]));
                    }
                    FieldTool.set(field, instance, floatList);
                    break;
                case "java.lang.Double[]":
                    List<Double> doubleList = new ArrayList<>();
                    for(int i=0; i<length;i++){
                        doubleList.add(Double.parseDouble(value[i]));
                    }
                    FieldTool.set(field, instance, doubleList);
                    break;
                case "java.lang.Boolean":
                    List<Boolean> booleanList = new ArrayList<>();
                    for(int i = 0;i < length;i++){
                        booleanList.add(Boolean.parseBoolean(value[i]));
                    }
                    FieldTool.set(field, instance, booleanList);
                    break;
                case "java.lang.Short[]":
                    List<Short> shortList = new ArrayList<>();
                    for(int i=0;i<length;i++){
                        shortList.add(Short.parseShort(value[i]));
                    }
                    FieldTool.set(field, instance, shortList);
                    break;
                case "java.lang.Byte":
                    List<Byte> byteList = new ArrayList<>();
                    for(int i=0;i<length;i++){
                        byteList.add(Byte.parseByte(value[i]));
                    }
                    FieldTool.set(field, instance, byteList);
                    break;
                default:
                    FieldTool.set(field, instance, new ArrayList<>(Arrays.asList(value)));
            }
        }

        public void parseBaseArray(){
            switch (type){
                case "int[]":
                    int[] integers = new int[length];
                    for(int i=0; i < length;i++){
                        integers[i] = Integer.parseInt(value[i]);
                    }
                    FieldTool.set(field, instance, integers);
                    break;
                case "long[]":
                    long[] longs = new long[length];
                    for(int i=0;i < length;i++){
                        longs[i] = Long.parseLong(value[i]);
                    }
                    FieldTool.set(field, instance, longs);
                    break;
                case "float[]":
                    float[] floats = new float[length];
                    for(int i=0; i<length;i++){
                        floats[i] = Float.parseFloat(value[i]);
                    }
                    FieldTool.set(field, instance, floats);
                    break;
                case "java.lang.Double[]":
                    double[] doubles = new double[length];
                    for(int i=0; i<length;i++){
                        doubles[i] = Double.parseDouble(value[i]);
                    }
                    FieldTool.set(field, instance, doubles);
                    break;
                case "java.lang.Boolean[]":
                    boolean[] booleans = new boolean[length];
                    for(int i = 0;i < length;i++){
                        booleans[i] = Boolean.parseBoolean(value[i]);
                    }
                    FieldTool.set(field, instance, booleans);
                    break;
                case "java.lang.Short[]":
                    short[] shorts = new short[length];
                    for(int i=0;i<length;i++){
                        shorts[i] = Short.parseShort(value[i]);
                    }
                    FieldTool.set(field, instance, shorts);
                    break;
                case "java.lang.Byte[]":
                    byte[] bytes = new byte[length];
                    for(int i=0;i<length;i++){
                        bytes[i] = Byte.parseByte(value[i]);
                    }
                    FieldTool.set(field, instance, bytes);
                    break;
                default:
                    FieldTool.set(field, instance, value);
            }
        }
    }
}
