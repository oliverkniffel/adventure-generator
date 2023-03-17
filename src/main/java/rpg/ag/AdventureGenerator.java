/*
 * Oliver Kniffel, 2021.
 */
package rpg.ag;

import java.util.Optional;
import java.util.Random;

public class AdventureGenerator {

  private final DiceRoller dice;
  private final DiceRollGenerator diceRollGenerator;

  public AdventureGenerator() {
    this.dice = new DiceRoller(new Random());
    this.diceRollGenerator = new DiceRollGenerator(this.dice);
  }

  public AdventureGenerator(final DiceRoller dice) {
    this.dice = dice;
    this.diceRollGenerator = new DiceRollGenerator(dice);
  }

  public Generated generate(Genre genre, String elementName) {
    if (genre == null) {
      throw new IllegalArgumentException("Genre parameter cannot be null.");
    }

    if (elementName == null || elementName.trim().length() == 0) {
      throw new IllegalArgumentException("Element name parameter cannot be null or blank.");
    }

    Optional<Generated> generatedDiceRollOptional = diceRollGenerator.generateDiceRoll(elementName);
    if (generatedDiceRollOptional.isPresent()) {
      return generatedDiceRollOptional.get();
    }

    GenreElement element = genre.getElement(elementName).orElseThrow(() -> new IllegalArgumentException("Unknown genre element " + elementName));
    return generate(genre, element);
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
