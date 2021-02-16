package Model.stmt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import Model.PrgState;
import Model.adt.Dict;
import Model.exp.Exp;
import Model.types.StringType;
import Model.types.Type;
import Model.values.StringValue;
import Model.values.Value;
import exc.MyException;
import exc.TypeMismatch;
import exc.VarNotDefined;

public class openRFile implements IStmt {
    final Exp file;

    public openRFile(Exp file) {
        this.file = file;
    }

    @Override
    public PrgState execute(PrgState state) {
        var symTable = state.getSymTable();
        var fileTable = state.getFileTable();
        Value result = this.file.eval(symTable, state.getHeap());
        if (!result.getType().equals(new StringType()))
            throw new TypeMismatch("Wrong parameter for open file");

        if (fileTable.isDefined((StringValue) result))
            throw new VarNotDefined("File already opened");

        if (!new File(((StringValue) result).getValue()).exists())
            throw new MyException("File not found");

        BufferedReader fileReader;
        try {
            fileReader = new BufferedReader(new FileReader(((StringValue) result).getValue()));
        } catch (FileNotFoundException e) {
            throw new MyException(e.getMessage());
        }

        fileTable.add((StringValue) result, fileReader);

        return null;
    }

    @Override
    public String toString() {
        return "open " + this.file.toString();
    }

    @Override
    public String toLog() {
        return this.toString();
    }

    @Override
    public Dict<String, Type> typecheck(Dict<String, Type> typeEnv) throws MyException {
        if (!this.file.typecheck(typeEnv).equals(new StringType())) throw new MyException("trying to open from non string variable");
        return typeEnv;
    }
    
}
