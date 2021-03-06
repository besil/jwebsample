package it.besil.tutorials.jwebsample.helloworld;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import it.besil.jweb.testing.AbstractBehaviouralTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by besil on 03/08/2016.
 */
public class HelloWorldTest extends AbstractBehaviouralTest {
    @Before
    public void prepare() throws Exception {
        addApp(new HelloWorldApp(getJWebConf()));
    }

    @Test
    public void helloTest() throws UnirestException {
        HttpResponse<JsonNode> resp = Unirest.get(getUrl("/hello"))
                .asJson();

        Assert.assertEquals("hello world", resp.getBody().getObject().getString("message"));
    }
}
