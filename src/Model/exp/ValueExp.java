package Model.exp;

import Model.adt.Dict;
import Model.adt.IDict;
import Model.types.Type;
import Model.values.Value;
import exc.MyException;

public class ValueExp implements Exp {
    final Value e;

    public ValueExp(Value val) {
        this.e = val;
    }

    @Override
    public Value eval(IDict<String, Value> symTable, IDict<Integer, Value> heap) {
        return e;
    }

    @Override
    public String toString() {
        return e.toString();
    }

    @Override
    public Type typecheck(Dict<String, Type> typeEnv) throws MyException {
        return e.getType();
    }
}
