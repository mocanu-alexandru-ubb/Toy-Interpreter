package Model.stmt;

import Model.PrgState;
import Model.adt.Dict;
import Model.types.Type;
import exc.MyException;

public class CompStmt implements IStmt {
    final IStmt first;
    final IStmt second;

    public CompStmt(IStmt first, IStmt second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public PrgState execute(PrgState state) {
        var stk = state.getExeStack();
        stk.push(second);
        stk.push(first);
        return null;
    }

    @Override
    public String toString() {
        return this.first.toString() + " | " + this.second.toString();
    }

    @Override
    public String toLog() {
        return this.first.toLog() + " |\n" + this.second.toLog();
    }

    @Override
    public Dict<String, Type> typecheck(Dict<String, Type> typeEnv) throws MyException {
        return second.typecheck(first.typecheck(typeEnv));
    }
}
