package com.cloudbrain.aiassistant.interfaces.ws;

import com.cloudbrain.aiassistant.application.AiAssistantService;
import com.cloudbrain.aiassistant.domain.entity.DiagnosticSession;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class AiAssistantWebSocketHandler {

    private final AiAssistantService aiService;
    private final SimpMessagingTemplate messagingTemplate;

    public AiAssistantWebSocketHandler(AiAssistantService aiService,
                                       SimpMessagingTemplate messagingTemplate) {
        this.aiService = aiService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/start-session")
    @SendTo("/topic/session")
    public DiagnosticSession startSession(Map<String, String> request) {
        return aiService.startSession(
                request.get("consultationId"),
                request.get("doctorId"));
    }

    @MessageMapping("/transcript")
    public void processTranscript(Map<String, String> msg) {
        String sessionId = msg.get("sessionId");
        String text = msg.get("text");
        String suggestion = aiService.processText(sessionId, text);

        // 推送回同一个 session 的订阅
        messagingTemplate.convertAndSend("/topic/suggestion/" + sessionId,
                Map.of("text", suggestion));
    }

    @MessageMapping("/end-session")
    @SendTo("/topic/session")
    public DiagnosticSession endSession(Map<String, String> msg) {
        return aiService.endSession(msg.get("sessionId"));
    }
}
