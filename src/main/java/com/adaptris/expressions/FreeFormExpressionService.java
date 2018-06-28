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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.Service;
import com.adaptris.core.ServiceException;
import com.adaptris.core.ServiceImp;
import com.adaptris.core.common.MetadataDataOutputParameter;
import com.adaptris.interlok.config.DataInputParameter;
import com.adaptris.interlok.config.DataOutputParameter;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import bsh.Interpreter;

/**
 * <p>
 * Implementation of {@link Service} that allows you to configure a mathematical/boolean algorithm<br/>
 * </p>
 * <p>
 * The major difference between this service and the standard {@link ExpressionService} is simply that this service does not require any {@link DataInputParameter}'s to be configured.
 * Instead you simply use static values in your algorithm, which includes the ability to use the {@link AdaptrisMessage.resolve()}. 
 * </p>
 * <p>
 * To choose where to set the result of the algorithm use any single {@link DataOutputParameter}.<br />
 * The default result of the expression will be a new metadata item with the key "expressionResult".
 * </p>
 * <p>
 * An example:<br />
 * A static algorithm such as the following;
 * 
 * <pre>
 *   ((10 * 10) * 10)
 * </pre>
 * Will produce the result 1000
 * </p>
 * <p>
 * A further example using the AdaptrisMessage.resolve() function allows us to create algorithms where the values are stored in metadata;
 * <pre>
 *   ((%message{key1} * %message{key2}) * %message{key3})
 * </pre>
 * Lets assume that the values for the metadata items identified by the keys 'key1', 'key2' and 'key3' are all "10", then again the result will be 1000.
 * </p>
 * <p>
 * Additionally you can also perform boolean calculations. A few examples; <br/>
 * <pre>
 *     (10 > 5)
 * </pre>
 * <pre>
 *     (%message{key1} <= 50)
 * </pre>
 * <pre>
 *     (%message{key1} == %message{key2})
 * </pre>
 * <pre>
 *     ((%message{key1} - 50 ) == %message{key2})
 * </pre>
 * </p>
 * 
 * @config expression-service
 * 
 */
@XStreamAlias("freeform-expression-service")
@AdapterComponent
@ComponentProfile(summary = "A service that allows you to evaluate a mathematical/boolean algorithm without specifying parameters.", tag = "service,expressions")
public class FreeFormExpressionService extends ServiceImp {

  protected transient Logger log = LoggerFactory.getLogger(this.getClass().getName());
  
  private static final String DEFAULT_RESULT_METADATA_KEY = "expressionResult";

  private DataOutputParameter<String> result;
  
  private String algorithm;
  
  public FreeFormExpressionService() {
    this.setResult(new MetadataDataOutputParameter(DEFAULT_RESULT_METADATA_KEY));
  }
  
  @Override
  public void doService(AdaptrisMessage msg) throws ServiceException {
    try {
      Interpreter interpreter = new Interpreter();
      interpreter.eval("result = (" + msg.resolve(this.getAlgorithm()) + ")");
      
      String stringResult = interpreter.get("result").toString();
      log.trace(this.getAlgorithm() + " evaluated to :" + stringResult);
      
      result.insert(stringResult, msg);
    } catch (Exception ex) {
      throw new ServiceException(ex);
    }
  }

  @Override
  public void prepare() throws CoreException {
  }

  @Override
  protected void initService() throws CoreException {
  }

  @Override
  protected void closeService() {
  }

  public DataOutputParameter<String> getResult() {
    return result;
  }

  public void setResult(DataOutputParameter<String> result) {
    this.result = result;
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }
}
