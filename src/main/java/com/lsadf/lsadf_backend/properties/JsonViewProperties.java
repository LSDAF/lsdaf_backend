package com.lsadf.lsadf_backend.properties;

import com.lsadf.lsadf_backend.constants.JsonViews;
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
