package com.adaptris.expressions;

import java.util.ArrayList;
import java.util.List;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.DefaultMessageFactory;
import com.adaptris.core.common.ConstantDataInputParameter;
import com.adaptris.core.util.LifecycleHelper;
import com.adaptris.interlok.config.DataInputParameter;

import junit.framework.TestCase;

public class MutableConstantDataParameterTest extends TestCase {
  
  private MutableConstantDataParameter mutableConstantDataParameterOne;
  private MutableConstantDataParameter mutableConstantDataParameterTwo;
  
  public void setUp() throws Exception {
    mutableConstantDataParameterOne = new MutableConstantDataParameter();
    mutableConstantDataParameterOne.setName("OutputOne");
    mutableConstantDataParameterOne.setStartingValue("10");
    
    mutableConstantDataParameterTwo = new MutableConstantDataParameter();
    mutableConstantDataParameterTwo.setName("OutputTwo");
    mutableConstantDataParameterTwo.setStartingValue("100");
  }
  
  public void tearDown() throws Exception {
    
  }
  
  public void testMutableAcrossServiceInvocations() throws Exception {
    
    this.doExpressionService(mutableConstantDataParameterOne);
    
    String result = mutableConstantDataParameterOne.extract(DefaultMessageFactory.getDefaultInstance().newMessage());
    assertEquals("20", result);
    
    this.doExpressionService(mutableConstantDataParameterOne);
    
    result = mutableConstantDataParameterOne.extract(DefaultMessageFactory.getDefaultInstance().newMessage());
    assertEquals("30", result);
    
    this.doExpressionService(mutableConstantDataParameterOne);
    
    result = mutableConstantDataParameterOne.extract(DefaultMessageFactory.getDefaultInstance().newMessage());
    assertEquals("40", result);
    
    this.doExpressionService(mutableConstantDataParameterOne);
    
    result = mutableConstantDataParameterOne.extract(DefaultMessageFactory.getDefaultInstance().newMessage());
    assertEquals("50", result);
    
    this.doExpressionService(mutableConstantDataParameterOne);
    
    result = mutableConstantDataParameterOne.extract(DefaultMessageFactory.getDefaultInstance().newMessage());
    assertEquals("60", result); 
  }
  
  public void testMultipleMutableAcrossServiceInvocations() throws Exception {
    this.doExpressionService(mutableConstantDataParameterOne);
    
    String result = mutableConstantDataParameterOne.extract(DefaultMessageFactory.getDefaultInstance().newMessage());
    assertEquals("20", result);
    
    this.doExpressionService(mutableConstantDataParameterTwo);
    
    result = mutableConstantDataParameterTwo.extract(DefaultMessageFactory.getDefaultInstance().newMessage());
    assertEquals("110", result);
    
    this.doExpressionService(mutableConstantDataParameterOne);
    
    result = mutableConstantDataParameterOne.extract(DefaultMessageFactory.getDefaultInstance().newMessage());
    assertEquals("30", result);
    
    this.doExpressionService(mutableConstantDataParameterTwo);
    
    result = mutableConstantDataParameterTwo.extract(DefaultMessageFactory.getDefaultInstance().newMessage());
    assertEquals("120", result);
  }
  
  private void doExpressionService(MutableConstantDataParameter constantDataParameter) throws CoreException {
    ExpressionService service = new ExpressionService();
    
    ConstantDataInputParameter constantDataInputParameterTwo = new ConstantDataInputParameter("10");
    
    List<DataInputParameter<String>> parameters = new ArrayList<>();
    parameters.add(constantDataParameter);
    parameters.add(constantDataInputParameterTwo);
    
    service.setParameters(parameters);
    service.setAlgorithm("$1+$2");
    service.setResult(constantDataParameter);
    
    LifecycleHelper.init(service);
    LifecycleHelper.start(service);
    
    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    
    service.doService(adaptrisMessage);
    
    LifecycleHelper.stop(service);
    LifecycleHelper.close(service);
  }

}
