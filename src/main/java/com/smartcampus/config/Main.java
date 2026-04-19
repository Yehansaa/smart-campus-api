package com.smartcampus.config;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.net.URI;

public class Main {
    public static void main(String[] args) {

        ResourceConfig config = new ResourceConfig().packages("com.smartcampus");

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create("http://localhost:8080/"),
                config
        );

        System.out.println("Server running at http://localhost:8080/");
    }
}