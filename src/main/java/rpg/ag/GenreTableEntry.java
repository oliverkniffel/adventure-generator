/*
 * Oliver Kniffel, 2021.
 */
package rpg.ag;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenreTableEntry {

  private int minRoll;
  private int maxRoll;
  private String text;
}
