package by.jazzteamtz.numberToStringConverter.service;

import by.jazzteamtz.numberToStringConverter.exceptions.IllegalArgumentException;

import java.io.IOException;
import java.math.BigInteger;

public interface NumberToWordConverter {

    String convertNumbToWords(BigInteger number) throws IOException, IllegalArgumentException;
}
