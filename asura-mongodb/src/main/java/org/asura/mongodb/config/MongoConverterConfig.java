package org.asura.mongodb.config;

import org.asura.mongodb.converter.BigDecimalToDecimal128Converter;
import org.asura.mongodb.converter.Decimal128ToBigDecimalConverter;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MongoConverterConfig implements InitializingBean {

    @Resource
    private MappingMongoConverter mappingMongoConverter;

    @Override
    public void afterPropertiesSet() throws Exception {
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper());

        List<Converter<?, ?>> converters = new ArrayList<>(2);
        converters.add(new BigDecimalToDecimal128Converter());
        converters.add(new Decimal128ToBigDecimalConverter());
        mappingMongoConverter.setCustomConversions(new MongoCustomConversions(converters));
    }
}
