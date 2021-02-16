package Model.values;

import Model.types.NumberType;
import Model.types.Type;
import exc.TypeMismatch;

public class NumberValue implements Value {
    int value;

    public NumberValue(int val) {
        this.value = val;
    }

    @Override
    public Type getType() {
        return new NumberType();
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    public boolean equals(Value other) throws TypeMismatch {
        if (!this.getType().equals(other.getType())) throw new TypeMismatch("Bad compparisson!");
        return this.value == ( (NumberValue) other ).getValue();
    }
}
