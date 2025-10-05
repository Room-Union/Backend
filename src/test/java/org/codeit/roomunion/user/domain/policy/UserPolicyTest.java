package org.codeit.roomunion.user.domain.policy;

import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.command.UserCreateCommandFixture;
import org.codeit.roomunion.user.domain.model.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserPolicyTest {

    @Test
    @DisplayName("모든 조건이 유효한 경우 예외가 발생하지 않아야 한다")
    void validate_ShouldNotThrowException_WhenAllConditionsAreValid() {
        // given
        UserCreateCommand command = UserCreateCommandFixture.create(
            "test@example.com",
            "Password123!",
            "nickname",
            Gender.MALE,
            Set.of(MeetingCategory.GAME, MeetingCategory.INFO_ECONOMY)
        );

        // when & then
        assertThatCode(() -> UserPolicy.validate(command))
            .doesNotThrowAnyException();
    }

    @Nested
    @DisplayName("이메일 검증")
    class EmailValidationTest {

        @ParameterizedTest
        @ValueSource(strings = {
            "test@example.com",
            "user@domain.co.kr",
            "valid.email@test.org",
            "number123@email.net"
        })
        @DisplayName("유효한 이메일 형식인 경우 예외가 발생하지 않아야 한다")
        void validate_ShouldNotThrowException_WhenEmailIsValid(String validEmail) {
            // given
            UserCreateCommand command = UserCreateCommandFixture.create(
                validEmail,
                "Password123!",
                "nickname",
                Gender.MALE,
                Set.of(MeetingCategory.GAME, MeetingCategory.INFO_ECONOMY)
            );

            // when & then
            assertThatCode(() -> UserPolicy.validate(command))
                .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "invalid-email",
            "@domain.com",
            "test@",
            "test.domain.com",
            "test@domain",
            ""
        })
        @DisplayName("유효하지 않은 이메일 형식인 경우 예외가 발생해야 한다")
        void validate_ShouldThrowException_WhenEmailIsInvalid(String invalidEmail) {
            // given
            UserCreateCommand command = UserCreateCommandFixture.create(
                invalidEmail,
                "Password123!",
                "nickname",
                Gender.MALE,
                Set.of(MeetingCategory.GAME, MeetingCategory.INFO_ECONOMY)
            );

            // when & then
            assertThatThrownBy(() -> UserPolicy.validate(command))
                .isInstanceOf(CustomException.class)
                .hasMessage("유효하지 않은 입력입니다.");
        }
    }

    @Nested
    @DisplayName("비밀번호 검증")
    class PasswordValidationTest {

        @ParameterizedTest
        @ValueSource(strings = {
            "Password1!",
            "Strong12@",
            "valid123#",
            "Test456$",
            "Good789+",
            "Abcd123!",        // 8자 경계값 (최소값)
            "Abcdefg12345!"    // 13자 경계값 (최대값)
        })
        @DisplayName("유효한 비밀번호인 경우 예외가 발생하지 않아야 한다")
        void validate_ShouldNotThrowException_WhenPasswordIsValid(String validPassword) {
            // given
            UserCreateCommand command = UserCreateCommandFixture.create(
                "test@example.com",
                validPassword,
                "nickname",
                Gender.MALE,
                Set.of(MeetingCategory.GAME, MeetingCategory.INFO_ECONOMY)
            );

            // when & then
            assertThatCode(() -> UserPolicy.validate(command))
                .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "weak",              // 너무 짧음 (8자 미만)
            "password",          // 숫자, 특수문자 없음
            "PASSWORD123",       // 특수문자 없음
            "Password",          // 숫자, 특수문자 없음
            "12345678!",         // 문자 없음
            "Abcdefgh",          // 숫자, 특수문자 없음
            "123456789",         // 문자, 특수문자 없음
            "!@#$%^*()_",        // 문자, 숫자 없음
            "VeryLongPassword123!", // 너무 길음 (13자 초과)
            "Pass1234567890123!", // 너무 길음 (13자 초과)
            ""                   // 빈 문자열
        })
        @DisplayName("유효하지 않은 비밀번호인 경우 예외가 발생해야 한다")
        void validate_ShouldThrowException_WhenPasswordIsInvalid(String invalidPassword) {
            // given
            UserCreateCommand command = UserCreateCommandFixture.create(
                "test@example.com",
                invalidPassword,
                "nickname",
                Gender.MALE,
                Set.of(MeetingCategory.GAME, MeetingCategory.INFO_ECONOMY)
            );

            // when & then
            assertThatThrownBy(() -> UserPolicy.validate(command))
                .isInstanceOf(CustomException.class)
                .hasMessage("유효하지 않은 입력입니다.");
        }
    }

    @Nested
    @DisplayName("카테고리 검증")
    class MeetingCategoryValidationTest {

        @Test
        @DisplayName("카테고리가 정확히 2개인 경우 예외가 발생하지 않아야 한다")
        void validate_ShouldNotThrowException_WhenCategoryCountIsTwo() {
            // given
            UserCreateCommand command = UserCreateCommandFixture.create(
                "test@example.com",
                "Password123!",
                "nickname",
                Gender.MALE,
                Set.of(MeetingCategory.GAME, MeetingCategory.INFO_ECONOMY)
            );

            // when & then
            assertThatCode(() -> UserPolicy.validate(command))
                .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @MethodSource("provideInvalidCategories")
        @DisplayName("카테고리가 유효하지 않은 경우 예외가 발생해야 한다")
        void validate_ShouldThrowException_WhenCategoriesAreInvalid(Set<MeetingCategory> invalidCategories) {
            // given
            UserCreateCommand command = UserCreateCommandFixture.create(
                "test@example.com",
                "Password123!",
                "nickname",
                Gender.MALE,
                invalidCategories
            );

            // when & then
            assertThatThrownBy(() -> UserPolicy.validate(command))
                .isInstanceOf(CustomException.class)
                .hasMessage("유효하지 않은 입력입니다.");
        }

        private static Stream<Arguments> provideInvalidCategories() {
            return Stream.of(
                Arguments.of((Object) null),                    // null
                Arguments.of(Set.of()),                        // 빈 리스트
                Arguments.of(Set.of(MeetingCategory.GAME)),           // 1개
                Arguments.of(Set.of(MeetingCategory.GAME, MeetingCategory.INFO_ECONOMY, MeetingCategory.HOBBY)) // 3개 이상
            );
        }
    }
}