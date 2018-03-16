package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    public static Sandwich parseSandwichJson(String json) {
        Log.d(TAG, json);

        try {
            JsonValue jsonValue = JsonParser.parse(json);
            Log.d(TAG, jsonValue.toString());
            
            JsonObject object = (JsonObject) jsonValue;
            JsonObject name = (JsonObject) object.get("name");
            JsonString mainName = (JsonString) name.get("mainName");
            JsonArray alsoKnownAs = (JsonArray) name.get("alsoKnownAs");
            JsonString placeOfOrigin = (JsonString) object.get("placeOfOrigin");
            JsonString description = (JsonString) object.get("description");
            JsonString image = (JsonString) object.get("image");
            JsonArray ingredients = (JsonArray) object.get("ingredients");
            
            String mainNameString = mainName.getString();
            List<String> alsoKnownAsList = new ArrayList<>();
            for (int i = 0; i < alsoKnownAs.size(); ++i) {
            	String str = ((JsonString) (alsoKnownAs.get(i))).getString();
            	alsoKnownAsList.add(str);
            }
            String placeOfOriginString = placeOfOrigin.getString();
            String descriptionString = description.getString();
            String imageString = image.getString();
            List<String> ingredientsList = new ArrayList<>();
            for (int i = 0; i < ingredients.size(); ++i) {
            	String str = ((JsonString) (ingredients.get(i))).getString();
            	ingredientsList.add(str);
            }
            
            return new Sandwich(mainNameString, alsoKnownAsList, placeOfOriginString, descriptionString, imageString, ingredientsList);
        } catch (RuntimeException | JsonException e) {
        	e.printStackTrace();
            Log.e(TAG, String.format("Exception: %s %s", e.getClass(), e.getMessage()));
            return null;
        }
    }

}
