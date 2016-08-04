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
We will create 2 apps: 
1. _Echo server_: general usage of the library
2. _Secret app_: for understanding filters

All the examples uses anonymous inner classes.

Our main will be like this:
``` java
public static void main(String[] args) throws IOException {
JWebConfiguration conf = new JWebConfiguration();
        JWebServer jweb = new JWebServer(conf);
        jweb.addApp(new EchoApp());
        jweb.addApp(new SecretApp());
}
```

### Echo server
We will create a simple Echo Rest Server, with unit testing.

The EchoApp is (about 60 lines of code, 3 classes):
``` java
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
                return "/echo";
            }
        });
    }

    public static class EchoPayload implements Payload {
        private String message;

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
```
The controller defines the binding between the **GET** + **/hello** request and the **EchoHandler**,
which contains the business logic.

The handler consumes an **EchoPayload**, 
which extracts the queryParameter _message_ from the requests, 
and produces the echoed message.

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

The **AbstractBehaviouralTest** spawns a server on localhost.

### Secret app
This app shows the usage of filters, for allowing only certain users to access some resources.

``` java
public class SecretApp extends JWebApp {
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
                return "/secret";
            }
        });
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
                        return new SuccessAnswer("user", "authorized");
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
    }
```

The controller defines a very simple logic, which shows the secret data.

In the **FilterHandler** you have access to raw Request and Response objects.
Filters return Error or Success answers, which blocks or not the client navigation.