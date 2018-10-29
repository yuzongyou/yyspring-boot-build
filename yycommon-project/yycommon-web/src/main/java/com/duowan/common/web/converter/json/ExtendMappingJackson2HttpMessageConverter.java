package com.duowan.common.web.converter.json;

import com.fasterxml.jackson.core.JsonGenerator;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;

/**
 * 支持Javascript参数
 *
 * @author Arvin
 */
public class ExtendMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    @Override
    protected void writePrefix(JsonGenerator generator, Object object) throws IOException {
        super.writePrefix(generator, object);
        String javascriptVar =
                (object instanceof MappingJacksonJavascriptValue ? ((MappingJacksonJavascriptValue) object).getJavascriptVar() : null);
        if (javascriptVar != null) {
            generator.writeRaw("var " + javascriptVar + " = ");
        }
    }

    @Override
    protected void writeSuffix(JsonGenerator generator, Object object) throws IOException {
        super.writeSuffix(generator, object);
        String javascriptVar =
                (object instanceof MappingJacksonJavascriptValue ? ((MappingJacksonJavascriptValue) object).getJavascriptVar() : null);
        if (javascriptVar != null) {
            generator.writeRaw(";");
        }
    }

}
