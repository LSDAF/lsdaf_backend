package com.lsadf.lsadf_backend.requests.item;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.lsadf_backend.requests.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Data
@NoArgsConstructor
//@AllArgsConstructor
@JsonPropertyOrder({})
public class ItemRequest implements Request {

    @Serial
    private static final long serialVersionUID = -1116418739363127022L;
}
