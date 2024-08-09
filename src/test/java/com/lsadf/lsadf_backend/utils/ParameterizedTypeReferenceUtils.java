package com.lsadf.lsadf_backend.utils;

import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.JwtAuthentication;
import com.lsadf.lsadf_backend.models.UserInfo;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import lombok.experimental.UtilityClass;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

@UtilityClass
public class ParameterizedTypeReferenceUtils {

    /**
     * Builds a ParameterizedTypeReference for a GenericResponse of UserInfo
     * @return ParameterizedTypeReference
     */
    public static ParameterizedTypeReference<GenericResponse<UserInfo>> buildParameterizedUserInfoResponse() {
        return new ParameterizedTypeReference<>() {
        };
    }

    /**
     * Builds a ParameterizedTypeReference for a GenericResponse of JwtAuthentication
     * @return ParameterizedTypeReference
     */
    public static ParameterizedTypeReference<GenericResponse<JwtAuthentication>> buildParameterizedJwtAuthenticationResponse() {
        return new ParameterizedTypeReference<>() {
        };
    }

    /**
     * Builds a ParameterizedTypeReference for a GenericResponse of GameSave
     * @return ParameterizedTypeReference
     */
    public static ParameterizedTypeReference<GenericResponse<GameSave>> buildParameterizedGameSaveResponse() {
        return new ParameterizedTypeReference<>() {
        };
    }

    /**
     * Builds a ParameterizedTypeReference for a GenericResponse of Void
     * @return ParameterizedTypeReference
     */
    public static ParameterizedTypeReference<GenericResponse<Void>> buildParameterizedVoidResponse() {
        return new ParameterizedTypeReference<>() {
        };
    }

    /**
     * Builds a ParameterizedTypeReference for a GenericResponse of List of GameSave
     * @return ParameterizedTypeReference
     */
    public static ParameterizedTypeReference<GenericResponse<List<GameSave>>> buildParameterizedGameSaveListResponse() {
        return new ParameterizedTypeReference<>() {
        };
    }
}
