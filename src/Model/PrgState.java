package Model;

import java.io.BufferedReader;

import Model.adt.IDict;
import Model.adt.IList;
import Model.adt.IStack;
import Model.stmt.IStmt;
import Model.values.StringValue;
import Model.values.Value;
import exc.EmptyStack;
import exc.MyException;

public class PrgState {
    final IStack<IStmt> exeStack;
    final IDict<String, Value> symTable;
    final IList<Value> out;
    final IDict<StringValue, BufferedReader> fileTable;
    final IDict<Integer, Value> heap;
    final int prgId;
    static int maxPrgId = 0;
    final IStmt originalProgram; // optional field, but good to have

    private synchronized int getNewId() {
        return maxPrgId++;
    }

    public PrgState(IStack<IStmt> stk, IDict<String, Value> table, IList<Value> output,
            IDict<StringValue, BufferedReader> fileTable, IDict<Integer, Value> heap, IStmt orgProgram,
            int originalId) {
        exeStack = stk;
        symTable = table;
        out = output;
        originalProgram = orgProgram;
        this.fileTable = fileTable;
        this.heap = heap;
        this.prgId = originalId;
    }

    public PrgState(IStack<IStmt> stk, IDict<String, Value> table, IList<Value> output,
            IDict<StringValue, BufferedReader> fileTable, IDict<Integer, Value> heap, IStmt orgProgram) {
        exeStack = stk;
        symTable = table;
        out = output;
        originalProgram = orgProgram;
        this.fileTable = fileTable;
        this.heap = heap;
        this.prgId = this.getNewId();
    }

    public boolean isNotCompleted() {
        return !this.exeStack.isEmpty();
    }

    public PrgState oneStep() throws MyException{
        if (exeStack.isEmpty())
            throw new EmptyStack("prgstate stack is empty");
        IStmt crtStmt = exeStack.pop();
        return crtStmt.execute(this);
    }

    public IStack<IStmt> getExeStack() {
        return exeStack;
    }
    
    public IDict<Integer, Value> getHeap() {
        return heap;
    }

    public IDict<String, Value> getSymTable() {
        return symTable;
    }

    public IList<Value> getOut() {
        return out;
    }

    public IDict<StringValue, BufferedReader> getFileTable() {
        return this.fileTable;
    }

    public String toString() {
        return  "\n========== Program #" + this.prgId + " ==========" +
                "Execution Stack:\n" + exeStack.toString() +
                "\n\nSymbol Table:\n" + symTable.toString() +
                "\n\nOutput:\n" + out.toString() +
                "\n\nFileTable:\n" + fileTable.toString() +
                "\n\nHeapTable:\n" + heap.toString();
    }

    public String toLog() {
        return  "\n========== Program #" + this.prgId + " ==========" +
                "\n\nExecution Stack:\n" + exeStack.toLog() +
                "\n\nSymbol Table:\n" + symTable.toString() +
                "\n\nOutput:\n" + out.toString() +
                "\n\nFileTable:\n" + fileTable.toString() +
                "\n\nHeapTable:\n" + heap.toString();
    }
}