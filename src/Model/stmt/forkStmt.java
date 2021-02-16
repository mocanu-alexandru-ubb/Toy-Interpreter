package Model.stmt;

import Model.PrgState;
import Model.adt.Dict;
import Model.adt.MyStack;
import Model.types.Type;
import exc.MyException;

public class forkStmt implements IStmt {
    final IStmt stmt;

    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        var heap = state.getHeap();
        var out = state.getOut();
        var fileTable = state.getFileTable();

        var symTableClone = symTable.deepClone();
        var exeStackNew = new MyStack<IStmt>();
        exeStackNew.push(this.stmt);
        var newPrg = new PrgState(exeStackNew, symTableClone, out, fileTable, heap, this.stmt);

        return newPrg;
    }

    @Override
    public String toString() {
        return "fork(" + this.stmt.toString() + ")";
    }

    @Override
    public String toLog() {
        return "frok(" + this.stmt.toLog() + ")";
    }

    public forkStmt(IStmt stmt) {
        this.stmt = stmt;
    }

    @Override
    public Dict<String, Type> typecheck(Dict<String, Type> typeEnv) throws MyException {
        this.stmt.typecheck(typeEnv.deepClone());
        return typeEnv;
    }
    
}
