package it.besil.tutorials.jwebsample.filter;

import it.besil.jweb.app.JWebApp;
import it.besil.jweb.app.answer.Answer;
import it.besil.jweb.app.answer.ErrorAnswer;
import it.besil.jweb.app.answer.SuccessAnswer;
import it.besil.jweb.app.filter.FilterType;
import it.besil.jweb.app.filter.JWebFilter;
import it.besil.jweb.app.filter.JWebFilterHandler;
import it.besil.jweb.app.handlers.JWebHandler;
import it.besil.jweb.app.payloads.EmptyPayload;
import it.besil.jweb.app.resources.HttpMethod;
import it.besil.jweb.app.resources.JWebController;
import spark.Request;
import spark.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by besil on 03/08/2016.
 */
public class FilterApp extends JWebApp {

    @Override
    public List<? extends JWebController> getControllers() {
        return Arrays.asList(new JWebController() {
            public HttpMethod getMethod() {
                return HttpMethod.get;
            }

            public JWebHandler getHandler() {
                return new JWebHandler<EmptyPayload>(EmptyPayload.class) {
                    @Override
                    public Answer process(EmptyPayload p) {
                        return new SuccessAnswer("secret", "secret data here");
                    }
                };
            }

            public String getPath() {
                return "/home";
            }
        });
    }

    @Override
    public List<? extends JWebFilter> getFilters() {
        return Arrays.asList(new JWebFilter() {
            @Override
            public JWebFilterHandler getHandler(final Service http) {
                return new JWebFilterHandler(http) {
                    public Answer process(Request request) {
                        if (!(request.queryParams().contains("user") && request.queryParams("user").equals("admin")))
                            return new ErrorAnswer("Not authorized");
                        return new SuccessAnswer("user", "authorized");
                    }
                };
            }

            @Override
            public String getPath() {
                return "/home";
            }

            @Override
            public FilterType getType() {
                return FilterType.before;
            }
        });
    }
}
