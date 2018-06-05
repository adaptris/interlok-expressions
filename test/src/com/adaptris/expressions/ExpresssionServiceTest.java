package com.adaptris.expressions;

import java.util.ArrayList;
import java.util.List;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.DefaultMessageFactory;
import com.adaptris.core.ServiceCase;
import com.adaptris.core.ServiceException;
import com.adaptris.core.common.ConstantDataInputParameter;
import com.adaptris.core.common.MetadataDataInputParameter;
import com.adaptris.core.common.MetadataDataOutputParameter;
import com.adaptris.interlok.config.DataInputParameter;

public class ExpresssionServiceTest extends ServiceCase {
  
  public ExpresssionServiceTest(String name) {
    super(name);
  }

  private ExpressionService service;
  
  public void setUp() throws Exception {
    service = new ExpressionService();
  }

  public void tearDown() throws Exception {
    
  }
  
  public void testSimpleAlgorithmDefaultResultMetadataKey() throws Exception {
    ConstantDataInputParameter constantDataInputParameterOne = new ConstantDataInputParameter("10");
    ConstantDataInputParameter constantDataInputParameterTwo = new ConstantDataInputParameter("10");
    
    List<DataInputParameter<String>> parameters = new ArrayList<>();
    parameters.add(constantDataInputParameterOne);
    parameters.add(constantDataInputParameterTwo);
    
    service.setParameters(parameters);
    service.setAlgorithm("$1+$2");
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    
    execute(service, adaptrisMessage);
    assertTrue(service.getResult() instanceof MetadataDataOutputParameter);
    assertEquals("20", adaptrisMessage.getMetadataValue("expressionResult"));
  }

  public void testInvalidFormat() throws Exception {
    ConstantDataInputParameter constantDataInputParameterOne = new ConstantDataInputParameter("x");
    ConstantDataInputParameter constantDataInputParameterTwo = new ConstantDataInputParameter("x");
    
    List<DataInputParameter<String>> parameters = new ArrayList<>();
    parameters.add(constantDataInputParameterOne);
    parameters.add(constantDataInputParameterTwo);
    
    service.setParameters(parameters);
    service.setAlgorithm("$1+$2");
    
    try {
      execute(service, DefaultMessageFactory.getDefaultInstance().newMessage());
      fail("Should fail because the parameters are not numbers.");
    } catch (ServiceException ex) {
      // expected
    }
  }
  
  public void testCustomResult() throws Exception {
    ConstantDataInputParameter constantDataInputParameterOne = new ConstantDataInputParameter("20");
    ConstantDataInputParameter constantDataInputParameterTwo = new ConstantDataInputParameter("20");
    
    List<DataInputParameter<String>> parameters = new ArrayList<>();
    parameters.add(constantDataInputParameterOne);
    parameters.add(constantDataInputParameterTwo);
    
    service.setParameters(parameters);
    service.setAlgorithm("$1+$2");
    service.setResult(new MetadataDataOutputParameter("customKey"));
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    
    execute(service, adaptrisMessage);
    
    assertEquals("40", adaptrisMessage.getMetadataValue("customKey"));
  }
  
  public void testBooleanEqualsResult() throws Exception {
    ConstantDataInputParameter constantDataInputParameterOne = new ConstantDataInputParameter("20");
    ConstantDataInputParameter constantDataInputParameterTwo = new ConstantDataInputParameter("20");
    
    List<DataInputParameter<String>> parameters = new ArrayList<>();
    parameters.add(constantDataInputParameterOne);
    parameters.add(constantDataInputParameterTwo);
    
    service.setParameters(parameters);
    service.setAlgorithm("$1.equals($2)");
    service.setResult(new MetadataDataOutputParameter("customKey"));
    service.setResultFormatter(new BooleanResultFormatter());
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    
    execute(service, adaptrisMessage);
    
    assertEquals("true", adaptrisMessage.getMetadataValue("customKey"));
  }
  
