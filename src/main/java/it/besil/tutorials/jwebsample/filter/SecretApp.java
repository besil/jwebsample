package it.besil.tutorials.jwebsample.filter;

import it.besil.jweb.app.JWebApp;
import it.besil.jweb.app.filter.FilterType;
import it.besil.jweb.app.filter.JWebFilter;
import it.besil.jweb.app.filter.JWebFilterHandler;
import it.besil.jweb.app.handlers.JWebHandler;
import it.besil.jweb.app.protocol.answer.Answer;
import it.besil.jweb.app.protocol.answer.ErrorAnswer;
import it.besil.jweb.app.protocol.answer.MessageAnswer;
import it.besil.jweb.app.protocol.answer.SuccessAnswer;
import it.besil.jweb.app.protocol.payloads.EmptyPayload;
import it.besil.jweb.app.resources.HttpMethod;
import it.besil.jweb.app.resources.JWebController;
import it.besil.jweb.server.conf.JWebConfiguration;
import spark.Request;
import spark.Response;
import spark.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by besil on 03/08/2016.
 */
public class SecretApp extends JWebApp {

    public SecretApp(JWebConfiguration jwebConf) {
        super(jwebConf);
    }

    @Override
    public List<? extends JWebFilter> getFilters() {
        return Arrays.asList(new JWebFilter() {
            @Override
            public JWebFilterHandler getHandler(final Service http) {
                return new JWebFilterHandler(http) {
                    public Answer process(Request request, Response response) {
                        if (!(request.queryParams().contains("user") && request.queryParams("user").equals("admin")))
                            return new ErrorAnswer("Not authorized");
                        return new SuccessAnswer("user authorized");
                    }
                };
            }

            @Override
            public String getPath() {
                return "/secret";
            }

            @Override
            public FilterType getType() {
                return FilterType.before;
            }
        });
    }    @Override
    public List<? extends JWebController> getControllers() {
        return Arrays.asList(new JWebController(getJWebConf()) {
            public HttpMethod getMethod() {
                return HttpMethod.get;
            }

            public JWebHandler getHandler() {
                return new JWebHandler<EmptyPayload, SecretAnswer>(EmptyPayload.class, SecretAnswer.class) {
                    @Override
                    public SecretAnswer process(EmptyPayload p) {
                        return new SecretAnswer("secret data here");
                    }
                };
            }

            public String getPath() {
                return "/secret";
            }
        });
    }

    public static class SecretAnswer extends MessageAnswer {
        public SecretAnswer(String message) {
            super(message);
        }
    }
}
