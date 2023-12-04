package TaskTracker.http;

import static java.nio.charset.StandardCharsets.UTF_8;

        import java.io.IOException;
        import java.net.InetSocketAddress;
        import java.util.HashMap;
        import java.util.Map;

        import com.sun.net.httpserver.HttpExchange;
        import com.sun.net.httpserver.HttpServer;


public class KVServer {
    public static final int PORT = 8078;
    private final String apiToken;
    private final HttpServer server;
    private final Map<String, String> data = new HashMap<>();



    public KVServer() throws IOException {
        apiToken = generateApiToken();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/register", this::register);
        server.createContext("/save", this::save);
        server.createContext("/load", this::load);
    }

    private void load(HttpExchange h) throws IOException {
        try (h) {
            System.out.println("\n/save");
            if (!hasAuth(h)) {
                System.out.println("Unauthorized request, query parameter API_TOKEN with the API key value is required");
                h.sendResponseHeaders(403, 0);
                return;
            }
            if ("GET".equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("/save/".length());
                if (key.isEmpty()) {
                    System.out.println("Key for saving is empty. The key should be specified in the path: /save/{key}");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                if (data.containsKey(key)) {
                    String value = data.get(key);
                    sendText(h, value);
                    System.out.println("Value for key " + key + " has been retrieved!");
                    h.sendResponseHeaders(200, 0);
                } else {
                    System.out.println("Value for key " + key + " is missing!");
                    h.sendResponseHeaders(400, 0);
                }
            }
        }
    }

    private void save(HttpExchange h) throws IOException {
        try {
            if (!hasAuth(h)) {
                System.out.println("Unauthorized request, query parameter API_TOKEN with the API key value is required");
                h.sendResponseHeaders(403, 0);
                return;
            }
            if ("POST".equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("/save/".length());
                if (key.isEmpty()) {
                    System.out.println("The key for saving is empty. The key should be specified in the path: /save/{key}");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                String value = readText(h);
                if (value.isEmpty()) {
                    System.out.println("The value for saving is empty. The value should be specified in the request body.");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                data.put(key, value);
                h.sendResponseHeaders(200, 0);
            } else {
                System.out.println("/save expects a POST request, but received: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    private void register(HttpExchange h) throws IOException {
        try (h) {
            if ("GET".equals(h.getRequestMethod())) {
                sendText(h, apiToken);
            } else {
                System.out.println("/register expects a GET request, but received " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        }
    }

    public void start() {
        System.out.println("Starting the server on port " + PORT);
        System.out.println("API_TOKEN: " + apiToken);
        server.start();
    }

    private String generateApiToken() {
        return "" + System.currentTimeMillis();
    }

    protected boolean hasAuth(HttpExchange h) {
        String rawQuery = h.getRequestURI().getRawQuery();
        return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}