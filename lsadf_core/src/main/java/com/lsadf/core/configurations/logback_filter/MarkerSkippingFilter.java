package com.lsadf.core.configurations.logback_filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import java.util.List;
import lombok.Setter;
import org.slf4j.Marker;

@Setter
public class MarkerSkippingFilter extends Filter<ILoggingEvent> {

  private String markerToMatch;

  @Override
  public FilterReply decide(ILoggingEvent event) {
    if (markerToMatch == null) {
      return FilterReply.NEUTRAL;
    }
    List<Marker> markers = event.getMarkerList();
    if (markers == null) {
      return FilterReply.NEUTRAL;
    }

    if (markers.stream().anyMatch(marker -> markerToMatch.equals(marker.getName()))) {
      return FilterReply.ACCEPT;
    }

    return FilterReply.DENY;
  }
}
