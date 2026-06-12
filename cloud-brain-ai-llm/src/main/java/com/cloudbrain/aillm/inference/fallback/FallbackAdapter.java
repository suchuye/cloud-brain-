package com.cloudbrain.aillm.inference.fallback;

import com.cloudbrain.aillm.inference.LlmAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "ai.llm.provider", havingValue = "fallback")
public class FallbackAdapter implements LlmAdapter {

    @Override
    public String chat(String systemPrompt, String userMessage) {
        var sb = new StringBuilder("【鉴别诊断】\n");
        String text = userMessage.toLowerCase();

        if (text.contains("头痛") || text.contains("头疼"))
            sb.append("1. 偏头痛（可能性：中）\n2. 紧张性头痛（可能性：中）\n");
        if (text.contains("胸痛") || text.contains("胸闷"))
            sb.append("⚠️ 1. 急性冠脉综合征（可能性：高）\n2. 心绞痛（可能性：中）\n");
        if (text.contains("咳嗽") || text.contains("发热"))
            sb.append("1. 上呼吸道感染（可能性：高）\n2. 肺炎待排（可能性：中）\n");
        if (text.contains("腹痛") || text.contains("腹泻"))
            sb.append("1. 急性胃肠炎（可能性：高）\n2. 阑尾炎待排（可能性：中）\n");

        sb.append("\n【建议检查】\n- 根据问诊情况完善相关检查\n- 关注生命体征变化\n");
        sb.append("\n【用药提醒】\n- 注意药物过敏史\n- 合并用药需评估相互作用");
        return sb.toString();
    }

    @Override
    public boolean isAvailable() { return true; }

    @Override
    public String getName() { return "Fallback(规则引擎)"; }
}
