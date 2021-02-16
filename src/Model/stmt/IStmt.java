package Model.stmt;

import Model.PrgState;
import Model.adt.Dict;
import Model.types.Type;
import exc.MyException;

public interface IStmt {
    PrgState execute (PrgState state) throws MyException;
    String toString();
    String toLog();
    Dict<String,Type> typecheck(Dict<String,Type> typeEnv) throws MyException;
}
