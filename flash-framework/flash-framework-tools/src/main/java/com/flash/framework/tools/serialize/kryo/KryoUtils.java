package com.flash.framework.tools.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.flash.framework.tools.serialize.kryo.factory.AbstractKryoFactory;
import com.flash.framework.tools.serialize.kryo.factory.ThreadLocalKryoFactory;

/**
 * @author zhurg
 * @date 2019/2/12 - 下午3:13
 */
public class KryoUtils {

    private static AbstractKryoFactory kryoFactory = new ThreadLocalKryoFactory();

    public static Kryo get() {
        return kryoFactory.getKryo();
    }

    public static void release(Kryo kryo) {
        kryoFactory.returnKryo(kryo);
    }

    public static void register(Class<?> clazz) {
        kryoFactory.registerClass(clazz);
    }

    public static void setRegistrationRequired(boolean registrationRequired) {
        kryoFactory.setRegistrationRequired(registrationRequired);
    }
}