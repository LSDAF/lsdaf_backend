package com.lsadf.lsadf_backend.requests.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.annotations.Nickname;
import com.lsadf.lsadf_backend.requests.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.lsadf.core.constants.JsonAttributes.GameSave.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AdminGameSaveUpdateRequest implements Request {

    @Serial
    private static final long serialVersionUID = -1619677650296221394L;

    @JsonProperty(value = NICKNAME)
    @Nickname
    @Schema(description = "Nickname of the user", example = "test")
    private String nickname;

}
