# JWeb examples
Examples of [JWeb](http://besil.github.io/jweb/) framework.

Details for installation inere [here](http://besil.github.io/jweb/).

## Description
In JWeb, there are a very few concepts to remember: an **App** is the main block of your application. An App
incapsulates one or more **Controllers** and **Filters**.

**Controllers** defines the business logic for a resource. A Controller is made of 3 elements:

1. A _path_, which is the resource url exposed by the server

2. A _method_ (ie GET, POST, ...) for the resource

3. A _handler_, which contains the business logic associated to the specific resource for that method

An **handler** consumes a **Payload** and produce an **Answer**, according to the business logic.

**Payloads** are user defined POJO, which extracts parameters from http requests.
**Answers** are simple objects with a status code and a map of key-values to return.

**Filters** are very similar to handlers, but they are executed _before_ or _after_ each request/response.

## Quickstart
We will create a simple Echo Rest Server, with unit testing.


``` java
public static void main(String[] args) throws IOException {
    JWebConfiguration conf = new JWebConfiguration();
    JWebServer jweb = new JWebServer(conf);
    jweb.addApp(new EchoApp());
    // You can add here as many apps as you want
}
```

The EchoApp is (about 60 lines of code, 3 classes):
``` java
public class EchoApp extends JWebApp {
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

    @Override
        public List<? extends JWebController> getControllers() {
            // Every app can be made by different controllers.
            // Each controller must specify
            // the method, the path where to bind and the handler
            return Arrays.asList(new JWebController() {
                public HttpMethod getMethod() {
                    return HttpMethod.get;
                }

                public JWebHandler getHandler() {
                    return new GetEchoHandler();
                }

                public String getPath() {
                    return "/echo";
                }
            });
        }
}
```

Run it, and open [localhost](http://localhost:4567/echo?message=hello). You will see your message echoed back.

About **testing**, I found useful testing web services like this:
``` java
public class EchoTest extends AbstractBehaviouralTest {
    @Before
    public void init() {
        this.addApp(new EchoApp());
    }

    @Test
    public void simpleTest() throws UnirestException {
        HttpResponse<JsonNode> resp = Unirest.get(getUrl("/echo"))
                .queryString("message", "ciao")
                .asJson();
        Assert.assertEquals("Echo: ciao",
            resp.getBody().getObject().getString("message"));
    }
}
```