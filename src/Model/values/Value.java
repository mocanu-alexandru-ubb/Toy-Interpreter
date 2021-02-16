package Model.values;

import Model.types.Type;
import exc.TypeMismatch;

//unknown value checked with instanceof
public interface Value {
    Type getType();
    String toString();
    boolean equals(Value other) throws TypeMismatch;
}
