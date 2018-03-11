package com.udacity.sandwichclub.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

class JsonObject implements JsonValue {

	private final Map<JsonString, JsonValue> map = new HashMap<>();
	
	void add(JsonString key, JsonValue value) {
		map.put(key, value);
	}
	
	JsonValue get(String key) {
		return map.get(new JsonString(key));
	}
	
	int size() {
		return map.size();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Entry<JsonString, JsonValue> entry : map.entrySet()) {
			if (builder.length() != 0) builder.append(", ");
			builder.append(String.format("%s : %s", entry.getKey(), entry.getValue()));
		}
		return "{" + builder.toString() + "}";
	}
}
