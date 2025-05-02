package com.hippodrome;

import static com.hippodrome.TestDataGenerator.createRandomHorses;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class HippodromeTests {



    @DisplayName("Check Hippodrome creation with wrong params")
    @ParameterizedTest(name = "Create hippodrome with {0}, and check errMsg: {2}")
    @MethodSource("wrongHippodromeParams")
    void checkHippodromeCreationWithNullParams(String caseDescription, List<Horse> horses, String expErrMsg) {
        Exception thrownException = assertThrows(IllegalArgumentException.class, () -> new Hippodrome(horses));
        String actualMsg = thrownException.getMessage();
        assertTrue(actualMsg.equalsIgnoreCase(expErrMsg), format("Expected: '%s', Actual: '%s'", expErrMsg, actualMsg));
    }

    static Stream<Arguments> wrongHippodromeParams() {
        return Stream.of(
                Arguments.of("null horses list", null, "Horses cannot be null."),
                Arguments.of("empty horses list", new ArrayList<>(), "Horses cannot be empty.")
        );
    }

    @DisplayName("Check horses created in exact order")
    @Test
    void checkHippodromeHorsesOrderCreation() {
        List<Horse> horses = createRandomHorses(30);
        Hippodrome hippodrome = new Hippodrome(horses);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(hippodrome.getHorses().size())
                .as("Horses size is not identical")
                .isEqualTo(horses.size());
        softly.assertThat(IntStream.range(0, horses.size())
                        .allMatch(i -> horses.get(i) == hippodrome.getHorses().get(i)))
                .as("Horses lists are not identical or order is wrong")
                .isTrue();
        softly.assertAll();
    }

    @DisplayName("Check move methods is triggered 1 time per every horse")
    @Test
    void checkHippodromeMoveMethod() {
        List<Horse> mockedHorses = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Horse horseMock = mock(Horse.class);
            mockedHorses.add(horseMock);
        }
        Hippodrome hippodrome = new Hippodrome(mockedHorses);
        hippodrome.move();

        for (Horse horse : mockedHorses) {
            verify(horse, times(1)).move();
        }
    }

    @DisplayName("Check getWinner() returns horse with MAX distance value")
    @Test
    void checkHippodromeGetWinnerMethod() {
        Horse firstHorse = new Horse("LOOSER", 5, 1);
        Horse winnerdHorse = new Horse("WINNER", 20);
        Horse thirdHorse = new Horse("MIDDLE", 10);

        List<Horse> horses = List.of(firstHorse, winnerdHorse, thirdHorse);
        Hippodrome hippodrome = new Hippodrome(horses);
        // Check that first horse is selected as winner before start as it has distance
        assertEquals(hippodrome.getWinner(), firstHorse, "Wrong winner horse is selected on start");
        // Check that firs horse is selected as winner on finish
        hippodrome.move();
        assertEquals(hippodrome.getWinner(), winnerdHorse, "Wrong winner horse is selected on finish");
    }
}
