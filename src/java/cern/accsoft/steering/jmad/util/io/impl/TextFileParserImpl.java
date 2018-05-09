package cern.accsoft.steering.jmad.util.io.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.tools.bzip2.CBZip2InputStream;

import cern.accsoft.steering.jmad.util.io.TextFileParser;
import cern.accsoft.steering.jmad.util.io.TextFileParserException;

/**
 * Simple parser, that opens a TextFile and returns the (trimmed) lines as List.
 * <p>
 * Additionaly, this parser can handle two different compression formats:
 * <ul>
 * <li>gzip
 * <li>bzip2
 * </ul>
 * To handle the bzip2 compression bzip2.jar has to be in the classpath. Which compression to use is decided on the
 * extension of the filename (".gz" for gzip and ".bz2" for bzip2).
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public class TextFileParserImpl implements TextFileParser {
    public static final String EXTENSION_GZIP = ".gz";
    public static final String EXTENSION_BZIP2 = ".bz2";

    public static final int LENGTH_BZIP_MAGIC_BYTES = 2;

    @Override
    public List<String> parse(File file) throws TextFileParserException {
        if (file == null) {
            throw new TextFileParserException("Failure: file must not be null!");
        }

        List<String> lines = new ArrayList<String>();
        BufferedReader bufferedReader = openReader(file);

        try {
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }

                line = line.trim();
                if (line.length() > 0) { // ignore empty lines
                    lines.add(line.trim());
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            throw new TextFileParserException("IO-failure!", e);
        }
        return lines;
    }

    /**
     * opens an valid reader, depending on the ending of the fileName.
     * 
     * @param file the file to open
     * @return the reader if successful
     * @throws TextFileParserException if the reader cannot be opened
     */
    private BufferedReader openReader(File file) throws TextFileParserException {
        BufferedReader reader = null;
        try {
            if (file.getCanonicalPath().endsWith(EXTENSION_GZIP)) {
                reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(file))));
            } else if (file.getCanonicalPath().endsWith(EXTENSION_BZIP2)) {
                /*
                 * Read magic "Bz" before the actual Data (just dump the first two Bytes of the File)
                 */
                FileInputStream tfsData = new FileInputStream(file);
                if (tfsData.read(new byte[LENGTH_BZIP_MAGIC_BYTES]) != LENGTH_BZIP_MAGIC_BYTES) {
                    throw new TextFileParserException("Error while reading from file stream.");
                }

                reader = new BufferedReader(new InputStreamReader(new CBZip2InputStream(tfsData)));

            } else {
                reader = new BufferedReader(new FileReader(file));
            }
        } catch (Exception e) {
            throw new TextFileParserException("Error while opening inputstream for filename '" + file.getAbsolutePath()
                    + "'.", e);
        }
        return reader;
    }

}
