package it.besil.tutorials.jwebsample.loginlogout;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.besil.jweb.app.payloads.Payload;
import spark.Request;
import spark.Response;

/**
 * Created by besil on 08/08/2016.
 */
public class LogoutPayload implements Payload {
    @JsonIgnore
    private Request request;
    @JsonIgnore
    private Response response;

    public void init(Request req, Response resp) {
        Payload.super.init(req, resp);
        this.request = req;
        this.response = resp;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }
}
