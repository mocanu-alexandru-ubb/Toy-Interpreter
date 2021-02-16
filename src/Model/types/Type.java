package Model.types;

import Model.values.Value;

public interface Type {
    boolean equals(Object toCompare);
    String toString();
    Value defaultValue();
}
