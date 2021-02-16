package Model.stmt;

import java.io.IOException;

import Model.PrgState;
import Model.adt.Dict;
import Model.exp.Exp;
import Model.types.StringType;
import Model.types.Type;
import Model.values.NumberValue;
import Model.values.StringValue;
import Model.values.Value;
import exc.MyException;
import exc.TypeMismatch;
import exc.VarNotDefined;

public class readFile implements IStmt {
    final Exp file;
    final String var;

    public readFile(Exp file, String var) {
        this.file = file;
        this.var = var;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        var fileTable = state.getFileTable();
        if (!symTable.isDefined(this.var))
            throw new VarNotDefined("Var not defined");

        Value result = this.file.eval(symTable, state.getHeap());
        if (!result.getType().equals(new StringType()))
            throw new TypeMismatch("msg");

        if (!fileTable.isDefined((StringValue) result))
            throw new MyException("File not opened");

        var file = fileTable.lookup((StringValue) result);
        String readLine;
        try {
            readLine = file.readLine();
        } catch (IOException e) {
            throw new MyException(e.getMessage());
        }

        if (readLine == null)
            symTable.update(var, new NumberValue(0));
        else
            symTable.update(var, new NumberValue(Integer.parseInt(readLine)));

        return null;
    }

    @Override
    public String toString() {
        return this.var + " = read(" + this.file.toString() + ")";
    }

    @Override
    public String toLog() {
        return this.toString();
    }

    @Override
    public Dict<String, Type> typecheck(Dict<String, Type> typeEnv) throws MyException {
        if (!this.file.typecheck(typeEnv).equals(new StringType())) throw new MyException("trying to read from non string variable");
        return typeEnv;
    }
    
}
