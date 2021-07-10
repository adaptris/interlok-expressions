package com.adaptris.expressions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.adaptris.core.common.FileDataOutputParameter;
import com.adaptris.core.common.MetadataDataOutputParameter;
import com.adaptris.interlok.config.DataOutputParameter;
import org.junit.Test;

public class FreeFormExpressionServiceTest {
    @Test
    public void testConstructor() {
        DataOutputParameter<String> result = (new FreeFormExpressionService()).getResult();
        assertTrue(result instanceof MetadataDataOutputParameter);
        assertEquals("expressionResult", ((MetadataDataOutputParameter) result).getMetadataKey());
    }

    @Test
    public void testSetResult() {
        FreeFormExpressionService freeFormExpressionService = new FreeFormExpressionService();
        FileDataOutputParameter fileDataOutputParameter = new FileDataOutputParameter();
        freeFormExpressionService.setResult(fileDataOutputParameter);
        assertSame(fileDataOutputParameter, freeFormExpressionService.getResult());
    }

    @Test
    public void testSetAlgorithm() {
        FreeFormExpressionService freeFormExpressionService = new FreeFormExpressionService();
        freeFormExpressionService.setAlgorithm("algorithm");
        assertEquals("algorithm", freeFormExpressionService.getAlgorithm());
    }
}

