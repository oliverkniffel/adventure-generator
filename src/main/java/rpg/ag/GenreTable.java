/*
 * Oliver Kniffel, 2021.
 */
package rpg.ag;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GenreTable implements GenreElement {
  
  private List<GenreTableEntry> entries = new ArrayList<>();
  private int maxRoll;

  public void addEntry(int minRoll, int maxRoll, String text) {
    if (this.entries == null) {
      this.entries = new ArrayList<>();
    }
    this.entries.add(new GenreTableEntry(minRoll, maxRoll, text));
  }
}
