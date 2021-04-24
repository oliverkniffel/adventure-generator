/*
 * Oliver Kniffel, 2021.
 */
package rpg.ag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedObjectProperty {

  private String name;
  private Generated value;

  @Override
  public String toString() {
    return String.format("%s: %s", name, value);
  }
}
