package com.udacity.sandwichclub.utils;

class JsonParser {

	static JsonValue parse(String json) throws JsonException {
        JsonParser parser = new JsonParser(json);
        return parser.parse();
	}
	
	// private static final String TAG = JsonParser.class.getSimpleName();
	
	private final JsonTokenizer tokenizer;
	
	private JsonParser(String json) {
		tokenizer = new JsonTokenizer(json);
	}
	
	private JsonValue parse() throws JsonException {
        JsonValue result = getValue();
        JsonToken last = tokenizer.nextToken();
        if (last != null) throw new JsonException(String.format("Building finished at token %s", last));
        return result;
	}
	
    private JsonValue getValue() throws JsonException {
        JsonToken token = tokenizer.nextToken();

		if (token instanceof JsonValue) return (JsonValue) token; // a single value
		
		if (token == JsonSpecialChars.START_OBJECT) {
			return getObject();
		}
		
		if (token == JsonSpecialChars.START_ARRAY) {
			return getArray();
		}
		return null;
    }

	private JsonArray getArray() throws JsonException {
		JsonArray array = new JsonArray();
		while (true) {
			JsonValue value = getValue();
			if (value == null) {
				if (array.size() == 0 && tokenizer.currentToken() == JsonSpecialChars.END_ARRAY) {
					return array; // empty array
				} else {
					throw new JsonException(String.format("Unexpected %s", tokenizer.currentToken()));
				}
			} else {
				array.add(value);
				JsonToken token = tokenizer.nextToken();
				if (token == JsonSpecialChars.COMMA) continue;
				if (token == JsonSpecialChars.END_ARRAY) return array;
			}
		}
	}
    
	private JsonObject getObject() throws JsonException {
		JsonObject object = new JsonObject();
		while (true) {
			JsonValue key = getValue();
			if (key == null) {
				if (object.size() == 0 && tokenizer.currentToken() == JsonSpecialChars.END_OBJECT) {
					return object; // empty object
				} else {
					throw new JsonException(String.format("Unexpected %s", tokenizer.currentToken()));
				}
			} else {
				JsonToken token = tokenizer.nextToken();
				JsonString keyString;
				if (!(key instanceof JsonString)) {
					throw new JsonException(String.format("Unexpected value %s, need String!", key));
				}
				keyString = (JsonString) key;
				if (token != JsonSpecialChars.COLON) throw new JsonException(String.format("Unexpected %s, need colon!", tokenizer.currentToken()));
				JsonValue value = getValue();
				if (value == null) {
					throw new JsonException(String.format("Unexpected %s", tokenizer.currentToken()));
				}
				object.add(keyString, value);
				token = tokenizer.nextToken();
				if (token == JsonSpecialChars.COMMA) continue;
				if (token == JsonSpecialChars.END_OBJECT) return object;
			}
		}
	}
    
}

/*
switch (token) {
    case JsonTokenizer.START_OBJECT:
        stack.push(JsonTokenizer.START_OBJECT);
        valueExpected = false;
        break;
    case JsonTokenizer.END_OBJECT:
        element = stack.pop();
        if (!JsonTokenizer.START_OBJECT.equals(element)) throw new RuntimeException("Parse error: matching START_OBJECT not found.");
        if (stack.empty()) break; // it should be over
        element = stack.pop();
        switch (element) {
            case "name":
                break;
            default:
                throw new RuntimeException("Parse error: no such object: " + element);
        }
        break;
    case JsonTokenizer.START_ARRAY:
        stack.push(JsonTokenizer.START_ARRAY);
        tempList = new ArrayList<>();
        break;
    case JsonTokenizer.END_ARRAY:
        element = stack.pop();
        if (!JsonTokenizer.START_ARRAY.equals(element)) throw new RuntimeException("Parse error: matching START_ARRAY not found.");
        element = stack.pop();
        switch (element) {
            case "alsoKnownAs":
                alsoKnownAs = tempList;
                tempList = null;
                break;
            case "ingredients":
                ingredients = tempList;
                tempList = null;
                break;
            default:
                throw new RuntimeException("Parse error: array not expected.");
        }
        valueExpected = false;
        break;
    case JsonTokenizer.COLON:
        if (valueExpected) throw new RuntimeException("Parse error: already in value mode.");
        valueExpected = true;
        break;
    case JsonTokenizer.COMMA:
        // if (tempList == null && !valueExpected) throw new RuntimeException("Parse error: not in array mode");
        break;
    default:
        if (!valueExpected) {
            stack.push(token);
        } else {
            if (tempList != null) {
                tempList.add(token);
            } else {
                element = stack.pop();
                Log.d(TAG, String.format("Value has been parsed: %s = %s", element, token));
                switch (element) {
                    case "mainName":
                        mainName = token;
                        break;
                    case "placeOfOrigin":
                        placeOfOrigin = token;
                        break;
                    case "description":
                        description = token;
                        break;
                    case "image":
                        image = token;
                        break;
                    default:
                        throw new RuntimeException("Parse error: unexpected value for " + element);
                }
                valueExpected = false;
            }
        }
        break;
}
*/