  public void testBooleanNotEqualsResult() throws Exception {
    ConstantDataInputParameter constantDataInputParameterOne = new ConstantDataInputParameter("50");
    ConstantDataInputParameter constantDataInputParameterTwo = new ConstantDataInputParameter("20");
    
    List<DataInputParameter<String>> parameters = new ArrayList<>();
    parameters.add(constantDataInputParameterOne);
    parameters.add(constantDataInputParameterTwo);
    
    service.setParameters(parameters);
    service.setAlgorithm("$1.equals($2)");
    service.setResult(new MetadataDataOutputParameter("customKey"));
    service.setResultFormatter(new BooleanResultFormatter());
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    
    execute(service, adaptrisMessage);
    
    assertEquals("false", adaptrisMessage.getMetadataValue("customKey"));
  }
  
  public void testBooleanGreaterResult() throws Exception {
    ConstantDataInputParameter constantDataInputParameterOne = new ConstantDataInputParameter("30");
    ConstantDataInputParameter constantDataInputParameterTwo = new ConstantDataInputParameter("20");
    
    List<DataInputParameter<String>> parameters = new ArrayList<>();
    parameters.add(constantDataInputParameterOne);
    parameters.add(constantDataInputParameterTwo);
    
    service.setParameters(parameters);
    service.setAlgorithm("$1 > $2");
    service.setResult(new MetadataDataOutputParameter("customKey"));
    service.setResultFormatter(new BooleanResultFormatter());
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    
    execute(service, adaptrisMessage);
    
    assertEquals("true", adaptrisMessage.getMetadataValue("customKey"));
  }
  
  public void testBooleanNotGreaterResult() throws Exception {
    ConstantDataInputParameter constantDataInputParameterOne = new ConstantDataInputParameter("10");
    ConstantDataInputParameter constantDataInputParameterTwo = new ConstantDataInputParameter("20");
    
    List<DataInputParameter<String>> parameters = new ArrayList<>();
    parameters.add(constantDataInputParameterOne);
    parameters.add(constantDataInputParameterTwo);
    
    service.setParameters(parameters);
    service.setAlgorithm("$1 > $2");
    service.setResult(new MetadataDataOutputParameter("customKey"));
    service.setResultFormatter(new BooleanResultFormatter());
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    
    execute(service, adaptrisMessage);
    
    assertEquals("false", adaptrisMessage.getMetadataValue("customKey"));
  }
  
  public void testBooleanLessResult() throws Exception {
    ConstantDataInputParameter constantDataInputParameterOne = new ConstantDataInputParameter("10");
    ConstantDataInputParameter constantDataInputParameterTwo = new ConstantDataInputParameter("20");
    
    List<DataInputParameter<String>> parameters = new ArrayList<>();
    parameters.add(constantDataInputParameterOne);
    parameters.add(constantDataInputParameterTwo);
    
    service.setParameters(parameters);
    service.setAlgorithm("$1 < $2");
    service.setResult(new MetadataDataOutputParameter("customKey"));
    service.setResultFormatter(new BooleanResultFormatter());
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    
    execute(service, adaptrisMessage);
    
    assertEquals("true", adaptrisMessage.getMetadataValue("customKey"));
  }
  
  public void testBooleanNotLessResult() throws Exception {
    ConstantDataInputParameter constantDataInputParameterOne = new ConstantDataInputParameter("30");
    ConstantDataInputParameter constantDataInputParameterTwo = new ConstantDataInputParameter("20");
    
    List<DataInputParameter<String>> parameters = new ArrayList<>();
    parameters.add(constantDataInputParameterOne);
    parameters.add(constantDataInputParameterTwo);
    
    service.setParameters(parameters);
    service.setAlgorithm("$1 < $2");
    service.setResult(new MetadataDataOutputParameter("customKey"));
    service.setResultFormatter(new BooleanResultFormatter());
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    
    execute(service, adaptrisMessage);
    
    assertEquals("false", adaptrisMessage.getMetadataValue("customKey"));
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {
    ExpressionService service = new ExpressionService();
    service.setAlgorithm("$1 + $2");
    service.getParameters().add(new ConstantDataInputParameter("10"));
    service.getParameters().add(new MetadataDataInputParameter("metadatakey"));
    return service;

  }
}
