package com.cloudbrain.aillm.application;

import com.cloudbrain.aillm.inference.LlmAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Application service for LLM-based clinical inference.
 * Selects the first available {@link LlmAdapter} at startup and uses it to perform
 * diagnosis and summarization tasks. Falls back to a static response when the
 * adapter returns no result.
 */
@Service
public class LlmInferenceService {

    private static final Logger log = LoggerFactory.getLogger(LlmInferenceService.class);

    private final LlmAdapter adapter;

    public LlmInferenceService(List<LlmAdapter> adapters) {
        this.adapter = adapters.stream()
                .filter(LlmAdapter::isAvailable)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No LLM adapter available"));
        log.info("LLM provider: {}", adapter.getName());
    }

    /**
     * Generates a structured differential diagnosis based on the doctor-patient
     * dialog history. Falls back to a static message if the LLM call fails.
     */
    public String diagnose(String dialogHistory) {
        String system = """
                你是资深临床医学专家。根据医生与患者的对话，提供：
                【鉴别诊断】按可能性排序
                【建议检查】必要的辅助检查
                【用药提醒】如有药物相关注意事项

                原则：简洁结构化，危急重症标注⚠️，不确定时说明信息不足
                """;

        String result = adapter.chat(system, "以下为问诊记录，请给出鉴别诊断：\n" + dialogHistory);
        return result != null ? result : fallbackDiagnose(dialogHistory);
    }

    /**
     * Summarizes the doctor-patient dialog into a structured JSON medical record.
     */
    public String summarize(String dialogHistory) {
        String system = "你是医疗记录助手，将门诊对话整理为JSON格式结构化摘要：";
        String prompt = "将以下对话生成病历摘要JSON：\n" + dialogHistory;
        String result = adapter.chat(system, prompt);
        return result != null ? result : "{\"chiefComplaint\":\"（摘要生成失败）\"}";
    }

    private String fallbackDiagnose(String text) {
        return """
                【鉴别诊断】
                LLM 服务暂不可用，请参考患者主诉和体格检查结果综合判断。

                【建议检查】
                - 根据临床需要完善相关检查

                【用药提醒】
                - 注意药物过敏史
                """;
    }
}
