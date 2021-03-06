package it.besil.tutorials.jwebsample.helloworld;

import it.besil.jweb.app.JWebApp;
import it.besil.jweb.app.handlers.JWebHandler;
import it.besil.jweb.app.protocol.answer.SuccessAnswer;
import it.besil.jweb.app.protocol.payloads.EmptyPayload;
import it.besil.jweb.app.resources.HttpMethod;
import it.besil.jweb.app.resources.JWebController;
import it.besil.jweb.server.conf.JWebConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by besil on 03/08/2016.
 */
public class HelloWorldApp extends JWebApp {
    private Logger log = LoggerFactory.getLogger(HelloWorldApp.class);

    public HelloWorldApp(JWebConfiguration jwebConf) {
        super(jwebConf);
    }

    @Override
    public List<? extends JWebController> getControllers() {
        return Arrays.asList(new JWebController(getJWebConf()) {
            public HttpMethod getMethod() {
                return HttpMethod.get;
            }

            public JWebHandler getHandler() {
                return new HelloWorldHandler();
            }

            public String getPath() {
                return "/hello";
            }
        });
    }

    public class HelloWorldHandler extends JWebHandler<EmptyPayload, SuccessAnswer> {
        public HelloWorldHandler() {
            super(EmptyPayload.class, SuccessAnswer.class);
        }

        @Override
        public SuccessAnswer process(EmptyPayload payload) {
            log.info("Hit /hello");
            return new SuccessAnswer("hello world");
        }
    }
}
