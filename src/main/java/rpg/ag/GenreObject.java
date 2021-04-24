/*
 * Oliver Kniffel, 2021.
 */
package rpg.ag;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GenreObject implements GenreElement {

  private List<GenreObjectProperty> properties;

  public void addProperty(String name, String expression) {
    if (this.properties == null) {
      this.properties = new ArrayList<>();
    }
    this.properties.add(new GenreObjectProperty(name, expression));
  }
}
