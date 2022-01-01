package gg.inventories;

import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;

public class InventoriesCore {
    private static String CLIENT_SECRET = null;
//            public static String API_URL = "http://localhost:3000/api";
    public static String API_URL = "https://inventories.chasem.dev/api";

    public static Logger getLogger() {
        return Logger.getLogger("InventoriesCore");
    }

    public static void setClientSecret(String clientSecret) {
        CLIENT_SECRET = clientSecret;
    }


    public static void sendUpdateRequest(JsonObject playerData) {
        if (CLIENT_SECRET == null) {
            getLogger().warning("Failed to send Inventory Update Request, no Client Secret provided.");
            return;
        }
        HttpClient httpClient = HttpClientBuilder.create().build();

        try {
            String postUrl = API_URL + "/sync";
            HttpPost post = new HttpPost(postUrl);
            post.setHeader("Accept-Encoding", "UTF-8");
            post.setHeader("Content-type", "application/json");
            post.setHeader("Authorization", Base64.getEncoder().encodeToString(CLIENT_SECRET.getBytes(StandardCharsets.UTF_8)));
            StringEntity postingString = new StringEntity(playerData.toString(), "UTF-8"); //convert to json
            post.setEntity(postingString);
            HttpResponse response = httpClient.execute(post);
            if(response.getStatusLine().getStatusCode() == 403){
                getLogger().warning("Trouble syncing playerData, Maximum player count has been reached.");
            }else if(response.getStatusLine().getStatusCode() == 404){
                getLogger().warning(response.getStatusLine().getStatusCode() + " - " + response.getStatusLine().getReasonPhrase());
                getLogger().warning("No server was found matching your configured clientSecret.");
                getLogger().warning("Navigate to your dashboard and copy your clientSecret. https://inventories.chasem.dev/dashboard");
            }else if(response.getStatusLine().getStatusCode() != 200){
                getLogger().warning("Error when syncing playerData.");
                getLogger().warning(response.getStatusLine().getStatusCode() + " - " + response.getStatusLine().getReasonPhrase());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

