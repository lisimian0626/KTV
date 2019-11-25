package com.bestarmedia.libcommon.util;

import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class Invoke {

    public static Object invokeMethod(Object owner, String methodName, Object[] args, Class[] argsClass) {
        Object ret = null;
        try {
            Class<? extends Object> ownerClass = owner.getClass();
            Method method = ownerClass.getMethod(methodName, argsClass);
            ret = method.invoke(owner, args);
        } catch (Exception e) {
            Log.d("Invoke", "invokeMethod owner=" + owner + " methodName=" + methodName, e);
        }
        return ret;
    }

    /**
     * 获取类的静态对象
     */
    public static Object getStaticProperty(String className, String fieldName) {
        Object property = null;
        try {
            Class ownerClass = Class.forName(className);
            Field field = ownerClass.getField(fieldName);
            property = field.get(ownerClass);
        } catch (Exception e) {
            Log.d("Invoke", "getStaticProperty className=" + className + " fieldName=" + fieldName, e);
        }
        return property;
    }

    /**
     * 获取对象实例
     */
    public static Object newInstance(String className, Object[] args) throws Exception {
        Class newoneClass = Class.forName(className);
        Class[] argsClass = new Class[args.length];
        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }
        Constructor cons = newoneClass.getConstructor(argsClass);
        return cons.newInstance(args);
    }

    /**
     * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
     */
    public static void setFieldValue(final Object object, final String fieldName, final Object value) {
        Field field = getDeclaredField(object, fieldName);
        if (field == null)
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        makeAccessible(field);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            // Log.e("zbkc", "", e);
        }
    }

    /**
     * 循环向上转型,获取对象的DeclaredField.
     */
    protected static Field getDeclaredField(final Object object, final String fieldName) {
        return getDeclaredField(object.getClass(), fieldName);
    }

    /**
     * 循环向上转型,获取类的DeclaredField.
     */
    @SuppressWarnings("unchecked")
    protected static Field getDeclaredField(final Class clazz, final String fieldName) {
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 强制转换fileld可访问.
     */
    protected static void makeAccessible(Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }
}
