package Jaxaar.BARSOverlay.HttpRequests;

import net.hypixel.api.http.RateLimit;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApacheHttpClient {

//    private final UUID apiKey;
    private final ExecutorService executorService;
    private final HttpClient httpClient;

    public ApacheHttpClient(/*UUID apiKey*/) {
//        this.apiKey = apiKey;
        this.executorService = Executors.newCachedThreadPool();
        this.httpClient = HttpClientBuilder.create().build();
    }


    public CompletableFuture<JaxHttpResponse> makeRequest(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                org.apache.http.HttpResponse response = this.httpClient.execute(new HttpGet(url));
                return new JaxHttpResponse(response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity(), "UTF-8"), null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, this.executorService);
    }

//    public CompletableFuture<JaxHttpResponse> makeAuthenticatedRequest(String url) {
//        return CompletableFuture.supplyAsync(() -> {
//            HttpGet request = new HttpGet(url);
//            request.addHeader("API-Key", this.apiKey.toString());
//            try {
//                org.apache.http.HttpResponse response = this.httpClient.execute(request);
//                return new JaxHttpResponse(response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity(), "UTF-8"), createRateLimitResponse(response));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }, this.executorService);
//    }

    private RateLimit createRateLimitResponse(HttpResponse response) {
        if (response.getStatusLine().getStatusCode() != 200) {
            return null;
        }

        int limit = Integer.parseInt(response.getFirstHeader("RateLimit-Limit").getValue());
        int remaining = Integer.parseInt(response.getFirstHeader("RateLimit-Remaining").getValue());
        int reset = Integer.parseInt(response.getFirstHeader("RateLimit-Reset").getValue());
        return new RateLimit(limit, remaining, reset);
    }

    public void shutdown() {
        this.executorService.shutdown();
    }

}
