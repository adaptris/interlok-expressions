package com.adaptris.expressions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BooleanResultFormatterTest {
    @Test
    public void testFormat() throws Exception {
        assertEquals("object", (new BooleanResultFormatter()).format("object"));
    }
}

