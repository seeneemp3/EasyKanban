package TaskTracker.http;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final HttpClient httpClient;
    private final String url;
    private final String token;
    private URI uri;

    public KVTaskClient(String url) {
        this.url = url;
        try {
            httpClient = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "register"))
                    .build();
            HttpResponse.BodyHandler<String> stringBodyHandler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> res = httpClient.send(req, stringBodyHandler);
            if (res.statusCode() == 200) {
                this.token = res.body();
            } else throw new RuntimeException("no api key in response");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public KVTaskClient(String url, String token) {
        httpClient = HttpClient.newHttpClient();
        this.url = url;
        this.token = token;
    }

    public String load(String key) throws IOException, InterruptedException {
        if (key == null) return "null";
        uri = URI.create(url + "/load/" + key + "?API_TOKEN=" + token);
        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> res = httpClient.send(req, handler);

        String jstr = "";
        if (res.statusCode() == 200) {
            JsonElement jel = JsonParser.parseString(res.body());
            if (jel.isJsonArray()) {
                JsonArray jsonArray = jel.getAsJsonArray();
                jstr = jsonArray.toString();
            }
            if (jel.isJsonObject()) {
                JsonObject jsonObject = jel.getAsJsonObject();
                jstr = jsonObject.toString();
            }
        } else {
            throw new RuntimeException("Request sending error");
        }
        return jstr;
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        uri = URI.create(url + "save/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        if (response.statusCode() != 200) {
            throw new RuntimeException("Request sending error");
        }
    }
}
