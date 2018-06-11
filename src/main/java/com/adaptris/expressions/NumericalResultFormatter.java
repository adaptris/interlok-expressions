package com.adaptris.expressions;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("numerical-result-formatter")
public class NumericalResultFormatter implements ResultFormatter {

  @Override
  public String format(Object object) throws Exception {
    return String.format("%.0f", object);
  }

}
