/*
 * Oliver Kniffel, 2021.
 */
package rpg.ag;

import lombok.Data;

@Data
public class GeneratedText implements Generated {

  private String text;

  public GeneratedText(String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return this.text;
  }
}
