package Repo;

import java.util.List;

import Model.PrgState;

public interface IRepo {
    void addPrg(PrgState newPrg);
    List<PrgState> getPrgList();
    void setPrgList(List<PrgState> newPrgList);
}
