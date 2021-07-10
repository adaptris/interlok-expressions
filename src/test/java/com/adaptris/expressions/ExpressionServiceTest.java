package com.adaptris.expressions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.adaptris.core.common.MetadataDataOutputParameter;
import com.adaptris.interlok.config.DataOutputParameter;
import org.junit.Test;

public class ExpressionServiceTest {
    @Test
    public void testConstructor() {
        ExpressionService actualExpressionService = new ExpressionService();
        assertTrue(actualExpressionService.getResultFormatter() instanceof NumericalResultFormatter);
        DataOutputParameter<String> result = actualExpressionService.getResult();
        assertTrue(result instanceof MetadataDataOutputParameter);
        assertEquals("expressionResult", ((MetadataDataOutputParameter) result).getMetadataKey());
    }
}

