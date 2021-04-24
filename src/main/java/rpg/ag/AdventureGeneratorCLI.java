/*
 * Oliver Kniffel, 2021.
 */
package rpg.ag;

import java.nio.file.Path;

public class AdventureGeneratorCLI {

  public static void main(String[] args) {
    AdventureGeneratorCLI cli = new AdventureGeneratorCLI();
    cli.execute(args);
  }

  public void execute(String[] args) {
    try {
      if (args != null && args.length == 2) {
        this.generate(args[0], args[1]);
      } else if (args != null && args.length == 1) {
        this.generate(".", args[0]);
      } else {
        System.out.println("Usage: ag [<data-dir>] <element-name>");
      }
    } catch (Exception ex) {
      System.err.println(ex.getMessage());
    }
  }

  public void generate(String dataDirectory, String elementType) throws Exception {
    GenreReader genreReader = new GenreReader();
    Genre genre = genreReader.readGenre(Path.of(dataDirectory));

    AdventureGenerator adventureGenerator = new AdventureGenerator();
    Generated generated = adventureGenerator.generate(genre, elementType);

    AdventurePrinter printer = new AdventurePrinter();
    printer.print(generated);
  }
}
