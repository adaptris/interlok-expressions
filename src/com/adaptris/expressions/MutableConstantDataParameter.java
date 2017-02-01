package com.adaptris.expressions;

import java.util.HashMap;
import java.util.Map;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.interlok.InterlokException;
import com.adaptris.interlok.config.DataDestination;
import com.adaptris.interlok.types.InterlokMessage;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <p>
 * Implementation of {@link com.adaptris.interlok.config.DataDestination} that allows you to configure a starting value, that may change if you use this
 * DataDestination object as the target of an expression evaluation.
 * </p>
 * 
 * @config mutable-constant-data-parameter
 * 
 */
@XStreamAlias("mutable-constant-data-parameter")
@AdapterComponent
@ComponentProfile(summary = "A preconfigured value that may change during message processing.", tag = "parameter")
public class MutableConstantDataParameter implements DataDestination<String, String> {
  
  private static Map<String, String> mutableValues;
  
  private String constantValue;
  
  private String name;

  public MutableConstantDataParameter() {
    mutableValues = new HashMap<String, String>();
  }
  
  @Override
  public String extract(InterlokMessage m) throws InterlokException {
    String returnedValue = mutableValues.get(this.getName());
    if(returnedValue == null) {
      mutableValues.put(this.getName(), this.getConstantValue());
      returnedValue = this.getConstantValue();
    }
    
    return returnedValue;
  }

  @Override
  public void insert(String data, InterlokMessage msg) throws InterlokException {
    mutableValues.put(this.getName(), data);
  }

  public String getConstantValue() {
    return constantValue;
  }

  public void setConstantValue(String constantValue) {
    this.constantValue = constantValue;
  }

  public static Map<String, String> getMutableValues() {
    return mutableValues;
  }

  public static void setMutableValues(Map<String, String> mutableValues) {
    MutableConstantDataParameter.mutableValues = mutableValues;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
