package com.mountain.library.helper;

import com.mountain.library.domain.AppConstant;
import com.mountain.library.domain.BigDecimalBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.number.CurrencyStyleFormatter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NumberUtils {

    private final static Locale LOCALE = new Locale("in", "ID");
    public static final BigDecimal JUTAAN = new BigDecimal(1000000L);
    public static final BigDecimal PULUHAN = BigDecimal.TEN;
    public static final BigDecimal MILYARAN = new BigDecimal(1000000000L);
    public static final BigDecimal TRILIUN = new BigDecimal(1000000000000L);
    public static final BigDecimal RATUSAN = new BigDecimal(100L);
    public static final BigDecimal SATUAN = BigDecimal.ONE;
    public static final BigDecimal RIBUAN = new BigDecimal(1000L);

    public static final BigDecimal[] ORDER = {TRILIUN, MILYARAN, JUTAAN, RIBUAN, RATUSAN, PULUHAN, SATUAN};
    public static final String[] ORDER_STR = {"Triliun", "Milyar", "Juta", "Ribu", "Ratus", "Puluh", ""};

    public static boolean isEqual(BigDecimal n1, BigDecimal n2){
        if(n1 == null || n2 == null){
            return false;
        }

        return n1.compareTo(n2) == 0;
    }

    public static boolean isNotEqual(BigDecimal n1, BigDecimal n2){
        if(n1 == null || n2 == null){
            return false;
        }

        return n1.compareTo(n2) != 0;
    }

    public static boolean isLessThan(BigDecimal n1, BigDecimal n2){
        if(n1 == null || n2 == null){
            return false;
        }

        int compareTo = n1.compareTo(n2);
        return n1.compareTo(n2) < 0;
    }

    public static boolean isGreaterThan(BigDecimal n1, BigDecimal n2){
        if(n1 == null || n2 == null){
            return false;
        }

        return n1.compareTo(n2) > 0;
    }

    public static boolean isZero(BigDecimal number) {
        if (number == null) {
            return false;
        }
        return number.compareTo(BigDecimal.ZERO) == 0;
    }

    public static boolean isNotZero(BigDecimal number) {
        return !isZero(number);
    }

    public static boolean isPositive(BigDecimal number) {
        if (number == null) {
            return false;
        }
        return number.compareTo(BigDecimal.ZERO) == 1;
    }

    public static boolean isZeroOrPositive(BigDecimal number) {
        if (number == null) {
            return false;
        }
        return number.compareTo(BigDecimal.ZERO) == 0 ||
                number.compareTo(BigDecimal.ZERO) == 1;
    }

    public static boolean isNegative(BigDecimal number) {
        if (number == null) {
            return false;
        }
        return number.compareTo(BigDecimal.ZERO) == -1;
    }

    public static boolean isZeroOrNegative(BigDecimal number) {
        if (number == null) {
            return false;
        }
        return number.compareTo(BigDecimal.ZERO) == 0 ||
                number.compareTo(BigDecimal.ZERO) == -1;
    }

    public static boolean isZeroOrNegative(int number) {
        if(number <= 0){
            return true;
        }

        return false;
    }

    public static boolean isZeroOrNegative(long number) {
        if(number <= 0l){
            return true;
        }

        return false;
    }

    public static BigDecimal ifZeroGetDefault(
            BigDecimal testingNumber, BigDecimal replacement) {

        return isZero(testingNumber) ? replacement : testingNumber;
    }

    public static BigDecimal ifNullGetDefault(
            BigDecimal testingNumber, BigDecimal replacement) {

        return testingNumber == null ? replacement : testingNumber;
    }

    public static BigDecimal ifNullGetZero(BigDecimal testingNumber) {
        return ifNullGetDefault(testingNumber, BigDecimal.ZERO);
    }

    public static BigDecimal ifZeroOrNegativeGetDefault(
            BigDecimal testingNumber, BigDecimal replacement) {

        return isZeroOrNegative(testingNumber) ? replacement : testingNumber;
    }

    public static BigDecimal ifNegativeGetZero(BigDecimal testingNumber) {
        return isNegative(testingNumber) ? BigDecimal.ZERO : testingNumber;
    }

    public static BigDecimal ifNegativeGetDefault(
            BigDecimal testingNumber, BigDecimal replacement) {

        return isNegative(testingNumber) ? replacement : testingNumber;
    }

    public static BigDecimal parseBigDecimalOrZero(String number) {
        BigDecimal result = BigDecimal.ZERO;
        if (StringUtils.isEmpty(number)) {
            return result;
        }
        try {
            result = new BigDecimal(number);
        } catch (Exception e) {
            result = BigDecimal.ZERO;
        }
        return result;
    }

    public static BigDecimal parseBigDecimal(String number) throws NumberFormatException {
        BigDecimal result = BigDecimal.ZERO;
        if (StringUtils.isEmpty(number)) {
            return result;
        }
        try {
            result = new BigDecimal(number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new NumberFormatException("Format tidak valid.");
        }
        return result;
    }

    public static BigDecimal parseCurrencyOrDefault(
            String number, BigDecimal replacement) {

        BigDecimal result = BigDecimal.ZERO;
        try {
            result = parseCurrency(number);
        } catch (Exception e) {
            result = replacement;
        }
        return result;
    }

    public static BigDecimal parseCurrencyOrZero(String number) {
        return parseCurrencyOrDefault(number, BigDecimal.ZERO);
    }

    public static double parseDouble(String number) throws Exception {
        return Double.parseDouble(number);
    }

    public static double parseDoubleOrDefault(String number, double replacement) {
        try {
            return parseDouble(number);
        } catch (Exception e) {
            return replacement;
        }
    }

    public static double parseDoubleOrZero(String number) {
        return parseDoubleOrDefault(number, 0.0);
    }

    public static int parseInt(String number) throws Exception {
        return Integer.parseInt(number);
    }

    public static int parseIntOrDefault(String number, int replacement) {
        try {
            return parseInt(number);
        } catch (Exception e) {
            return replacement;
        }
    }

    public static int parseIntOrZero(String number) {
        return parseIntOrDefault(number, 0);
    }

    public static long parseLong(String number) throws Exception {
        return Long.parseLong(number);
    }

    public static long parseLongOrZero(String number) {
        return parseLongOrDefault(number, 0);
    }

    public static long parseLongOrDefault(String number, long replacement) {
        try {
            return parseLong(number);
        } catch (Exception e) {
            return replacement;
        }
    }

    public static BigDecimal parseCurrency(String number) throws Exception {
        BigDecimal result = BigDecimal.ZERO;
        if (StringUtils.isEmpty(number)) {
            return result;
        }
        String numStr = number.replaceFirst("Rp.", "")
                .replaceAll("\\.", "")
                .replaceFirst(",", ".").trim();

        if (StringUtils.isEmpty(numStr)) {
            return result;
        }
        result = new BigDecimal(numStr);
        return result;
    }

    public static String formatCurrency(
            BigDecimal temp, boolean usingCurrencySymbol, boolean useParenthesis) {

        String format = "";
        try {
            BigDecimal number = ifNullGetZero(temp);
            if (useParenthesis) {
                number = ifNegativeGetDefault(number, number.abs());
            }
            number = number.setScale(2, RoundingMode.HALF_UP);
            CurrencyStyleFormatter formatter = new CurrencyStyleFormatter();
            format = formatter.print(number, LOCALE);
            if (!usingCurrencySymbol) {
                format = format.replaceFirst("Rp", "");
            }
            if (useParenthesis && isNegative(temp)) {
                format = "( " + format + " )";
            }
        } catch (Exception ex) {
        }
        return format;
    }

    public static String formatCurrency(BigDecimal temp) {
        String format = temp == null ? BigDecimal.ZERO.toPlainString() : temp.toString();
        return formatCurrency(format);
    }

    public static String formatCurrency(double temp) {
        return formatCurrency(temp + "");
    }

    public static String formatCurrency(String temp) {
        String format = temp;
        try {
            format = formatCurrency(temp, false);
        } catch (Exception ex) {
        }
        return format;
    }

    public static String formatCurrency(String temp, Locale locale) {
        String format = temp;
        try {
            format = formatCurrency(temp, locale, false);
        } catch (Exception ex) {
        }
        return format;
    }

    public static String formatCurrency(String temp, boolean usingCurrencySymbol) {
        String format = temp;
        try {
            format = formatCurrency(temp, LOCALE, usingCurrencySymbol);
        } catch (Exception ex) {
        }
        return format;
    }

    public static String formatCurrency(
            String temp, Locale locale, boolean usingCurrencySymbol) {

        return formatCurrency(temp, locale, usingCurrencySymbol, 2);
    }

    public static String formatCurrency(
            BigDecimal number, Locale locale, boolean usingCurrencySymbol) {

        return formatCurrency(number.toPlainString(), locale, usingCurrencySymbol);
    }

    public static String formatCurrency(BigDecimal temp, boolean usingCurrencySymbol) {
        return formatCurrency(temp.toPlainString(), usingCurrencySymbol);
    }

    public static String formatCurrency(int temp, int scale) {
        return formatCurrency(temp + "", scale);
    }

    public static String formatCurrency(double temp, int scale) {
        return formatCurrency(temp + "", scale);
    }

    public static String formatCurrency(BigDecimal temp, int scale) {
        if(temp == null){
            return "";
        }
        return formatCurrency(temp.toPlainString(), scale);
    }

    public static String formatCurrency(String temp, int scale) {
        String format = temp;
        try {
            format = formatCurrency(temp, LOCALE, false, scale);
        } catch (Exception ex) {
        }
        return format;
    }

    public static String formatCurrency(
            String temp, Locale locale, boolean usingCurrencySymbol, int scale) {

        String format = temp;
        try {
            BigDecimal number = new BigDecimal(temp);
            number = number.setScale(2, RoundingMode.HALF_UP);
            CurrencyStyleFormatter formatter = new CurrencyStyleFormatter();
            formatter.setRoundingMode(RoundingMode.HALF_UP);
            formatter.setFractionDigits(scale);
            format = formatter.print(number, LOCALE);
            if (!usingCurrencySymbol) {
                format = format.replaceFirst("Rp", "");
            }
        } catch (Exception ex) {
        }
        return format;
    }

    public static BigDecimal divide(BigDecimal multiplicand, BigDecimal multiplier) {
        multiplicand = ifNullGetZero(multiplicand);
        multiplier = ifNullGetZero(multiplier);
        BigDecimal result = BigDecimal.ZERO;
        try {
            result = multiplicand.setScale(2, RoundingMode.HALF_UP)
                    .divide(
                            multiplier.setScale(2, RoundingMode.HALF_UP),
                            2, RoundingMode.HALF_UP);
        } catch (Exception e) {
        }
        return result;
    }

    public static BigDecimal divide(BigDecimal dividend, int divisor) {
        BigDecimal n2 = new BigDecimal(divisor);
        return divide(dividend, n2);
    }

    public static BigDecimal add(BigDecimal n1, BigDecimal n2) {
        n1 = ifNullGetZero(n1);
        n2 = ifNullGetZero(n2);
        BigDecimal result = BigDecimal.ZERO;
        try {
            result = n1.add(n2).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
        }
        return result;
    }

    public static BigDecimal substract(BigDecimal n1, BigDecimal n2) {
        n1 = ifNullGetZero(n1);
        n2 = ifNullGetZero(n2);
        BigDecimal result = BigDecimal.ZERO;
        try {
            result = n1.subtract(n2).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
        }
        return result;
    }

    public static BigDecimal times(BigDecimal n1, BigDecimal n2) {
        n1 = ifNullGetZero(n1);
        n2 = ifNullGetZero(n2);
        BigDecimal result = BigDecimal.ZERO;
        try {
            result = n1.setScale(2, RoundingMode.HALF_UP)
                    .multiply(n2.setScale(2, RoundingMode.HALF_UP))
                    .setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
        }
        return result;
    }

    public static BigDecimal times(BigDecimal multiplicand, int multiplier) {
        BigDecimal n2 = new BigDecimal(multiplier);
        return times(multiplicand, n2);
    }

    public static BigDecimal times(BigDecimal multiplicand, long multiplier) {
        BigDecimal n2 = new BigDecimal(multiplier);
        return times(multiplicand, n2);
    }

    public static String toHumanReadable(BigDecimal number) {
        String terbilang = "";
        String str = number.toPlainString();
        int indexOf = str.indexOf(".");
        String komaStr = str.substring(str.indexOf(".") + 1);
        BigDecimal koma = new BigDecimal(komaStr);
        terbilang = getTerbilang(number);
        if (indexOf != -1) {
            if (koma.compareTo(BigDecimal.ZERO) != 0) {
                terbilang += " Koma " + getTerbilang(koma);
            }
        }
        return terbilang;
    }

    private static String getTerbilang(BigDecimal number) {
        Map<Integer, String> digits = new HashMap<>();
        digits.put(0, "Nol");
        digits.put(1, "Satu");
        digits.put(2, "Dua");
        digits.put(3, "Tiga");
        digits.put(4, "Empat");
        digits.put(5, "Lima");
        digits.put(6, "Enam");
        digits.put(7, "Tujuh");
        digits.put(8, "Delapan");
        digits.put(9, "Sembilan");
        digits.put(10, "Sepuluh");
        digits.put(11, "Sebelas");
        digits.put(12, "Dua belas");
        digits.put(13, "Tiga belas");
        digits.put(14, "Empat belas");
        digits.put(15, "Lima belas");
        digits.put(16, "Enam belas");
        digits.put(17, "Tujuh belas");
        digits.put(18, "Delapan belas");
        digits.put(19, "Sembilan belas");
        String result = "";
        //jika 0
        if (number.compareTo(BigDecimal.ZERO) == 0) {
            return result = digits.get(BigDecimal.ZERO.intValue());
        }

        BigDecimal remaining = number;
        BigDecimal koma = null;
        for (int i = 0; i < ORDER.length; i++) {
            BigDecimal divisor = ORDER[i];
            BigDecimal[] divideAndRemainder = remaining.divideAndRemainder(divisor);
            BigDecimal r = divideAndRemainder[0];
            BigDecimal s = divideAndRemainder[1];
            //lanjutnkan looping jika hasil lebih besar dari 0
            if (r.compareTo(BigDecimal.ZERO) != 1) {
                remaining = s;
                continue;
            }
            //jika number antara 10-19
            if (ORDER_STR[i].equals("Puluh")) {
                if (r.compareTo(BigDecimal.ONE) == 0) {
                    result += " " + digits.get(remaining.intValue());
                    break;
                }
            }
            String str = "";
            if (r.compareTo(new BigDecimal("11")) == 1) {
                str = getTerbilang(r);
            } else {
                str = digits.get(r.intValue());
            }
            result += " " + str + " " + ORDER_STR[i];
            result = result.replaceAll("Satu Ratus", "Seratus").replaceAll("Satu Ribu", "Seribu");
            remaining = s;
        }
        return result.trim();
    }

    public static BigDecimal round(
            BigDecimal number, String mode, BigDecimal rupiahTerdekat) {

        if (number == null) {
            return null;
        }
        mode = StringUtils.isEmpty(mode) ? AppConstant.ROUNDING_MODE_NONE : mode;
        int scale = 0;
        BigDecimal result = BigDecimal.ZERO;
        switch (mode) {
            case AppConstant.ROUNDING_MODE_NONE:
                result = number;
                break;
            case AppConstant.ROUNDING_MODE_HALF_UP:
                result = number.setScale(scale, RoundingMode.HALF_UP);
                break;
            case AppConstant.ROUNDING_MODE_HALF_DOWN:
                result = number.setScale(scale, RoundingMode.HALF_DOWN);
                break;
            case AppConstant.ROUNDING_MODE_HALF_EVEN:
                result = number.setScale(scale, RoundingMode.HALF_EVEN);
                break;
            case AppConstant.ROUNDING_MODE_CEILING:
                result = number.setScale(scale, RoundingMode.CEILING);
                break;
            case AppConstant.ROUNDING_MODE_FLOOR:
                result = number.setScale(scale, RoundingMode.FLOOR);
                break;
            case AppConstant.ROUNDING_MODE_RUPIAH_CEILING:
                result = ceilMoney(number, rupiahTerdekat);
                break;
            case AppConstant.ROUNDING_MODE_RUPIAH_FLOOR:
                result = floorMoney(number, rupiahTerdekat);
                break;
            case AppConstant.ROUNDING_MODE_RUPIAH_NORMAL:
                result = roundMoney(number, rupiahTerdekat);
                break;
            default:
                result = number;
        }
        return result;
    }

    public static BigDecimal floorMoney(BigDecimal money, BigDecimal smallestMoney) {
        BigDecimal sisa = money.remainder(smallestMoney);
        if (isZero(sisa)) {
            return money;
        }
        return substract(money, sisa);
    }

    public static BigDecimal ceilMoney(BigDecimal money, BigDecimal smallestMoney) {
        BigDecimal sisa = money.remainder(smallestMoney);
        if (isZero(sisa)) {
            return money;
        }
        BigDecimal addition = substract(smallestMoney, sisa);
        return add(money, addition);
    }

    public static BigDecimal roundMoney(BigDecimal money, BigDecimal smallestMoney) {
        BigDecimal sisa = money.remainder(smallestMoney);
        if (isZero(sisa)) {
            return money;
        }
        BigDecimal two = new BigDecimal(2);
        BigDecimal half = divide(smallestMoney, two);
        int comparation = sisa.compareTo(half);
        return comparation >= 0 ?
                ceilMoney(money, smallestMoney) : floorMoney(money, smallestMoney);
    }

    public static BigDecimal percentageFrom(
            double percentage, BigDecimal percentageResult) {

        percentageResult = ifNullGetZero(percentageResult);
        BigDecimal result = new BigDecimalBuilder()
                .add(100)
                .times(percentageResult)
                .divide(percentage)
                .build();
        return result;
    }

    public static BigDecimal persentageRatio(BigDecimal n1, BigDecimal total) {
        n1 = ifNullGetZero(n1);
        total = ifNullGetZero(total);
        BigDecimal percentage = new BigDecimalBuilder()
                .add(n1)
                .times(100)
                .divide(total)
                .build();
        return percentage;
    }

    public static BigDecimal percentageResult(double percentage, BigDecimal total) {
        total = ifNullGetZero(total);
        BigDecimal result = new BigDecimalBuilder()
                .add(percentage)
                .times(total)
                .divide(100)
                .build();
        return result;
    }

    public static BigDecimal abs(BigDecimal number){
        if(isZeroOrPositive(number)){
            return number;
        }

        return number.negate();
    }

    public static boolean isStringDigit(String str){
        try{
            Long.parseLong(str);
        }catch(Exception e){
            return false;
        }
        return true;
    }
}
