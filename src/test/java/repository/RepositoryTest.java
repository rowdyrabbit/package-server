package repository;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.EMPTY_LIST;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RepositoryTest {

    private Repository repo;

    @Before
    public void setup() {
        repo = new Repository();
    }

    @Test
    public void shouldNotInstallPackageIfDependenciesNotIntalled() {
        List<String> deps = new ArrayList<>();
        deps.add("libc");
        boolean success = repo.addPackage("gwt", deps);
        assertFalse(success);
        assertFalse(repo.packageInstalled("gwt"));
    }

    @Test
    public void shouldInstallPackageIfDependenciesInstalled() {
        repo.addPackage("libc", EMPTY_LIST);

        List<String> deps = new ArrayList<>();
        deps.add("libc");
        boolean success = repo.addPackage("gwt", deps);
        assertTrue(success);
        assertTrue(repo.packageInstalled("gwt"));
    }

    @Test
    public void shouldInstallPackageIfHasNoDependencies() {
        boolean success = repo.addPackage("libc", EMPTY_LIST);
        assertTrue(success);
        assertTrue(repo.packageInstalled("libc"));
    }

    @Test
    public void shouldRemovePackageIfNoOtherPackageIsDependentOnIt() {
        repo.addPackage("libc", EMPTY_LIST);
        boolean success = repo.removePackage("libc");
        assertTrue(success);
    }

    @Test
    public void shouldReturnTrueWhenRemovingPackageThatIsNotInstalled() {
        boolean success = repo.removePackage("not-installed");
        assertTrue(success);
    }

    @Test
    public void shouldNotRemovePackageIfAnotherPackageIsDependentOnIt() {
        repo.addPackage("libc", EMPTY_LIST);
        List<String> deps = new ArrayList<>();
        deps.add("libc");
        repo.addPackage("gwt", deps);
        boolean success = repo.removePackage("libc");
        assertFalse(success);
    }

    @Test
    public void shouldReturnTrueIfPackageIsInstalled() {
        repo.addPackage("libc", EMPTY_LIST);
        boolean installed = repo.packageInstalled("libc");
        assertTrue(installed);
    }

    @Test
    public void shouldReturnFalseIfPackageNotInstalled() {
        boolean installed = repo.packageInstalled("not-there");
        assertFalse(installed);
    }

}
