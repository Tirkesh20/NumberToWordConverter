import by.jazzteamtz.numberToStringConverter.service.DefaultNumberToWordsConverter;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class ConvertNumToWordsTest {
    private final DefaultNumberToWordsConverter Converter = new DefaultNumberToWordsConverter();


    @Test
    public void testConvert_0to19() throws Exception {
        String[] nameNum = new String[]{"ноль", "один", "два", "три", "четыре",
                "пять", "шесть", "семь", "восемь", "девять", "десять", "одиннадцать", "двенадцать", "тринадцать",
                "четырнадцать", "пятнадцать", "шестнадцать", "семнадцать", "восемнадцать", "девятнадцать"};

        System.out.println("Junit test: Convert to words numbers 0-19");

        for (int i = 0; i < 20; i++) {
            assertEquals(" Error in the numbers  0-19", nameNum[i],
                    Converter.convertNumbToWords(new BigInteger(String.valueOf(i))));
        }
    }


    @Test
    public void testConvert_20to999() throws Exception {

        String[] nameNum = new String[]{"сорок", "семьдесят один", "сто", "двести два",
                "пятьсот пятьдесят пять", "триста десять", "девятьсот девяносто девять"};
        int[] numbers = new int[]{40, 71, 100, 202, 555, 310, 999};

        System.out.println("Junit test: Convert to words numbers 20-999");

        for (int i = 0; i < 7; i++) {
            assertEquals(" Error in the numbers  0-19", nameNum[i],
                    Converter.convertNumbToWords(new BigInteger(String.valueOf(numbers[i]))));
        }
    }


    @Test
    public void testConvert_BigNumber() throws Exception {

        String[] nameNum = new String[]{"два миллиарда сто сорок семь миллионов четыреста восемьдесят три тысячи шестьсот сорок семь",
                "минус один миллион одиннадцать",
                "сто двадцать три миллиона четыреста пятьдесят шесть тысяч семьсот восемьдесят девять"};
        int[] numbers = new int[]{Integer.MAX_VALUE, -1000011, 123456789};

        System.out.println("Junit test: Convert to words big numbers ");

        for (int i = 0; i < 3; i++) {
            assertEquals(" Error in the big numbers", nameNum[i],
                    Converter.convertNumbToWords(new BigInteger(String.valueOf(numbers[i]))));
        }
    }


    @Test
    public void testConvert_genderForms_1and2() throws Exception {

        String[] nameNum = new String[]{"одна тысяча", "две тысячи", "один миллион", "два миллиона"};
        int[] numbers = new int[]{1000, 2000, 1000000, 2000000};

        System.out.println("Junit test: Convert to words number exist units 1/2");

        for (int i = 0; i < 4; i++) {
            assertEquals(" Error in the numbers exist units 1/2", nameNum[i],
                    Converter.convertNumbToWords(new BigInteger(String.valueOf(numbers[i]))));
        }
    }


//    @Test Working with java 7 and below only
//    public void testConvert_number_from_excel() throws Exception {
//        System.out.println("Junit test: Data Driven Test .Different number from excel file ");
//        InputStream in = new FileInputStream("DataTest/TestNumber.xls");
//        HSSFWorkbook wb = new HSSFWorkbook(in);
//        long inNumber = 0;
//        String inString = null;
//        Sheet sheet = wb.getSheetAt(0);
//        for (Row row : sheet) {
//            for (Cell cell : row) {
//                int cellType = cell.getCellType();
//                switch (cellType) {
//                    case Cell.CELL_TYPE_NUMERIC -> inNumber = (long) cell.getNumericCellValue();
//                    case Cell.CELL_TYPE_STRING -> inString = cell.getStringCellValue();
//                    default -> {
//                    }
//                }
//            }
//            assertEquals("Error in number: " + inNumber, inString,
//                    Converter.convertNumbToWords(new BigInteger(String.valueOf(inNumber))));
//        }
//    }

}
