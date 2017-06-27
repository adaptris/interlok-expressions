package com.adaptris.expressions;

import java.util.ArrayList;
import java.util.List;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.DefaultMessageFactory;
import com.adaptris.core.ServiceException;
import com.adaptris.core.common.ConstantDataInputParameter;
import com.adaptris.core.common.MetadataDataInputParameter;
import com.adaptris.core.common.MetadataDataOutputParameter;
import com.adaptris.core.util.LifecycleHelper;
import com.adaptris.interlok.config.DataInputParameter;

import junit.framework.TestCase;

public class ExpresssionServiceTest extends TestCase {
  
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
    
    startService();
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    
    service.doService(adaptrisMessage);
    
    stopService();
    
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
    
    startService();
    
    try {
      service.doService(DefaultMessageFactory.getDefaultInstance().newMessage());
      fail("Should fail because the parameters are not numbers.");
    } catch (ServiceException ex) {
      stopService();
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
    
    startService();
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    
    service.doService(adaptrisMessage);
    
    stopService();
    
    assertEquals("40", adaptrisMessage.getMetadataValue("customKey"));
  }
  
  
  private void stopService() {
    LifecycleHelper.stop(service);
    LifecycleHelper.close(service);
  }

  private void startService() throws CoreException {
    LifecycleHelper.init(service);
    LifecycleHelper.start(service);
  }
}
