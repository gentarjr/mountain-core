package com.mountain.library.helper;

import com.mountain.library.domain.AppConstant;
import com.mountain.library.domain.ErrCode;
import com.mountain.library.exceptions.EmptyFieldException;
import com.mountain.library.exceptions.InvalidFieldException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class ValidationUtils {

    public static void checkEmptyField(String... fields) throws EmptyFieldException {
        for (String f : fields) {
            if(StringUtils.isEmpty(f)){
                throw new EmptyFieldException(ErrCode.INF_FIELDEMPTY, "Field tidak boleh kosong");
            }
        }
    }

    public static void checkEmptyField(String[]... fields) throws EmptyFieldException{
        rejectIfEmptyField(" kosong", fields);
    }

    public static void rejectIfEmptyField(String defaultMessage, String[]... fields) throws EmptyFieldException{
        for (String[] f : fields) {
            if(StringUtils.isEmpty( f[1] )){
                throw new EmptyFieldException(ErrCode.INF_FIELDEMPTY, f[0] + defaultMessage);
            }
        }
    }

    public static void checkWhitespaceField(String... fields) throws InvalidFieldException {
        for (String f : fields) {
            if(StringUtils.containsWhitespace(f)){
                throw new InvalidFieldException(ErrCode.INF_FIELDSPACE, "Field tidak boleh mengandung whitespace");
            }
        }
    }

    public static void checkWhitespaceField(String[]... fields) throws InvalidFieldException{
        rejectIfWhitespaceField(" kosong", fields);
    }

    public static void rejectIfWhitespaceField(String defaultMessage, String[]... fields) throws InvalidFieldException {
        for (String[] f : fields) {
            if (StringUtils.containsWhitespace(f[1])) {
                throw new InvalidFieldException(ErrCode.INF_FIELDSPACE, f[0] + defaultMessage);
            }
        }
    }

    public static void rejectIfNotAlphanumeric(String defaultMessage, String[]... fields) throws InvalidFieldException {
        for (String[] f : fields) {
            if (!StringUtils.isAlphanumericSpace(f[1])) {
                throw new InvalidFieldException(ErrCode.INF_FIELDINVALID, f[0] + defaultMessage);
            }
        }
    }

    public static void validateEmail(String email, String errMesg)
            throws InvalidFieldException {

        errMesg = StringUtils.isEmpty(errMesg) ? "Email tidak valid" : errMesg;
        EmailValidator emailValidator = EmailValidator.getInstance();
        if (StringUtils.isNotEmpty(email) && !emailValidator.isValid(email)) {
            throw new InvalidFieldException(ErrCode.INF_FIELDINVALID, errMesg);
        }//end of if
    }

    public static void validateEmail(String email) throws InvalidFieldException {
        validateEmail(email, "");
    }

    public static void validateDate(String date, String pattern, String errMesg)
            throws InvalidFieldException{

        errMesg  = StringUtils.isEmpty(errMesg) ? "Field tidak valid" : errMesg;

        if( StringUtils.isNotEmpty(date) ){
            LocalDate localDate = null;
            try {
                localDate = DateUtils.parseDate(date, pattern);
            } catch (Exception e) {
                throw new InvalidFieldException(ErrCode.INF_FIELDINVALID, errMesg);
            }
        }//end of if
    }

    public static void validateDate(String date, String errMesg)
            throws InvalidFieldException{

        errMesg  = StringUtils.isEmpty(errMesg) ? "Field tidak valid" : errMesg;
        if( StringUtils.isNotEmpty(date) ){
            LocalDate localDate = null;
            try {
                localDate = DateUtils.parseDate(date, AppConstant.DEFAULT_FORMAT_TANGGAL);
            } catch (Exception e) {
                throw new InvalidFieldException(ErrCode.INF_FIELDINVALID, errMesg);
            }
        }//end of if
    }

    public static void validateTime(String time, String errMesg)
            throws InvalidFieldException{

        errMesg  = StringUtils.isEmpty(errMesg) ? "Field tidak valid" : errMesg;

        if( StringUtils.isNotEmpty(time) ){
            LocalTime localTime = null;
            try {
                localTime = DateUtils.parseTime(time, AppConstant.DEFAULT_FORMAT_TIME);
            } catch (Exception e) {
                throw new InvalidFieldException(ErrCode.INF_FIELDINVALID, errMesg);
            }
        }//end of if
    }

    public static void validateNumber(String number, String errMesg)
            throws InvalidFieldException{

        errMesg  = StringUtils.isEmpty(errMesg) ? "Field tidak valid" : errMesg;

        if( StringUtils.isNotEmpty(number) ){
            try {
                Long.parseLong(number);
            } catch (NumberFormatException e) {
                throw new InvalidFieldException(ErrCode.INF_FIELDINVALID, errMesg);
            }
        }//end of if
    }

    public static void validateDouble(String number, String errMesg)
            throws InvalidFieldException{

        errMesg  = StringUtils.isEmpty(errMesg) ? "Field tidak valid" : errMesg;

        if( StringUtils.isNotEmpty(number) ){
            try {
                Double.parseDouble(number);
            } catch (NumberFormatException e) {
                throw new InvalidFieldException(ErrCode.INF_FIELDINVALID, errMesg);
            }
        }//end of if
    }

    public static void validateValue(String val, String errMessage, String... expectedValues)
            throws InvalidFieldException{

        if( StringUtils.isEmpty(val) ){
            return;
        }

        errMessage  = StringUtils.isEmpty(errMessage) ? "Field tidak valid" : errMessage;

        for (String expectedValue : expectedValues) {
            if(val.equals(expectedValue)){
                return;
            }
        }
        throw new InvalidFieldException(ErrCode.INF_FIELDINVALID, errMessage);
    }

    public static void validateBoolean(String val, String errMessage)
            throws InvalidFieldException{

        if( StringUtils.isEmpty(val) ){
            return;
        }

        errMessage  = StringUtils.isEmpty(errMessage) ? "Field tidak valid" : errMessage;

        List<String> asList = Arrays.asList("0", "1", "true", "false");
        for (String expectedValue : asList) {
            if(val.equals(expectedValue)){
                return;
            }
        }
        throw new InvalidFieldException(ErrCode.INF_FIELDINVALID, errMessage);
    }

    public static void validateTipeUser(String val)throws InvalidFieldException{
        validateValue(val, "Tipe user tidak valid", "USER", "MERCHANT", "SYSADMIN");
    }
}
