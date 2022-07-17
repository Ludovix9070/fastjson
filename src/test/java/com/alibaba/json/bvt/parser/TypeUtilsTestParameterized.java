package com.alibaba.json.bvt.parser;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

import com.alibaba.json.bvt.serializer.MapTestParameterized;
import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class TypeUtilsTestParameterized extends TestCase{

    enum Type {t_0, t_1, t_2,t_cast_integer,t_cast_integer2,t_cast_to_long,t_cast_to_Long,t_cast_to_short,t_cast_to_Short,
    t_cast_to_byte,t_cast_to_Byte,t_cast_to_BigInteger,t_cst_to_BigDecimal,t_cast_to_boolean,t_cast_to_Boolean,
    t_cast_null,t_cast_to_String,t_cast_to_Date,t_cast_to_SqlDate,t_cast_to_SqlDate_string,
    t_cast_to_SqlDate_null,t_cast_to_SqlDate_null2,t_cast_to_SqlDate_util_Date,t_cast_to_SqlDate_sql_Date,
        t_cast_to_SqlDate_sql_Date2,t_cast_to_SqlDate_calendar,t_cast_to_SqlDate_error,t_cast_to_timestamp,
    t_cast_to_timestamp_string,t_cast_to_timestamp_number,t_cast_to_timestamp_null,t_cast_to_timestamp_null2,
    t_cast_to_timestamp_1970_01_01_00_00_00,t_cast_to_BigDecimal_same,t_cast_to_BigInteger_same,t_cast_Array,
    t_cast_to_timestamp_util_Date,t_cast_to_timestamp_sql_Date,t_cast_to_timestamp_sql_timestamp,
    t_cast_to_timestamp_calendar,t_cast_to_timestamp_not_error,t_cast_ab,t_cast_ab_1,t_cast_ab_error,t_error,
    t_error2,t_3};
    private Type type;
    private HashMap map;
    private JSONObject object = null;    //3
    private Object p1;
    private Object p2;
    private Object p3;

    private Object expected1;
    private Object expected2;

    @Parameterized.Parameters
    public static Collection parameters(){
        return Arrays.asList(new Object[][] {
                {Type.t_0, null, null, null, null, null},
                {Type.t_1, null, null, null, null, null},
                {Type.t_0, "id", "name","panlei","panlei",1L}

        });
    }

    public TypeUtilsTestParameterized(Type type,Object p1, Object p2, Object p3, Object exp1, Object exp2){

        this.type = type;
        this.expected1 = exp1;
        this.expected2 = exp2;

        switch(type){
            case t_0:
                configure_t_0();
            case t_1:
                configure_t_1();

            case t_2:
                configure_t_2(p1,p2,p3);
        }
    }

    private void configure_t_0() {
        this.map = new HashMap();
    }

    private void configure_t_1() {
        this.object = new JSONObject();
    }

    private void configure_t_2(Object p1, Object p2, Object p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.object = new JSONObject();
        this.object.put((String)p1,1);
        this.object.put((String)p2,p3);
    }

    @Test
    public void test_0() throws Exception {
        Assume.assumeTrue(type == Type.t_0);
        Assert.assertTrue(this.map == TypeUtils.castToJavaBean(map, Map.class));
    }

    @Test
    public void test_1() throws Exception {
        Assume.assumeTrue(type == Type.t_1);
        Assert.assertTrue(this.object == TypeUtils.castToJavaBean(object, Map.class));
    }

    @Test
    public void test_2() throws Exception {
        Assume.assumeTrue(type == Type.t_2);
        User user = TypeUtils.castToJavaBean(this.object, User.class);
        Assert.assertEquals((long)this.expected2, user.getId());
        Assert.assertEquals(this.expected1, user.getName());
    }


    public static class User {

        private long id;
        private String name;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class A implements IA {

    }

    public static interface IA {

    }

    public static class B extends A {

    }

    public static class C extends B {

        public int getId() {
            throw new UnsupportedOperationException();
        }

        public void setId(int id) {
            throw new UnsupportedOperationException();
        }
    }

    public static void f(List<?> list) {

    }
}
