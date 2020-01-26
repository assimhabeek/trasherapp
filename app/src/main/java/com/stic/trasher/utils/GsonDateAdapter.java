package com.stic.trasher.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.sql.Date;


public class GsonDateAdapter extends TypeAdapter<Date> {
    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        if (value == null)
            out.nullValue();
        else
            out.value(value.getTime());
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        if (in != null && in.peek() != JsonToken.NULL)
            return new Date(in.nextLong());
        else
            return null;
    }
}