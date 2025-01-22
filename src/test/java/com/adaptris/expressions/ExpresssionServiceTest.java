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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.DefaultMessageFactory;
import com.adaptris.core.ServiceException;
import com.adaptris.core.common.ConstantDataInputParameter;
import com.adaptris.core.common.MetadataDataInputParameter;
import com.adaptris.core.common.MetadataDataOutputParameter;
import com.adaptris.interlok.config.DataInputParameter;
import com.adaptris.interlok.junit.scaffolding.services.ExampleServiceCase;

public class ExpresssionServiceTest extends ExampleServiceCase {

  private ExpressionService service;

  @BeforeEach
  public void setUp() throws Exception {
    service = new ExpressionService();
  }

  @Test
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

  @Test
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

  @Test
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

  @Test
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

  @Test
  public void testBooleanEqualsResolveResult() throws Exception {
    List<DataInputParameter<String>> parameters = new ArrayList<>();

    service.setParameters(parameters);
    service.setAlgorithm("%message{key1} == %message{key2}");
    service.setResult(new MetadataDataOutputParameter("customKey"));
    service.setResultFormatter(new BooleanResultFormatter());

    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();
    adaptrisMessage.addMessageHeader("key1", "20");
    adaptrisMessage.addMessageHeader("key2", "20");


    execute(service, adaptrisMessage);

    assertEquals("true", adaptrisMessage.getMetadataValue("customKey"));
  }

  @Test
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

  @Test
  public void testBooleanNotEqualsResolveResult() throws Exception {
    List<DataInputParameter<String>> parameters = new ArrayList<>();

    service.setParameters(parameters);
    service.setAlgorithm("%message{key1} == %message{key2}");
    service.setResult(new MetadataDataOutputParameter("customKey"));
    service.setResultFormatter(new BooleanResultFormatter());

    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();

    adaptrisMessage.addMessageHeader("key1", "10");
    adaptrisMessage.addMessageHeader("key2", "20");

    execute(service, adaptrisMessage);

    assertEquals("false", adaptrisMessage.getMetadataValue("customKey"));
  }

  @Test
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

  @Test
  public void testBooleanGreaterResolveResult() throws Exception {
    List<DataInputParameter<String>> parameters = new ArrayList<>();

    service.setParameters(parameters);
    service.setAlgorithm("%message{key1} > %message{key2}");
    service.setResult(new MetadataDataOutputParameter("customKey"));
    service.setResultFormatter(new BooleanResultFormatter());

    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();

    adaptrisMessage.addMessageHeader("key1", "20");
    adaptrisMessage.addMessageHeader("key2", "10");

    execute(service, adaptrisMessage);

    assertEquals("true", adaptrisMessage.getMetadataValue("customKey"));
  }

  @Test
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

  @Test
  public void testBooleanNotGreaterResolveResult() throws Exception {
    List<DataInputParameter<String>> parameters = new ArrayList<>();

    service.setParameters(parameters);
    service.setAlgorithm("%message{key1} > %message{key2}");
    service.setResult(new MetadataDataOutputParameter("customKey"));
    service.setResultFormatter(new BooleanResultFormatter());

    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();

    adaptrisMessage.addMessageHeader("key1", "10");
    adaptrisMessage.addMessageHeader("key2", "20");

    execute(service, adaptrisMessage);

    assertEquals("false", adaptrisMessage.getMetadataValue("customKey"));
  }

  @Test
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

  @Test
  public void testBooleanLessResolveResult() throws Exception {
    List<DataInputParameter<String>> parameters = new ArrayList<>();

    service.setParameters(parameters);
    service.setAlgorithm("%message{key1} < %message{key2}");
    service.setResult(new MetadataDataOutputParameter("customKey"));
    service.setResultFormatter(new BooleanResultFormatter());

    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();

    adaptrisMessage.addMessageHeader("key1", "10");
    adaptrisMessage.addMessageHeader("key2", "20");

    execute(service, adaptrisMessage);

    assertEquals("true", adaptrisMessage.getMetadataValue("customKey"));
  }

  @Test
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

  @Test
  public void testBooleanNotLessResolveResult() throws Exception {
    List<DataInputParameter<String>> parameters = new ArrayList<>();

    service.setParameters(parameters);
    service.setAlgorithm("%message{key1} < %message{key2}");
    service.setResult(new MetadataDataOutputParameter("customKey"));
    service.setResultFormatter(new BooleanResultFormatter());

    AdaptrisMessage adaptrisMessage = DefaultMessageFactory.getDefaultInstance().newMessage();

    adaptrisMessage.addMessageHeader("key1", "30");
    adaptrisMessage.addMessageHeader("key2", "20");

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
