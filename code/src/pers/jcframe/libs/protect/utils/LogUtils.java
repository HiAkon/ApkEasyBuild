package pers.jcframe.libs.protect.utils;

public class LogUtils
{
    public static void i(String str)
    {
        if ("################################################".equals(str)
            || "#                                              #".equals(str)
            || "#        ## #   #    ## ### ### ##  ###        #".equals(str)
            || "#       # # #   #   # #  #  # # # #  #         #".equals(str)
            || "#       ### #   #   ###  #  # # ##   #         #".equals(str)
            || "#       # # ### ### # #  #  ### # # ###        #".equals(str)
            || "# Obfuscation by Allatori Obfuscator v5.6 DEMO #".equals(str)
            || "#           http://www.allatori.com            #".equals(str))
        {
            return;
        }
        System.out.print(str + "\n");
    }
}
