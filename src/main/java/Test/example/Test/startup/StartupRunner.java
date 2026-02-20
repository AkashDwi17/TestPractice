package Test.example.Test.startup;

import Test.example.Test.dto.GenerateWebhookResponse;
import Test.example.Test.service.SqlSolverService;
import Test.example.Test.service.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private WebhookService webhookService;

    @Autowired
    private SqlSolverService sqlSolverService;

    @Override
    public void run(String... args) {

        try {

            // 🔹 Replace with YOUR real details
            String name = "Akash Dwivedi";
            String regNo = "REG12347";
            String email = "your-email@example.com";

            System.out.println("Calling Generate Webhook API...");

            GenerateWebhookResponse response =
                    webhookService.generateWebhook(name, regNo, email);

            System.out.println("Webhook URL: " + response.getWebhook());
            System.out.println("Access Token: " + response.getAccessToken());

            String finalSql =
                    sqlSolverService.getFinalSqlQuery(regNo);

            System.out.println("Final SQL Query: " + finalSql);

            System.out.println("Submitting final query...");

            String result =
                    webhookService.submitFinalQuery(
                            response.getWebhook(),
                            response.getAccessToken(),
                            finalSql
                    );

            System.out.println("Submission Response: " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}