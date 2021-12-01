import by.jazzteamtz.numberToStringConverter.service.DefaultNumberToWordsConverter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class ConverterNumToWordsFromTextFileTest {
    private BufferedReader br = null;
    private final DefaultNumberToWordsConverter converter = new DefaultNumberToWordsConverter();

    @Before
    public void setUp() throws Exception {
        br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("DataTest/TestNumber.txt"), StandardCharsets.UTF_8));
    }

    @After
    public void tearDown() throws Exception {
        if (br != null) {
            br.close();
        }
        br = null;
    }


    @Test
    public void testConvertDataFromFile() throws Exception {
        String tmpLineStr;
        String[] Units;
        while ((tmpLineStr = br.readLine()) != null) {
            if (!Objects.equals(tmpLineStr, "")) {
                Units = tmpLineStr.split(":");
                assertEquals(Units[1],
                        converter.convertNumbToWords(new BigInteger(Units[0])));
            }
        }
    }
}
