package Model.stmt;

import Model.PrgState;
import Model.adt.Dict;
import Model.exp.Exp;
import Model.types.BoolType;
import Model.types.Type;
import Model.values.BoolValue;
import Model.values.Value;
import exc.MyException;
import exc.TypeMismatch;

public class WhileStmt implements IStmt {
    final Exp condition;
    final IStmt stmt;

    public WhileStmt(Exp condition, IStmt stmt) {
        this.condition = condition;
        this.stmt = stmt;
    }

    @Override
    public String toString() {
        return "while( " + this.condition.toString() + " ) { " + this.stmt.toString() + " }";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        Value result = this.condition.eval(symTable, state.getHeap());
        if (!result.getType().equals(new BoolType()))
            throw new TypeMismatch("Expression not evaluated to bool.");

        if (result.equals(BoolValue.constTrue)) {
            state.getExeStack().push(this);
            state.getExeStack().push(this.stmt);
        }

        return null;
    }

    @Override
    public String toLog() {
        return "while( " + this.condition.toString() + " ) { " + this.stmt.toString() + " }";
    }

    @Override
    public Dict<String, Type> typecheck(Dict<String, Type> typeEnv) throws MyException {
        if(!this.condition.typecheck(typeEnv).equals(new BoolType())) throw new MyException("expected boolean type for while condition");
        return this.stmt.typecheck(typeEnv);
    }

    
}
