package Model.stmt;

import Model.PrgState;
import Model.adt.Dict;
import Model.types.Type;
import exc.MyException;

public class NOPStmt implements IStmt {
    @Override
    public PrgState execute(PrgState state) {
        return null;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public String toLog() {
        return this.toString();
    }

    @Override
    public Dict<String, Type> typecheck(Dict<String, Type> typeEnv) throws MyException {
        return typeEnv;
    }
}
