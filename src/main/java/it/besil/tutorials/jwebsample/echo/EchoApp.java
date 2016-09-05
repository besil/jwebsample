package it.besil.tutorials.jwebsample.echo;

import it.besil.jweb.app.JWebApp;
import it.besil.jweb.app.handlers.JWebHandler;
import it.besil.jweb.app.protocol.answer.MessageAnswer;
import it.besil.jweb.app.protocol.payloads.JsonBodyPayload;
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
                return HttpMethod.post;
            }

            public JWebHandler getHandler() {
                return new GetEchoHandler();
            }

            public String getPath() {
                return "/echo";
            }
        });
    }

    public static class EchoPayload extends JsonBodyPayload {
        private String message;

        @Override
        public void init(Request req, Response resp) {
//            Payload.super.init(req, resp);
            super.init(req, resp);
            System.out.println("This is how you call a default method implementation of an interface");
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class GetEchoHandler extends JWebHandler<EchoPayload, EchoAnswer> {
        public GetEchoHandler() {
            super(EchoPayload.class, EchoAnswer.class);
        }

        @Override
        public EchoAnswer process(EchoPayload ep) {
            return new EchoAnswer(ep.getMessage());
        }
    }

    public static class EchoAnswer extends MessageAnswer {
        String echo;

        public EchoAnswer(String message) {
            super(message);
            this.echo = message;
        }

        public String getEcho() {
            return echo;
        }

        public void setEcho(String echo) {
            this.echo = echo;
        }
    }
}
