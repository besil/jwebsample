package it.besil.tutorials.jwebsample;

import it.besil.tutorials.jwebsample.echo.EchoTest;
import it.besil.tutorials.jwebsample.filter.FilterTest;
import it.besil.tutorials.jwebsample.helloworld.HelloWorldTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by besil on 03/08/2016.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({EchoTest.class, HelloWorldTest.class, FilterTest.class})
public class AllTests {

}
