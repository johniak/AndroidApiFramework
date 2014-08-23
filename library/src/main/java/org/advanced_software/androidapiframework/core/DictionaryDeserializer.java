package org.advanced_software.androidapiframework.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.TreeMap;

/**
 * Deserialize json array of key, value object to TreeMap (order of elements is important)
 * [
 * { "key" : "foo", "value" : "bar"},
 * { "key" : "foo1", "value" : "bar1"},
 * ]
 * Created by johniak on 18.03.14.
 */
public class DictionaryDeserializer implements JsonDeserializer<TreeMap> {
    @Override
    public TreeMap deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        JsonArray mapArray= jsonElement.getAsJsonArray();
        for(JsonElement mapElement :mapArray){
            JsonObject mapObject = mapElement.getAsJsonObject();
            treeMap.put(mapObject.get("key").getAsString(),mapObject.get("value").getAsString());
        }
        return treeMap;
    }
}