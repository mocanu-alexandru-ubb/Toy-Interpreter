package Model.values;

import Model.types.StringType;
import Model.types.Type;
import exc.TypeMismatch;

public class StringValue implements Value {
    String value;

    public StringValue(String val) {
        this.value = val;
    }

    @Override
    public Type getType() {
        return new StringType();
    }

    public String getValue(){
        return this.value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public boolean equals(Value other) {
        if (!this.getType().equals(other.getType())) throw new TypeMismatch("Bad compparisson!");
        return this.value.equals(( (StringValue) other ).getValue());
    }
}
