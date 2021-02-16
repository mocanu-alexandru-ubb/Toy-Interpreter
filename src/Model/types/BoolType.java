package Model.types;

import Model.values.BoolValue;
import Model.values.Value;

public class BoolType implements Type {
    @Override
    public boolean equals(Object toCompare) {
        return toCompare instanceof BoolType;
    }

    @Override
    public String toString() {
        return "bool";
    }

    @Override
    public Value defaultValue() {
        return BoolValue.constFalse;
    }
}
