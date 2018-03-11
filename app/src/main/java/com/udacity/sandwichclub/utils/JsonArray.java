package com.udacity.sandwichclub.utils;

import java.util.ArrayList;
import java.util.List;

class JsonArray implements JsonValue {

	private final List<JsonValue> list = new ArrayList<>();

	void add(JsonValue value) {
		list.add(value);
	}

	int size() {
		return list.size();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (JsonValue value : list) {
			if (builder.length() != 0) builder.append(", ");
			builder.append(value.toString());
		}
		return "[" + builder.toString() + "]";
	}

	JsonValue get(int i) {
		return list.get(i);
	}
}
