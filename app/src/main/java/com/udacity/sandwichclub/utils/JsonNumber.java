package com.udacity.sandwichclub.utils;

import android.os.Build;

class JsonNumber implements JsonValue {
	
	private final double number;
	
	JsonNumber(double d) {
		number = d;
	}
	
	@SuppressWarnings("unused")
    public double getNumber() {
		return number;
	}
	
	@Override
	public String toString() {
		return Double.toString(number);
	}
	
	@Override
	public int hashCode() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			return Double.hashCode(number);
		} else {
			return (int) number;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof JsonNumber)) return false;
		JsonNumber other = (JsonNumber) obj;
		return number == other.number;
	}
}
