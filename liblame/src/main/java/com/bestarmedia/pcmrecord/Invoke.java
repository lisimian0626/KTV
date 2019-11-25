package com.bestarmedia.pcmrecord;

import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Invoke {

    public Invoke() {

    }

    /**
     * java反射机制调用函数
     * <p>
     * owner：newInstance获取的对象
     * methodName ： 方法名
     * args，方法的参数列表
     */
    public static Object invokeMethod(Object owner, String methodName, Object[] args) {
        Object ret = null;
        try {
            Class<? extends Object> ownerClass = owner.getClass();
            Class[] argsClass = new Class[args.length];

            for (int i = 0, j = args.length; i < j; i++) {
                if (args[i].getClass().getName().equals(Integer.class.getName())) {
                    argsClass[i] = int.class;
                } else if (args[i].getClass().getName().equals(Float.class.getName())) {
                    argsClass[i] = float.class;
                } else if (args[i].getClass().getName().startsWith("android.view.SurfaceView")) {
                    argsClass[i] = android.view.SurfaceHolder.class;
                } else {
                    argsClass[i] = args[i].getClass();
                }
            }

            Method method = ownerClass.getMethod(methodName, argsClass);
            ret = method.invoke(owner, args);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Invoke", "invokeMethod owner=" + owner + " methodName=" + methodName, e);
        }
        return ret;
    }

    public static Object invokeMethod(Object owner, String methodName, Object[] args, Class[] argsClass) {
        Object ret = null;
        try {
            Class<? extends Object> ownerClass = owner.getClass();
            Method method = ownerClass.getMethod(methodName, argsClass);
            ret = method.invoke(owner, args);
        } catch (Exception e) {
            e.printStackTrace();
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
            Class<?> ownerClass = Class.forName(className);

            Field field = ownerClass.getField(fieldName);

            property = field.get(ownerClass);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Invoke", "getStaticProperty className=" + className + " fieldName=" + fieldName, e);
        }
        return property;
    }


    /**
     * 获取对象实例
     */
    public static Object newInstance(String className, Object[] args, Class[] clzs) throws Exception {
        Class<?> newoneClass = Class.forName(className);
        Constructor<?> cons = newoneClass.getConstructor(clzs);

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
            //Log.e("zbkc", "", e);
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
    protected static Field getDeclaredField(final Class<?> clazz, final String fieldName) {
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
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
