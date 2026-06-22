package com.cloudbrain.outpatient.infrastructure.search;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

/**
 * Service for indexing and searching EMR documents in Elasticsearch via the REST API.
 * Supports full-text multi_match search and patient-scoped term queries.
 */
@Service
public class EmrSearchService {

    private static final String ES_URL = "http://localhost:9200";
    private static final String INDEX = "emr";

    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper;

    public EmrSearchService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Indexes an EMR document in Elasticsearch, upserting by document ID.
     */
    public void index(EmrDocument doc) {
        try {
            String json = mapper.writeValueAsString(doc);
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(ES_URL + "/" + INDEX + "/_doc/" + doc.getId()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            http.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to index EMR", e);
        }
    }

    /**
     * Retrieves a single EMR document from Elasticsearch by its ID, or null if not found.
     */
    public EmrDocument findById(String emrId) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(ES_URL + "/" + INDEX + "/_doc/" + emrId))
                    .GET().build();
            var resp = http.send(req, HttpResponse.BodyHandlers.ofString());
            Map<String, Object> map = mapper.readValue(resp.body(),
                    new TypeReference<>() {});
            return mapper.convertValue(map.get("_source"), EmrDocument.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Searches EMR documents by keyword across the chiefComplaint and presentIllness fields.
     */
    public List<EmrDocument> searchByKeyword(String keyword) {
        try {
            String body = """
                    {"query":{"multi_match":{"fields":["chiefComplaint","presentIllness"],"query":"%s"}}}
                    """.formatted(keyword);
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(ES_URL + "/" + INDEX + "/_search"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            var resp = http.send(req, HttpResponse.BodyHandlers.ofString());
            Map<String, Object> map = mapper.readValue(resp.body(),
                    new TypeReference<>() {});
            List<Map<String, Object>> hits = (List<Map<String, Object>>)
                    ((Map<String, Object>) map.get("hits")).get("hits");
            return hits.stream()
                    .map(h -> mapper.convertValue(h.get("_source"), EmrDocument.class))
                    .toList();
        } catch (Exception e) {
            return List.of();
        }
    }

    /**
     * Searches EMR documents by exact patient ID match using a term query.
     */
    public List<EmrDocument> searchByPatient(String patientId) {
        try {
            String body = """
                    {"query":{"term":{"patientId":"%s"}}}
                    """.formatted(patientId);
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(ES_URL + "/" + INDEX + "/_search"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            var resp = http.send(req, HttpResponse.BodyHandlers.ofString());
            Map<String, Object> map = mapper.readValue(resp.body(),
                    new TypeReference<>() {});
            List<Map<String, Object>> hits = (List<Map<String, Object>>)
                    ((Map<String, Object>) map.get("hits")).get("hits");
            return hits.stream()
                    .map(h -> mapper.convertValue(h.get("_source"), EmrDocument.class))
                    .toList();
        } catch (Exception e) {
            return List.of();
        }
    }
}
