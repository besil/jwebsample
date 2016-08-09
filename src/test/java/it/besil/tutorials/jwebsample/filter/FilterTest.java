package it.besil.tutorials.jwebsample.filter;

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
public class FilterTest extends AbstractBehaviouralTest {
    @Before
    public void prepare() {
        addApp(new SecretApp(getJWebConf()));
    }

    @Test
    public void filterTest() throws UnirestException {
        HttpResponse<JsonNode> home = Unirest.get(getUrl("/secret")).asJson();
        Assert.assertEquals("Not authorized", home.getBody().getObject().get("error"));

        home = Unirest.get(getUrl("/secret"))
                .queryString("user", "admin")
                .asJson();
        Assert.assertEquals("secret data here", home.getBody().getObject().get("secret"));
    }
}
