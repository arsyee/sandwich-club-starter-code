package com.udacity.sandwichclub.utils;

class JsonString implements JsonValue {
	
	private final String str;
	
	JsonString(String str) {
		this.str = str;
	}

	public String getString() {
		return str;
	}
	
	@Override
	public String toString() {
		return "'" + str + "'";
	}
	
	@Override
	public int hashCode() {
		return str.hashCode();
	}
	
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof JsonString)) return false;
		JsonString other = (JsonString) obj;
		return str.equals(other.str);
	}
}
