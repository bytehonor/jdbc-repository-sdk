package com.bytehonor.sdk.server.bytehonor.web.response;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.bytehonor.sdk.jdbc.bytehonor.web.error.ExceptionHolder;
import com.bytehonor.sdk.jdbc.bytehonor.web.response.ResponseStragety;
import com.bytehonor.sdk.jdbc.bytehonor.web.response.ResponseStragetyFactory;
import com.bytehonor.sdk.jdbc.bytehonor.web.response.stragety.ReturnErrorResponseStragety;
import com.bytehonor.sdk.jdbc.bytehonor.web.response.stragety.ReturnNullResponseStragety;

public class ResponseStragetyFactoryTest {

    @Test
    public void testBuild() {
        ResponseStragety s1 = ResponseStragetyFactory.build(null, null, null, null);

        boolean t1 = s1 instanceof ReturnNullResponseStragety;

        ResponseStragety s2 = ResponseStragetyFactory.build(new ExceptionHolder(), null, null, null);

        boolean t2 = s2 instanceof ReturnErrorResponseStragety;

        boolean isOk = t1 && t2;

        assertTrue("testBuild", isOk);
    }

}
