package test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
@RunWith(Suite.class)

@Suite.SuiteClasses({

        CreationTest.class,
        DeleteTest.class,
        ModificationTest.class
})
public class TestSuite {
}
