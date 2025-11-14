package com.raishxn.legendsarena.config;

import java.util.Map;

// A raiz do messages.yml é um 'messages'
public class MessagesConfig {

    private Map<String, String> messages;

    public Map<String, String> getMessages() { return messages; }
    public void setMessages(Map<String, String> messages) { this.messages = messages; }

    // Método auxiliar para obter uma mensagem por chave
    public String getMessage(String key) {
        return messages.getOrDefault(key, "Mensagem não encontrada: " + key);
    }
}