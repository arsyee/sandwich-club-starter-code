package com.udacity.sandwichclub.utils;

import java.util.Map;

import java.util.HashMap;

class JsonSpecialChars implements JsonToken {
	private static final String WHITESPACES = " \t\r\n";
	
    static final JsonSpecialChars START_OBJECT = new JsonSpecialChars('{');
    static final JsonSpecialChars END_OBJECT = new JsonSpecialChars('}');
    static final JsonSpecialChars START_ARRAY = new JsonSpecialChars('[');
    static final JsonSpecialChars END_ARRAY = new JsonSpecialChars(']');
    static final JsonSpecialChars COMMA = new JsonSpecialChars(',');
    static final JsonSpecialChars COLON = new JsonSpecialChars(':');
	
	private static final Map<String, JsonToken> tokens = populateTokens();
	
	private final char jsonChar;
	private JsonSpecialChars(char ch) {
		jsonChar = ch;
	}
	
	private static Map<String, JsonToken> populateTokens() {
		Map<String, JsonToken> result = new HashMap<>();
		put(result, START_OBJECT);
		put(result, END_OBJECT);
		put(result, START_ARRAY);
		put(result, END_ARRAY);
		put(result, COMMA);
		put(result, COLON);
		return result;
	}
	
	private static void put(Map<String, JsonToken> result, JsonSpecialChars token) {
		result.put(asString(token.jsonChar), token);
	}

	static JsonToken getJsonSpecialChar(char ch) {
		if (!tokens.containsKey(asString(ch))) return null;
		return tokens.get(asString(ch));
	}
	
	static boolean isJsonSpecialChar(char ch) {
		return tokens.containsKey(asString(ch));
	}

	private static String asString(char ch) {
		return new String(new char[] {ch});
	}
	
	static boolean isWhitespace(char ch) {
		return WHITESPACES.indexOf(ch) != -1;
	}
	
	@Override
	public String toString() {
		return "Token: " + asString(jsonChar);
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
