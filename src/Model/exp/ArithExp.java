package Model.exp;
import Model.adt.Dict;
import Model.adt.IDict;
import Model.types.NumberType;
import Model.types.Type;
import Model.values.NumberValue;
import Model.values.Value;
import exc.BadOperator;
import exc.DivizionByZero;
import exc.MyException;

public class ArithExp implements Exp {
    final char op;
    final Exp e1, e2;

    public ArithExp(char op, Exp left, Exp right) {
        this.op = op;
        this.e1 = left;
        this.e2 = right;
    }

    public Value eval(IDict<String, Value> symTable, IDict<Integer, Value> heap) {
        NumberValue op1 = (NumberValue) e1.eval(symTable, heap);
        NumberValue op2 = (NumberValue) e2.eval(symTable, heap);

        switch (op){
            case('+'):
                return (new NumberValue(op1.getValue() + op2.getValue()));
            case('/'):
                if(op2.getValue()==0) throw new DivizionByZero("0");
                return (new NumberValue(op1.getValue() / op2.getValue()));
            case('-'):
                return (new NumberValue(op1.getValue() - op2.getValue()));
            case('*'):
                return (new NumberValue(op1.getValue() * op2.getValue()));
        }
        throw new BadOperator("Not a valid arithmetic operator");

    }

    public String toString() { return e1.toString() + " " + op + " " + e2.toString(); }

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
