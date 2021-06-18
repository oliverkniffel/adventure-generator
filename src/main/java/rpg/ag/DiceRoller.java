/*
 * Oliver Kniffel, 2021.
 */
package rpg.ag;

import java.util.Random;

public class DiceRoller {

  private Random random;

  public DiceRoller() {
    this(new Random());
  }

  public DiceRoller(Random random) {
    this.setRandom(random);
  }

  public void setRandom(Random random) {
    if (random == null) {
      throw new IllegalArgumentException("Property 'random' cannot be null.");
    }
    this.random = random;
  }

  public int roll(int number, int type) {
    int result = 0;

    for (int i = 0; i < number; i++) {
      result += random.nextInt(type) + 1;
    }

    return result;
  }
}
