package eu.xenit.docker.alfresco;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

public class ThrowingMap<T, U> extends AbstractMap<T, U> {

    private Map<T, U> map;
    private String name;

    public ThrowingMap(Map<T, U> map, String name) {
        this.name = "[" + name + "]";
        this.map = map;
    }

    @Override
    public U get(Object o) {
        U value = map.get(o);
        if (value == null) {
            throw new NullPointerException(name + " Key does not exist: " + o.toString());
        }
        return value;
    }

    @Override
    public U put(T t, U u) {
        if (u == null) {
            throw new NullPointerException(name + " Value is null: " + t.toString());
        }
        return map.put(t, u);
    }

    @Override
    public U remove(Object o) {
        return map.remove(o);
    }

    @Override
    public Set<Entry<T, U>> entrySet() {
        return map.entrySet();
    }
}
