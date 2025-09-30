package org.codeit.roomunion.user.domain.policy;

<<<<<<< HEAD
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
=======
import org.codeit.roomunion.moim.domain.model.Category;
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
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
<<<<<<< HEAD
            Set.of(MeetingCategory.GAME, MeetingCategory.INFO_ECONOMY)
=======
            Set.of(Category.GAME, Category.INFO_ECONOMY)
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
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
<<<<<<< HEAD
                Set.of(MeetingCategory.GAME, MeetingCategory.INFO_ECONOMY)
=======
                Set.of(Category.GAME, Category.INFO_ECONOMY)
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
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
<<<<<<< HEAD
                Set.of(MeetingCategory.GAME, MeetingCategory.INFO_ECONOMY)
=======
                Set.of(Category.GAME, Category.INFO_ECONOMY)
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
            );

            // when & then
            assertThatThrownBy(() -> UserPolicy.validate(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email format");
        }
    }

    @Nested
    @DisplayName("비밀번호 검증")
    class PasswordValidationTest {

        @ParameterizedTest
        @ValueSource(strings = {
<<<<<<< HEAD
            "Password1!",
            "Strong12@",
            "valid123#",
            "Test456$",
            "Good789+",
            "Abcd123!",        // 8자 경계값 (최소값)
            "Abcdefg12345!"    // 13자 경계값 (최대값)
=======
            "Password123!",
            "Strong123@",
            "Valid1234#"
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
        })
        @DisplayName("유효한 비밀번호인 경우 예외가 발생하지 않아야 한다")
        void validate_ShouldNotThrowException_WhenPasswordIsValid(String validPassword) {
            // given
            UserCreateCommand command = UserCreateCommandFixture.create(
                "test@example.com",
                validPassword,
                "nickname",
                Gender.MALE,
<<<<<<< HEAD
                Set.of(MeetingCategory.GAME, MeetingCategory.INFO_ECONOMY)
=======
                Set.of(Category.GAME, Category.INFO_ECONOMY)
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
            );

            // when & then
            assertThatCode(() -> UserPolicy.validate(command))
                .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(strings = {
<<<<<<< HEAD
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
=======
            "weak",              // 너무 짧음
            "password",          // 대문자, 숫자, 특수문자 없음
            "PASSWORD123",       // 소문자, 특수문자 없음
            "Password",          // 숫자, 특수문자 없음
            "12345678!",         // 대소문자 없음
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
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
<<<<<<< HEAD
                Set.of(MeetingCategory.GAME, MeetingCategory.INFO_ECONOMY)
=======
                Set.of(Category.GAME, Category.INFO_ECONOMY)
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
            );

            // when & then
            assertThatThrownBy(() -> UserPolicy.validate(command))
                .isInstanceOf(IllegalArgumentException.class)
<<<<<<< HEAD
                .hasMessage("Password must be 8-13 characters long and contain letters, numbers, and special characters (!, @, #, $, %, ^, *, (, ), _, +, =, -, ~)");
=======
                .hasMessage("Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, and one special character");
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
        }
    }

    @Nested
    @DisplayName("카테고리 검증")
<<<<<<< HEAD
    class MeetingCategoryValidationTest {
=======
    class CategoryValidationTest {
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))

        @Test
        @DisplayName("카테고리가 정확히 2개인 경우 예외가 발생하지 않아야 한다")
        void validate_ShouldNotThrowException_WhenCategoryCountIsTwo() {
            // given
            UserCreateCommand command = UserCreateCommandFixture.create(
                "test@example.com",
                "Password123!",
                "nickname",
                Gender.MALE,
<<<<<<< HEAD
                Set.of(MeetingCategory.GAME, MeetingCategory.INFO_ECONOMY)
=======
                Set.of(Category.GAME, Category.INFO_ECONOMY)
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
            );

            // when & then
            assertThatCode(() -> UserPolicy.validate(command))
                .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @MethodSource("provideInvalidCategories")
        @DisplayName("카테고리가 유효하지 않은 경우 예외가 발생해야 한다")
<<<<<<< HEAD
        void validate_ShouldThrowException_WhenCategoriesAreInvalid(Set<MeetingCategory> invalidCategories) {
=======
        void validate_ShouldThrowException_WhenCategoriesAreInvalid(Set<Category> invalidCategories) {
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
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
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("At least two categories must be selected");
        }

        private static Stream<Arguments> provideInvalidCategories() {
            return Stream.of(
                Arguments.of((Object) null),                    // null
                Arguments.of(Set.of()),                        // 빈 리스트
<<<<<<< HEAD
                Arguments.of(Set.of(MeetingCategory.GAME)),           // 1개
                Arguments.of(Set.of(MeetingCategory.GAME, MeetingCategory.INFO_ECONOMY, MeetingCategory.HOBBY)) // 3개 이상
=======
                Arguments.of(Set.of(Category.GAME)),           // 1개
                Arguments.of(Set.of(Category.GAME, Category.INFO_ECONOMY, Category.HOBBY)) // 3개 이상
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
            );
        }
    }
}