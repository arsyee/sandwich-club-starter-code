package com.udacity.sandwichclub.utils;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;

class JsonTokenizer {

    @SuppressWarnings("unused")
	private static final String TAG = JsonTokenizer.class.getSimpleName();
	
	private final String json;
	private int pos = 0;
		
	JsonTokenizer(String json) {
		this.json = json;
	}
	
    private static final char APOSTROPHE = '\"';
    private static final String NUM_START = "-0123456789";
    private static final String PRIMITVE_START = "tfn";

    private JsonToken currentToken = null;
    
	JsonToken currentToken() {
		// Log.d(TAG, "tokenizer: " + currentToken);
		return currentToken;
	}

	JsonToken nextToken() throws JsonException {
		currentToken = nextTokenRaw();
		return currentToken();
	}
	
    @SuppressLint("DefaultLocale")
	private JsonToken nextTokenRaw() throws JsonException {
        while (pos < json.length() && JsonSpecialChars.isWhitespace(json.charAt(pos))) ++pos;
        if (pos == json.length()) {
        	// Log.d(TAG, "nextToken() reached end of line");
        	return null;
        }
        
        char ch = json.charAt(pos);
        if (JsonSpecialChars.isJsonSpecialChar(ch)) {
        	++pos;
        	return JsonSpecialChars.getJsonSpecialChar(ch);
        }
        if (ch == APOSTROPHE) {
        	return nextString();
        }
        if (NUM_START.indexOf(ch) != -1) {
        	return nextNumber();
        }
        if (PRIMITVE_START.indexOf(ch) != -1) {
        	return nextPrimitive();
        }
        throw new JsonException(String.format("Unexpected char at %d: '%c' in %s", pos, ch, json));
    }
    
    @SuppressLint("DefaultLocale")
	private JsonToken nextString() throws JsonException {
    	char ch = json.charAt(pos);
    	if (ch != APOSTROPHE) throw new JsonException(String.format("Parse error at char %d in %s", pos, json));
    	boolean escape = false;
    	StringBuilder builder = new StringBuilder();
    	while (true) {
    		++pos;
    		if (pos >= json.length()) throw new JsonException(String.format("Unexpected end of JsonString: %s in %s", builder, json));
    		ch = json.charAt(pos);
    		if (escape) {
    			switch (ch) {
    				case '\"' : builder.append('\"'); break;
    				case '\\' : builder.append('\\'); break;
    				case '/' : builder.append('/'); break;
    				case 'b' : builder.append('\b'); break;
    				case 'f' : builder.append('\f'); break;
    				case 'n' : builder.append('\n'); break;
    				case 'r' : builder.append('\r'); break;
    				case 't' : builder.append('\t'); break;
    				case 'u' :
    					String sequence = null;
    					try {
    						sequence = json.substring(pos + 1, pos + 5);
    						pos += 4;
    						char u = (char) Integer.parseInt(sequence, 16);
    						builder.append(u); break;
    					} catch (IndexOutOfBoundsException e) {
    						throw new JsonException(String.format("Not enough characters to parse unicode sequence at %d in %s", pos+1, json));
    					} catch (NumberFormatException e) {
    						throw new JsonException(String.format("Unexpected unicode sequence at %d: %s in %s", pos+1, sequence, json));
    					}
    				default:
    					throw new JsonException(String.format("Unexpected escape sequence at %d in JsonString '%s' in %s", pos, builder, json));
    			}
    			escape = false;
    			continue;
    		}
    		if (ch == '\"') {
    			++pos;
    			return new JsonString(builder.toString());
    		}
    		if (ch == '\\') {
    			escape = true;
    			continue;
    		}
    		builder.append(ch);
    	}
    }
    
    @SuppressLint("DefaultLocale")
	private JsonToken nextNumber() throws JsonException {
    	final String digits = "0123456789";
    	
    	final int STATE_SIGN   = 0;
    	final int STATE_INT_0  = 1;
    	final int STATE_INT_1  = 2;
    	final int STATE_INT    = 3;
    	final int STATE_POINT  = 4;
    	final int STATE_FRAC_1 = 5;
    	final int STATE_FRAC   = 6;
    	final int STATE_E      = 7;
    	final int STATE_E_SIGN = 8;
    	final int STATE_E_NUM  = 9;
    	final int STATE_END    = 10;
    	
    	int state = 0;
    	
    	int sign = 1;
    	int intPart = 0;
    	double fracPart = 0;
    	double fracE = 1.0;
    	int eSign = 1;
    	int ePart = 0;

    	char ch;
    	while (state < STATE_END) {
    		if (pos >= json.length()) break; // end of json string reached
    		ch = json.charAt(pos);
    		// Log.d(TAG, String.format("Iterating on number: %d %d", pos, state));
    		switch (state) {
    			case STATE_SIGN:
    				if (ch == '-') {
    					sign = -1;
    					++pos;
    				}
    				++state;
    				break;
    			case STATE_INT_0:
    				if (ch == '0') {
    					++pos;
    					state = STATE_POINT;
    				} else {
    					++state;
    				}
    				break;
    			case STATE_INT_1:
    			case STATE_FRAC_1:
    				if (digits.indexOf(ch) == -1) {
    					throw new JsonException(String.format("Unexpected character %c at %d in JsonNumber in %s", ch, pos, json));
    				}
    				++state;
    				break;
    			case STATE_INT:
    				if (digits.indexOf(ch) != -1) {
    					intPart = 10 * intPart + ch - '0';
    					++pos;
    				} else {
    					++state;
    				}
    				break;
    			case STATE_POINT:
    				if (ch == '.') {
    					++pos;
    					++state;
    				} else {
    					state = STATE_E;
    				}
    				break;
    			case STATE_FRAC:
    				if (digits.indexOf(ch) != -1) {
    					fracE /= 10.0;
    					fracPart = fracPart + (((double) (ch - '0')) * fracE);
    					++pos;
    				} else {
    					++state;
    				}
    				break;
    			case STATE_E:
    				if (ch == 'e' || ch == 'E') {
    					++pos;
    					++state;
    				} else {
    					state = STATE_END;
    				}
    				break;
    			case STATE_E_SIGN:
    				if (ch == '-') {
    					eSign = -1;
    					++pos;
    				} else if (ch == '+') {
    					++pos;
    				}
					++state;
    				break;
    			case STATE_E_NUM:
    				if (digits.indexOf(ch) != -1) {
    					ePart = 10 * ePart + ch - '0';
    					++pos;
    				} else {
    					++state;
    				}
    				break;
    			default:
    				break;
    		}
    	}
		return new JsonNumber(sign * (intPart + fracPart) * Math.pow(10, eSign * ePart));
    }
    
    @SuppressLint("DefaultLocale")
	private JsonToken nextPrimitive() throws JsonException {
    	char ch = json.charAt(pos);
		String sequence;
		Map<Character, Boolean> valueMap = new HashMap<>();
		valueMap.put('t', true);
		valueMap.put('f', false);
		valueMap.put('n', null);
    	if (valueMap.containsKey(ch)) {
        	Boolean b = valueMap.get(ch);
        	String asString = String.format("%s", b);
			sequence = json.substring(pos, pos + asString.length());
			if (sequence.equals(asString)) {
				pos += asString.length();
				return JsonConstant.getConstant(b);
			} else {
				throw new JsonException(String.format("Parse error at char %d in %s", pos, json));
			}    		
    	} else {
   			throw new JsonException(String.format("Parse error at char %d in %s", pos, json));
        }
    }

}
