package it.besil.tutorials.jwebsample.echo;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import it.besil.jweb.server.JWebServer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import it.besil.jweb.testing.AbstractBehaviouralTest;

/**
 * Created by besil on 03/08/2016.
 */
public class BehaviouralEchoTest extends AbstractBehaviouralTest {

    @Before
    public void init() {
        this.addApp(new EchoApp());
    }

    @Test
    public void simpleTest() throws UnirestException {
        HttpResponse<JsonNode> resp = Unirest.get(getUrl("/echo"))
                .queryString("message", "ciao")
                .asJson();

        Assert.assertEquals("Echo: ciao", resp.getBody().getObject().getString("message"));
    }
}
