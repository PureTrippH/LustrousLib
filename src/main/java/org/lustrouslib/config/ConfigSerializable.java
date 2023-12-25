package org.lustrouslib.config;

public interface ConfigSerializable<E> {
    public void serialize();
    public E deserialize();
}
