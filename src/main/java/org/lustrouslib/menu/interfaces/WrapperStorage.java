package org.lustrouslib.menu.interfaces;

public interface WrapperStorage<T, E> {
    public E getWrapper(T unwrapped);
    public void wrap(T unwrapped);
    public void removeWrapper(T unwrapped);

}
