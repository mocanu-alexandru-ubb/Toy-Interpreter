package View;

import Controller.Controller;
import Model.PrgState;
import Model.adt.*;
import Model.exp.ArithExp;
import Model.exp.RelationalExp;
import Model.exp.ValueExp;
import Model.exp.VarExp;
import Model.exp.rH;
import Model.stmt.*;
import Model.types.BoolType;
import Model.types.NumberType;
import Model.types.RefType;
import Model.types.StringType;
import Model.values.NumberValue;
import Model.values.StringValue;
import Model.values.Value;
import Repo.Repo;
import exc.MyException;


import static Model.values.BoolValue.constTrue;

import java.io.BufferedReader;
import java.io.File;

public class Main {
    private static CompStmt assamble (IStmt... statements) {
        if (statements.length == 1) {
            return new CompStmt(statements[0], new NOPStmt());
        }
        return new CompStmt(statements[0], assamble(java.util.Arrays.copyOfRange(statements, 1, statements.length)));
    }

    private static Controller createOneTimeUse(IStmt code) {
        try {
            code.typecheck(new Dict<>());
        }
        catch (MyException e) {
            code = new PrintStmt(new ValueExp(new StringValue(e.toString())));
        }

        Repo repo = new Repo();
        Controller ctr = new Controller(repo);

        MyStack<IStmt> exeStack = new MyStack<>();
        exeStack.push(code);
        IDict<String, Value> symTable = new Dict<>();
        IList<Value> out = new List<>();
        IDict<StringValue, BufferedReader> fileTable = new Dict<>();
        IDict<Integer, Value> heap = new Dict<>();
        PrgState myPrgState = new PrgState(exeStack, symTable, out, fileTable, heap, code);

        ctr.addProgram(myPrgState);

        return ctr;
    }

