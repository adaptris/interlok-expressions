package com.adaptris.expressions;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("boolean-result-formatter")
public class BooleanResultFormatter implements ResultFormatter {

  @Override
  public String format(Object object) throws Exception {
    return object.toString();
  }

}
