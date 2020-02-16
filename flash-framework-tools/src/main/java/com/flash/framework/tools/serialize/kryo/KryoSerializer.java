package com.flash.framework.tools.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.flash.framework.tools.serialize.Serializer;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author zhurg
 * @date 2019/2/12 - 下午3:29
 */
public class KryoSerializer<T> implements Serializer<T> {

    @Nullable
    @Override
    public byte[] serialize(@Nullable T obj) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);

        Kryo kryo = KryoUtils.get();
        kryo.writeClassAndObject(output, obj);
        output.flush();

        return byteArrayOutputStream.toByteArray();
    }

    @Nullable
    @Override
    public T deserialize(@Nullable byte[] array) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
        Input input = new Input(byteArrayInputStream);

        Kryo kryo = KryoUtils.get();
        return (T) kryo.readClassAndObject(input);
    }

    @Nullable
    @Override
    public T deserialize(@Nullable byte[] array, Class<T> clazz) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
        Input input = new Input(byteArrayInputStream);

        Kryo kryo = KryoUtils.get();
        return (T) kryo.readObjectOrNull(input, clazz);
    }
}