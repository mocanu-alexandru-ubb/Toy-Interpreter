import Model.adt.*;
import Model.exp.ArithExp;
import Model.exp.LogicExp;
import Model.exp.ValueExp;
import Model.values.BoolValue;
import Model.values.NumberValue;
import exc.BadOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static Model.values.BoolValue.constFalse;
import static Model.values.BoolValue.constTrue;

public class Tester {
    @Test
    public void testLogicAnd() {
        var result = (BoolValue) new LogicExp('&', new ValueExp(constTrue), new ValueExp(constFalse)).eval(new Dict<>(), new Dict<>());
        Assertions.assertEquals(constFalse, result);
    }

    @Test
    public void testLogicOr() {
        var result = (BoolValue) new LogicExp('|', new ValueExp(constTrue), new ValueExp(constFalse)).eval(new Dict<>(), new Dict<>());
        Assertions.assertEquals(constTrue, result);
    }

    @Test
    public void testArithAdd() {
        var result = (NumberValue) new ArithExp('+', new ValueExp(new NumberValue(5)), new ValueExp(new NumberValue(10))).eval(new Dict<>(), new Dict<>());
        Assertions.assertEquals(15, result.getValue());
    }
    @Test
    public void testArithSub() {
        var result = (NumberValue) new ArithExp('-', new ValueExp(new NumberValue(5)), new ValueExp(new NumberValue(10))).eval(new Dict<>(), new Dict<>());
        Assertions.assertEquals(-5, result.getValue());
    }
    @Test
    public void testArithMul() {
        var result = (NumberValue) new ArithExp('*', new ValueExp(new NumberValue(5)), new ValueExp(new NumberValue(10))).eval(new Dict<>(), new Dict<>());
        Assertions.assertEquals(50, result.getValue());
    }
    @Test
    public void testArithDiv() {
        var result = (NumberValue) new ArithExp('/', new ValueExp(new NumberValue(10)), new ValueExp(new NumberValue(5))).eval(new Dict<>(), new Dict<>());
        Assertions.assertEquals(2, result.getValue());
    }
    @Test
    public void testArithBadOP() {
        Assertions.assertThrows(BadOperator.class, () -> new ArithExp('=',new ValueExp(new NumberValue(5)), new ValueExp(new NumberValue(10))).eval(new Dict<>(), new Dict<>()));
    }
}
