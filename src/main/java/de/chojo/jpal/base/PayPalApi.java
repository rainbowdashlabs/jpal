package de.chojo.jpal.base;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.chojo.jpal.entities.auth.OAuth2Response;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class PayPalApi {
    private final String base;
    private final String clientId;
    private final String secret;

    HttpClient httpClient = HttpClient.newBuilder()
                                      .followRedirects(HttpClient.Redirect.NORMAL)
                                      .version(HttpClient.Version.HTTP_2)
                                      .build();

    private final ObjectMapper mapper = new ObjectMapper()
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public PayPalApi(String base, String clientId, String secret) {
        this.base = base;
        this.clientId = clientId;
        this.secret = secret;
    }

    protected String url() {
        return "https://%s.paypal.com".formatted(base);
    }

    protected URI uri(String path) {
        return URI.create("%s/%s".formatted(url(), path));
    }

    public HttpClient http() {
        return httpClient;
    }

    public String clientId() {
        return clientId;
    }

    public String secret() {
        return secret;
    }

    public String authHeader() {
        String auth = "%s:%s".formatted(clientId(), secret());
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    }

    public CompletableFuture<Optional<OAuth2Response>> auth() {
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(uri("v1/oauth2/token"))
                                         .header("Content-Type", "application/x-www-form-urlencoded")
                                         .header("Authorization", authHeader())
                                         .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
                                         .build();

        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpResponse<String> response = http().send(request, HttpResponse.BodyHandlers.ofString());
                return Optional.of(mapper.readValue(response.body(), OAuth2Response.class));
            } catch (IOException | InterruptedException e) {
                return Optional.empty();
            }
        });
    }
}
