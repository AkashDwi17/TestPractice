package Test.example.Test.service;

import Test.example.Test.dto.FinalQueryRequest;
import Test.example.Test.dto.GenerateWebhookRequest;
import Test.example.Test.dto.GenerateWebhookResponse;
import Test.example.Test.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WebhookService {

    @Autowired
    private WebClient webClient;

    public GenerateWebhookResponse generateWebhook(String name, String regNo, String email) {

        GenerateWebhookRequest request =
                new GenerateWebhookRequest(name, regNo, email);

        return webClient.post()
                .uri(Constants.GENERATE_WEBHOOK_URL)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GenerateWebhookResponse.class)
                .block();
    }

    public String submitFinalQuery(String webhookUrl,
                                   String accessToken,
                                   String finalQuery) {

        FinalQueryRequest request = new FinalQueryRequest(finalQuery);

        return webClient.post()
                .uri(webhookUrl)
                .header("Authorization", accessToken)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}