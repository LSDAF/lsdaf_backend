package com.lsadf.core.properties;

import com.lsadf.core.constants.JsonViews;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonViewProperties {
    private JsonViews.JsonViewType defaultJsonView;
    private boolean defaultJsonViewEnabled;
}
