/*
    Copyright Adaptris Ltd

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.adaptris.expressions;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
  
  @Before
  public void setUp() throws Exception {
    mutableConstantDataParameterOne = new MutableConstantDataParameter();
    mutableConstantDataParameterOne.setName("OutputOne");
    mutableConstantDataParameterOne.setStartingValue("10");
    
    mutableConstantDataParameterTwo = new MutableConstantDataParameter();
    mutableConstantDataParameterTwo.setName("OutputTwo");
    mutableConstantDataParameterTwo.setStartingValue("100");
  }
  
  @After
  public void tearDown() throws Exception {
  }
  
  @Test
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
  
  @Test
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
