package com.vdegree.february.im.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 10:12
 */
@Configuration
public class JsonConfig {

    @Bean
    public Gson gson(){
        return new GsonBuilder()
//                .enableComplexMapKeySerialization()
//                .registerTypeAdapter(WSCMD.class,new JsonSerializer(){
//                    @Override
//                    public JsonElement serialize(Object o, Type type, JsonSerializationContext jsonSerializationContext) {
//                        if(o instanceof WSCMD){
//                            return new JsonPrimitive(((WSCMD) o).getType());
//                        }
//                        return null;
//                    }
//                })
//                .registerTypeHierarchyAdapter(WSCMD.class, new JsonDeserializer () {
//                    @Override
//                    public Object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//                            // 必须是基本数据类型
//                            if (jsonElement.isJsonPrimitive()) {
//                                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
//                                if (jsonPrimitive.isNumber()) { // 数字
//                                    return WSCMD.get(jsonPrimitive.getAsInt());
//                                } else if (jsonPrimitive.isString()) { // 字符串
//                                    return WSCMD.valueOf(jsonPrimitive.getAsString());
//                                }
//                            }
//                        throw new IllegalArgumentException("bad param:" + jsonElement.getAsString());
//                    }
//                })
                .serializeNulls()
//                .setDateFormat(DateFormat.LONG)
//                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
//                .setPrettyPrinting()
                .setVersion(1.0)
//                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }
}
