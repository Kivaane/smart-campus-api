package com.smartcampus;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class Main {
    
    private static final String BASE_URI = "http://localhost:8080/api/v1/";

    public static HttpServer startServer() {
        // Create ResourceConfig using your ApplicationConfig
        final ResourceConfig rc = ResourceConfig.forApplicationClass(
            com.smartcampus.config.AppConfig.class
        );

        // Create and start Grizzly HTTP server
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) {
        try {
            final HttpServer server = startServer();
            
            System.out.println("═══════════════════════════════════════════════════════════");
            System.out.println("  Smart Campus API Server Started Successfully!");
            System.out.println("═══════════════════════════════════════════════════════════");
            System.out.println();
            System.out.println("  🌐 Base URL:      " + BASE_URI);
            System.out.println("  📡 API Endpoint:  " + BASE_URI + "api/v1");
            System.out.println();
            System.out.println("  Test Discovery Endpoint:");
            System.out.println("     curl http://localhost:8080/api/v1");
            System.out.println();
            System.out.println("  Press Ctrl+C to stop the server.");
            System.out.println("═══════════════════════════════════════════════════════════");
            
            // Keep server running
            Thread.currentThread().join();
            
        } catch (Exception e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
