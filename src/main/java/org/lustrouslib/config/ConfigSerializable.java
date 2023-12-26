package org.lustrouslib.config;

public interface ConfigSerializable<E> {
    public void serialize(String prevPath);
    public E deserialize(String path);
}
