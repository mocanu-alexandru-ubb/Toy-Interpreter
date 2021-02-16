package Model.stmt;

import Model.PrgState;
import Model.adt.Dict;
import Model.exp.Exp;
import Model.types.Type;
import Model.values.Value;
import exc.MyException;
import exc.TypeMismatch;

public class AssignStmt implements IStmt {
    final String id;
    final Exp exp;

    public AssignStmt(String id, Exp exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) {
        var table = state.getSymTable();
        if (!table.isDefined(id))
            throw new MyException("undefined variable!");
        Value result = exp.eval(table, state.getHeap());
        if (!table.lookup(id).getType().equals(result.getType()))
            throw new TypeMismatch("bad assignment!");
        table.update(id, result);

        return null;
    }

    @Override
    public String toString() {
        return id + "=" + exp.toString();
    }

    @Override
    public String toLog() {
        return this.toString();
    }

    @Override
    public Dict<String, Type> typecheck(Dict<String, Type> typeEnv) throws MyException {
        Type expectedType = typeEnv.lookup(id);
        Type actualType = exp.typecheck(typeEnv);
        if (expectedType.equals(actualType)) return typeEnv;
        throw new MyException("expected type: " + expectedType.toString() + " received: " + actualType.toString() + "\n");
    }
}
