package Model.exp;

import Model.adt.Dict;
import Model.adt.IDict;
import Model.types.NumberType;
import Model.types.RefType;
import Model.types.Type;
import Model.values.RefValue;
import Model.values.Value;
import exc.MyException;
import exc.TypeMismatch;
import exc.VarNotDefined;

public class rH implements Exp {
    final Exp expression;

    @Override
    public Value eval(IDict<String, Value> symTable, IDict<Integer, Value> heap) {
        var result = expression.eval(symTable, heap);
        if (!result.getType().equals(new RefType(new NumberType())))
            throw new TypeMismatch("msg");

        RefValue resultAsRef = (RefValue) result;

        if (!heap.isDefined(resultAsRef.getAddr()))
            throw new VarNotDefined("heap address not allocated");

        return heap.lookup(resultAsRef.getAddr());
    }

    @Override
    public String toString() {
        return "rH(" + this.expression.toString() + ")";
    }

    public rH(Exp expression) {
        this.expression = expression;
    }

    @Override
    public Type typecheck(Dict<String, Type> typeEnv) throws MyException {
        Type t = expression.typecheck(typeEnv);

        if (!t.equals(new RefType())) throw new MyException("variable not a reference");

        return ((RefType) t).getInner();
    }
    
}
