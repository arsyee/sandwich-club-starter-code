package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    public static Sandwich parseSandwichJson(String json) {
        Log.d(TAG, json);
        // need an instance so I can conveniently store a pointer to the current position
        try {
            return new JsonUtils(json).parse();
        } catch (RuntimeException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    private int i = 0;
    private String json = null;
    private String unprocessedJson = null;

    private JsonUtils(String json) {
        this.json = json;
        this.unprocessedJson = json;
    }

    private Sandwich parse() {
        String mainName = null;
        List<String> alsoKnownAs = null;
        String placeOfOrigin = null;
        String description = null;
        String image = null;
        List<String> ingredients = null;

        String token;
        Stack<String> stack = new Stack<>();
        List<String> tempList = null;
        String element;
        boolean valueExpected = false;

        while ((token = nextToken()) != null) {
            Log.d(TAG,  String.format("Tokenizer: %s", token));
            switch (token) {
                case START_OBJECT:
                    stack.push(START_OBJECT);
                    valueExpected = false;
                    break;
                case END_OBJECT:
                    element = stack.pop();
                    if (!START_OBJECT.equals(element)) throw new RuntimeException("Parse error: matching START_OBJECT not found.");
                    if (stack.empty()) break; // it should be over
                    element = stack.pop();
                    switch (element) {
                        case "name":
                            break;
                        default:
                            throw new RuntimeException("Parse error: no such object: " + element);
                    }
                    break;
                case START_ARRAY:
                    stack.push(START_ARRAY);
                    tempList = new ArrayList<>();
                    break;
                case END_ARRAY:
                    element = stack.pop();
                    if (!START_ARRAY.equals(element)) throw new RuntimeException("Parse error: matching START_ARRAY not found.");
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
                case COLON:
                    if (valueExpected) throw new RuntimeException("Parse error: already in value mode.");
                    valueExpected = true;
                    break;
                case COMMA:
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
        }

        return new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);
    }

    // JSON Tokenizer

    private static final String SPACE = " ";
    private static final String TAB = "\t";
    private static final String CARRIGE_RETURN = "\r";
    private static final String LINE_FEED = "\n";

    private static final String START_OBJECT = "{";
    private static final String END_OBJECT = "}";
    private static final String START_ARRAY = "[";
    private static final String END_ARRAY = "]";
    private static final String COMMA = ",";
    private static final String COLON = ":";

    private static final String APOSTROPHE = "\"";

    private String nextToken() {
        if (unprocessedJson.length() == 0) return null;
        while (unprocessedJson.startsWith(SPACE) || unprocessedJson.startsWith(TAB) || unprocessedJson.startsWith(CARRIGE_RETURN) || unprocessedJson.startsWith(LINE_FEED)) {
            unprocessedJson = unprocessedJson.substring(1);
        }
        if (unprocessedJson.length() == 0) return null;
        String firstChar = unprocessedJson.substring(0, 1);
        switch (firstChar) {
            case START_OBJECT:
            case END_OBJECT:
            case START_ARRAY:
            case END_ARRAY:
            case COMMA:
            case COLON:
                unprocessedJson = unprocessedJson.substring(1);
                return firstChar;
            case APOSTROPHE:
                int nextApostrophe = unprocessedJson.indexOf(APOSTROPHE, 1);
                while (unprocessedJson.charAt(nextApostrophe - 1) == '\\') {
                    nextApostrophe = unprocessedJson.indexOf(APOSTROPHE, nextApostrophe + 1);
                }
                String token = unprocessedJson.substring(0, nextApostrophe + 1);
                unprocessedJson = unprocessedJson.substring(nextApostrophe + 1);
                token = token.substring(1, token.length() - 1);
                token = token.replace("\\\"", "\"");
                return token;
            default:
                return null;
        }
    }
}
