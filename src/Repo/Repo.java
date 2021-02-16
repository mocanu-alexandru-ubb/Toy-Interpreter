package Repo;

import Model.PrgState;

import java.util.ArrayList;
import java.util.List;

public class Repo implements IRepo {
    List<PrgState> myPrgStates;

    public Repo() {
        myPrgStates = new ArrayList<>();
    }

    @Override
    public void addPrg(PrgState newPrg) {
        myPrgStates.add(newPrg);
    }

    @Override
    public List<PrgState> getPrgList() {
        return this.myPrgStates;
    }

    @Override
    public void setPrgList(List<PrgState> newPrgList) {
        this.myPrgStates = newPrgList;
    }
}
