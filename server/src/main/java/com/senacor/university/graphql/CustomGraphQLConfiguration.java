package com.senacor.university.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.oembedler.moon.graphql.boot.GraphQLWebAutoConfiguration;
import com.senacor.university.graphql.error.CustomDataFetcherExceptionHandler;
import com.senacor.university.graphql.error.CustomGraphQLErrorHandler;
import com.senacor.university.graphql.json.SourceLocationDeserializer;
import graphql.analysis.MaxQueryComplexityInstrumentation;
import graphql.analysis.MaxQueryDepthInstrumentation;
import graphql.execution.AsyncExecutionStrategy;
import graphql.execution.ExecutionStrategy;
import graphql.execution.instrumentation.ChainedInstrumentation;
import graphql.language.SourceLocation;
import graphql.servlet.GraphQLServletListener;
import graphql.servlet.ObjectMapperConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Map;

import static java.util.Arrays.asList;

@Configuration
public class CustomGraphQLConfiguration {

    private static final Logger operationLogger = LoggerFactory.getLogger("operations");

    @Value("${graphql.complexity.max}")
    private int maxComplexity;

    @Value("${graphql.depth.max}")
    private int maxDepth;

    @Bean
    ObjectMapperConfigurer customObjectMapperConfigurer() {
        return mapper -> {
            mapper.registerModule(new Jdk8Module());
            mapper.registerModule(new JavaTimeModule());
            mapper.registerModule(new ParameterNamesModule());
        };
    }

    @Bean
    Jackson2ObjectMapperBuilderCustomizer customJackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder.deserializerByType(SourceLocation.class, new SourceLocationDeserializer());
        };
    }

    @Bean
    ChainedInstrumentation customInstrumentation() {
        return new ChainedInstrumentation(asList(new MaxQueryComplexityInstrumentation(maxComplexity), new MaxQueryDepthInstrumentation(maxDepth)));
    }

    @Bean
    GraphQLServletListener operationLoggingServletListener(ObjectMapper objectMapper) {
        return new OperationLoggingServletListener(objectMapper);
    }

    @Bean
    CustomDataFetcherExceptionHandler customDataFetcherExceptionHandler() {
        return new CustomDataFetcherExceptionHandler();
    }

    @Bean
    ExecutionStrategy customQueryExecutionStrategy(CustomDataFetcherExceptionHandler exceptionHandler) {
        return new AsyncExecutionStrategy(exceptionHandler);
    }

    @Bean
    Map<String, ExecutionStrategy> executionStrategies(ExecutionStrategy customQueryExecutionStrategy) {
        return Collections.singletonMap(GraphQLWebAutoConfiguration.QUERY_EXECUTION_STRATEGY, customQueryExecutionStrategy);
    }

    @Bean
    CustomGraphQLErrorHandler customGraphQLErrorHandler() {
        return new CustomGraphQLErrorHandler();
    }

}