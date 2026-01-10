package connection;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class OrangeLiveObjectsReceiver {

    public static void main(String[] args) throws IOException {
        // 1. Create a server on Port 8080
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        System.out.println("Webhook Server started!");
        System.out.println("Listening on port " + port + "...");
        System.out.println("Waiting for Orange Live Objects data...");

        // 2. Define the endpoint context (e.g., http://localhost:8080/api/weather)
        server.createContext("/api/weather", new WeatherHandler());

        // 3. Start
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    // This class handles the incoming request from Orange
    static class WeatherHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            // Only accept POST requests (Webhooks are always POST)
            if ("POST".equals(t.getRequestMethod())) {

                // Read the JSON Body
                InputStream is = t.getRequestBody();
                String jsonBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);

                System.out.println("\n--- NEW DATA RECEIVED FROM ORANGE ---");
                System.out.println(jsonBody);

                // Send response "200 OK" to Orange so it knows we got it
                String response = "Received";
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                // Reject non-POST requests
                t.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            }
        }
    }
}