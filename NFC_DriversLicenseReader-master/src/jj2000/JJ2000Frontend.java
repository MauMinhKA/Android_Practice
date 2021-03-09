

package jj2000;

import jj2000.j2k.decoder.Decoder;
import jj2000.j2k.util.ParameterList;
import android.graphics.Bitmap;

/**
 * Frontend for JJ2000 library.
 */
public class JJ2000Frontend {
  /** The parameter info, with all possible options. */
  private static String pinfoDecoder[][] = Decoder.getAllParameters();

  public static Bitmap decode(byte[] input) {
    // Get the dfault parameter values
    ParameterList defpl = new ParameterList();
    for (int i = pinfoDecoder.length - 1; i >= 0; --i) {
      if (pinfoDecoder[i][3] != null) {
        defpl.put(pinfoDecoder[i][0], pinfoDecoder[i][3]);
      }
    }

    ParameterList pl = new ParameterList(defpl);

    //pl.setProperty("rate", "3");

    Decoder dec = new Decoder(pl);
    return dec.run(input);
  }
}
