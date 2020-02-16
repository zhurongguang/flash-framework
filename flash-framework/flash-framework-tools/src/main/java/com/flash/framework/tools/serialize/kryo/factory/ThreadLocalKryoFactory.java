package com.flash.framework.tools.serialize.kryo.factory;

import com.esotericsoftware.kryo.Kryo;

/**
 * @author
 * @date 2019/2/12 - 下午3:16
 */
public class ThreadLocalKryoFactory extends AbstractKryoFactory {

    private final ThreadLocal<Kryo> holder = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            return create();
        }
    };

    @Override
    public void returnKryo(Kryo kryo) {
        // do nothing
    }

    @Override
    public Kryo getKryo() {
        return holder.get();
    }
}