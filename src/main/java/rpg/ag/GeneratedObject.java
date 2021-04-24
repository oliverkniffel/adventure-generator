/*
 * Oliver Kniffel, 2021.
 */
package rpg.ag;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GeneratedObject implements Generated {

  private List<GeneratedObjectProperty> properties;

  public void addProperty(String name, Generated value) {
    if (this.properties == null) {
      this.properties = new ArrayList<>();
    }
    this.properties.add(new GeneratedObjectProperty(name, value));
  }

  @Override
  public String toString() {
    return this.properties.toString();
  }
}
