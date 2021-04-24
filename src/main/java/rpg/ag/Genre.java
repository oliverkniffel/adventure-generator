/*
 * Oliver Kniffel, 2021.
 */
package rpg.ag;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.Data;

@Data
public class Genre {

  private Map<String, GenreElement> elements = new HashMap<>();

  public Optional<GenreElement> getElement(String elementName) {
    return Optional.ofNullable(this.elements.get(elementName));
  }

  public void addElement(String elementName, GenreElement element) {
    this.elements.put(elementName, element);
  }
}
