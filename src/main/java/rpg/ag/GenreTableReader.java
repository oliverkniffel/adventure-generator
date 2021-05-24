/*
 * Oliver Kniffel, 2021.
 */
package rpg.ag;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class GenreTableReader implements GenreElementReader {

  public GenreElement read(InputStream inputStream) throws Exception {
    GenreTable genreTable = new GenreTable();

    try (CSVParser csvParser = new CSVParser(new InputStreamReader(inputStream, Charset.forName("UTF-8")), CSVFormat.DEFAULT.withDelimiter(';'))) {
      int minRoll = 0;
      int maxRoll = 0;

      for (CSVRecord csvRecord : csvParser) {
        if (csvRecord.size() == 1) {
          minRoll = maxRoll + 1;
          maxRoll = minRoll;
          genreTable.addEntry(minRoll, maxRoll, csvRecord.get(0));
        } else if (csvRecord.size() == 2) {
          minRoll = maxRoll + 1;
          maxRoll = minRoll + Math.max(1, Integer.parseInt(csvRecord.get(0))) - 1;
          genreTable.addEntry(minRoll, maxRoll, csvRecord.get(1));
        } else {
          throw new IOException("Invalid number of colums in CSV file. " + csvRecord.toString());
        }
      }
      
      genreTable.setMaxRoll(maxRoll);
    }

    return genreTable;
  }
}
