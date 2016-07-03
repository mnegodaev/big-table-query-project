package ru.mnegodaev.webapp.jetty;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Запуск приложения на встроенном сервере Jetty
 * TODO доделать веб-приложение
 */
public class Launcher {
    public static void main(String[] args) throws Exception {
        Server server = new Server();

        ServerConnector http = new ServerConnector(server);
        http.setHost("localhost");
        http.setPort(8080);
        http.setIdleTimeout(30000);

        WebAppContext context = new WebAppContext("../btq-1.0-SNAPSHOT.war", "/");

        server.addConnector(http);
        server.setHandler(context);

        server.start();
        server.join();
    }
}
