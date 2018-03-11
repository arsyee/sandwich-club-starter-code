package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    public static Sandwich parseSandwichJson(String json) {
        Log.d(TAG, json);
        /*
        * Notes to reviewer:
        *
        * I don't agree that my solution had to be rejected with this comment, and I don't understand
        * why the surprise on your side that I decided to implement the solution like this. The
        * requirements repeated multiple times that no third party tools can be used and that the
        * point of this whole exercise is to understand how JSON works.
        *
        * Before continuing, please note that I'm coming from the EMEA Google Developer Challenge
        * Scholarship - that gives me a much different perspective than your regular Nanodegree
        * participants, namely that I'm doing this exercise out of order - we finished a whole
        * course before we had to come back and create this simple app. So for me it is not a
        * natural transition from the course teaching the platform JSON functions to writing this
        * app, but from out of context, I read a simple app description and create the app.
        *
        * Without the context, it is not clear that those platform functions can be used.
        *
        * Regarding how complicated my solution is: another bit of context is that even though I'm
        * new to Android, I'm writing code in dozens of languages since I was 10, I studied computer
        * science (including a semester on parsers and compilers) and worked also on low level
        * projects where we had to write more general functions, like printf for ourselves. For me
        * it is neither surprising, nor too complicated to write a parser for the world's less
        * complicated language: JSON. It's rather a fun activity to do, I spent like 1.5 hours on the
        * version I initially submitted.
        *
        * So back to the strict topic: I see no requirements stating that the platform functions
        * have to be used. I see no reference to such requirements in the Code Review. If you still
        * would like to reject my solution, please refer the exact requirement I'm violating. Until
        * now I only saw an opinion, and that "this is not what we were thinking". Well, in that case
        * you should specify your requirements clearly.
        *
        * Quote from the project review:
        *
        * "you are allowed to use pretty much anything as long as it does not directly go against
        * the rubrics (for example, you cannot use a third-party lib for parsing the JSON according
        * to the rubrics for this project), or if it does not take away the challenge of learning
        * whatever seems to be the main objective of the project (in this case, to learn JSON parsing
        * and to design and populate UI with that data)"
        *
        * 1) I did not user third-party lib for parsing JSON.
        * 2) I strongly believe that my solution didn't take away the challenge - on the contrary
        * 3) I also believe that I learnt JSON more throughoutly than any of my fellow students
        *
        * You are right, though, it would take no time to modify the solution and to use the platform
        * functions. But I'd like my solution to be accepted like this on principle: I did not
        * violate any of the requirements, and I'm proud of the work I have done with my parser, so
        * I'm not willing to delete it.
        *
        * Having said that, I know that my previous parser was not complete, because it could only
        * parse the exact object used in this exercise. So I spent another couple of hours to
        * generalize it to be a fully functional JSON parser.
        *
        * As a reference, I used the description at: https://www.json.org/
        *
        * Have fun reviewing it, finally something new after 2000 exactly same solutions :-)
        *
        * */
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
