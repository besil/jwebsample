package it.besil.tutorials.jwebsample.loginlogout;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.besil.jweb.app.commons.session.SessionPayload;
import spark.Request;
import spark.Response;

/**
 * Created by besil on 08/08/2016.
 */
public class LoginPayload extends SessionPayload {
    private String userid;
    private String password;
    @JsonIgnore
    private Request request;
    @JsonIgnore
    private Response response;

    @Override
    public void init(Request req, Response response) {
        super.init(req, response);
        this.request = req;
        this.response = response;
    }

    public String getUserid() {
        return userid;
    }

    public String getPassword() {
        return password;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }
}
