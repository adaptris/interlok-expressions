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

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.Service;
import com.adaptris.core.ServiceException;
import com.adaptris.core.ServiceImp;
import com.adaptris.core.util.InputFieldExpression;
import org.apache.commons.lang3.StringUtils;
import com.adaptris.core.common.MetadataDataOutputParameter;
import com.adaptris.interlok.config.DataInputParameter;
import com.adaptris.interlok.config.DataOutputParameter;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import bsh.Interpreter;

/**
 * <p>
 * Implementation of {@link Service} that allows you to configure a mathematical algorithm and parameters.<br/>
 * Once all parameters have been evaluated to numerical values, the algorithm will be executed.
 * </p>
 * <p>
 * To specify the parameters for the algorithm simply use as many {@link DataInputParameter}'s as required.
 * </p>
 * <p>
 * To choose where to set the result of the algorithm use any single {@link DataOutputParameter}.<br />
 * The default result of the expression will be a new metadata item with the key "expressionResult".
 * </p>
 * <p>
 * When configuring your algorithm you can specify parameters using the dollar + index (starting at 1) of the configured input parameter;
 * "$1, $2, $3..."
 * </p>
 * <p>
 * An example:<br />
 * if you have configured 3 input parameters; a constant parameter and 2 metadata parameters in this order;
 *
 * <pre>
 *   &lt;parameters&gt;
 *       &lt;constant-data-input-parameter&gt;
 *           &lt;value&gt;1000&lt;/value&gt;
 *       &lt;/constant-data-input-parameter&gt;
 *       &lt;metadata-data-input-parameter&gt;
 *           &lt;metadata-key&gt;key1&lt;/metadata-key&gt;
 *       &lt;/metadata-data-input-parameter&gt;
 *       &lt;metadata-data-input-parameter&gt;
 *           &lt;metadata-key&gt;key2&lt;/metadata-key&gt;
 *       &lt;/metadata-data-input-parameter&gt;
 *   &lt;/parameters&gt;
 * </pre>
 *
 * Then your algorithm may target those parameters by index like this;
 *
 * <pre>
 * ((($1 * $2) + 10) / $3)
 * </pre>
 *
 * In this case we will take the constant configured input value of "1000" and multiple it by the numerical value of the metadata item named
 * "key1", before finally dividing the result by the numerical value of the metadata item named "key2".
 * </p>
 * <p>
 * A further example using the AdaptrisMessage.resolve() function allows us to create algorithms where the values are stored in metadata;
 *
 * <pre>
 *   ((%message{key1} * %message{key2}) * %message{key3})
 * </pre>
 *
 * Lets assume that the values for the metadata items identified by the keys 'key1', 'key2' and 'key3' are all "10", then again the result
 * will be 1000.
 * </p>
 * <p>
 * Additionally you can also perform boolean calculations. To do this, you will need to override the result-formatter. By default we use
 * {@link NumericalResultFormatter}, for algorithms that return true or false, you will need to set the result-formatter to
 * boolean-result-formatter ({@link BooleanResultFormatter})
 * </p>
 * <p>
 * Then your algorithm can test and return boolean values. A few examples; <br/>
 *
 * <pre>
 * ($1 > $2)
 * </pre>
 *
 * <pre>
 * ($1 <= $2)
 * </pre>
 *
 * <pre>
 * ($1.equals($2))
 * </pre>
 * </p>
 *
 * @config expression-service
 *
 */
@XStreamAlias("expression-service")
@AdapterComponent
@ComponentProfile(summary = "A service that allows you to evaluate a mathematical algorithm.", tag = "service,expressions")
public class ExpressionService extends ServiceImp {

  private static final String DEFAULT_RESULT_METADATA_KEY = "expressionResult";

  private DataOutputParameter<String> result;

  private String algorithm;

  private List<DataInputParameter<String>> parameters;

  private ResultFormatter resultFormatter;

  public ExpressionService() {
    setParameters(new ArrayList<DataInputParameter<String>>());
    setResult(new MetadataDataOutputParameter(DEFAULT_RESULT_METADATA_KEY));
    setResultFormatter(new NumericalResultFormatter());
  }

  @Override
  public void doService(AdaptrisMessage msg) throws ServiceException {
    Interpreter interpreter = new Interpreter();
    String algorithm = getAlgorithm();

    try {
      for (int counter = 0; counter < getParameters().size(); counter++) {
        Double extractedValue = null;
        String extractedString = getParameters().get(counter).extract(msg);
        log.trace("Parameter value extracted: {}", extractedString);
        try {
          extractedValue = Double.valueOf(extractedString);
        } catch (NumberFormatException ex) {
          log.error("Cannot convert your input data into a number: " + extractedString);
          throw new ServiceException(ex);
        }
        interpreter.set("$" + (counter + 1), extractedValue);
      }

      //Check and evaluate if algorithm is an expression
      if(InputFieldExpression.isExpression(algorithm)) {
        algorithm = msg.resolve(algorithm);
      }

      interpreter.eval("result = (" + algorithm + ")");

      String stringResult = getResultFormatter().format(interpreter.get("result"));
      log.trace(algorithm + " evaluated to :" + stringResult);

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

  public void setResult(DataOutputParameter<String> output) {
    result = output;
  }

  public List<DataInputParameter<String>> getParameters() {
    return parameters;
  }

  public void setParameters(List<DataInputParameter<String>> parameters) {
    this.parameters = parameters;
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }

  public ResultFormatter getResultFormatter() {
    return resultFormatter;
  }

  public void setResultFormatter(ResultFormatter resultFormatter) {
    this.resultFormatter = resultFormatter;
  }

}
