
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
    private static String FILEPATH = "C:\\Users\\Patrick\\Desktop\\test\\";
    private static String FILENAME = "DEKA_PRIIP_CLEVERSOFT_WERTE_20221026_20221026_201244.csv";
    private static String DELIMITER = ";";


    public static void main(String[] args) throws IOException {

        Multimap<String, CSVRecord> map = ArrayListMultimap.create();

        logger.info("Reading CSV...");
        Reader in = new FileReader(FILEPATH+FILENAME);
        CSVFormat format = CSVFormat.newFormat(';').withRecordSeparator("\n");
        Iterable<CSVRecord> records = format.withFirstRecordAsHeader().withSkipHeaderRecord(false).parse(in);
        Set<String> headers = records.iterator().next().toMap().keySet();

        for (CSVRecord record : records) {
            map.put(record.get("ISIN"), record);
        }

        logger.info("Writting splitted CSV...");

        int countISIN = map.keys().size();
        int currentPos = 0;
        for(String isin : map.keys()) {
            logger.info("Print {} of {} ISIN {}", currentPos, countISIN, isin);
            Collection<CSVRecord> rowsForIsin = map.get(isin);
            try (CSVPrinter printer = new CSVPrinter(new FileWriter(FILEPATH+isin+"_splitted.csv"), format)) {
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
