package com.flash.framework.tools.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhurg
 * @date 2019/2/12 - 下午3:17
 */
@Slf4j
public class CompatibleKryo extends Kryo {

    @Override
    public Serializer getDefaultSerializer(Class type) {
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null.");
        }

        /**
         * Kryo requires every class to provide a zero argument constructor. For any class does not match this condition, kryo have two ways:
         * 1. Use JavaSerializer,
         * 2. Set 'kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));', StdInstantiatorStrategy can generate an instance bypassing the constructor.
         *
         * In practice, it's not possible for Dubbo users to register kryo Serializer for every customized class. So in most cases, customized classes with/without zero argument constructor will
         * default to the default serializer.
         * It is the responsibility of kryo to handle with every standard jdk classes, so we will just escape these classes.
         */
        if (!isJdk(type) && !type.isArray() && !type.isEnum() && !checkZeroArgConstructor(type)) {
            if (log.isWarnEnabled()) {
                log.warn(type + " has no zero-arg constructor and this will affect the serialization performance");
            }
            return new JavaSerializer();
        }
        return super.getDefaultSerializer(type);
    }

    private static boolean checkZeroArgConstructor(Class clazz) {
        try {
            clazz.getDeclaredConstructor();
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private static boolean isJdk(Class clazz) {
        return clazz.getName().startsWith("java.") || clazz.getName().startsWith("javax.");
    }

}