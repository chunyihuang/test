package com.h5.game.common.tools.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by 黄春怡 on 2017/4/1.
 */
public class IntegerTypeAdapter extends TypeAdapter<Integer> {
    public void write(JsonWriter writer, Integer value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.value(value);
    }

    public Integer read(JsonReader reader) throws IOException {
        if(reader.peek() == JsonToken.NULL){
            reader.nextNull();
            return null;
        }
        String stringValue = reader.nextString();
        try{
            Integer value = Integer.valueOf(stringValue);
            return value;
        }catch(NumberFormatException e){
            return null;
        }
    }
}
