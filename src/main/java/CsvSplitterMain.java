
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.*;


public class CsvSplitterMain {

    private static Logger logger = LoggerFactory.getLogger(CsvSplitterMain.class);
    private static String SOURCE_FILEPATH = "C:\\Users\\patrick.klingler\\Downloads\\feri\\";
    private static String SOURCE_FILENAME = "ferirat.csv";
    private static String DELIMITER = ";";
    private static String TARGET_FILEPATH = "C:\\Users\\patrick.klingler\\Downloads\\feri\\";
    private static String TARGET_FILENAME = "ferirat_splitted.csv";


    public static void main(String[] args) throws IOException {

        Multimap<String, CSVRecord> map = ArrayListMultimap.create();

        logger.info("Reading CSV...");
        Reader in = new FileReader(SOURCE_FILEPATH+SOURCE_FILENAME);
        CSVFormat format = CSVFormat.newFormat(';').withRecordSeparator("\n");
        Iterable<CSVRecord> records = format.withFirstRecordAsHeader().withSkipHeaderRecord(false).parse(in);
        Set<String> headers = records.iterator().next().toMap().keySet();

        for (CSVRecord record : records) {
            map.put(record.get("ISIN"), record);
        }

        logger.info("Writting splitted CSV...");

        int countISIN = map.keys().size();
        int currentPos = 0;
        for(String isin : map.keySet()) {
            logger.info("Print {} of {} ISIN {}", currentPos, countISIN, isin);
            Collection<CSVRecord> rowsForIsin = map.get(isin);
            try (CSVPrinter printer = new CSVPrinter(new FileWriter(TARGET_FILEPATH+isin+"_"+TARGET_FILENAME), format)) {
                printer.printRecord(Arrays.asList(headers.toArray()));
                for(CSVRecord rec : rowsForIsin) {
                    printer.printRecord(rec);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            currentPos++;
        }
    }

}
