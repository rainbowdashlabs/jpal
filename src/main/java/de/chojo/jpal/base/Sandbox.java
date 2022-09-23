package de.chojo.jpal.base;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class Sandbox extends PayPalApi {
    public Sandbox(String clientId, String secret) {
        super("api-m.sandbox", clientId, secret);
    }

}
