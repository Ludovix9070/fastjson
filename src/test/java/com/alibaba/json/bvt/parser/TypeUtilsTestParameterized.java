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
    t_cast_to_byte,t_cast_to_Byte,t_cast_to_BigInteger,t_cast_to_BigDecimal,t_cast_to_boolean,t_cast_to_Boolean,
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
    private Object p4;

    private Object expected1;
    private Object expected2;

    private Calendar calendar;
    private BigDecimal bigDec;
    private BigInteger bigInt;
    private B b;
    private A a;


    @Parameterized.Parameters
    public static Collection parameters(){
        return Arrays.asList(new Object[][] {
                {Type.t_0, null, null, null, null, null, null},
                {Type.t_1, null, null, null, null, null, null},
                {Type.t_2, "id", "name","panlei",1,"panlei",1L},
                {Type.t_cast_integer, "id", 1L,null, null, 1,null},
                {Type.t_cast_integer2, "id", 1L,null, null, 1,null},
                {Type.t_cast_to_long, "id", 1,null, null, 1L,null},
                {Type.t_cast_to_Long, "id", 1,null, null, 1L,null},
                {Type.t_cast_to_short, "id", 1,null, null, (short) 1,null},
                {Type.t_cast_to_Short, "id", 1,null, null, (short) 1,null},
                {Type.t_cast_to_byte, "id", 1,null, null, (byte) 1,null},
                {Type.t_cast_to_Byte, "id", 1,null, null, (byte) 1,null},
                {Type.t_cast_to_BigInteger, "id", 1,null, null, new BigInteger("1"),null},
                {Type.t_cast_to_BigDecimal, "id", 1,null, null, new BigDecimal("1"),null},
                {Type.t_cast_to_boolean, "id", 1,null, null, Boolean.TRUE,null},
                {Type.t_cast_to_Boolean, "id", 1,null, null, Boolean.TRUE,null},
                {Type.t_cast_null, "id", null,null, null, null,null},
                {Type.t_cast_to_String, "id", 1,null, null, "1",null},
                {Type.t_cast_to_Date, "date", System.currentTimeMillis(),null, null, new Date(System.currentTimeMillis()),null},
                {Type.t_cast_to_SqlDate, "date", System.currentTimeMillis(),null, null, new java.sql.Date(System.currentTimeMillis()),null},
                {Type.t_cast_to_SqlDate_string, "date", System.currentTimeMillis(),null, null, new java.sql.Date(System.currentTimeMillis()),null},
                {Type.t_cast_to_SqlDate_null, "date", null,null, null, null,null},
                {Type.t_cast_to_SqlDate_null2, null, null,null, null, null,null},
                {Type.t_cast_to_SqlDate_util_Date, "date", System.currentTimeMillis(),null, null, new java.sql.Date(System.currentTimeMillis()),null},
                {Type.t_cast_to_SqlDate_sql_Date, "date", System.currentTimeMillis(),null, null, new java.sql.Date(System.currentTimeMillis()),null},
                {Type.t_cast_to_SqlDate_sql_Date2, System.currentTimeMillis(), null, null, null, null,null},
                {Type.t_cast_to_SqlDate_calendar, "date", System.currentTimeMillis(),null, null, new java.sql.Date(System.currentTimeMillis()),null},
                {Type.t_cast_to_SqlDate_error, "date", 0,null, null, null,null},
                {Type.t_cast_to_timestamp, "date", System.currentTimeMillis(),null, null, new java.sql.Timestamp(System.currentTimeMillis()),null},
                {Type.t_cast_to_timestamp_string, "date", System.currentTimeMillis(),null, null, new java.sql.Timestamp(System.currentTimeMillis()),null},
                {Type.t_cast_to_timestamp_number, "date", System.currentTimeMillis(),null, null, new java.sql.Timestamp(System.currentTimeMillis()),null},
                {Type.t_cast_to_timestamp_null, "date", null,null, null, null,null},
                {Type.t_cast_to_timestamp_null2, null, null,null, null, null,null},
                {Type.t_cast_to_timestamp_1970_01_01_00_00_00, "Asia/Shanghai", "1970-01-01 08:00:00",0, null, null,null}, //expected in base a terzo parametro
                {Type.t_cast_to_BigDecimal_same, "123", null,null, null, true,null},
                {Type.t_cast_to_BigInteger_same, "123", null,null, null, true,null},
                {Type.t_cast_Array, null, null,null, null, null,null},
                {Type.t_cast_to_timestamp_util_Date, "date", new Date(System.currentTimeMillis()),null, null, new java.sql.Timestamp(System.currentTimeMillis()),null},
                {Type.t_cast_to_timestamp_sql_Date, "date", new java.sql.Date(System.currentTimeMillis()),null, null, new java.sql.Timestamp(System.currentTimeMillis()),null},
                {Type.t_cast_to_timestamp_sql_timestamp, System.currentTimeMillis(), null, null, null, null,null},
                {Type.t_cast_to_timestamp_calendar, "date", System.currentTimeMillis(),null, null, new java.sql.Timestamp(System.currentTimeMillis()),null},
                {Type.t_cast_to_timestamp_not_error, "date", -1, -1L, null, null,null},
                {Type.t_cast_ab, "value", null, null, null, null,null},
                {Type.t_cast_ab_1, "value", null, null, null, null,null},
                {Type.t_cast_ab_error, "value", null, null, null, null,null},
                {Type.t_error, "id", 1, null, null, null,null},
                {Type.t_error2, "id", 1, null, null, null,null},
                {Type.t_3, "id", "name","panlei",1,"panlei",1L}
        });
    }

    public TypeUtilsTestParameterized(Type type,Object p1, Object p2, Object p3, Object p4, Object exp1, Object exp2){

        this.type = type;
        this.expected1 = exp1;
        this.expected2 = exp2;

        switch(type){
            case t_0:
                configure_t_0();
                break;
            case t_1:
                configure_t_1();
                break;

            case t_2:
            case t_3:
                configure_t_2(p1,p2,p3,p4);
                break;

            case t_cast_integer:
            case t_cast_integer2:
            case t_cast_to_long:
            case t_cast_to_Long:
            case t_cast_to_short:
            case t_cast_to_Short:
            case t_cast_to_byte:
            case t_cast_to_Byte:
            case t_cast_to_BigInteger:
            case t_cast_to_BigDecimal:
            case t_cast_to_boolean:
            case t_cast_to_Boolean:
            case t_cast_null:
            case t_cast_to_String:
            case t_cast_to_timestamp:
            case t_cast_to_timestamp_null:
            case t_error:
            case t_error2:
                configure_cast_1(p1,p2);
                break;

            case t_cast_to_Date:
            case t_cast_to_SqlDate_null:
            case t_cast_to_SqlDate_util_Date:
            case t_cast_to_SqlDate_sql_Date:
            case t_cast_to_SqlDate_error:
            case t_cast_to_SqlDate:
            case t_cast_to_timestamp_util_Date:
            case t_cast_to_timestamp_sql_Date:
                configure_date_1(p1,p2);
                break;
            case t_cast_to_SqlDate_string:
            case t_cast_to_timestamp_string:
                configure_date_2(p1,p2);
                break;

            case t_cast_to_SqlDate_null2:
            case t_cast_to_timestamp_null2:
            case t_cast_Array:
                configure_date_null2(p1);
                break;

            case t_cast_to_SqlDate_sql_Date2:
            case t_cast_to_timestamp_sql_timestamp:
                configure_sql_date2(p1);
                break;

            case t_cast_to_SqlDate_calendar:
            case t_cast_to_timestamp_calendar:
                configure_calendar(p1,p2);
                break;

            case t_cast_to_timestamp_number:
                configure_timestamp_number(p1, p2);
                break;

            case  t_cast_to_timestamp_1970_01_01_00_00_00:
                configure_strange_ts(p1,p2,p3);

            case t_cast_to_BigDecimal_same:
                configure_big_decimal_same(p1);

            case t_cast_to_BigInteger_same:
                configure_big_integer_same(p1);

            case t_cast_to_timestamp_not_error:
                configure_ts_not_error(p1,p2,p3);

            case t_cast_ab:
            case t_cast_ab_1:
                configure_cast_ab(p1);

            case t_cast_ab_error:
                configure_ab_error(p1);

        }
    }

    private void configure_t_0() {
        this.map = new HashMap();
    }

    private void configure_t_1() {
        this.object = new JSONObject();
    }

    private void configure_t_2(Object p1, Object p2, Object p3, Object p4) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.object = new JSONObject();
        this.object.put((String)p1,p4);
        this.object.put((String)p2,p3);
    }

    private void configure_cast_1(Object p1, Object p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.object = new JSONObject();
        this.object.put((String)p1,p2);

    }

    private void configure_date_1(Object p1, Object p2) {
        this.p1 = p1;
        this.p2 = p2; //magari potrebbe essere utile avere millis parametrico
        this.object = new JSONObject();
        this.object.put((String)p1,p2);

    }

    private void configure_date_2(Object p1, Object p2) {
        this.p1 = p1;
        this.p2 = p2; //magari potrebbe essere utile avere millis parametrico
        this.object = new JSONObject();
        this.object.put((String)p1,Long.toString((long)p2));

    }

    private void configure_timestamp_number(Object p1, Object p2) {
        this.p1 = p1;
        this.p2 = p2; //magari potrebbe essere utile avere millis parametrico
        this.object = new JSONObject();
        this.object.put((String)p1,new BigDecimal(Long.toString((long)p2)));

    }

    private void configure_date_null2(Object p1) {
        this.p1 = p1;

    }

    private void configure_sql_date2(Object p1) {
        this.p1 = p1; //in questo caso corrisponde a millis

    }

    private void configure_calendar(Object p1, Object p2) {
        this.p1 = p1;  //date
        this.p2 = p2;   //millis
        this.calendar = Calendar.getInstance();
        this.calendar.setTimeInMillis((long)p2);
        this.object = new JSONObject();
        this.object.put((String)p1,this.calendar);
    }

    private void configure_strange_ts(Object p1, Object p2, Object p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    private void configure_big_decimal_same(Object p1) {
        this.p1 = p1;
        this.bigDec = new BigDecimal((String)p1);

    }

    private void configure_big_integer_same(Object p1) {
        this.p1 = p1;
        this.bigInt = new BigInteger((String)p1);

    }

    private void configure_ts_not_error(Object p1, Object p2, Object p3) {
        this.p1 = p1;
        this.p2 = p2; //magari potrebbe essere utile avere millis parametrico
        this.p3 = p3;
        this.object = new JSONObject();
        this.object.put((String)p1,p2);

    }

    private void configure_cast_ab(Object p1){
        this.p1 = p1;
        this.b = new B();
        this.object = new JSONObject();
        this.object.put((String)this.p1,this.b);
    }

    private void configure_ab_error(Object p1){
        this.p1 = p1;
        this.a = new A();
        this.object = new JSONObject();
        this.object.put((String)this.p1,this.a);
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

    @Test
    public void test_cast_Integer() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_integer);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, int.class));
    }

    @Test
    public void test_cast_Integer_2() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_integer2);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, Integer.class));
    }

    @Test
    public void test_cast_to_long() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_long);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, long.class));
    }

    @Test
    public void test_cast_to_Long() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_Long);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, Long.class));
    }

    @Test
    public void test_cast_to_short() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_short);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, short.class));
    }

    @Test
    public void test_cast_to_Short() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_Short);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, Short.class));
    }

    @Test
    public void test_cast_to_byte() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_byte);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, byte.class));
    }

    @Test
    public void test_cast_to_Byte() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_Byte);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, Byte.class));
    }

    @Test
    public void test_cast_to_BigInteger() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_BigInteger);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, BigInteger.class));
    }

    @Test
    public void test_cast_to_BigDecimal() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_BigDecimal);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, BigDecimal.class));
    }

    @Test
    public void test_cast_to_boolean() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_boolean);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, boolean.class));
    }

    @Test
    public void test_cast_to_Boolean() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_Boolean);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, Boolean.class));
    }

    @Test
    public void test_cast_null() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_Boolean);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, Boolean.class));
    }

    @Test
    public void test_cast_to_String() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_Boolean);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, String.class));
    }

    @Test
    public void test_cast_to_Date() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_Date);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, Date.class));
    }

    @Test
    public void test_cast_to_SqlDate() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_SqlDate);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, java.sql.Date.class));
    }

    @Test
    public void test_cast_to_SqlDate_String() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_SqlDate_string);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, java.sql.Date.class));
    }

    @Test
    public void test_cast_to_SqlDate_null() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_SqlDate_null);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, java.sql.Date.class));
    }

    @Test
    public void test_cast_to_SqlDate_null2() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_SqlDate_null2);
        Assert.assertEquals(this.expected1, TypeUtils.castToSqlDate(this.p1));
    }

    @Test
    public void test_cast_to_SqlDate_util_Date() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_SqlDate_util_Date);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, java.sql.Date.class));
    }

    @Test
    public void test_cast_to_SqlDate_sql_Date() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_SqlDate_sql_Date);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, java.sql.Date.class));
    }

    @Test
    public void test_cast_to_SqlDate_sql_Date2() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_SqlDate_sql_Date2);
        java.sql.Date date = new java.sql.Date((long)this.p1);
        //in questo caso millis viene usato solo come input, l'expected viene calcolato sull'input
        Assert.assertEquals(date, TypeUtils.castToSqlDate(date));
    }

    @Test
    public void test_cast_to_SqlDate_calendar() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_SqlDate_calendar);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, java.sql.Date.class));
    }

    @Test
    public void test_cast_to_SqlDate_error() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_SqlDate_error);
        JSONException error = null;
        try{
            this.object.getObject((String)this.p1,java.sql.Date.class);
        }catch(JSONException e){
            error = e;
        }
        Assert.assertNotNull(error);
    }

    @Test
    public void test_cast_to_Timestamp() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_timestamp);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, java.sql.Timestamp.class));
    }

    @Test
    public void test_cast_to_Timestamp_string() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_timestamp_string);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, java.sql.Timestamp.class));
    }

    @Test
    public void test_cast_to_Timestamp_number() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_timestamp_number);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, java.sql.Timestamp.class));
    }

    @Test
    public void test_cast_to_Timestamp_null() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_timestamp_null);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, java.sql.Timestamp.class));
    }

    @Test
    public void test_cast_to_Timestamp_null2() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_timestamp_null2);
        Assert.assertEquals(this.expected1, TypeUtils.castToTimestamp(this.p1));
    }

    @Test
    public void test_cast_to_Timestamp_strange() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_timestamp_1970_01_01_00_00_00);
        JSON.defaultTimeZone = TimeZone.getTimeZone((String)this.p1);
        //in questo caso l'expected è soggetto a terzo parametro
        Assert.assertEquals(new Timestamp((long)this.p3), TypeUtils.castToTimestamp(this.p2));
    }

    @Test
    public void test_cast_to_BigDecimal_same() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_BigDecimal_same);
        Assert.assertEquals(this.expected1, this.bigDec == TypeUtils.castToBigDecimal(this.bigDec));
    }

    @Test
    public void test_cast_to_BigInteger_same() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_BigInteger_same);
        Assert.assertEquals(this.expected1, this.bigInt == TypeUtils.castToBigInteger(this.bigInt));
    }

    @Test
    public void test_cast_Array() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_Array);
        Assert.assertEquals(Integer[].class, TypeUtils.cast(new ArrayList(), Integer[].class, null).getClass());
    }

    @Test
    public void test_cast_to_Timestamp_util_date() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_timestamp_util_Date);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, java.sql.Timestamp.class));
    }

    @Test
    public void test_cast_to_Timestamp_sql_date() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_timestamp_sql_Date);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, java.sql.Timestamp.class));
    }

    @Test
    public void test_cast_to_timestamp_sql_timestamp() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_timestamp_sql_timestamp);
        java.sql.Timestamp date = new java.sql.Timestamp((long)this.p1);
        //in questo caso millis viene usato solo come input, l'expected viene calcolato sull'input
        Assert.assertEquals(date, TypeUtils.castToTimestamp(date));
    }

    @Test
    public void test_cast_to_timestamp_calendar() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_timestamp_calendar);
        Assert.assertEquals(this.expected1, this.object.getObject((String)this.p1, java.sql.Timestamp.class));
    }

    @Test
    public void test_cast_to_timestamp_not_error() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_to_timestamp_not_error);
        JSONException error = null;
        try{
            this.object.getObject((String)this.p1,java.sql.Timestamp.class);
        }catch(JSONException e){
            error = e;
        }
        Assert.assertNull(error);
        Assert.assertEquals(new Timestamp((long)this.p3), (java.sql.Timestamp) this.object.getObject((String)this.p1, java.sql.Timestamp.class));
    }

    @Test
    public void test_cast_ab() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_ab);
        Assert.assertEquals(this.b, this.object.getObject((String)this.p1, A.class));
    }

    @Test
    public void test_cast_ab1() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_ab_1);
        Assert.assertEquals(this.b, this.object.getObject((String)this.p1, IA.class));
    }

    @Test
    public void test_cast_ab_error() throws Exception {
        Assume.assumeTrue(type == Type.t_cast_ab_error);
        JSONException error = null;
        try{
            this.object.getObject((String)this.p1,B.class);
        }catch(JSONException e){
            error = e;
        }
        Assert.assertNotNull(error);
    }

    @Test
    public void test_error() throws Exception {
        Assume.assumeTrue(type == Type.t_error);
        JSONException error = null;
        try{
            TypeUtils.castToJavaBean(this.object,C.class,ParserConfig.getGlobalInstance());
        }catch(JSONException e){
            error = e;
        }
        Assert.assertNotNull(error);
    }

    @Test
    public void test_error2() throws Exception {
        Assume.assumeTrue(type == Type.t_error2);
        Method method = TypeUtilsTestParameterized.class.getMethod("f",List.class);
        Throwable error2 = null;
        try{
            TypeUtils.cast(this.object,method.getGenericParameterTypes()[0],ParserConfig.getGlobalInstance());
        }catch(JSONException ex){
            error2 = ex;
        }
        Assert.assertNotNull(error2);
    }

    @Test
    public void test_3() throws Exception {
        Assume.assumeTrue(type == Type.t_3);
        User user = JSON.toJavaObject(this.object,User.class);
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
