package Model.stmt;

import Model.PrgState;
import Model.adt.Dict;
import Model.types.Type;
import exc.MyException;

public class VarDeclStmt implements IStmt {
    final String id;
    final Type type;

    public VarDeclStmt(String id, Type type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        var table = state.getSymTable();
        if (table.isDefined(this.id))
            throw new MyException("variable already defined!");

        table.add(id, type.defaultValue());

        return null;
    }

    @Override
    public String toString() {
        return type.toString() + " " + id;
    }

    @Override
    public String toLog() {
        return this.toString();
    }

    @Override
    public Dict<String, Type> typecheck(Dict<String, Type> typeEnv) throws MyException {
        typeEnv.add(id, type);
        return typeEnv;
    }
}
