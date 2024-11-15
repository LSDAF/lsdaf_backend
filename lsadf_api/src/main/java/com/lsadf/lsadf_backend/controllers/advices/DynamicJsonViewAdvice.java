package com.lsadf.lsadf_backend.controllers.advices;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.core.constants.JsonViews;
import com.lsadf.lsadf_backend.exceptions.DynamicJsonViewException;
import com.lsadf.lsadf_backend.properties.JsonViewProperties;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class DynamicJsonViewAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;
    private final JsonViewProperties jsonViewProperties;

    @Autowired
    public DynamicJsonViewAdvice(ObjectMapper objectMapper,
                                 JsonViewProperties jsonViewProperties) {
        this.objectMapper = objectMapper;
        this.jsonViewProperties = jsonViewProperties;

        // init objectMapper typeReference registers
        objectMapper.getTypeFactory().constructType(GenericResponse.class);
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.hasMethodAnnotation(JsonView.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            return null;
        }

        // Check if we should override the JSON view
        if (jsonViewProperties.isDefaultJsonViewEnabled()) {
            JsonViews.JsonViewType viewType = jsonViewProperties.getDefaultJsonView();
            Class<?> viewClass = switch (viewType) {
                case ADMIN -> JsonViews.Admin.class;
                case INTERNAL -> JsonViews.Internal.class;
                case EXTERNAL -> JsonViews.External.class;
            };

            try {
                GenericResponse<Object> genericResponse = (GenericResponse<Object>) body;
                Object data = genericResponse.getData();
                String json = objectMapper.writerWithView(viewClass).writeValueAsString(data);
                JsonNode jsonNode = objectMapper.readTree(json);
                genericResponse.setData(jsonNode);
                return genericResponse;
            } catch (Exception e) {
                throw new DynamicJsonViewException("Failed to apply JSON view", e);
            }
        }

        // Otherwise, use the view specified in the @JsonView annotation on the method
        return body;
    }
}
