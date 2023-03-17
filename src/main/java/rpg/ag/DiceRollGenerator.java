/*
 * Oliver Kniffel, 2022.
 */
package rpg.ag;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiceRollGenerator {

  private static final Pattern dicePattern = Pattern.compile("^(\\d+)D(\\d+)([\\+\\-\\*\\/]\\d+)*$", Pattern.CASE_INSENSITIVE);
  
  private final DiceRoller dice;

  public DiceRollGenerator(final DiceRoller dice) {
    this.dice = dice;
  }

  public Optional<Generated> generateDiceRoll(final String expression) {
    final Matcher diceMatcher = dicePattern.matcher(expression);
    if (diceMatcher.find()) {
      final int number = Integer.parseInt(diceMatcher.group(1));
      final int type = Integer.parseInt(diceMatcher.group(2));
      int roll = dice.roll(number, type);

      final String modifier = diceMatcher.group(3);
      if (modifier != null) {

        switch (modifier.charAt(0)) {
        case '+':
          roll += Integer.parseInt(modifier.substring(1));
          break;
        case '-':
          roll -= Integer.parseInt(modifier.substring(1));
          break;
        case '*':
          roll *= Integer.parseInt(modifier.substring(1));
          break;
        case '/':
          roll = (int) Math.round(1.0 * roll / Integer.parseInt(modifier.substring(1)));
          break;
        default:
          throw new IllegalArgumentException("Invalid dice expression: " + expression);
        }
      }

      return Optional.of(new GeneratedText(Integer.toString(roll)));
    }

    return Optional.empty();
  }
}
