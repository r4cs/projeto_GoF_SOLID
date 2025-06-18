package com.br.ecommerce.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

// A anotação @Converter marca esta classe como um conversor JPA
@Converter
public class MapToJsonConverter implements AttributeConverter<Map<String, Object>, String> {

    // ObjectMapper é thread-safe e deve ser uma instância única
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Converte um Map<String, Object> para uma String JSON para armazenamento no banco de dados.
     * @param attribute O Map a ser convertido.
     * @return A representação JSON do Map, ou null se o Map for null.
     */
    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            // Registre o erro, mas retorne uma string JSON vazia para evitar falha.
            // Isso pode ser ajustado para lançar uma exceção ou outro tratamento de erro.
            System.err.println("Erro ao serializar Map para JSON: " + e.getMessage());
            return "{}"; // Retorna um objeto JSON vazio em caso de erro
        }
    }

    /**
     * Converte uma String JSON do banco de dados de volta para um Map<String, Object>.
     * @param dbData A String JSON a ser convertida.
     * @return O Map<String, Object> deserializado, ou um Map vazio se a string for nula/vazia ou ocorrer um erro.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return new HashMap<>(); // Retorna um mapa vazio para strings nulas ou vazias
        }
        try {
            // Usando Map.class para permitir tipos flexíveis de chave-valor dentro do JSON
            return objectMapper.readValue(dbData, Map.class);
        } catch (IOException e) {
            System.err.println("Erro ao deserializar JSON para Map: " + e.getMessage());
            return new HashMap<>();
        }
    }
}
