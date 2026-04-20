package com.smartcampus.config;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.net.URI;

public class Main {
    public static void main(String[] args) {

        // Load all resources (rooms, sensors, discovery, etc.)
        ResourceConfig config = new ResourceConfig()
                .packages("com.smartcampus");

        // API base path 
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create("http://localhost:8080/api/v1/"),
                config
        );

        System.out.println("Server running at http://localhost:8080/api/v1/");
    }
}