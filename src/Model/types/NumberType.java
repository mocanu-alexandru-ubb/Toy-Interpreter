package Model.types;

import Model.values.NumberValue;
import Model.values.Value;

public final class NumberType implements Type {
    @Override
    public boolean equals(Object toCompare) {
        return toCompare instanceof NumberType;
    }

    @Override
    public String toString() {
        return "number";
    }

    @Override
    public Value defaultValue() {
        return new NumberValue(0);
    }
}
