package com.example.movierentalstoreapplication.config;

import com.example.movierentalstoreapplication.dtos.converters.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Configuration
public class ConverterConfiguration implements WebMvcConfigurer {

    private List<Converter> converters;

    public ConverterConfiguration(@Autowired List<Converter> converters) {
        this.converters = converters;
    }

    @Bean
    @Primary
    public ConversionService addMvcConversionService(
            @Qualifier("mvcConversionService") ConversionService conversionService
    ) {
        return conversionService;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToMovieRentalStatusConverter());
        registry.addConverter(new StringToMovieTypeConverter());
        registry.addConverterFactory(new StringToEnumConverterFactory());

        for (Converter converter : converters) {
            registry.addConverter(converter);
        }
    }
}