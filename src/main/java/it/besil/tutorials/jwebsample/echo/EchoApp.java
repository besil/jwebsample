package it.besil.tutorials.jwebsample.echo;

import it.besil.jweb.app.JWebApp;
import it.besil.jweb.app.handlers.JWebHandler;
import it.besil.jweb.app.protocol.answer.SuccessAnswer;
import it.besil.jweb.app.protocol.payloads.Payload;
import it.besil.jweb.app.resources.HttpMethod;
import it.besil.jweb.app.resources.JWebController;
import it.besil.jweb.server.conf.JWebConfiguration;
import spark.Request;
import spark.Response;

import java.util.Arrays;
import java.util.List;

/**
 * Created by besil on 03/08/2016.
 */
public class EchoApp extends JWebApp {
    public EchoApp(JWebConfiguration jwebConf) {
        super(jwebConf);
    }

    @Override
    public List<? extends JWebController> getControllers() {
        return Arrays.asList(new JWebController(getJWebConf()) {
            public HttpMethod getMethod() {
                return HttpMethod.get;
            }

            public JWebHandler getHandler() {
                return new GetEchoHandler();
            }

            public String getPath() {
                return "/echo";
            }
        });
    }

    public static class EchoPayload implements Payload {
        private String message;

        public void init(Request req, Response resp) {
            Payload.super.init(req, resp);
            System.out.println("This is how you call a default method implementation of an interface");
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class GetEchoHandler extends JWebHandler<EchoPayload, SuccessAnswer> {
        public GetEchoHandler() {
            super(EchoPayload.class, SuccessAnswer.class);
        }

        @Override
        public SuccessAnswer process(EchoPayload ep) {
            return new SuccessAnswer("Echo: " + ep.getMessage());
        }
    }
}
