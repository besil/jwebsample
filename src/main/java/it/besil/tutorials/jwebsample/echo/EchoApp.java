package it.besil.tutorials.jwebsample.echo;

import it.besil.jweb.app.JWebApp;
import it.besil.jweb.app.answer.Answer;
import it.besil.jweb.app.answer.SuccessAnswer;
import it.besil.jweb.app.handlers.JWebHandler;
import it.besil.jweb.app.payloads.Payload;
import it.besil.jweb.app.resources.HttpMethod;
import it.besil.jweb.app.resources.JWebController;
import spark.Request;

import java.util.Arrays;
import java.util.List;

/**
 * Created by besil on 03/08/2016.
 */
public class EchoApp extends JWebApp {

    @Override
    public List<? extends JWebController> getControllers() {
        return Arrays.asList(new JWebController() {
            public HttpMethod getMethod() {
                return HttpMethod.get;
            }

            public JWebHandler getHandler() {
                return new GetEchoHandler();
            }

            public String getPath() {
                return "/api/echo";
            }
        });
    }


    public static class EchoPayload implements Payload {
        private String message;

        public EchoPayload() {

        }

        public void init(Request req) {
            this.message = req.queryParams("message");
        }

        public String getMessage() {
            return message;
        }
    }

    public static class GetEchoHandler extends JWebHandler<EchoPayload> {
        public GetEchoHandler() {
            super(EchoPayload.class);
        }

        @Override
        public Answer process(EchoPayload ep) {
            return new SuccessAnswer("message", "Echo: " + ep.getMessage());
        }
    }
}
