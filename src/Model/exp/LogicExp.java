package Model.exp;

import Model.adt.Dict;
import Model.adt.IDict;
import Model.types.BoolType;
import Model.types.Type;
import Model.values.BoolValue;
import Model.values.Value;
import exc.BadOperator;
import exc.MyException;

import static Model.values.BoolValue.constFalse;
import static Model.values.BoolValue.constTrue;

public class LogicExp implements Exp {
    final char op;
    final Exp e1, e2;

    public LogicExp(char op, Exp left, Exp right) {
        this.op = op;
        this.e1 = left;
        this.e2 = right;
    }

    public Value eval(IDict<String, Value> symTable, IDict<Integer, Value> heap) {
        BoolValue op1 = (BoolValue) e1.eval(symTable, heap);
        BoolValue op2 = (BoolValue) e2.eval(symTable, heap);

        switch (op) {
            case ('&'):
                if (op1 == constTrue && op2 == constTrue)
                    return constTrue;
                else
                    return constFalse;
            case ('|'):
                if (op1 == constFalse && op2 == constFalse)
                    return constFalse;
                else
                    return constTrue;
        }
        throw new BadOperator("Not a valid logic operator");
    }

    public String toString() {
        return e1.toString() + " " + op + " " + e2.toString();
    }

    @Override
    public Type typecheck(Dict<String, Type> typeEnv) throws MyException {
        Type t1, t2;
        t1 = e1.typecheck(typeEnv);
        t2 = e2.typecheck(typeEnv);

        if (!t1.equals(new BoolType())) throw new MyException("first op not integer");
        if (!t2.equals(new BoolType())) throw new MyException("second op not integer");

        return new BoolType();
    }
}
