package Model.adt;

import java.util.Stack;

public class List<T> implements IList<T> {
    Stack<T> list;

    public List() {
        this.list = new Stack<>();
    }

    @Override
    public void add(T v) {
        this.list.push(v);
    }

    @Override
    public T pop() {return list.pop();}

    @Override
    public String toString() {
        return this.list.toString();
    }

    @Override
    public String toLog() {
        String text = "";
        for (T item : list) {
            text += item.toString() + "\n";
        }
        return text;
    }
}
