package Model.adt;
import java.util.HashMap;
import java.util.Map;

public class Dict<T1, T2> implements IDict<T1, T2> {
    HashMap<T1, T2> dictionary;

    public Dict() {
        dictionary = new HashMap<>();
    }

    @Override
    public void add(T1 v1, T2 v2) {
        this.dictionary.put(v1, v2);
    }

    @Override
    public void update(T1 v1, T2 v2) {
        this.dictionary.replace(v1, v2);
    }

    @Override
    public void remove(T1 id) {
        this.dictionary.remove(id);
    }

    @Override
    public T2 lookup(T1 id) {
        return this.dictionary.get(id);
    }

    @Override
    public boolean isDefined(T1 id) {
        return (this.dictionary.get(id) != null);
    }

    @Override
    public String toString() {
        return this.dictionary.toString();
    }

    @Override
    public Map<T1,T2> getContent() {
        return this.dictionary;
    }

    @Override
    public void setContent(Map<T1, T2> safeGarbageCollector) {
        this.dictionary = (HashMap<T1, T2>) safeGarbageCollector;
    }

    @Override
    public Dict<T1, T2> deepClone() {
        Dict<T1, T2> deepCopy = new Dict<T1, T2>();

        for (T1 element : this.dictionary.keySet()) {
            deepCopy.add(element, this.dictionary.get(element));
        }

        return deepCopy;
    }

}
