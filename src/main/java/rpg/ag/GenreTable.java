/*
 * Oliver Kniffel, 2021.
 */
package rpg.ag;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GenreTable implements GenreElement {
  private List<String> entries = new ArrayList<>();
}
