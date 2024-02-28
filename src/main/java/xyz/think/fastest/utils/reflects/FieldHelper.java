package xyz.think.fastest.utils.reflects;

import xyz.think.fastest.common.exceptions.ReflectionException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * @Date: 2021/12/26
 */
@SuppressWarnings("unchecked")
public final class FieldHelper {

    private final Object instance;
    private final Field field;

    private FieldHelper(Object instance, Field field){
        this.instance = instance;
        this.field = field;
        if(Objects.isNull(this.instance) || Objects.isNull(this.field)){
            throw new ReflectionException("instance or field could not null");
        }
        if(Modifier.isFinal(this.field.getModifiers())) {
            throw new ReflectionException("field is final.");
        }
        this.field.setAccessible(true);
    }

    public <T> T get(){
        try {
            return (T) this.field.get(this.instance);
        }catch (IllegalAccessException | IllegalArgumentException e){
            throw new ReflectionException("No permission", e);
        }
    }

    public boolean getBoolean(){
        try {
            return this.field.getBoolean(this.instance);
        }catch (IllegalAccessException | IllegalArgumentException e){
            throw new ReflectionException("No permission", e);
        }
    }

    public byte getByte(){
        try {
            return this.field.getByte(this.instance);
        }catch (IllegalAccessException | IllegalArgumentException e){
            throw new ReflectionException("Illegal exception:", e);
        }
    }

    public char getChar(){
        try {
            return this.field.getChar(this.instance);
        }catch (IllegalAccessException | IllegalArgumentException e){
            throw new ReflectionException("Illegal exception:", e);
        }
    }

    public short getShort(){
        try {
            return this.field.getShort(this.instance);
        }catch (IllegalAccessException | IllegalArgumentException e){
            throw new ReflectionException("Illegal exception:", e);
        }
    }

    public int getInt(){
        try {
            return this.field.getInt(this.instance);
        }catch (IllegalAccessException | IllegalArgumentException e){
            throw new ReflectionException("Illegal exception:", e);
        }
    }

    public long getLong(){
        try {
            return this.field.getLong(this.instance);
        }catch (IllegalAccessException | IllegalArgumentException e){
            throw new ReflectionException("Illegal exception:", e);
        }
    }

    public float getFloat(){
        try {
            return this.field.getFloat(this.instance);
        }catch (IllegalAccessException | IllegalArgumentException e){
            throw new ReflectionException("Illegal exception:", e);
        }
    }

    public double getDouble(){
        try {
            return this.field.getDouble(this.instance);
        }catch (IllegalAccessException | IllegalArgumentException e){
            throw new ReflectionException("Illegal exception:", e);
        }
    }

    public void set(Object value){
        try {
            this.field.set(this.instance, value);
        }catch (IllegalAccessException | IllegalArgumentException e){
            throw new ReflectionException("Illegal exception:", e);
        }
    }

    public void setBoolean(boolean value){
        try {
            this.field.setBoolean(this.instance, value);
        }catch (IllegalAccessException | IllegalArgumentException e){
            throw new ReflectionException("Illegal exception:", e);
        }
    }

    public void setByte(byte value){
        try {
            this.field.setByte(this.instance, value);
        }catch (IllegalAccessException | IllegalArgumentException e){
            throw new ReflectionException("Illegal exception:", e);
        }
    }

    public void setChar(char value){
        try {
            this.field.setChar(this.instance, value);
        }catch (IllegalAccessException | IllegalArgumentException e){
            throw new ReflectionException("Illegal exception:", e);
        }
    }

    public void setShort(short value){
        try {
            this.field.setShort(this.instance, value);
        }catch (IllegalAccessException | IllegalArgumentException e){
            throw new ReflectionException("Illegal exception:", e);
        }
    }

    public void setInt(byte value){
        try {
            this.field.setInt(this.instance, value);
        }catch (IllegalAccessException | IllegalArgumentException e){
            throw new ReflectionException("Illegal exception:", e);
        }
    }

    public void setLong(byte value){
        try {
            this.field.setLong(this.instance, value);
        }catch (IllegalAccessException | IllegalArgumentException e){
            throw new ReflectionException("Illegal exception:", e);
        }
    }

    public void setFloat(float value){
        try {
            this.field.setFloat(this.instance, value);
        }catch (IllegalAccessException | IllegalArgumentException e){
            throw new ReflectionException("Illegal exception:", e);
        }
    }

    public void setDouble(double value){
        try {
            this.field.setDouble(this.instance, value);
        }catch (IllegalAccessException | IllegalArgumentException e){
            throw new ReflectionException("Illegal exception:", e);
        }
    }

    public static FieldHelper getInstance(Object instance, Field field){
        return new FieldHelper(instance, field);
    }

    public static FieldHelper getInstance(Object instance, String fieldName){
        return new FieldHelper(instance, ReflectUtil.getDeclaredField(instance, fieldName));
    }
}
