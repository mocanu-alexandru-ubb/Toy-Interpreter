package Controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import Model.PrgState;
import Model.adt.IDict;
import Model.values.RefValue;
import Model.values.Value;
import Model.types.NumberType;
import Model.types.RefType;
import Repo.Repo;
import exc.MyException;

public class Controller {
    final Repo repo;
    ExecutorService executor;

    Map<Integer, Value> safeGarbageCollector(List<Integer> symTableAddr, Map<Integer, Value> heap) {
        return heap.entrySet().stream()
        .filter(e -> symTableAddr.contains(e.getKey()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    List<Integer> getAddrFromSymTable(List<Collection<Value>> symTables, IDict<Integer, Value> heap) {
        List<Integer> addrs = new Stack<Integer>();
        for (Collection<Value> symTableValues : symTables)
            for (Value v : symTableValues) {
                if (v instanceof RefValue) {
                    RefValue v1 = (RefValue) v;
                    RefType t1 = (RefType) v1.getType();
                    while (v1.getAddr() != 0 && t1.getInner().equals(new RefType(new NumberType()))) {
                        addrs.add(v1.getAddr());
                        v1 = (RefValue) heap.lookup(v1.getAddr());
                        t1 = (RefType) v1.getType();
                    }
                    addrs.add(v1.getAddr());
                }
            }
        return addrs;

        // return symTableValues.stream()
        // .filter(v -> v instanceof RefValue)
        // .map(v -> { RefValue v1 = (RefValue) v; return v1.getAddr(); })
        // .collect(Collectors.toList());
    }

    List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream().filter(prg -> prg.isNotCompleted()).collect(Collectors.toList());
    }

    public Controller(Repo r) {
        repo = r;
    }

    public void addProgram(PrgState newPrg) {
        repo.addPrg(newPrg);
    }

    public void allStep() {
        allStep(false);
    }

    void oneStepForAllPrg(PrintWriter logFile, List<PrgState> prgList) {
        // before the execution, print the PrgState List into the log file

        logFile.println("!!! new step !!!\nbefore execution:");
        prgList.forEach(prg ->logFile.println(prg.toLog()));

        // RUN concurrently one step for each of the existing PrgStates
        // //-----------------------------------------------------------------------//prepare
        // the list of callables
        List<Callable<PrgState>> callList = prgList.stream()
        .map((PrgState p) -> (Callable<PrgState>) (() -> {return p.oneStep();}))
        .collect(Collectors.toList());
        // start the execution of the callables
        // it returns the list of new created PrgStates (namely threads)
        List<PrgState> newPrgList;
        try {
            newPrgList = executor.invokeAll(callList).stream().map(future -> {
                try {
                    return future.get();
                } catch (MyException | InterruptedException | ExecutionException e) {
                    System.out.println(e);
                    return null;
                }
            }).filter(p -> p != null).collect(Collectors.toList());
        } catch (InterruptedException e) {
            System.out.println("Something went wrong:\n" + e);
            return;
        }

        //add the new created threads to the list of existing threads 
        prgList.addAll(newPrgList);//------------------------------------------------------------------------------
    
        //after the execution, print the PrgState List into the log file
        logFile.println("after execution:\n");
        prgList.forEach(prg ->logFile.println(prg.toLog()));    
        //Save the current programs in the repository
        repo.setPrgList(prgList);
    }

    public void allStep(boolean printStates) {
        PrintWriter logFile;
        try {
            File f = new File("logFilePath.txt");
            System.out.println(f.getAbsolutePath());
            logFile = new PrintWriter(new BufferedWriter(new FileWriter("logFilePath.txt", true)));
        } catch (IOException e1) {
            System.out.println("Could not open file.");
            return;
        }
        logFile.println("\nBeginning of execution:\n");

        executor = Executors.newFixedThreadPool(2);
        // remove the completed programs
        List<PrgState> prgList = removeCompletedPrg(repo.getPrgList());

        while (prgList.size() > 0) {
            // HERE you can call conservativeGarbageCollector
            var heap = prgList.get(0).getHeap();
            List<Collection<Value>> symTables = new ArrayList<>();
            prgList.stream().forEach(prg -> symTables.add(prg.getSymTable().getContent().values()));
            heap.setContent(safeGarbageCollector(getAddrFromSymTable(symTables, heap), heap.getContent()));
            oneStepForAllPrg(logFile, prgList);
            // remove the completed programs
            prgList = removeCompletedPrg(repo.getPrgList());
        }
        executor.shutdownNow();
        // HERE the repository still contains at least one Completed Prg
        // and its List<PrgState> is not empty. Note that oneStepForAllPrg calls the method
        // setPrgList of repository in order to change the repository
        
        // update the repository state
        repo.setPrgList(prgList);
        logFile.close();
    }
}
