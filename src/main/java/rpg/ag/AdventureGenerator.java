/*
 * Oliver Kniffel, 2021.
 */
package rpg.ag;

import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdventureGenerator {

  private DiceRoller dice;
  private Pattern dicePattern = Pattern.compile("^(\\d+)D(\\d+)([\\+\\-\\*\\/]\\d+)*$", Pattern.CASE_INSENSITIVE);

  public AdventureGenerator() {
    this.setDice(new DiceRoller(new Random()));
  }

  public void setDice(DiceRoller dice) {
    if (dice == null) {
      throw new IllegalArgumentException("Property 'dice' cannot be null.");
    }
    this.dice = dice;
  }

  public void setRandom(Random random) {
    this.dice.setRandom(random);
  }

  public Generated generate(Genre genre, String elementName) {
    if (genre == null) {
      throw new IllegalArgumentException("Genre parameter cannot be null.");
    }

    if (elementName == null || elementName.trim().length() == 0) {
      throw new IllegalArgumentException("Element name parameter cannot be null or blank.");
    }

    Optional<Generated> generatedDiceRollOptional = generateDiceRoll(elementName);
    if (generatedDiceRollOptional.isPresent()) {
      return generatedDiceRollOptional.get();
    }

    GenreElement element = genre.getElement(elementName).orElseThrow(() -> new IllegalArgumentException("Unknown genre element " + elementName));
    return generate(genre, element);
  }

  public Optional<Generated> generateDiceRoll(String expression) {
    Matcher diceMatcher = dicePattern.matcher(expression);
    if (diceMatcher.find()) {
      int number = Integer.parseInt(diceMatcher.group(1));
      int type = Integer.parseInt(diceMatcher.group(2));
      int roll = dice.roll(number, type);

      String modifier = diceMatcher.group(3);
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

  private Generated generate(Genre genre, GenreElement element) {
    if (element instanceof GenreObject) {
      GeneratedObject result = new GeneratedObject();
      GenreObject genreObject = (GenreObject) element;
      for (GenreObjectProperty property : genreObject.getProperties()) {
        result.addProperty(property.getName(), this.evaluateExpression(genre, property.getExpression()));
      }
      return result;
    } else if (element instanceof GenreTable) {
      GenreTable genreTable = (GenreTable) element;
      int roll = dice.roll(1, genreTable.getMaxRoll());
      for (GenreTableEntry entry : genreTable.getEntries()) {
        if (roll >= entry.getMinRoll() && roll <= entry.getMaxRoll()) {
          return this.evaluateExpression(genre, entry.getText());
        }
      }
      throw new IllegalStateException("Failed to get entry from genre table.");
    } else {
      throw new IllegalStateException("Genre element of unknown type as input. " + element.getClass().getName());
    }
  }

  private Generated evaluateExpression(Genre genre, String expression) {
    if (expression.matches("^\\{[^{}]*\\}$")) {
      String elementName = expression.replaceAll("\\{", "").replaceAll("\\}", "").trim();
      return this.generate(genre, elementName);
    } else {
      StringBuilder buffer = new StringBuilder(expression);
      int pos = 0;
      while (pos < buffer.length()) {
        int start = buffer.indexOf("{", pos);
        if (start != -1) {
          int end = buffer.indexOf("}", start);
          if (end == -1) {
            throw new IllegalArgumentException("Unmatched brackets.");
          }
          String elementName = buffer.substring(start + 1, end).trim();
          Generated generated = this.generate(genre, elementName);
          buffer.replace(start, end + 1, generated.toString());
          pos = start;
        } else {
          break;
        }
      }
      return new GeneratedText(buffer.toString());
    }
  }
}
