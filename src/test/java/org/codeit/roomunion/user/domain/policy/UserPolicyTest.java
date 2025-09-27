package org.codeit.roomunion.user.domain.policy;

import org.codeit.roomunion.moim.domain.model.Category;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.command.UserCreateCommandFixture;
import org.codeit.roomunion.user.domain.model.Gender;
import org.junit.jupiter.api.BeforeEach;
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

    private UserPolicy userPolicy;

    @BeforeEach
    void setUp() {
        userPolicy = new UserPolicy();
    }

    @Test
    @DisplayName("모든 조건이 유효한 경우 예외가 발생하지 않아야 한다")
    void validate_ShouldNotThrowException_WhenAllConditionsAreValid() {
        // given
        UserCreateCommand command = UserCreateCommandFixture.create(
            "test@example.com",
            "Password123!",
            Gender.MALE,
            Set.of(Category.TEST, Category.TEST1),
            "테스트 사용자입니다."
        );

        // when & then
        assertThatCode(() -> userPolicy.validate(command))
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
                Gender.MALE,
                Set.of(Category.TEST, Category.TEST1),
                "테스트 사용자입니다."
            );

            // when & then
            assertThatCode(() -> userPolicy.validate(command))
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
                Gender.MALE,
                Set.of(Category.TEST, Category.TEST1),
                "테스트 사용자입니다."
            );

            // when & then
            assertThatThrownBy(() -> userPolicy.validate(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email format");
        }
    }

    @Nested
    @DisplayName("비밀번호 검증")
    class PasswordValidationTest {

        @ParameterizedTest
        @ValueSource(strings = {
            "Password123!",
            "Strong123@",
            "Valid1234#",
            "Test123$abc"
        })
        @DisplayName("유효한 비밀번호인 경우 예외가 발생하지 않아야 한다")
        void validate_ShouldNotThrowException_WhenPasswordIsValid(String validPassword) {
            // given
            UserCreateCommand command = UserCreateCommandFixture.create(
                "test@example.com",
                validPassword,
                Gender.MALE,
                Set.of(Category.TEST, Category.TEST1),
                "테스트 사용자입니다."
            );

            // when & then
            assertThatCode(() -> userPolicy.validate(command))
                .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "weak",              // 너무 짧음
            "password",          // 대문자, 숫자, 특수문자 없음
            "PASSWORD123",       // 소문자, 특수문자 없음
            "Password",          // 숫자, 특수문자 없음
            "12345678!",         // 대소문자 없음
            ""                   // 빈 문자열
        })
        @DisplayName("유효하지 않은 비밀번호인 경우 예외가 발생해야 한다")
        void validate_ShouldThrowException_WhenPasswordIsInvalid(String invalidPassword) {
            // given
            UserCreateCommand command = UserCreateCommandFixture.create(
                "test@example.com",
                invalidPassword,
                Gender.MALE,
                Set.of(Category.TEST, Category.TEST1),
                "테스트 사용자입니다."
            );

            // when & then
            assertThatThrownBy(() -> userPolicy.validate(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, and one special character");
        }
    }

    @Nested
    @DisplayName("카테고리 검증")
    class CategoryValidationTest {

        @Test
        @DisplayName("카테고리가 정확히 2개인 경우 예외가 발생하지 않아야 한다")
        void validate_ShouldNotThrowException_WhenCategoryCountIsTwo() {
            // given
            UserCreateCommand command = UserCreateCommandFixture.create(
                "test@example.com",
                "Password123!",
                Gender.MALE,
                Set.of(Category.TEST, Category.TEST1),
                "테스트 사용자입니다."
            );

            // when & then
            assertThatCode(() -> userPolicy.validate(command))
                .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @MethodSource("provideInvalidCategories")
        @DisplayName("카테고리가 유효하지 않은 경우 예외가 발생해야 한다")
        void validate_ShouldThrowException_WhenCategoriesAreInvalid(Set<Category> invalidCategories) {
            // given
            UserCreateCommand command = UserCreateCommandFixture.create(
                "test@example.com",
                "Password123!",
                Gender.MALE,
                invalidCategories,
                "테스트 사용자입니다."
            );

            // when & then
            assertThatThrownBy(() -> userPolicy.validate(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("At least two categories must be selected");
        }

        private static Stream<Arguments> provideInvalidCategories() {
            return Stream.of(
                Arguments.of((Object) null),                    // null
                Arguments.of(Set.of()),                        // 빈 리스트
                Arguments.of(Set.of(Category.TEST))           // 1개
            );
        }
    }
}