package Model.stmt;

import Model.PrgState;
import Model.adt.Dict;
import Model.exp.Exp;
import Model.types.RefType;
import Model.types.Type;
import Model.values.RefValue;
import exc.MyException;
import exc.TypeMismatch;
import exc.VarNotDefined;

public class allocStmt implements IStmt {
    final String varName;
    final Exp expression;
    static int freeAddress = 1;

    @Override
    public PrgState execute(PrgState state) {
        var symTable = state.getSymTable();
        if (!symTable.isDefined(varName))
            throw new VarNotDefined("");

        if (!(symTable.lookup(varName).getType() instanceof RefType)) {
            throw new TypeMismatch("");
        }

        RefValue varC = (RefValue) symTable.lookup(varName);
        var result = expression.eval(symTable, state.getHeap());

        if (!(((RefType) varC.getType()).getInner().equals(result.getType()))) {
            throw new TypeMismatch("wrong assign");
        }

        state.getHeap().add(freeAddress, result);
        symTable.update(varName, new RefValue(freeAddress, result.getType()));
        freeAddress++;

        return null;
    }

    @Override
    public String toString() {
        return "new (" + varName + ", " + expression.toString() + ")";
    }

    @Override
    public String toLog() {
        return this.toString();
    }

    public allocStmt(String varName, Exp expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public Dict<String, Type> typecheck(Dict<String, Type> typeEnv) throws MyException {
        Type varType = typeEnv.lookup(varName);
        if (!varType.equals(new RefType())) throw new MyException("allocating for a non reference variable");
        Type expType = expression.typecheck(typeEnv);

        if (!((RefType) varType).getInner().equals(expType)) throw new MyException("allocation of different types");

        return typeEnv;
    }
    
}
