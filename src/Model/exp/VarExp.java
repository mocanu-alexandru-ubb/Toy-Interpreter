package Model.exp;

import Model.adt.Dict;
import Model.adt.IDict;
import Model.types.Type;
import Model.values.Value;
import exc.MyException;
import exc.VarNotDefined;

public class VarExp implements Exp {
    final String id;

    public VarExp(String id) {
        this.id = id;
    }

    public Value eval(IDict<String, Value> symTable, IDict<Integer, Value> heap) throws VarNotDefined{
        if (!symTable.isDefined(id)) throw new VarNotDefined("bad lookup");
        return symTable.lookup(id);
    }

    public String toString() {return id;}

    @Override
    public Type typecheck(Dict<String, Type> typeEnv) throws MyException {
        return typeEnv.lookup(id);
    }
}
