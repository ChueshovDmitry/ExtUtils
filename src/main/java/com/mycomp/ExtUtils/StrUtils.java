package com.mycomp.ExtUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by kondakov on 15.01.2015.
 */
public class StrUtils {

    public static final int agLeft = 0;
    public static final int agCenter = 1;
    public static final int agRight = 2;

    /**
     * Добавляет указанное количество символов строке
     * @param in - строка
     * @param space - добавляемый символ
     * @param width - общая длинна строки, которая должна получится
     * @param align - выравнивание исходной строки
     * @return
     */
    public static String spaceStr(String in,String space,int width,int align)
    {
        String result = "";
        int lenghtIn = in.length();
        int leftLenght = 0;
        int rightLenght = width;

        switch (align) {
            case agLeft:{
                leftLenght = 0;
                rightLenght = width;
                break;
            }
            case agCenter:{
                int pr = Math.round((width)/2);
                int pr2 = (int) Math.round(Math.ceil((double)(width) / 2));

                int in1 = Math.round((lenghtIn)/2);
                int in2 = (int) Math.round(Math.ceil((double)(lenghtIn) / 2));

                leftLenght = pr+in2;
                rightLenght = pr2+in1;
                break;
            }
            case agRight:{
                leftLenght = width;
                rightLenght = 0;
                break;
            }
        }
        result = in;
        for(int i=lenghtIn;i<=leftLenght-1; i++){
            result = space+result;
        }

        for(int i=lenghtIn;i<=rightLenght-1; i++){
            result = result+space;
        }
        return result;
    }

    /**
     * Получение StackTrace как String с задаваемым форматом
     */
    public static String getCustomStackTrace(Throwable aThrowable)
    {
        // add the class name and any message passed to constructor
        final StringBuilder result = new StringBuilder("ERROR: ");
        result.append(aThrowable.toString());
        final String NEW_LINE = System.getProperty("line.separator");
        result.append(NEW_LINE);
        // add each element of the stack trace
        for (StackTraceElement element : aThrowable.getStackTrace()) {
            result.append(element);
            result.append(NEW_LINE);
        }

        return result.toString();
    }

    /**
     * Строка текущей даты в заданном формате
     * @param format - формат
     * @return строка даты
     */
    public static String currentDate(String format)
    {
        Date curTime = new Date();
        DateFormat dtfrm = DateFormat.getDateTimeInstance();
        String dateTime = dtfrm.format(curTime);
        if(!format.equals(""))
        {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            dateTime = sdf.format(curTime);
        }

        return dateTime;
    }

    public static String currentDate()
    {
        return currentDate("dd.MM.yyyy HH:mm:ss");
    }

    /**
     * Метод генерит Гуид
     * @return строка гуида
     */
    public static String generatedGUID()
    {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
