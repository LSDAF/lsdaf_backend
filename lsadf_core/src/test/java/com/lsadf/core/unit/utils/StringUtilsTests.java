package com.lsadf.core.unit.utils;

import com.lsadf.core.utils.StringUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTests {

    @Test
    void should_capitalize_string() {
        // given
        String str = "test";

        // when
        String result = StringUtils.capitalize(str);

        // then
        assertThat(result).isEqualTo("Test");
    }

    @Test
    void should_return_empty_string_when_input_is_empty() {
        // given
        String str = "";

        // when
        String result = StringUtils.capitalize(str);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void should_return_null_if_input_is_null() {
        // given
        String str = null;

        // when
        String result = StringUtils.capitalize(str);

        // then
        assertThat(result).isNull();
    }

    @Test
    void should_return_capitalized_string_if_already_capitalized() {
        // given
        String str = "Test";

        // when
        String result = StringUtils.capitalize(str);

        // then
        assertThat(result).isEqualTo("Test");
    }

}
