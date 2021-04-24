/*
 * Oliver Kniffel, 2021.
 */
package rpg.ag;

import java.util.Random;

public class AdventureGenerator {

  private Random random;

  public AdventureGenerator() {
    this.setRandom(new Random());
  }

  public void setRandom(Random random) {
    if (random == null) {
      throw new IllegalArgumentException("Property 'random' cannot be null.");
    }
    this.random = random;
  }

  public Generated generate(Genre genre, String elementName) {
    if (genre == null) {
      throw new IllegalArgumentException("Genre parameter cannot be null.");
    }
    if (elementName == null || elementName.trim().length() == 0) {
      throw new IllegalArgumentException("Element name parameter cannot be null or blank.");
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
      String tableEntryExpression = genreTable.getEntries().get(random.nextInt(genreTable.getEntries().size()));
      return this.evaluateExpression(genre, tableEntryExpression);
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
