package com.lsadf.core.requests.user;

import static com.lsadf.core.constants.JsonAttributes.User.PASSWORD;
import static com.lsadf.core.constants.JsonAttributes.User.USERNAME;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.requests.Request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest implements Request {

  @Serial private static final long serialVersionUID = 4723986067784943176L;

  @NotBlank
  @JsonProperty(value = USERNAME)
  @Email
  private String username;

  @NotBlank
  @JsonProperty(value = PASSWORD)
  private String password;
}
