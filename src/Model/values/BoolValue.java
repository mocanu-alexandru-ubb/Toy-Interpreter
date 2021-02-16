package Model.values;

import Model.types.BoolType;
import Model.types.Type;
import exc.TypeMismatch;

public enum BoolValue implements Value {
    constTrue, constFalse;

    public String toString() {
        if (this == constFalse)
            return "false";
        else
            return "true";
    }

    @Override
    public Type getType() {
        return new BoolType();
    }

    @Override
    public boolean equals(Value other) throws TypeMismatch {
        if (!this.getType().equals(other.getType())) throw new TypeMismatch("Bad Comparrison!");
        return this == other;
    }
}
