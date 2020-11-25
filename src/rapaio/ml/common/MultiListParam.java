package rapaio.ml.common;

import rapaio.util.function.SFunction;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * This implementation models a parameter which is a map of keys and values.
 * <p>
 * Created by <a href="mailto:padreati@yahoo.com">Aurelian Tutuianu</a> on 8/3/20.
 */
public class MultiListParam<K, T, S extends ParamSet<S>> implements Param<Map<K, List<T>>, S> {

    private static final long serialVersionUID = 6971154885891319057L;
    private final S params;
    private final TreeMap<K, List<T>> defaultValue;
    private final TreeMap<K, List<T>> valueMap;
    private final String name;
    private final String description;
    private final SFunction<Map<K, List<T>>, Boolean> validator;

    public MultiListParam(S params, Map<K, List<T>> defaultValue, String name, String description,
                          SFunction<Map<K, List<T>>, Boolean> validator) {
        this.params = params;
        this.defaultValue = new TreeMap<>(defaultValue);
        this.valueMap = new TreeMap<>(defaultValue);
        this.name = name;
        this.description = description;
        this.validator = validator;

        params.registerParameter(this);
    }

    @Override
    public TreeMap<K, List<T>> get() {
        return valueMap;
    }

    public List<T> get(K key) {
        return valueMap.get(key);
    }

    @Override
    public S set(Map<K, List<T>> value) {
        this.valueMap.clear();
        this.valueMap.putAll(value);
        return params;
    }

    public S add(Map<K, List<T>> value) {
        this.valueMap.putAll(value);
        return params;
    }

    @SafeVarargs
    public final S add(K key, T... values) {
        this.valueMap.put(key, Arrays.asList(values));
        return params;
    }

    @Override
    public Map<K, List<T>> defaultValue() {
        return defaultValue;
    }

    public K getReverseKey(T value) {
        for (Map.Entry<K, List<T>> entry : valueMap.entrySet()) {
            if (entry.getValue().contains(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean hasDefaultValue() {
        if (defaultValue == null) {
            return valueMap == null;
        }
        if (valueMap == null) {
            return false;
        }
        if (defaultValue.size() != valueMap.size()) {
            return false;
        }

        for (var entry : defaultValue.entrySet()) {
            var ref = entry.getValue();
            var comp = valueMap.get(entry.getKey());
            if (comp == null) {
                return false;
            }
            for (int i = 0; i < ref.size(); i++) {
                T value = ref.get(i);
                if (value instanceof ParametricEquals) {
                    boolean eq = ((ParametricEquals<T>) value).equalOnParams(comp.get(i));
                    if (!eq) {
                        return false;
                    }
                } else {
                    if (!Objects.equals(value, comp.get(i))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public S clear() {
        valueMap.clear();
        valueMap.putAll(defaultValue);
        return params;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public boolean validate(Map<K, List<T>> value) {
        return validator.apply(value);
    }
}