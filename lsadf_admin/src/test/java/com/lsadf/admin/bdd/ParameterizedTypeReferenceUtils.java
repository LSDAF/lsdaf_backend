package com.lsadf.admin.bdd;

import com.lsadf.core.models.*;
import com.lsadf.core.responses.GenericResponse;
import lombok.experimental.UtilityClass;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Utility class for building ParameterizedTypeReferences
 */
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
     * Builds a ParameterizedTypeReference for a GenericResponse of Characteristics
     * @return ParameterizedTypeReference
     */
    public static ParameterizedTypeReference<GenericResponse<Characteristics>> buildParameterizedCharacteristicsResponse() {
        return new ParameterizedTypeReference<>() {
        };
    }

    /**
     * Builds a ParameterizedTypeReference for a GenericResponse of Currency
     * @return ParameterizedTypeReference
     */
    public static ParameterizedTypeReference<GenericResponse<Currency>> buildParameterizedCurrencyResponse() {
        return new ParameterizedTypeReference<>() {
        };
    }

    /**
     * Builds a ParameterizedTypeReference for a GenericResponse of Inventory
     * @return ParameterizedTypeReference
     */
    public static ParameterizedTypeReference<GenericResponse<Inventory>> buildParameterizedInventoryResponse() {
        return new ParameterizedTypeReference<>() {
        };
    }

    /**
     * Builds a ParameterizedTypeReference for a GenericResponse of Stage
     * @return ParameterizedTypeReference
     */
    public static ParameterizedTypeReference<GenericResponse<Stage>> buildParameterizedStageResponse() {
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

    /**
     * Builds a ParameterizedTypeReference for a GenericResponse of List of GameSaveEntity
     * @return ParameterizedTypeReference
     */
    public static ParameterizedTypeReference<GenericResponse<Boolean>> buildParameterizedBooleanResponse() {
        return new ParameterizedTypeReference<>() {
        };
    }

    /**
     * Builds a ParameterizedTypeReference for a GenericResponse of GlobalInfo
     * @return ParameterizedTypeReference
     */
    public static ParameterizedTypeReference<GenericResponse<GlobalInfo>> buildParameterizedGlobalInfoResponse() {
        return new ParameterizedTypeReference<>() {
        };
    }

    /**
     * Builds a ParameterizedTypeReference for a GenericResponse of list of User
     * @return ParameterizedTypeReference
     */
    public static ParameterizedTypeReference<GenericResponse<List<User>>> buildParameterizedUserListResponse() {
        return new ParameterizedTypeReference<>() {
        };
    }

    /**
     * Builds a ParameterizedTypeReference for a GenericResponse of User
     * @return ParameterizedTypeReference
     */
    public static ParameterizedTypeReference<GenericResponse<User>> buildParamaterizedUserResponse() {
        return new ParameterizedTypeReference<>() {
        };
    }
}
