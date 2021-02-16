package Model.adt;

import java.util.ArrayDeque;
import java.util.Deque;

import Model.stmt.IStmt;

public class MyStack<T extends IStmt> implements IStack<T> {
    Deque<T> stack;

    public MyStack() {
        this.stack = new ArrayDeque<>();
    }

    @Override
    public T pop() {
        return this.stack.pop();
    }

    @Override
    public void push(T v) {
        this.stack.push(v);
    }

    @Override
    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    @Override
    public String toString(){
        return this.stack.toString();
    }

    @Override
    public String toLog() {
        String text = "";
        for (T item : stack) {
            text += item.toLog() + "\n";
        }
        return text;
    }
}