    public static void main(String[] args) {
        // int v; v=2;Print(v)
        IStmt ex1 = new CompStmt(new VarDeclStmt("v", new NumberType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new NumberValue(2))), new PrintStmt(new VarExp("v"))));

        // int a;int b; a=2+3*5;b=a+1;Print(b)
        IStmt ex2 = new CompStmt(new VarDeclStmt("a", new NumberType()),
                new CompStmt(new VarDeclStmt("b", new NumberType()),
                        new CompStmt(
                                new AssignStmt("a",
                                        new ArithExp('+', new ValueExp(new NumberValue(2)),
                                                new ArithExp('*', new ValueExp(new NumberValue(3)),
                                                        new ValueExp(new NumberValue(5))))),
                                new CompStmt(
                                        new AssignStmt("b",
                                                new ArithExp('+', new VarExp("a"), new ValueExp(new NumberValue(1)))),
                                        new PrintStmt(new VarExp("b"))))));

        // bool a; int v; a=true;(If a Then v=2 Else v=3);Print(v)
        IStmt ex3 = new CompStmt(new VarDeclStmt("a", new BoolType()),
                new CompStmt(new VarDeclStmt("v", new NumberType()),
                        new CompStmt(new AssignStmt("a", new ValueExp(constTrue)), new CompStmt(
                                new IfStmt(new VarExp("a"), new AssignStmt("v", new ValueExp(new NumberValue(2))),
                                        new AssignStmt("v", new ValueExp(new NumberValue(3)))),
                                new PrintStmt(new VarExp("v"))))));

        // bool a; a = 5;
        IStmt ex4 = new CompStmt(new VarDeclStmt("a", new BoolType()),
                new AssignStmt("a", new ValueExp(new NumberValue(5))));

        File f = new File("test.in");
        System.out.println(f.getAbsolutePath());

        // string varf; varf="test.in"; openRFile(varf); int varc; readFile(varf,varc);
        // print(varc); readFile(varf,varc); print(varc); closeRFile(varf);
        IStmt ex5 = new CompStmt(new VarDeclStmt("varf", new StringType()),
                new CompStmt(new AssignStmt("varf", new ValueExp(new StringValue("test.in"))),
                        new CompStmt(new openRFile(new VarExp("varf")),
                                new CompStmt(new VarDeclStmt("varc", new NumberType()),
                                        new CompStmt(new readFile(new VarExp("varf"), "varc"),
                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                        new CompStmt(new readFile(new VarExp("varf"), "varc"),
                                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                                        new closeRFile(new VarExp("varf"))))))))));

        //int v; v=4; (while (v>0) print(v);v=v-1);print(v)
        IStmt ex6 = new CompStmt(new VarDeclStmt("v", new NumberType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new NumberValue(4))), 
                    new CompStmt(
                        new WhileStmt(
                            new RelationalExp(">", new VarExp("v"), new ValueExp(new NumberValue(0))), 
                            new CompStmt(new PrintStmt(new VarExp("v")), 
                            new AssignStmt("v", new ArithExp('-', new VarExp("v"), new ValueExp(new NumberValue(1)))))),
                        new PrintStmt(new VarExp("v")))));


        IStmt ex7 = assamble(new VarDeclStmt("v", new RefType(new NumberType())),
            new allocStmt("v", new ValueExp(new NumberValue(20))),
            new VarDeclStmt("a", new RefType(new RefType(new NumberType()))),
            new allocStmt("a", new VarExp("v")),
            new allocStmt("v", new ValueExp(new NumberValue(55))),
            new allocStmt("v", new ValueExp(new NumberValue(66))),
            new PrintStmt(new rH(new VarExp("v"))),
            new PrintStmt(new rH(new rH(new VarExp("a")))));

        // int v; Ref int a; v=10;new(a,22);
        // fork(wH(a,30);v=32;print(v);print(rH(a)));   print(v);print(rH(a)) 
        IStmt ex8 = assamble( new VarDeclStmt("v", new NumberType()),
            new VarDeclStmt("a", new RefType(new NumberType())),
            new AssignStmt("v", new ValueExp(new NumberValue(10))),
            new allocStmt("a", new ValueExp(new NumberValue(22))),
            new forkStmt(assamble(
                new wH("a", new ValueExp(new NumberValue(30))),
                new AssignStmt("v", new ValueExp(new NumberValue(32))),
                new PrintStmt(new VarExp("v")),
                new PrintStmt(new rH(new VarExp("a")))
            )),
            new PrintStmt(new VarExp("v")),
            new PrintStmt(new rH(new VarExp("a")))
        );

        //int v; v=10; fork(while(v > 0) {print("1"); v = v-1;}); while (v>0) {print("1"); v = v-1;})
        IStmt ex9 = assamble(new VarDeclStmt("v", new NumberType()),
            new AssignStmt("v", new ValueExp(new NumberValue(100))),
            new forkStmt(assamble(
                new WhileStmt(new RelationalExp(">", new VarExp("v"), new ValueExp(new NumberValue(0))), new CompStmt(new PrintStmt(new ValueExp(new StringValue("1"))), new AssignStmt("v", new ArithExp('-', new VarExp("v"), new ValueExp(new NumberValue(1))))))
            )),
            new WhileStmt(new RelationalExp(">", new VarExp("v"), new ValueExp(new NumberValue(0))), new CompStmt(new PrintStmt(new ValueExp(new StringValue("2"))), new AssignStmt("v", new ArithExp('-', new VarExp("v"), new ValueExp(new NumberValue(1))))))
        );

        //int a; a = True
        IStmt ex10 = assamble(new VarDeclStmt("a", new NumberType()), new AssignStmt("a", new ValueExp(constTrue)));

        //ref int a; new(a, True)
        IStmt ex11 = assamble(new VarDeclStmt("a", new RefType(new NumberType())), new allocStmt("a", new ValueExp(constTrue)));

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        menu.addCommand(new RunCommand("1", ex1.toString(), createOneTimeUse(ex1)));
        menu.addCommand(new RunCommand("2", ex2.toString(), createOneTimeUse(ex2)));
        menu.addCommand(new RunCommand("3", ex3.toString(), createOneTimeUse(ex3)));
        menu.addCommand(new RunCommand("4", ex4.toString(), createOneTimeUse(ex4)));
        menu.addCommand(new RunCommand("5", ex5.toString(), createOneTimeUse(ex5)));
        menu.addCommand(new RunCommand("6", ex6.toString(), createOneTimeUse(ex6)));
        menu.addCommand(new RunCommand("7", ex7.toString(), createOneTimeUse(ex7)));
        menu.addCommand(new RunCommand("8", ex8.toString(), createOneTimeUse(ex8)));
        menu.addCommand(new RunCommand("9", ex9.toString(), createOneTimeUse(ex9)));
        menu.addCommand(new RunCommand("10", ex10.toString(), createOneTimeUse(ex10)));
        menu.addCommand(new RunCommand("11", ex11.toString(), createOneTimeUse(ex11)));
        menu.show();

    }
}
