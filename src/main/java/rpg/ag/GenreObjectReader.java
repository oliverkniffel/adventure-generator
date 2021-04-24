/*
 * Oliver Kniffel, 2021.
 */
package rpg.ag;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class GenreObjectReader implements GenreElementReader {

  public GenreElement read(InputStream inputStream) throws Exception {
    GenreObject genreObject = new GenreObject();

    try (CSVParser csvParser = new CSVParser(new InputStreamReader(inputStream, Charset.forName("UTF-8")), CSVFormat.DEFAULT.withDelimiter(';'))) {
      for (CSVRecord csvRecord : csvParser) {
        String name = csvRecord.get(0);
        String expression = csvRecord.get(1);
        genreObject.addProperty(name, expression);
      }
    }

    return genreObject;
  }
}
