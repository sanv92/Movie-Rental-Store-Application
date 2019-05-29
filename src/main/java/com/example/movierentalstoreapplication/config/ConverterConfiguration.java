package com.example.movierentalstoreapplication.config;

import com.example.movierentalstoreapplication.dtos.converters.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class ConverterConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToMovieRentalStatusConverter());
        registry.addConverter(new StringToMovieTypeConverter());
        registry.addConverterFactory(new StringToEnumConverterFactory());
    }

    @Bean
    @Primary
    public ConversionService conversionService(@Autowired List<Converter> converters) {
        ConversionServiceFactoryBean factory = new ConversionServiceFactoryBean();

        factory.setConverters(new HashSet<>(converters));
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    private Set<Converter> getConverters() {
        Set<Converter> converters = new HashSet<>();

        converters.add(new CustomerDtoToCustomer());
        converters.add(new CustomerToCustomerBalanceDto());
        converters.add(new CustomerToCustomerDto());
        converters.add(new MovieDtoToMovie());
        converters.add(new MovieOrderToMovieOrderDto());
        converters.add(new MovieRentalToMovieRentalDto());
        converters.add(new MovieToMovieDto());
        converters.add(new StringToMovieRentalStatusConverter());
        converters.add(new StringToMovieTypeConverter());

        return converters;
    }
}