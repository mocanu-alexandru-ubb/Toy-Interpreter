package Model.types;

import Model.values.RefValue;
import Model.values.Value;

public class RefType implements Type {
    final Type inner;

    public RefType() {
        this.inner = null;
    }

    public RefType(Type inner) {
        this.inner = inner;
    }

    public Type getInner() {
        return this.inner;
    }

    @Override
    public boolean equals(Object toCompare) {
        return toCompare instanceof RefType;
    }

    @Override
    public Value defaultValue() {
        return new RefValue(0, inner);
    }
    
    @Override
    public String toString() {
        return "ref(" + this.inner.toString() + ")";
    }

}
