package repository;


import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repository {

    private Map<String, List<String>> repo = new HashMap<>();


    public synchronized boolean addPackage(String name, List<String> dependencies) {
        for (String dep: dependencies) {
            if (!repo.containsKey(dep)) {
                return false;
            }
        }
        repo.put(name, dependencies);
        return true;
    }

    public synchronized boolean removePackage(String name) {
        Collection<List<String>> allDeps = repo.values();
        for (List<String> depList : allDeps) {
            for (String str : depList) {
                if (str.equals(name)) {
                    return false;
                }
            }
        }
        repo.remove(name);
        return true;
    }

    public synchronized boolean packageInstalled(String name) {
        return repo.containsKey(name);
    }

}
