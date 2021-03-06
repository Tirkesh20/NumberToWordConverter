package by.jazzteamtz.numberToStringConverter.service;

import by.jazzteamtz.numberToStringConverter.exceptions.IllegalArgumentException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DefaultNumberToWordsConverter implements NumberToWordConverter {
    private boolean FLAG_READER = false;
    private final String PATH_FILE_UNITS = "resources/dataName/nameUnits.txt";
    private final String PATH_FILE_TRIADS = "resources/dataName/nameTriads.txt";
    private final static int MALE_GENDER = 1;
    private final static int FEMALE_GENDER = -1;
    private final static String SEPARATOR = " ";

    /**
     * Данный мап хранит все возможные наименования для составления имени триад .
     * Ключём являются числовые представления .
     */
    private Map<Integer, String> nameTriad;

    /**
     * Мап для хранения  наименования чисел состоящих из латинского имени степени тысячи .
     * Ключём служит степень.
     */
    private Map<Integer, String> nameUnits ;

    /**
     * Окочания для тысяч- и -иллионов/-ардов соответственно.
     */
    private final String[][] endings = {{"а", "и", ""}, {"", "а", "ов"}};

    public String convertNumbToWords(BigInteger number) throws IOException, IllegalArgumentException {
        readResources();

        StringBuilder numberStr = new StringBuilder(number.toString());

        if (Objects.equals(numberStr.toString(), "0")) return nameTriad.get(0);

        StringBuilder nameNumber = new StringBuilder();

        /* Если есть минус добовляем к итоговой строкеи удаляем минус из строки */
        if (numberStr.charAt(0) == '-') {
            nameNumber.append("минус ");
            numberStr = new StringBuilder(numberStr.substring(1));
        }

        /* Дополняет строку нялми до кратности 3 для удобной работы с подстроками по 3 символа*/
        for (int i = 0; i < numberStr.length() % 3; i++)
            numberStr.insert(0, "0");

        /*
          Конвертирует в слова ,попорядку по всей строке, группу из 3 цифр и добовляет имя порядка из
          мап nameUnit .Если степень 1(тысячи) передаём константу FEMALE_GENDER для конвертирования 1/2
          в форму женского рода , иначе передаётся MALE_GENDER
          */
        try {
            for (int i = 0; i < numberStr.length() / 3; i++) {
                int degree = numberStr.length() / 3 - i - 1;
                final String substring = numberStr.substring((i * 3), i * 3 + 3);
                if (degree == 1)
                    nameNumber.append(convertTriad(substring, FEMALE_GENDER)).append(getFormNameUnit(degree, substring));
                else
                    nameNumber.append(convertTriad(substring, MALE_GENDER)).append(getFormNameUnit(degree, substring));
            }
        } catch (NullPointerException ex) {
            throw new IllegalArgumentException(ex);
        }
        return nameNumber.toString().trim();
    }

    private void readResources() throws IOException {
        if (!FLAG_READER) {
            nameUnits = initNameFromFile(PATH_FILE_UNITS);
            nameTriad = initNameFromFile(PATH_FILE_TRIADS);
            FLAG_READER = true;
        }
    }


    private Map<Integer, String> initNameFromFile( String path) throws IOException {
        Map<Integer, String> receiver = new HashMap<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(path), StandardCharsets.UTF_8))) {
            String tmpLineStr;
            String[] Units;
            while ((tmpLineStr = br.readLine()) != null) {
                if (!Objects.equals(tmpLineStr, "")) {
                    Units = tmpLineStr.split(SEPARATOR);
                    receiver.put(Integer.valueOf(Units[0]), Units[1]);
                }
            }
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
        return receiver;
    }

    /**
     * Конвертирует триады(числа до трёх знаков) в пропись.
     * Принимает триаду как строку и константу для вычисления формы 1/2 по их роду.
     */
    private String convertTriad(String triad, int gender) {
        int hundreds = Character.getNumericValue(triad.charAt(0));
        int dozens = Character.getNumericValue(triad.charAt(1));
        int units = Character.getNumericValue(triad.charAt(2));
        if (Objects.equals(triad, "000")) return "";
        String words = "";
        /*Триады высчитываются путём умножения значений порядков(сотни,десятки,еденицы)на 100/10/1
         соответственно и взятием по этому ключу имени порядка*/
        if (hundreds != 0)
            words += nameTriad.get(hundreds * 100) + SEPARATOR;
        if (dozens == 1 && units <= 9 && units >= 0)   //Для чисел от 10 до 19 ключи для мапа считаются отдельно
            words += nameTriad.get(dozens * 10 + units) + SEPARATOR;
        else {
            if (dozens != 0)
                words += nameTriad.get(dozens * 10) + SEPARATOR;
            if (units > 2)
                words += nameTriad.get(units) + SEPARATOR;
            /*Если в еденицах числа имеется 1 или 2 , то ключ для мапа умножается на переданную константу
             gender. MALE_GENDER является 1 и значение ключа не меняется - один/два.
             FEMALE_GENDER является -1 и значения инвентируются в -1/-2 что есть женские формы одна/две.*/
            else if (units != 0)
                words += nameTriad.get(units * gender) + SEPARATOR;
        }
        return words;
    }

    /**
     * Если название числа со степенью тысячи degree существует и строка number не равна "000" , то возвращает
     * название с окончанием основаным на числе number.
     */
    private String getFormNameUnit(int degree, String number) {
        if (nameUnits.get(degree) == null && degree != 0)
            throw new NullPointerException("Don't exist name " + degree + " thousands of degrees");
        if (Objects.equals(number, "000")) return "";
        if (degree == 1) return nameUnits.get(degree) +
                endings[0][selectForm(Integer.parseInt(number))] + SEPARATOR;
        if (degree > 1) return nameUnits.get(degree) +
                endings[1][selectForm(Integer.parseInt(number))] + SEPARATOR;
        else
            return "";
    }

    /**
     * Получаем номер окончания  для названия из мап nameUnit, основываясь на числе n ,в массиве ending
     */
    private int selectForm(int n) {
        n = Math.abs(n) % 100;
        int n1 = n % 10;
        if (n > 10 && n < 20) return 2;
        if (n1 > 1 && n1 < 5) return 1;
        if (n1 == 1) return 0;
        return 2;
    }

}
