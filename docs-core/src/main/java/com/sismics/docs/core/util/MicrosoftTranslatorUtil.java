package com.sismics.docs.core.util;

import jakarta.json.Json;
import jakarta.json.JsonArray;

import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

public class MicrosoftTranslatorUtil {
    private static final String API_KEY;
    private static final String ENDPOINT;
    private static final String REGION;

    static {
        ResourceBundle bundle = ResourceBundle.getBundle("application");
        API_KEY = bundle.getString("microsoft.translator.apiKey");
        ENDPOINT = bundle.getString("microsoft.translator.endpoint");
        REGION = bundle.getString("microsoft.translator.region");
    }

    public static String translate(String text, String targetLang) throws Exception {
        // 构造 HTTP 请求
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT + "/translate?api-version=3.0&to=" + targetLang))
                .header("Ocp-Apim-Subscription-Key", API_KEY)
                .header("Ocp-Apim-Subscription-Region", REGION)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "[{\"Text\":\"" + text.replace("\"", "\\\"") + "\"}]"
                ))
                .build();

        // 处理响应
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("TranslationError: " + response.body());
        }

        // 解析响应 JSON
        JsonArray jsonArray = Json.createReader(new StringReader(response.body())).readArray();
        return jsonArray.getJsonObject(0).getJsonArray("translations")
                .getJsonObject(0).getString("text");
    }
}
