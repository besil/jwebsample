package it.besil.tutorials.jwebsample.loginlogout;

import it.besil.jweb.app.JWebApp;
import it.besil.jweb.app.answer.EmptyAnswer;
import it.besil.jweb.app.commons.session.SessionManager;
import it.besil.jweb.app.commons.session.SessionPayload;
import it.besil.jweb.app.handlers.JWebHandler;
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
    private Logger log = LoggerFactory.getLogger(LoginLogoutExampleApp.class);

    public LoginLogoutExampleApp(JWebConfiguration conf) {
        super(conf);
    }

    @Override
    public List<? extends JWebController> getControllers() {
        return Arrays.asList(
                new JWebController(getJWebConf()) {
                    @Override
                    public HttpMethod getMethod() {
                        return HttpMethod.post;
                    }

                    @Override
                    public JWebHandler getHandler() {
                        return new JWebHandler<LoginPayload, AuthAnswer>(LoginPayload.class, AuthAnswer.class) {
                            @Override
                            public AuthAnswer process(LoginPayload lp) {
                                String userid = lp.getUserid();
                                String password = lp.getPassword();
                                try {
                                    if (userid.equals("admin") && password.equals("password")) {
                                        try {
                                            SessionManager sm = new SessionManager(getJWebConf());
                                            sm.createSession(lp.getRequest(), lp.getResponse(), userid);
                                        } catch (Exception e) {
                                            log.warn("Error creating session");
                                            e.printStackTrace();
                                        }
                                        log.debug("User " + userid + " succesfully logged in");
                                        return new AuthAnswer("login succesful");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return new AuthAnswer("login error");
                            }
                        };
                    }

                    @Override
                    public String getPath() {
                        return "/login";
                    }
                },
                new JWebController(getJWebConf()) {
                    @Override
                    public HttpMethod getMethod() {
                        return HttpMethod.get;
                    }

                    @Override
                    public JWebHandler getHandler() {
                        return new JWebHandler<SessionPayload, AuthAnswer>(SessionPayload.class, AuthAnswer.class) {
                            @Override
                            public AuthAnswer process(SessionPayload p) {
                                return new AuthAnswer(p.getSessionId());
                            }
                        };
                    }

                    @Override
                    public String getPath() {
                        return "/api/home";
                    }
                },
                new JWebController(getJWebConf()) {
                    @Override
                    public HttpMethod getMethod() {
                        return HttpMethod.get;
                    }

                    @Override
                    public JWebHandler getHandler() {
                        return new JWebHandler<LogoutPayload, AuthAnswer>(LogoutPayload.class, AuthAnswer.class) {
                            @Override
                            public AuthAnswer process(LogoutPayload lp) {
                                try {
                                    new SessionManager(getJWebConf()).invalidateSession(lp.getRequest(), lp.getResponse());
                                    return new AuthAnswer("logout succesful");
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                return new AuthAnswer("logout error");
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

    public static class AuthAnswer extends EmptyAnswer {
        private String mail;

        public AuthAnswer(String mail) {
            this.mail = mail;
        }

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }
    }
}
