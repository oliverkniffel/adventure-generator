/*
 * Oliver Kniffel, 2021.
 */
package rpg.ag;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DiceRollGeneratorTest {

  private DiceRollGenerator testedObj;

  @Mock
  private DiceRoller dice;

  @ParameterizedTest
  // @formatter:off
  @CsvSource({ 
    "2d6, 2, 6, 7, 7", 
    "1D100, 1, 100, 55, 55", 
    "1d6+3, 1, 6, 3, 6", 
    "1d6-6, 1, 6, 3, -3", 
    "1d6*4, 1, 6, 3, 12", 
    "1d6/2, 1, 6, 6, 3", 
    "1d6/2, 1, 6, 3, 2", 
    "1d6/3, 1, 6, 5, 2" 
  })
  // @formatter:on
  void testGenerateDiceRoll(String expression, int number, int type, int roll, String expected) {
    this.testedObj = new DiceRollGenerator(dice);
    Mockito.when(dice.roll(number, type)).thenReturn(Integer.valueOf(roll));

    // When
    GeneratedText result = (GeneratedText) testedObj.generateDiceRoll(expression).get();

    // Then
    Mockito.verify(dice).roll(number, type);
    assertEquals(expected, result.getText());
  }
}
