package com.stic.trasher.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Date;
import java.util.ArrayList;

import dz.stic.model.Challenge;

public class GsonUtil {

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateAdapter())
                .create();
    }


    static class DateAdapter extends TypeAdapter<Date> {
        @Override
        public void write(JsonWriter out, Date value) throws IOException {
            if (value == null)
                out.nullValue();
            else
                out.value(value.getTime() / 1000);
        }

        @Override
        public Date read(JsonReader in) throws IOException {
            if (in != null)
                return new Date(in.nextLong() * 1000);
            else
                return null;
        }

    }


    public static ArrayList<Challenge> fromJsonToChallege(String json) {
        Type challengeListType = new TypeToken<ArrayList<Challenge>>() {
        }.getType();
        if (json.trim().equals("")) {
            return new ArrayList<>();
        } else {
            return getGson().fromJson(json, challengeListType);
        }
    }
}
