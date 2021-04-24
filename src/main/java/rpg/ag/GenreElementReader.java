/*
 * Oliver Kniffel, 2021.
 */
package rpg.ag;

import java.io.InputStream;

public interface GenreElementReader {

  public GenreElement read(InputStream inputStream) throws Exception;
}
