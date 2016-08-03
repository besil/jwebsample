package it.besil.tutorials.jwebsample.helloworld;

import it.besil.jweb.app.JWebApp;
import it.besil.jweb.app.answer.Answer;
import it.besil.jweb.app.answer.SuccessAnswer;
import it.besil.jweb.app.handlers.JWebHandler;
import it.besil.jweb.app.payloads.EmptyPayload;
import it.besil.jweb.app.resources.HttpMethod;
import it.besil.jweb.app.resources.JWebController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by besil on 03/08/2016.
 */
public class HelloWorldApp extends JWebApp {
    private Logger log = LoggerFactory.getLogger(HelloWorldApp.class);

    @Override
    public List<? extends JWebController> getControllers() {
        return Arrays.asList(new JWebController() {
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

    public class HelloWorldHandler extends JWebHandler<EmptyPayload> {
        public HelloWorldHandler() {
            super(EmptyPayload.class);
        }

        @Override
        public Answer process(EmptyPayload payload) {
            log.info("Hit /hello");
            return new SuccessAnswer("message", "hello world");
        }
    }
}
