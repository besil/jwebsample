package it.besil.tutorials.jwebsample.loginlogout;

import it.besil.jweb.app.payloads.Payload;
import spark.Request;
import spark.Response;

/**
 * Created by besil on 08/08/2016.
 */
public class LoginPayload implements Payload {
    private String userid;
    private String password;
    private Request request;
    private Response response;

    @Override
    public void init(Request req, Response response) {
        this.userid = req.queryParams("userid") != null ? req.queryParams("userid") : "";
        this.password = req.queryParams("password") != null ? req.queryParams("password") : "";
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
