package it.besil.tutorials.jwebsample;

import it.besil.tutorials.jwebsample.echo.BehaviouralEchoTest;
import it.besil.tutorials.jwebsample.helloworld.BehaviouralHelloWorldTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by besil on 03/08/2016.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({BehaviouralEchoTest.class, BehaviouralHelloWorldTest.class})
public class AllTests {
}
