package it.besil.tutorials.jwebsample;

import it.besil.jweb.app.commons.DynamicContentApp;
import it.besil.jweb.server.JWebServer;
import it.besil.jweb.server.conf.JWebConfiguration;
import it.besil.tutorials.jwebsample.echo.EchoApp;
import it.besil.tutorials.jwebsample.filter.FilterApp;
import it.besil.tutorials.jwebsample.helloworld.HelloWorldApp;

import java.io.IOException;

/**
 * Created by besil on 03/08/2016.
 */
public class CustomMain {
    public static void main(String[] args) throws IOException {
        JWebConfiguration conf = new JWebConfiguration();
        JWebServer jweb = new JWebServer(conf);

        jweb.addApp(new DynamicContentApp("mapping"));
        jweb.addApp(new FilterApp());
        jweb.addApp(new HelloWorldApp());
        jweb.addApp(new EchoApp());
    }
}
