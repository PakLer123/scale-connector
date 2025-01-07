package org.example;
import org.apache.catalina.startup.Tomcat;

public class TomcatServer {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080); // Set your desired port
        tomcat.getConnector(); // Initialize the connector

        // Add a web application (replace paths as needed)
        tomcat.addWebapp("", new java.io.File("src/main/webapp").getAbsolutePath());

        tomcat.start();
        tomcat.getServer().await();
    }
}

