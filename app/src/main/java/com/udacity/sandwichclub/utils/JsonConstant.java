package com.udacity.sandwichclub.utils;

import java.util.Map;

import java.util.HashMap;

class JsonConstant implements JsonValue {
	
    private static final JsonConstant TRUE = new JsonConstant(true);
    private static final JsonConstant FALSE = new JsonConstant(false);
    private static final JsonConstant NULL = new JsonConstant(null);
	
	private static final Map<String, JsonToken> tokens = populateTokens();
	
	private final Boolean jsonBoolean;
	
	private JsonConstant(Boolean b) {
		jsonBoolean = b;
	}
	
	private static Map<String, JsonToken> populateTokens() {
		Map<String, JsonToken> result = new HashMap<>();
		put(result, TRUE);
		put(result, FALSE);
		put(result, NULL);
		return result;
	}
	
	private static void put(Map<String, JsonToken> result, JsonConstant token) {
		result.put(asString(token.jsonBoolean), token);
	}

	static JsonToken getConstant(Boolean b) {
		if (!tokens.containsKey(asString(b))) return null;
		return tokens.get(asString(b));
	}
	
	private static String asString(Boolean b) {
		return String.format("%s", b);
	}
	
	@Override
	public String toString() {
		return "Token: " + jsonBoolean.toString();
	}

	@SuppressWarnings("unused")
	public Boolean getValue() {
		return jsonBoolean;
	}
	
	// I don't need equals() - I use a factory, I have a single instance per token type
	
	/*
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof JsonSpecialChars)) return false;
		JsonSpecialChars other = (JsonSpecialChars) obj;
		if (token == null) return other.token == null;
		return token.equals(other.token);
	}

	@Override
	public int hashCode() {
		return token.hashCode();
	}
	*/
}
