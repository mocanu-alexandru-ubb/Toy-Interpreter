package Model.exp;

import Model.adt.Dict;
import Model.adt.IDict;
import Model.types.Type;
import Model.values.Value;
import exc.MyException;

public interface Exp {

    Value eval(IDict<String, Value> symTable, IDict<Integer, Value> heap);
    String toString();
    Type typecheck(Dict<String,Type> typeEnv) throws MyException;
}
