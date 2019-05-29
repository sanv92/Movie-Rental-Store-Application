package com.example.movierentalstoreapplication.dtos.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

@Component
public class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {

    private static class StringToEnumConverter<T extends Enum> implements Converter<String, T> {

        private Class<T> enumType;

        StringToEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        public T convert(String source) {
            String name = source.trim().toUpperCase();

            return (T) Enum.valueOf(this.enumType, name);
        }
    }

    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumConverter(targetType);
    }
}