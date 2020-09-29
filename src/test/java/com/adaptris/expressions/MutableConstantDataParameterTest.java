package com.adaptris.expressions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.adaptris.core.DefaultMessageFactory;
import com.adaptris.core.MultiPayloadAdaptrisMessageImp;
import com.adaptris.interlok.InterlokException;
import com.adaptris.util.GuidGenerator;
import org.junit.Test;

public class MutableConstantDataParameterTest {
    @Test
    public void testConstructor() {
        assertTrue((new MutableConstantDataParameter()).log instanceof org.slf4j.impl.SimpleLogger);
    }

    @Test
    public void testExtract() throws InterlokException {
        GuidGenerator guid = new GuidGenerator();
        MultiPayloadAdaptrisMessageImp m = new MultiPayloadAdaptrisMessageImp("42", guid, new DefaultMessageFactory());
        assertNull((new MutableConstantDataParameter()).extract(m));
    }

    @Test
    public void testSetStartingValue() {
        MutableConstantDataParameter mutableConstantDataParameter = new MutableConstantDataParameter();
        mutableConstantDataParameter.setStartingValue("constantValue");
        assertEquals("constantValue", mutableConstantDataParameter.getStartingValue());
    }

    @Test
    public void testSetName() {
        MutableConstantDataParameter mutableConstantDataParameter = new MutableConstantDataParameter();
        mutableConstantDataParameter.setName("name");
        assertEquals("name", mutableConstantDataParameter.getName());
    }
}

