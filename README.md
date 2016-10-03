# JWeb examples
Examples of [JWeb](http://besil.github.io/jweb/) framework.

Details for installation are available [here](http://besil.github.io/jweb/).

#### Table of contents ####

* [Description](#Description)
* [Built in apps](#built-in-apps)
  * [RestDocsApp](#restdocsapp)
  * [DynamicContentApp](#dynamiccontentapp)
  * [SessionManagementApp](#sessionmanagementapp)
* [Quickstart - write your own web apps](#quickstart---write-your-own-web-apps)
  * [Echo App](#echo-app)
  * [Secret App](#secret-app)


## Description
In JWeb, there are a very few concepts to remember: an **App** is the main block of your application. An App
incapsulates one or more **Controllers** and **Filters**.

**Controllers** defines the business logic for a resource. A Controller is made of 3 elements:

1. A _path_, which is the resource url exposed by the server

2. A _method_ (ie GET, POST, ...) for the resource

3. A _handler_, which contains the business logic associated to the specific resource for that method

An **handler** consumes a **Payload** and produce an **Answer**, according to the business logic.

**Payloads** are user defined POJO, which extracts parameters from http requests.
**Answers** are user defined POJO too, which are by default rendered as a JSON object.

**Filters** are very similar to handlers, but they are executed _before_ or _after_ each request/response.

## Built in apps
By default, jweb includes some apps out-of-the-box.

### RestDocsApp
This app automatically produces JSON Schemas from your defined controller.

This is a killer feature I've always been looking for: JSON schema is obtained scanning your classes automatically, foreach 
controller you install on the server, without writing any file other than your Java code.

In order to use it, just install the app
``` java
JWebConfiguration jwconf = new JWebConfiguration();
JWebServer jweb = new JWebServer(jwconf);
jweb.addApp(new RestDocsApp(jwconf));
// Your apps here
jweb.addApp(new SimpleApp(jwconf));
```

Go to _host:port_/**restdocs**, and you will get the index of all your endpoints:
``` json
{
  "docs" : [ "/simple" ]
}
```

Go to the specific one: _host:port_/**restdocs/simple**
``` json
{
  "path" : "/simple",
  "schema" : [ {
    "method" : "get",
    "payloadSchema" : {
      "type" : "object",
      "id" : "urn:jsonschema:it:besil:jweb:main:SimpleApp:SimplePayload",
      "properties" : {
        "simple" : {
          "type" : "string"
        },
        "secret" : {
          "type" : "integer"
        },
        "apps" : {
          "type" : "array",
          "items" : {
            "type" : "string"
          }
        },
        "date" : {
          "type" : "integer",
          "format" : "utc-millisec"
        },
        "numsofnums" : {
          "type" : "array",
          "items" : {
            "type" : "array",
            "items" : {
              "type" : "integer"
            }
          }
        }
      }
    },
    "answerSchema" : {
      "type" : "object",
      "id" : "urn:jsonschema:it:besil:jweb:main:SimpleApp:SimpleAnswer",
      "properties" : {
        "simple" : {
          "type" : "string"
        },
        "num" : {
          "type" : "integer"
        },
        "brd" : {
          "type" : "any"
        },
        "date" : {
          "type" : "integer",
          "format" : "utc-millisec"
        },
        "dates" : {
          "type" : "array",
          "items" : {
            "type" : "integer",
            "format" : "utc-millisec"
          }
        },
        "readers" : {
          "type" : "array",
          "items" : {
            "type" : "any"
          }
        },
        "payloads" : {
          "type" : "array",
          "items" : {
            "type" : "object",
            "id" : "urn:jsonschema:it:besil:jweb:main:SimpleApp:SimplePayload",
            "properties" : {
              "simple" : {
                "type" : "string"
              },
              "secret" : {
                "type" : "integer"
              },
              "apps" : {
                "type" : "array",
                "items" : {
                  "type" : "string"
                }
              },
              "date" : {
                "type" : "integer",
                "format" : "utc-millisec"
              },
              "numsofnums" : {
                "type" : "array",
                "items" : {
                  "type" : "array",
                  "items" : {
                    "type" : "integer"
                  }
                }
              }
            }
          }
        }
      }
    }
  }, {
    "method" : "post",
    "payloadSchema" : {
      "type" : "object",
      "id" : "urn:jsonschema:it:besil:jweb:main:SimpleApp:SimplePayload",
      "properties" : {
        "simple" : {
          "type" : "string"
        },
        "secret" : {
          "type" : "integer"
        },
        "apps" : {
          "type" : "array",
          "items" : {
            "type" : "string"
          }
        },
        "date" : {
          "type" : "integer",
          "format" : "utc-millisec"
        },
        "numsofnums" : {
          "type" : "array",
          "items" : {
            "type" : "array",
            "items" : {
              "type" : "integer"
            }
          }
        }
      }
    },
    "answerSchema" : {
      "type" : "object",
      "id" : "urn:jsonschema:it:besil:jweb:app:answer:MessageAnswer",
      "properties" : {
        "message" : {
          "type" : "string"
        }
      }
    }
  } ]
}
```

If you want to exclude a field from your answer, just use the ```@JsonIgnore``` annotation
to the field you don't want to be documented here. 

### DynamicContentApp
This app is very simple: it maps a json produced by an endpoint and
maps it to the client requested format (by _Accept_ header).
Right now, only **json** and **HTML** are supported.

Your controller will always return an _Answer_ object, which by default is translated into a json.

**DynamicContentApp** takes the json object produced, and apply an HTML template, dynamically rendering
the json key-values inside the template. 
The template engine used is [Jinjava](http://product.hubspot.com/blog/jinjava-a-jinja-for-your-java).

All you need to do in order to use the app is _adding it to the server_
``` java
JWebConfiguration conf = new JWebConfiguration();
JWebServer jweb = new JWebServer(conf);
jweb.addApp(new DynamicContentApp(conf, "mapping"));
```

and put a file named _mapping_ into your resource folder. An example is
``` java
# Hello route
/hello         templates/hello/hello.html
# Echo route
/echo          templates/echo/echo.html
# Login logout example
/login         templates/loginlogout/login.html
/logout        templates/loginlogout/logout.html
/api/home      templates/loginlogout/home.html
```
Every time the route **/hello** is hit and the request header contains
**Accept: text/html**, the json produced by the _/hello_ endpoint is mapped
to the _hello.html_ content. Otherwise, json is produced.

### SessionManagementApp
This app is very useful for managing user sessions.
Sessions details are configurable from the _jweb.properties_ file.

In order to start using this app, you must add it in your server:
``` java
JWebConfiguration conf = new JWebConfiguration();
JWebServer jweb = new JWebServer(conf);
jweb.addApp(new SessionManagerApp(conf, "/api/*"));
```

The constructor takes a string argument (_"/api/*"_), which means
that all endpoints starting with _/api/_ will be accessible 
only for logged in users.

Don't forget to add the configuration in the _jwebserver.properties_ file:
``` java
### Session App
session.db.url=jdbc:h2:mem:test;DB_CLOSE_DELAY=-1
session.db.user=admin
session.db.password=admin
session.timeout.duration=3600
session.cookie.name=jwebtoken
```

Using this configuration, sessions will be persisted in memory, which can be good.

To create session, you have to instantiate in your code the **SessionManager** object, which provides
utility methods to create, retrieve or delete a session from the client.
Session is stored using cookies.

So, your login handler could look like this:
``` java
new JWebHandler<LoginPayload>(LoginPayload.class) {
@Override
public Answer process(LoginPayload lp) {
    String userid = lp.getUserid();
    String password = lp.getPassword();
    try {
        if (userid.equals("admin") && password.equals("password")) {
            try {
                new SessionManager(getJWebConf()).createSession(lp.getRequest(), lp.getResponse(), userid);
            } catch (Exception e) {
                log.warn("Error creating session");
                e.printStackTrace();
            }
            log.debug("User " + userid + " succesfully logged in");
            return new SuccessAnswer("login", "succesful");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return new ErrorAnswer("login error");
}
```

Please find the complete example in the **loginlogout** package

## Quickstart - write your own web apps
We will create some apps: 

1. _Echo server_: general usage of the library

2. _Secret app_: for understanding filters

Our main will be like this:
``` java
public static void main(String[] args) throws IOException {
    JWebConfiguration conf = new JWebConfiguration();
    JWebServer jweb = new JWebServer(conf);
    jweb.addApp(new EchoApp());
    jweb.addApp(new SecretApp());
}
```

### Echo App
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
```

The controller defines a very simple logic, which shows the secret data.

In the **FilterHandler** you have access to raw Request and Response objects.
Filters return Error or Success answers, which blocks or not the client navigation.