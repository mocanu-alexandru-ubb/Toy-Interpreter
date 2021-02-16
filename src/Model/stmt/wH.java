package Model.stmt;

import Model.PrgState;
import Model.adt.Dict;
import Model.exp.Exp;
import Model.types.NumberType;
import Model.types.RefType;
import Model.types.Type;
import Model.values.RefValue;
import exc.MyException;
import exc.TypeMismatch;
import exc.VarNotDefined;

public class wH implements IStmt {
    final String varName;
    final Exp expression;

    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        var heap = state.getHeap();

        if (!symTable.isDefined(this.varName))
            throw new VarNotDefined("ref value not defined");

        if (!symTable.lookup(this.varName).getType().equals(new RefType(new NumberType())))
            throw new TypeMismatch("trying to write to not ref value");

        RefValue storedVar = (RefValue) symTable.lookup(this.varName);

        if (!heap.isDefined(storedVar.getAddr()))
            throw new VarNotDefined("heap memmory not allocated");

        var result = this.expression.eval(symTable, heap);

        if (!result.getType().equals(((RefType) storedVar.getType()).getInner()))
            throw new TypeMismatch("writing to a different type");

        heap.update(storedVar.getAddr(), result);

        return null;
    }

    @Override
    public String toLog() {
        return this.toString();
    }

    @Override
    public String toString() {
        return "@" + this.varName + "=" + this.expression.toString();
    }

    public wH(String varName, Exp expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public Dict<String, Type> typecheck(Dict<String, Type> typeEnv) throws MyException {
        if (!typeEnv.lookup(varName).equals(new RefType())) throw new MyException("trying to write to a non reference type");
        RefType t = (RefType) typeEnv.lookup(varName);
        if (!t.getInner().equals(this.expression.typecheck(typeEnv))) throw new MyException("type mismatch");
        return typeEnv;
    }
    
}
