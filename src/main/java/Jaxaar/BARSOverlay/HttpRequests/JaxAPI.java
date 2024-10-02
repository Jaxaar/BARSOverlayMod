package Jaxaar.BARSOverlay.HttpRequests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.hypixel.api.adapters.*;
import net.hypixel.api.data.type.GameType;
import net.hypixel.api.data.type.ServerType;
import net.hypixel.api.exceptions.BadResponseException;
import net.hypixel.api.exceptions.BadStatusCodeException;
import net.hypixel.api.http.HTTPQueryParams;
import net.hypixel.api.reply.*;
import net.hypixel.api.util.Utilities;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class JaxAPI {
    static final String BASE_URL = "https://api.mojang.com/users/profiles/minecraft/";

    private final ApacheHttpClient httpClient;
    public static final Gson GSON = new GsonBuilder()
//            .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
//            .registerTypeAdapter(MojangPlayerReply.MojangPlayer.class, new MojangPlayerTypeAdapter())
            .create();

    /**
     * @param httpClient a {@link ApacheHttpClient} that implements the HTTP behaviour for communicating with the API
     */
    public JaxAPI(ApacheHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Shuts down the {@link ApacheHttpClient}
     */
    public void shutdown() {
        httpClient.shutdown();
    }


    public CompletableFuture<MojangPlayerData> getPlayerByName(String player) {
        return get(true, MojangPlayerData.class, player);
    }


    private <R extends AbstractReply> CompletableFuture<R> get(boolean authenticated, Class<R> clazz, String request) {
        return get(authenticated, clazz, request, null);
    }


    private <R extends AbstractReply> CompletableFuture<R> get(boolean authenticated, Class<R> clazz, String request, HTTPQueryParams params) {
        String url = BASE_URL + request;
        if (params != null) {
            url = params.getAsQueryString(url);
        }
//        System.out.println(url);
        CompletableFuture<JaxHttpResponse> future = httpClient.makeRequest(url);
//        System.out.println("Future launched");
        return future
                .thenApply(this::checkResponse)
                .thenApply(response -> {
//                    System.out.println(response.getBody());
                    R reply = GSON.fromJson(response.getBody(), clazz);

                    return reply;
                });
    }

//    private CompletableFuture<ResourceReply> requestResource(String resource) {
//        return httpClient.makeRequest(BASE_URL + "resources/" + resource)
//                .thenApply(this::checkResponse)
//                .thenApply(response -> checkReply(new ResourceReply(Utilities.GSON.fromJson(response.getBody(), JsonObject.class))));
//    }

    /**
     * Checks the status of the response and throws an exception if needed
     */
    private JaxHttpResponse checkResponse(JaxHttpResponse response) {
//        System.out.println("Resp");
//        System.out.println(response.getBody());
//        System.out.println(response.getStatusCode());

        if (response.getStatusCode() == 200) {
            return response;
        }

//        String cause;
//        try {
//            cause = GSON.fromJson(response.getBody(), JsonObject.class).get("cause").getAsString();
//        } catch (JsonSyntaxException ignored) {
//            cause = "Unknown (body is not json)";
//        }
        throw new BadStatusCodeException(response.getStatusCode(), "Couldn't find player");
    }
}

