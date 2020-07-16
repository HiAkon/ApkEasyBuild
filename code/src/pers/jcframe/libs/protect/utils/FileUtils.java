package pers.jcframe.libs.protect.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class FileUtils
{
    public static String readFile(String fileName)
    {
        String jsonStr = "";
        try
        {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader =
                new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1)
            {
                sb.append((char)ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
