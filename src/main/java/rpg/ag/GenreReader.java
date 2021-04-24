/*
 * Oliver Kniffel, 2021.
 */
package rpg.ag;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GenreReader {

  private Map<String, GenreElementReader> genreElementReaders;

  public GenreReader() {
    genreElementReaders = new HashMap<>();
    genreElementReaders.put(".object.csv", new GenreObjectReader());
    genreElementReaders.put(".table.csv", new GenreTableReader());
  }

  public Genre readGenre(Path genreDirPath) throws Exception {
    Genre genre = new Genre();

    for (String fileExtension : genreElementReaders.keySet()) {
      readGenreElements(genre, genreDirPath, fileExtension, genreElementReaders.get(fileExtension));
    }

    return genre;
  }

  private void readGenreElements(Genre genre, Path genreDirPath, String fileExtension, GenreElementReader genreElementReader) throws Exception {
    // @formatter:off
    List<Path> elementFilePaths = Files.list(genreDirPath)
        .filter(path -> path.getFileName().toString().endsWith(fileExtension))
        .collect(Collectors.toList());
    // @formatter:on

    for (Path path : elementFilePaths) {
      String elementName = path.getFileName().toString().replace(fileExtension, "");
      GenreElement element = genreElementReader.read(new FileInputStream(path.toFile()));
      genre.addElement(elementName, element);
    }
  }
}
