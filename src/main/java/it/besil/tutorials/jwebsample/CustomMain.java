package it.besil.tutorials.jwebsample;

import it.besil.jweb.app.commons.DynamicContentApp;
import it.besil.jweb.app.commons.session.SessionManagerApp;
import it.besil.jweb.server.JWebServer;
import it.besil.jweb.server.conf.JWebConfiguration;
import it.besil.tutorials.jwebsample.echo.EchoApp;
import it.besil.tutorials.jwebsample.filter.SecretApp;
import it.besil.tutorials.jwebsample.helloworld.HelloWorldApp;
import it.besil.tutorials.jwebsample.loginlogout.LoginLogoutExampleApp;

import java.io.IOException;

/**
 * Created by besil on 03/08/2016.
 */
public class CustomMain {
    public static void main(String[] args) throws IOException {
        JWebConfiguration conf = new JWebConfiguration();
        JWebServer jweb = new JWebServer(conf);

        // By the library
        jweb.addApp(new DynamicContentApp(conf, "mapping"));
        jweb.addApp(new SessionManagerApp(conf, "/api/*"));

        // First demo
        jweb.addApp(new EchoApp());
        // Second demo
        jweb.addApp(new SecretApp());
        // Login Logout example
        jweb.addApp(new LoginLogoutExampleApp(conf));

        jweb.addApp(new HelloWorldApp());
    }
}
