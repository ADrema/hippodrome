package com.hippodrome;

import static com.hippodrome.TestDataGenerator.DEFAULT_DISTANCE;
import static com.hippodrome.TestDataGenerator.DEFAULT_HORSE_NAME;
import static com.hippodrome.TestDataGenerator.DEFAULT_SPEED;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

import java.util.stream.Stream;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HorseTests {
    // Horse params
    private static final String NAME_PARAM = "name";
    private static final String SPEED_PARAM = "speed";
    private static final String DISTANCE_PARAM = "distance";

    // Error messages
    private static final String HORSE_OBJ_CANNOT_BE_NULL_ERR_MSG = "Horse object should not be null.";
    private static final String NAME_CANNOT_BE_BLANK_ERR_MSG = "Name cannot be blank.";
    private static final String HORSE_PARAM_MATCHES_ERR_MSG = "Horse %s should matches with initial params";
    private static final String HORSE_SHOULD_BE_CREATED_WITHOUT_EXCPTN_ERR_MSG = "Horse object should be" + " created without throwing an exception.";

    @DisplayName("Check horse creation with all valid params")
    @Test
    void checkHorseCreation() {
        Horse horse = assertDoesNotThrow(() -> new Horse(DEFAULT_HORSE_NAME, DEFAULT_SPEED, DEFAULT_DISTANCE), HORSE_SHOULD_BE_CREATED_WITHOUT_EXCPTN_ERR_MSG);
        assertNotNull(horse, HORSE_OBJ_CANNOT_BE_NULL_ERR_MSG);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(horse.getName()).as(HORSE_PARAM_MATCHES_ERR_MSG, NAME_PARAM).isEqualTo(DEFAULT_HORSE_NAME);
        softly.assertThat(horse.getSpeed()).as(HORSE_PARAM_MATCHES_ERR_MSG, SPEED_PARAM).isEqualTo(DEFAULT_SPEED);
        softly.assertThat(horse.getDistance()).as(HORSE_PARAM_MATCHES_ERR_MSG, DISTANCE_PARAM).isEqualTo(DEFAULT_DISTANCE);
        softly.assertAll();
    }

    @DisplayName("Check horse creation with 2 params: name and speed")
    @Test
    void checkHorseCreationWithTwoParams() {
        Horse horse = assertDoesNotThrow(() -> new Horse(DEFAULT_HORSE_NAME, DEFAULT_SPEED), HORSE_SHOULD_BE_CREATED_WITHOUT_EXCPTN_ERR_MSG);
        assertNotNull(horse, HORSE_OBJ_CANNOT_BE_NULL_ERR_MSG);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(horse.getName()).as(HORSE_PARAM_MATCHES_ERR_MSG, NAME_PARAM).isEqualTo(DEFAULT_HORSE_NAME);
        softly.assertThat(horse.getSpeed()).as(HORSE_PARAM_MATCHES_ERR_MSG, SPEED_PARAM).isEqualTo(DEFAULT_SPEED);
        softly.assertThat(horse.getDistance()).as(HORSE_PARAM_MATCHES_ERR_MSG, DISTANCE_PARAM).isEqualTo(0);
        softly.assertAll();
    }

    @DisplayName("Check horse creation with valid params")
    @Test
    void checkHorseMoveMethod() {
        double mockedRandomValue = 0.4;
        double expectedDistance = DEFAULT_DISTANCE + DEFAULT_SPEED * mockedRandomValue;

        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)) {
            mockedStatic.when(() -> Horse.getRandomDouble(0.2, 0.9)).thenReturn(mockedRandomValue);
            Horse horse = new Horse(DEFAULT_HORSE_NAME, DEFAULT_SPEED, DEFAULT_DISTANCE);
            horse.move();

            assertEquals(expectedDistance, horse.getDistance(), 0, "Distance was not updated correctly.");
            mockedStatic.verify(() -> Horse.getRandomDouble(0.2, 0.9), times(1));
        }
    }

    @DisplayName("Check wrong horse constructor params")
    @ParameterizedTest(name = "Create horse with name: {0}, speed: {1}, distance: {2} and check errMsg: {3}")
    @MethodSource("horseConstructorInvalidTestData")
    void checkExceptionForConstructorWithWrongParams(String name, double speed, double distance, String expErrMsg) {
        Exception thrownException = assertThrows(IllegalArgumentException.class, () -> new Horse(name, speed, distance));
        String actualMsg = thrownException.getMessage();
        assertTrue(actualMsg.equalsIgnoreCase(expErrMsg), format("Expected: '%s', Actual: '%s'", expErrMsg, actualMsg));
    }

    static Stream<Arguments> horseConstructorInvalidTestData() {
        return Stream.of(
                // Invalid horse name
                Arguments.of(null, DEFAULT_SPEED, DEFAULT_DISTANCE, "Name cannot be null."), // Null name
                Arguments.of("", DEFAULT_SPEED, DEFAULT_DISTANCE, NAME_CANNOT_BE_BLANK_ERR_MSG), // Empty name
                Arguments.of("   ", DEFAULT_SPEED, DEFAULT_DISTANCE, NAME_CANNOT_BE_BLANK_ERR_MSG), // Blank name (spaces)
                Arguments.of("\t", DEFAULT_SPEED, DEFAULT_DISTANCE, NAME_CANNOT_BE_BLANK_ERR_MSG), // Blank name (tab)
                Arguments.of("\n", DEFAULT_SPEED, DEFAULT_DISTANCE, NAME_CANNOT_BE_BLANK_ERR_MSG), // Blank name (new line)
                // Negative speed
                Arguments.of(DEFAULT_HORSE_NAME, -DEFAULT_SPEED, DEFAULT_DISTANCE, "Speed cannot be negative."),
                // Negative distance
                Arguments.of(DEFAULT_HORSE_NAME, DEFAULT_SPEED, -DEFAULT_DISTANCE, "Distance cannot be negative."));
    }
}
