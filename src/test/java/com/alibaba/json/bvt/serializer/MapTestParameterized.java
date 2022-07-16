package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.annotation.JSONField;
import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class MapTestParameterized extends TestCase {
    enum Type {t_no_sort, t_null, t_Json};
    private Type type;
    private String expected;
    private JSONObject obj = null;
    private MapNullValue mapNullVal;
    private Map<String, Object> map;
    private MapNullValue mapnv;

    public MapTestParameterized(Type type, String expected, String ids, Object id, String value1, Object value2){

        configure(type, expected, ids, id, value1, value2);

    }

    private void configure(Type type, String expected, String ids, Object id, String value1, Object value2) {
        this.type = type;
        this.expected = expected;

        if (checkString(value1)) {


            switch (type) {
                case t_no_sort:   /*faccio il check su entrambe le stringhe*/
                    if (checkString(value2.toString())) {
                        this.obj = new JSONObject(true);
                        obj.put(value1, value2);
                    }
                    if (checkString(ids)) {
                        if (this.obj == null) this.obj = new JSONObject(true);
                        obj.put(ids, id);
                    }

                case t_null:  /*mi interessa check solo su stringa 1*/

                    this.obj = new JSONObject(true);
                    obj.put(value1, value2);


                case t_Json:  /*mi interessa check solo su stringa 1 */
                    this.map = new HashMap<>();
                    map.put(value1, value2);
                    this.mapnv = new MapNullValue();
                    this.mapnv.setMap(map);


            }
        }
    }

    @Parameterized.Parameters
    public static Collection parameters(){
        return Arrays.asList(new Object[][] {
                {Type.t_no_sort, "{'name':'jobs','id':33}", "id", 33, "name", "jobs"},
                {Type.t_null, "{\"name\":null}", null, null, "name", null},
                {Type.t_Json, "{\"map\":{\"Ariston\":null}}", null, null, "Ariston", null},
        });
    }

    private boolean checkString(String inputString){
        if(inputString != null && inputString != "")
            return true;
        else
            return false;
    }

    @Test
    public void test_no_sort(){
        Assume.assumeTrue(type == Type.t_no_sort);
        String text = toJSONString(obj);
        Assert.assertEquals(expected, text);
    }

    @Test
    public void test_null(){
        Assume.assumeTrue(type == Type.t_null);
        String text = JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals(expected, text);
    }

    public static final String toJSONString(Object object) {
        SerializeWriter out = new SerializeWriter();

        try {
            JSONSerializer serializer = new JSONSerializer(out);
            serializer.config(SerializerFeature.SortField, false);
            serializer.config(SerializerFeature.UseSingleQuotes, true);

            serializer.write(object);

            return out.toString();
        } catch (StackOverflowError e) {
            throw new JSONException("maybe circular references", e);
        } finally {
            out.close();
        }
    }

    @Test
    public void test_onJSONField() {
        Assume.assumeTrue(type == Type.t_Json);
        String json = JSON.toJSONString( mapnv );
        assertEquals(expected, json);
    }

    class MapNullValue {
        @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue})
        private Map map;

        public Map getMap() {
            return map;
        }

        public void setMap( Map map ) {
            this.map = map;
        }
    }
}
