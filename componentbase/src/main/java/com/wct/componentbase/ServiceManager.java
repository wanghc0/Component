package com.wct.componentbase;

import android.app.Application;

import androidx.annotation.NonNull;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangchenteng
 * @date 2020/8/31
 * @desc
 */
public class ServiceManager {
    private static Application sApplication;
    /**
     * 用来存放接口类和其对应实例
     */
    private static final ConcurrentHashMap<Class, Object> SERVICES = new ConcurrentHashMap<>();

    public static void init(Application application) {
        sApplication = application;
    }

    public static Application getApplication() {
        return sApplication;
    }

    /**
     * 获取对应的接口的实例
     * @param clazz 接口类
     * @param <T>
     * @return 接口类对应的实例。对于未注册的接口类，返回其对应的空实现。
     */
    public static <T extends IService> T getService(Class<T> clazz) {
        T impl = (T) SERVICES.get(clazz);
        if (impl == null) {
            impl = getEmptyImpl(clazz);
            registerService(clazz, impl);
        }
        return impl;
    }

    /**
     * 注册接口类和其对应的实现
     * @param clazz
     * @param obj
     * @param <T> 接口类
     */
    public static <T extends IService> void registerService(Class<T> clazz, T obj) {
        SERVICES.put(clazz, obj);
    }

    /**
     * 获取接口类对应的空实现
     * @param klass
     * @param <T>
     * @param <C>
     * @return
     */
    @NonNull
    private static <T, C> T getEmptyImpl(Class<C> klass) {
        String fullPackage = klass.getPackage().getName();
        String name = klass.getSimpleName();
        name = name.substring(1); //去掉接口类名称的首字母 I，eg: IAccountService --> AccountService
        final String implName = fullPackage + "." + name + "Empty"; // AccountService --> AccountServiceEmpty
        try {
            @SuppressWarnings("unchecked") final Class<T> aClass = (Class<T>) Class.forName(implName);
            return aClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("cannot find implementation for "
                    + klass.getCanonicalName() + ". " + implName + " does not exist");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot access the constructor"
                    + klass.getCanonicalName());
        } catch (InstantiationException e) {
            throw new RuntimeException("Failed to create an instance of "
                    + klass.getCanonicalName());
        }
    }
}
