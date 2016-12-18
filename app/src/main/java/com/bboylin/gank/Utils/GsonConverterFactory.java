package com.bboylin.gank.Utils;

import com.baoyz.treasure.Converter;
import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by lin on 2016/11/10.
 */

public class GsonConverterFactory implements Converter.Factory {

    @Override
    public <F> Converter<F, String> fromType(Type fromType) {
        return value -> new Gson().toJson(value);
    }

    @Override
    public <T> Converter<String, T> toType(final Type toType) {
        return value -> new Gson().fromJson(value, toType);
    }
}
