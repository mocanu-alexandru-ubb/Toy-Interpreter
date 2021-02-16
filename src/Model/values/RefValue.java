package Model.values;

import Model.types.RefType;
import Model.types.Type;
import exc.TypeMismatch;

public class RefValue implements Value {
    final int address;
    final Type locationType;

    public RefValue(int address, Type locationType){
        this.address = address;
        this.locationType = locationType;
    }

    @Override
    public Type getType() {
        return new RefType(this.locationType);
    }

    public int getAddr() {
        return address;
    }

    @Override
    public boolean equals(Value other) throws TypeMismatch {
        if (!(other instanceof RefType)) throw new TypeMismatch("msg");
        return ((RefValue) other).getAddr() == this.address;
    }
    
    @Override
    public String toString() {
        return "@" + address + "->" + locationType.toString();
    }
}
