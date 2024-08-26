package com.jialin.BulletinBoard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * This service checks the content of Notes
 * By leveraging Google Perspective toxic content detector, we are able to detect the toxic contents in the notes
 */
@Slf4j
@Component
public class ContentCheckService {
    private static final String KEY = "AIzaSyDXGi2NO6g3YduukMnd4f6EZ_TBLlikWX8";
    private static final String PERSPECTIVE_URL =
            "https://commentanalyzer.googleapis.com/v1alpha1/comments:analyze?key=" + KEY;
    private static final String REQUEST_BODY = """
            {comment: {text: "%s"},
                   languages: ["en"],
                   requestedAttributes: {TOXICITY:{}} }
            """;


    /**
     * For any input content, validate if it's content by toxic score evaluated from Google Perspective
     *
     * @param content content of String to be checked by Google Perspective
     * @return assertion on if this content is toxic
     */
    public boolean isToxic(String content) {
        try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
            // Assemble POST request
            HttpPost postRequest = new HttpPost(PERSPECTIVE_URL);
            postRequest.setHeader("Content-Type", "application/json");
            postRequest.setEntity(new StringEntity(String.format(REQUEST_BODY, content)));

            CloseableHttpResponse response = closeableHttpClient.execute(postRequest);

            String responseString = EntityUtils.toString(response.getEntity());
            closeableHttpClient.close();

            // When toxic score is higher than 0.4, we decide it's toxic content
            return parseScore(responseString) > 0.4;
        } catch(IOException e) {
            log.error(e.toString());
            throw new RuntimeException();
        }
    }

    /**
     * For the given response, parse the toxic score and return
     *
     * @param response
     * @return toxic score of double type
     */
    private double parseScore(String response) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(response);
            return jsonNode.path("attributeScores")
                    .path("TOXICITY")
                    .path("summaryScore")
                    .path("value")
                    .asDouble();
        } catch (JsonProcessingException e) {
            log.error("Fail to parse toxic score for response: " + response);
            throw new RuntimeException(e);
        }
    }
}
