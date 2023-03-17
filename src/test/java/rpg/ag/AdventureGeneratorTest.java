/*
 * Oliver Kniffel, 2021.
 */
package rpg.ag;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdventureGeneratorTest {

  private AdventureGenerator testedObj;

  @Mock
  private DiceRoller dice;

  public Genre prepareGenre() {
    final Genre genre = new Genre();
    genre.addElement("table", prepageGenreTable());
    return genre;
  }

  public GenreTable prepageGenreTable() {
    final GenreTable genreTable = new GenreTable();
    genreTable.addEntry(1, 1, "Entry 1");
    genreTable.addEntry(2, 2, "Entry 2");
    return genreTable;
  }

  @Test
  void generate() {
    // Given
    Mockito.when(dice.roll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(1);
    this.testedObj = new AdventureGenerator(this.dice);
    final Genre genre = prepareGenre();

    // When
    Generated generated = this.testedObj.generate(genre, "table");

    Assertions.assertNotNull(generated);
    Assertions.assertTrue(generated instanceof GeneratedText);
  }
}
