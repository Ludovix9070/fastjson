package com.alibaba.json.bvt.basicType;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Ignore;

/**
 * Created by wenshao on 10/08/2017.
 */
@Ignore
public class FloatNullTest_primitive extends TestCase {
    public void test_null() throws Exception {
        Model model = JSON.parseObject("{\"v1\":null,\"v2\":null}", Model.class);
        assertNotNull(model);
        assertEquals(0F, model.v1);
        assertEquals(0F,model.v2);
    }

    public void test_null_1() throws Exception {
        Model model = JSON.parseObject("{\"v1\":null ,\"v2\":null }", Model.class);
        assertNotNull(model);
        assertEquals(0F,model.v1);
        assertEquals(0F,model.v2);
    }

    public void test_null_2() throws Exception {
        Model model = JSON.parseObject("{\"v1\":\"null\",\"v2\":\"null\" }", Model.class);
        assertNotNull(model);
        assertEquals(0F,model.v1);
        assertEquals(0F,model.v2);
    }

    public static class Model {
        public float v1;
        public float v2;
    }
}
