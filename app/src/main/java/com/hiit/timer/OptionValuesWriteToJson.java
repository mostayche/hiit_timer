package com.hiit.timer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class OptionValuesWriteToJson {

    private List<Integer> optionList;


    public String writeOptionsToJson(int exerciseTime, int pauseTime, int repeat) {
        optionList = new ArrayList<>();
        optionList.add(exerciseTime);
        optionList.add(pauseTime);
        optionList.add(repeat);
        Gson gson = new Gson();
        return gson.toJson(optionList);
    }


    public List<Integer> optionsFromFile(String optionGson) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Double.class, new DoubleSerializer()).setPrettyPrinting().create();
        return castToInts(gson.fromJson(optionGson, ArrayList.class));
    }

    private List<Integer> castToInts(List<Double> doubleList) {
        List<Integer> intOptionList = new ArrayList<>();
        for (Double x : doubleList) {
            intOptionList.add(x.intValue());
        }
        return intOptionList;
    }


    private static class DoubleSerializer implements JsonSerializer<Double> {
        @Override
        public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
            return src == src.longValue() ? new JsonPrimitive(src.intValue()) : new JsonPrimitive(src);
        }
    }

    public List<Integer> createDefaultTimeValues(){
        List<Integer> defaultTimesList = new ArrayList<>();
        defaultTimesList.add(20);
        defaultTimesList.add(10);
        defaultTimesList.add(8);
        return defaultTimesList;
    }


}
