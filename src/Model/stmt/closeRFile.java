package Model.stmt;

import java.io.IOException;

import Model.PrgState;
import Model.adt.Dict;
import Model.exp.Exp;
import Model.types.StringType;
import Model.types.Type;
import Model.values.StringValue;
import Model.values.Value;
import exc.MyException;
import exc.TypeMismatch;

public class closeRFile implements IStmt {
    final Exp file;

    public closeRFile(Exp file) {
        this.file = file;
    }

    @Override
    public PrgState execute(PrgState state) {
        var symTable = state.getSymTable();
        var fileTable = state.getFileTable();

        Value result = this.file.eval(symTable, state.getHeap());

        if (!result.getType().equals(new StringType()))
            throw new TypeMismatch("msg");

        if (!fileTable.isDefined((StringValue) result))
            throw new MyException("file not opened");

        try {
            fileTable.lookup((StringValue) result).close();
            fileTable.remove((StringValue) result);
        } catch (IOException e) {
            throw new MyException(e.getMessage());
        }

        return null;
    }

    @Override
    public String toString() {
        return "close (" + this.file.toString() + ")";
    }

    @Override
    public String toLog() {
        return this.toString();
    }

    @Override
    public Dict<String, Type> typecheck(Dict<String, Type> typeEnv) throws MyException {
        if (!this.file.typecheck(typeEnv).equals(new StringType())) throw new MyException("trying to read from a non string variable");
        return typeEnv;
    }
    
}
