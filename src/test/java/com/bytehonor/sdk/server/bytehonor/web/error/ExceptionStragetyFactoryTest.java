package com.bytehonor.sdk.server.bytehonor.web.error;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.bytehonor.sdk.define.bytehonor.error.InternalRestfulException;
import com.bytehonor.sdk.define.bytehonor.error.ServerBasicException;
import com.bytehonor.sdk.jdbc.bytehonor.web.error.ExceptionStragety;
import com.bytehonor.sdk.jdbc.bytehonor.web.error.ExceptionStragetyFactory;
import com.bytehonor.sdk.jdbc.bytehonor.web.error.stragety.InternalRestfulExceptionStragety;
import com.bytehonor.sdk.jdbc.bytehonor.web.error.stragety.NullPointerExceptionStragety;
import com.bytehonor.sdk.jdbc.bytehonor.web.error.stragety.ServerDefinedExceptionStragety;

public class ExceptionStragetyFactoryTest {

    @Test
    public void testBuild() {

        ExceptionStragety s1 = ExceptionStragetyFactory.build(new NullPointerException());
        boolean t1 = s1 instanceof NullPointerExceptionStragety;

        ExceptionStragety s2 = ExceptionStragetyFactory.build(new InternalRestfulException());
        boolean t2 = s2 instanceof InternalRestfulExceptionStragety;

        ExceptionStragety s3 = ExceptionStragetyFactory.build(new ServerBasicException());
        boolean t3 = s3 instanceof ServerDefinedExceptionStragety;

        boolean isOk = t1 && t2 && t3;
        assertTrue("testBuild", isOk);
    }

}
