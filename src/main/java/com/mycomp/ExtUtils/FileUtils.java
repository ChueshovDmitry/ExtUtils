package com.mycomp.ExtUtils;

import com.mycomp.ExtUtils.FilePathParams;

import java.io.*;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Created by kondakov on 15.01.2015.
 */
public class FileUtils {

    /**
     * Разбирает строку пути файла на части
     * @param pathFile - строка пути к файлу
     * @return
     */
    public static FilePathParams getFilePathParams(String pathFile) {
        FilePathParams result = new FilePathParams();
        File file = new File(pathFile);
        String fileName = file.getName();
        result.nameFile = fileName.split("\\.")[0];
        result.extFile = fileName.split("\\.")[1];
        result.path = file.getParent();
        return result;
    }

    /**
     * Запись строки в текстовый файл
     * @param pathFile - путь к файлу
     * @param str - строка
     * @param app - добавляем в конец файла, false - записываем сначала файла
     */
    public static void writeFile(String pathFile, String str, Boolean app) throws IOException {
            String rez = new String(str.getBytes(Charset.forName("UTF-8")));
            File script = new File(pathFile);
            FileWriter out = new FileWriter(script, app);
            out.write(rez);
            out.close();
    }

    /**
     * Запись строки в текстовый файл
     * @param pathFile - путь к файлу
     * @param is - поток
     * @throws IOException
     */

    public static void writeFile(String pathFile, InputStream is) throws IOException {
            File file=new File(pathFile);
            OutputStream out=new FileOutputStream(file);
            byte buf[]=new byte[1024];
            int len;
            while((len=is.read(buf))>0)
                out.write(buf,0,len);
            out.close();
            is.close();
    }

    /**
     * Читаем xml, удаляем символы переноса строки, и проблеы между тегами
     * @param path - путь к файлу
     * @return строка отформатированной xml
     */
    public static String readFile(String path,boolean delXmlSpace) throws IOException {
        String result = "";
        BufferedReader out = new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8"));
        String line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        while(( line = out.readLine() ) != null ) {
            stringBuilder.append(line);
        }
        String r = stringBuilder.toString();
        if(delXmlSpace){
            r = delSpace(r);
        }
        result = r.trim();
        out.close();
        return result;
    }

    /**
     * Удаляет перенос строки
     * @param in - строка с переносами
     * @return вернет строку без символов переноса
     */

    public static String delSpace(String in){
        return in.replaceAll("\n|\r\n", "");
    }

    /**
     * Чтение настроек из файла
     * @param pathFileProperty путь файла проперти
     * @param key ключ параметра
     * @return возвращает строку значения параметра
     * @throws IOException
     */
    public static String getProperty(String pathFileProperty, String key) throws IOException{
        InputStreamReader file = new InputStreamReader(new FileInputStream(pathFileProperty));
        Properties systemSettings = new Properties();
        systemSettings.load(file);
        return systemSettings.getProperty(key);
    }

    /**
     * Удаляет директориию и файлы в ней
     * @param dir
     */
    public static void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                File f = new File(dir, children[i]);
                deleteDirectory(f);
            }
            dir.delete();
        } else dir.delete();
    }
    public static void printLog(String message){
        printLog(message, "./");
    }
    public static void printLog(String message, String pathLogs){
        message = "["+StrUtils.currentDate()+"]"+message+ "\n";
        try{
//            String pathLogs = "/tmp/logsServices/AdjustTimeService/";
            File dir = new File(pathLogs);
            if(!dir.exists())
                dir.mkdirs();
            String logFileName = pathLogs+"log.out";
            File logFile = new File(logFileName);
            if(dir.exists()){
                if(!logFile.exists()) {
                    try{
                        logFile.createNewFile();
                    }catch (Exception e) {
                        System.out.println("Невозможно создать запись в лог сервиса!\n"+StrUtils.getCustomStackTrace(e));
                    }
                }else{
                    if (logFile.length()>1024*1024*10){
                        String newFname = pathLogs+"log_"+StrUtils.currentDate()+".out";
                        logFile.renameTo(new File(newFname));
                        logFile = new File(logFileName);
                        if(!logFile.exists()) {
                            try{
                                logFile.createNewFile();
                            }catch (Exception e) {
                                System.out.println("Невозможно создать запись в лог сервиса!\n"+StrUtils.getCustomStackTrace(e));
                            }
                        }
                    }
                }
                if(logFile.canWrite()){
                    writeFile(logFileName, message, true);
                }else{
                    System.out.println("Невозможно создать запись в лог (файл лога заблокирован)!");
                }
            }else{
                System.out.println("Невозможно создать директорию для логов!");
            }
        }catch (Exception e) {
            System.out.println("Невозможно создать запись в лог сервиса!\n"+StrUtils.getCustomStackTrace(e));
        }
    }

    public static String newNameFile(String path, String filaName, String extName) {
        //String path = "./";
        String result = path + filaName + "." + extName;
        int i = 0;
        while (isFile(result)){
            result = path + filaName + "(" + (++i) + ")." + extName;
        }
        return result;
    }


    private static boolean isFile(String path) {
        File file = new File(path);
        return file.exists();
    }
}
