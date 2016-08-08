package it.besil.tutorials.jwebsample.loginlogout;

import it.besil.jweb.app.JWebApp;
import it.besil.jweb.app.answer.Answer;
import it.besil.jweb.app.answer.ErrorAnswer;
import it.besil.jweb.app.answer.SuccessAnswer;
import it.besil.jweb.app.commons.session.SessionManager;
import it.besil.jweb.app.handlers.JWebHandler;
import it.besil.jweb.app.payloads.EmptyPayload;
import it.besil.jweb.app.resources.HttpMethod;
import it.besil.jweb.app.resources.JWebController;
import it.besil.jweb.server.conf.JWebConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by besil on 08/08/2016.
 */
public class LoginLogoutExampleApp extends JWebApp {
    private final JWebConfiguration conf;
    private Logger log = LoggerFactory.getLogger(LoginLogoutExampleApp.class);

    public LoginLogoutExampleApp(JWebConfiguration conf) {
        this.conf = conf;
    }

    @Override
    public List<? extends JWebController> getControllers() {
        return Arrays.asList(
                new JWebController() {
                    @Override
                    public HttpMethod getMethod() {
                        return HttpMethod.post;
                    }

                    @Override
                    public JWebHandler getHandler() {
                        return new JWebHandler<LoginPayload>(LoginPayload.class) {
                            @Override
                            public Answer process(LoginPayload lp) {
                                String userid = lp.getUserid();
                                String password = lp.getPassword();
                                try {
                                    if (userid.equals("admin") && password.equals("password")) {
                                        try {
                                            new SessionManager(conf).createSession(lp.getRequest(), lp.getResponse(), userid);
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
                        };
                    }

                    @Override
                    public String getPath() {
                        return "/login";
                    }
                },
                new JWebController() {
                    @Override
                    public HttpMethod getMethod() {
                        return HttpMethod.get;
                    }

                    @Override
                    public JWebHandler getHandler() {
                        return new JWebHandler<EmptyPayload>(EmptyPayload.class) {
                            @Override
                            public Answer process(EmptyPayload p) {
                                return new SuccessAnswer("message", "welcome logged user");
                            }
                        };
                    }

                    @Override
                    public String getPath() {
                        return "/api/home";
                    }
                },
                new JWebController() {
                    @Override
                    public HttpMethod getMethod() {
                        return HttpMethod.get;
                    }

                    @Override
                    public JWebHandler getHandler() {
                        return new JWebHandler<LogoutPayload>(LogoutPayload.class) {
                            @Override
                            public Answer process(LogoutPayload lp) {
                                try {
                                    new SessionManager(conf).invalidateSession(lp.getRequest(), lp.getResponse());
                                    return new SuccessAnswer("logout", "succesful");
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                return new ErrorAnswer("logout error");
                            }
                        };
                    }

                    @Override
                    public String getPath() {
                        return "/logout";
                    }
                }
        );
    }
}
