package Model.exp;

import Model.adt.Dict;
import Model.adt.IDict;
import Model.types.NumberType;
import Model.types.Type;
import Model.values.Value;
import exc.BadOperator;
import exc.MyException;
import Model.values.NumberValue;

import static Model.values.BoolValue.constFalse;
import static Model.values.BoolValue.constTrue;

public class RelationalExp implements Exp {
    final String op;
    final Exp e1, e2;

    public RelationalExp(String op, Exp left, Exp right) {
        this.op = op;
        this.e1 = left;
        this.e2 = right;
    }

    public Value eval(IDict<String, Value> symTable, IDict<Integer, Value> heap) {
        NumberValue op1 = (NumberValue) e1.eval(symTable, heap);
        NumberValue op2 = (NumberValue) e2.eval(symTable, heap);

        switch (op) {
            case ("<"):
                return (op1.getValue() < op2.getValue()) ? constTrue : constFalse;
            case ("<="):
                return (op1.getValue() <= op2.getValue()) ? constTrue : constFalse;
            case (">"):
                return (op1.getValue() > op2.getValue()) ? constTrue : constFalse;
            case (">="):
                return (op1.getValue() >= op2.getValue()) ? constTrue : constFalse;
            case ("=="):
                return (op1.getValue() == op2.getValue()) ? constTrue : constFalse;
            case ("!="):
                return (op1.getValue() != op2.getValue()) ? constTrue : constFalse;
        }
        throw new BadOperator("Not a valid relational operator");
    }

    public String toString() {
        return e1.toString() + " " + op + " " + e2.toString();
    }

    @Override
    public Type typecheck(Dict<String, Type> typeEnv) throws MyException {
        Type t1, t2;
        t1 = e1.typecheck(typeEnv);
        t2 = e2.typecheck(typeEnv);

        if (!t1.equals(new NumberType())) throw new MyException("first op not integer");
        if (!t2.equals(new NumberType())) throw new MyException("second op not integer");

        return new NumberType();
    }
}
