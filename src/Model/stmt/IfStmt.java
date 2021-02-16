package Model.stmt;

import Model.PrgState;
import Model.adt.Dict;
import Model.exp.Exp;
import Model.types.BoolType;
import Model.types.NumberType;
import Model.types.Type;
import Model.values.BoolValue;
import Model.values.Value;
import exc.MyException;

public class IfStmt implements IStmt {
    final Exp cond;
    final IStmt ifStmt;
    final IStmt elseStmt;

    public IfStmt(Exp cond, IStmt ifStmt, IStmt elseStmt) {
        this.cond = cond;
        this.ifStmt = ifStmt;
        this.elseStmt = elseStmt;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        Value condVal = cond.eval(state.getSymTable(), state.getHeap());
        if (condVal.getType().equals(new NumberType()))
            throw new MyException("invalid condition");

        if (condVal == BoolValue.constTrue)
            state.getExeStack().push(ifStmt);
        else
            state.getExeStack().push(elseStmt);

        return null;
    }

    @Override
    public String toString() {
        return "If (" + cond.toString() + ") then " + ifStmt.toString() + " else " + elseStmt.toString();
    }

    @Override
    public String toLog() {
        return this.toString();
    }

    @Override
    public Dict<String, Type> typecheck(Dict<String, Type> typeEnv) throws MyException {
        if (cond.typecheck(typeEnv).equals(new BoolType())) {
            ifStmt.typecheck(typeEnv);
            elseStmt.typecheck(typeEnv);
            return typeEnv;
        }
        throw new MyException("expected bool value for if condition");
    }
}
