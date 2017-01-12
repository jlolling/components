package org.talend.components.simplefileio.runtime;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.beam.sdk.transforms.DoFnTester;
import org.apache.commons.csv.CSVRecord;
import org.apache.hadoop.io.Text;
import org.junit.Test;

/**
 * Unit tests for {@link SimpleFileIoInputRuntime.ExtractCsvRecord}.
 */
public class ExtractCsvRecordTest {

    /** The DoFn under test. */
    private final DoFnTester<Text, CSVRecord> fnBasic = DoFnTester.of(new SimpleFileIoInputRuntime.ExtractCsvRecord(';'));

    public static String[] toArray(CSVRecord record) {
        String[] fields = new String[record.size()];
        for (int i = 0; i < fields.length; i++)
            fields[i] = record.get(i);
        return fields;
    }

    public static List<String[]> toArrays(List<CSVRecord> records) {
        List<String[]> out = new ArrayList<>(records.size());
        for (CSVRecord r : records) {
            out.add(toArray(r));
        }
        return out;
    }

    public static void assertLine(String msg, DoFnTester<Text, CSVRecord> fn, String inputLine, String... expected)
            throws Exception {
        assertThat(msg + ":" + inputLine, toArray(fn.processBundle(new Text(inputLine)).get(0)), arrayContaining(expected));
    }

    @Test
    public void testBasic() throws Exception {
        // Check the DoFn on every possible input line from the examples.
        for (CsvExample csvEx : CsvExample.getCsvExamples()) {
            for (String inputLine : csvEx.getPossibleInputLines()) {
                assertLine("basic", fnBasic, inputLine, csvEx.getValues());
            }
        }

        // Empty lines are skipped
        assertThat(fnBasic.processBundle(new Text("")), hasSize(0));

        // TODO: Error when three quotes
        // assertLine("basic", fnBasic, "\"\"\"", "\"");
    }

}
