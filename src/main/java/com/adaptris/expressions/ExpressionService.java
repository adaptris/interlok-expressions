package com.adaptris.expressions;

import java.util.ArrayList;
import java.util.List;

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
 * When configuring your algorithm you can specify parameters using the dollar + index (starting at 1) of the configured input parameter; "$1, $2, $3..."
 * </p>
 * <p>
 * An example:<br />
 * if you have configured 3 input parameters; a constant parameter and 2 metadata parameters in this order;
 * 
 * <pre>
 *   <parameters>
 *       <constant-data-input-parameter>
 *           <value>1000</value>
 *       </constant-data-input-parameter>
 *       <metadata-data-input-parameter>
 *           <metadata-key>key1</metadata-key>
 *       </metadata-data-input-parameter>
 *       <metadata-data-input-parameter>
 *           <metadata-key>key2</metadata-key>
 *       </metadata-data-input-parameter>
 *   </parameters>
 * </pre>
 * 
 * Then your algorithm may target those parameters by index like this;
 * 
 * <pre>
 *     ((($1 * $2) + 10) / $3)
 * </pre>
 * 
 * In this case we will take the constant configured input value of "1000" and multiple it by the numerical value of the metadata item named "key1", before finally
 * dividing the result by the numerical value of the metadata item named "key2".
 * </p>
 * <p>
 * Additionally you can also perform boolean calculations.  To do this, you will need to override the result-formatter.  By default we use {@link NumericalResultFormatter}, for algorithms
 * that return true or false, you will need to set the result-formatter to boolean-result-formatter ({@link BooleanResultFormatter})
 * </p> 
 * <p>
 * Then your algorithm can test and return boolean values.  A few examples; <br/>
 * <pre>
 *     ($1 > $2)
 * </pre>
 * <pre>
 *     ($1 <= $2)
 * </pre>
 * <pre>
 *     ($1.equals($2))
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
  
  protected transient Logger log = LoggerFactory.getLogger(this.getClass().getName());
  
  private static final String DEFAULT_RESULT_METADATA_KEY = "expressionResult";

  private DataOutputParameter<String> result;
  
  private String algorithm;
  
  private List<DataInputParameter<String>> parameters;
  
  private ResultFormatter resultFormatter;
  
  public ExpressionService() {
	this.setParameters(new ArrayList<DataInputParameter<String>>());
	this.setResult(new MetadataDataOutputParameter(DEFAULT_RESULT_METADATA_KEY));
	this.setResultFormatter(new NumericalResultFormatter());
  }
  
  @Override
  public void doService(AdaptrisMessage msg) throws ServiceException {
    Interpreter interpreter = new Interpreter();
    
    try {
      for(int counter = 0; counter < this.getParameters().size(); counter ++) {
        Double extractedValue = null;
        String extractedString = this.getParameters().get(counter).extract(msg);
        log.trace("Parameter value extracted: {}", extractedString);
        try {
          extractedValue = new Double(extractedString);
        } catch (NumberFormatException ex) {
          log.error("Cannot convert your input data into a number: " + extractedString);
          throw new ServiceException(ex);
        }
        interpreter.set("$" + (counter + 1), extractedValue);
      }
  
      interpreter.eval("result = (" + this.getAlgorithm() + ")");
      
      String stringResult = this.getResultFormatter().format(interpreter.get("result"));
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

  public void setResult(DataOutputParameter<String> output) {
    this.result = output;
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