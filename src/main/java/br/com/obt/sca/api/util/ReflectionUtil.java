package br.com.obt.sca.api.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {

    private static final String FIELD_ID = "id";
    private static final String FIELD_STATUS = "status";

    public static Object getIdFromObject(Object value, String fieldSearch) {
        Object result = null;
        Field field = null;
        try {
            field = value.getClass().getDeclaredField(fieldSearch);
            field.setAccessible(true);
            result = field.get(value);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            try {
                field = value.getClass().getField(fieldSearch);
                field.setAccessible(true);
                result = field.get(value);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException exx) {
                return null;
            }
        }
        return result;
    }

    public static Long getIdByReflection(Object bean) {
        Object obj = getIdFromObject(bean, FIELD_ID);
        if (obj == null) {
            return null;
        }
        return (Long) obj;
    }

    public static Boolean getStatusByReflection(Object bean) {
        Object obj = getIdFromObject(bean, FIELD_STATUS);
        if (obj == null) {
            return null;
        }
        return (Boolean) obj;
    }

    public static Object setStatusByReflection(Object value, Boolean status) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Method setter = null;
        try {
            setter = value.getClass().getDeclaredMethod("setStatus", Boolean.class);
            setter.invoke(value, status);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException ex) {
            try {
                setter = value.getClass().getMethod("setStatus", Boolean.class);
                setter.invoke(value, status);
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException ex2) {
                 throw new NoSuchMethodException("Não foi possível alterar o status, tente novamente!");
            }
        }
        return value;
    }
}
