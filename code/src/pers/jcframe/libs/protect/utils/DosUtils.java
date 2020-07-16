package pers.jcframe.libs.protect.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DosUtils
{
    public static boolean execute(String dos, boolean log, String keyword)
    {
        try
        {
            Runtime rt = Runtime.getRuntime();
            final Process process = rt.exec("cmd /c " + dos);
            // 标准输入流（必须写在 waitFor 之前）
            String inStr = consumeInputStream(process.getInputStream(), log);
            // 标准错误流（必须写在 waitFor 之前）
            String errStr = consumeInputStream(process.getErrorStream(), log);
            
            int retCode = process.waitFor();
            if (retCode == 0)
            {
                if (keyword != null && inStr.indexOf(keyword) > -1)
                {
                    return true;
                }
            }
            return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 消费inputstream，并返回
     */
    private static String consumeInputStream(InputStream is, boolean log)
        throws IOException
    {
        BufferedReader br =
            new BufferedReader(new InputStreamReader(is, "GBK"));
        String s;
        StringBuilder sb = new StringBuilder();
        while ((s = br.readLine()) != null)
        {
            if (log)
            {
                LogUtils.i(s);
            }
            sb.append(s + "\n");
        }
        return sb.toString();
    }
}
