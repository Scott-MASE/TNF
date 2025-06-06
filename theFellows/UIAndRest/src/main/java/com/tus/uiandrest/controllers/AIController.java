package com.tus.uiandrest.controllers;

import com.tus.common.entity.Anomaly;
import com.tus.uiandrest.repositories.AnomalyRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI Analysis", description = "AI-based anomaly analysis using OpenRouter API")
public class AIController {

    private AnomalyRepository anomalyRepo;

    public AIController(AnomalyRepository anomalyRepo){
        this.anomalyRepo = anomalyRepo;
    }

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${OPENROUTER_API_KEY}")
    private String OPENROUTER_API_KEY;
    private static final String OPENROUTER_API_URL = "https://openrouter.ai/api/v1/chat/completions";

    @Operation(summary = "Get AI analysis of the latest 20 anomalies")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/analysis")
    public ResponseEntity<String> getAIAnalysis() {
        List<Anomaly> latestAnomalies = anomalyRepo.findTop20ByOrderByTimestampDesc();

        if (latestAnomalies.isEmpty()) {
            return ResponseEntity.ok("No anomaly data available for analysis.");
        }

        StringBuilder prompt = new StringBuilder();
        prompt.append("Please analyze the following network anomalies and provide a concise summary of patterns, causes, and solutions. ");
        prompt.append("Respond strictly in clean, semantic HTML without any inline CSS or styling. ");
        prompt.append("Structure your response using the following headings only: ");
        prompt.append("<h2>Patterns Observed</h2>, <h2>Possible Causes</h2>, and <h2>Suggested Solutions</h2>. ");
        prompt.append("Use appropriate HTML tags such as <p> for paragraphs, <ul>/<li> for lists, and <strong> or <b> for bold text. ");
        prompt.append("Do NOT use Markdown syntax such as asterisks (**), underscores (_), or backticks (`). ");
        prompt.append("Do NOT include any global or inline styles. I will apply styling separately using a stylesheet. ");
        prompt.append("Do not repeat the question or prompt—just provide the analysis.");




        for (Anomaly anomaly : latestAnomalies) {
            prompt.append(String.format(
                    "• [%s] Node %d (Network %d): %s (Traffic: %.2f Mbps)%n",
                    anomaly.getTimestamp(),
                    anomaly.getNodeId(),
                    anomaly.getNetworkId(),
                    anomaly.getAnomalyType(),
                    anomaly.getTrafficVolume()
            ));

        }


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(OPENROUTER_API_KEY);

        Map<String, Object> body = Map.of(
                "model", "deepseek/deepseek-r1-0528-qwen3-8b:free",
                "messages", List.of(Map.of(
                        "role", "user",
                        "content", prompt.toString()
                ))
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(OPENROUTER_API_URL, request, String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("AI analysis failed: " + e.getMessage());
        }
    }
}
