package pers.jcframe.libs.protect;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import com.alibaba.fastjson.JSON;

import pers.jcframe.libs.protect.bean.Config;
import pers.jcframe.libs.protect.bean.QiHooApkProtect;
import pers.jcframe.libs.protect.bean.Walle;
import pers.jcframe.libs.protect.utils.DosUtils;
import pers.jcframe.libs.protect.utils.FileUtils;
import pers.jcframe.libs.protect.utils.LogUtils;

/**
 *
 * 类名称：Index.java
 * <p>
 * 类描述：入口，你懂的
 * <p>
 * 创建人：Zhang Huai Kang
 * <p>
 * 创建时间：2020/7/15
 * <p>
 * 
 * @version
 */
public class Index
{
    private static String rootPath = null;
    
    private static Config config;
    
    private static String apkPath;
    
    private static String projectName = "project";
    
    private static String tempDir;
    
    private static String outPath;
    
    private static String tempApkPath;
    
    private static String apkName;
    
	public static void main(String[] args)
    {
        tempDir = new SimpleDateFormat("MM_dd_HH_mm_ss")
            .format(System.currentTimeMillis());
        
        if (args == null || args.length == 0 || args.length > 2)
        {
            LogUtils.i("参数为空或非法!");
        }
        
        File directory = new File("");
        try
        {
            rootPath = directory.getCanonicalPath();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        if (rootPath == null)
        {
            LogUtils.i("初始化失败!");
            return;
        }
        
        if (args.length == 2)
        {
            if (!new File(rootPath + "/" + args[0]).exists())
            {
                LogUtils.i("请确认配置目录是否存在：" + args[0]);
                return;
            }
            
            projectName = args[0];
            apkPath = args[1];
        }
        else
        {
            apkPath = args[0];
            
        }
        
        if (apkPath.indexOf(".apk") == -1)
        {
            LogUtils.i("文件非法，非APK");
            return;
        }
        
        apkName = new File(apkPath).getName();
        
        String configJson =
            FileUtils.readFile(rootPath + "/" + projectName + "/config.json");
        
        if (configJson == null)
        {
            LogUtils.i("读取配置失败!");
            return;
        }
        
        config = JSON.parseObject(configJson, Config.class);
        
        if (config == null)
        {
            LogUtils.i("读取配置失败!");
            return;
        }
        
        if (config.getOutputPath() == null)
        {
            outPath = rootPath + "/" + projectName + "/" + tempDir;
        }
        else
        {
            outPath = config.getOutputPath() + "/" + tempDir;
        }
        
        LogUtils.i("存储目录：" + outPath);
        
        // 创建存放目录
        new File(outPath).mkdirs();
        
        if (config.isEnabledAndResGuard())
        {
            andresguard();
        }
        qiHooJiaGu();
    }
    
    public static void andresguard()
    {
    	LogUtils.i("正在混淆APK资源");
        DosUtils.execute("java -jar " + rootPath
            + "\\libs\\AndResGuard\\AndResGuard-cli.jar " + apkPath
            + " -config " + rootPath + "/" + projectName
            + "/config_andresguard_resproguard.xml -out " + outPath, false, "");
    	LogUtils.i("混淆APK资源结束");
        apkName = apkName.replace(".apk", "_unsigned.apk");
        apkPath = outPath + "/" + apkName;
    }
    
    public static void qiHooJiaGu()
    {
        String configJson = FileUtils
            .readFile(rootPath + "/" + projectName + "/config_360.json");
        
        QiHooApkProtect qiHooApkProtect =
            JSON.parseObject(configJson, QiHooApkProtect.class);
        
        boolean status;
        if (qiHooApkProtect.isCheckUpdate())
        {
            LogUtils.i("加固保检查更新");
            DosUtils.execute("java -jar " + rootPath
                + "\\libs\\360\\jiagu.jar -update", true, "");
        }
        
        LogUtils.i("正在登录加固保");
        status = DosUtils
            .execute("java -jar " + rootPath + "\\libs\\360\\jiagu.jar -login "
                + qiHooApkProtect.getLogin(), false, "login success");
        
        if (status)
        {
            LogUtils.i("加固保登录成功");
        }
        else
        {
            LogUtils.i("加固保登录失败");
            return;
        }
        
        StringBuffer sb = new StringBuffer();
        sb.append("java -jar " + rootPath + "\\libs\\360\\jiagu.jar");
        if (qiHooApkProtect.getImportsign() != null)
        {
            sb.append(" -importsign ");
            sb.append(rootPath + "/" + projectName+"/"+qiHooApkProtect.getImportsign());
        }

        if(qiHooApkProtect.getConfig_so()!=null)
        {
            sb.append(" -config_so");
            sb.append(" ");
            sb.append(qiHooApkProtect.getConfig_so());
        }
        
        if(qiHooApkProtect.getConfig_so_private()!=null)
        {
            sb.append(" -config_so_private");
            sb.append(" ");
            sb.append(qiHooApkProtect.getConfig_so_private());
        }
        
        if(qiHooApkProtect.getConfig_assets()!=null)
        {
            sb.append(" -config_assets");
            sb.append(" ");
            sb.append(qiHooApkProtect.getConfig_assets());
        }
        
        DosUtils.execute(sb.toString(), false, null);
        
        LogUtils.i("开始加固");
        sb.setLength(0);
        sb.append("java -jar " + rootPath + "\\libs\\360\\jiagu.jar");
        sb.append(" -jiagu");
        sb.append(" ");
        sb.append(apkPath);
        sb.append(" ");
        sb.append(outPath);
        sb.append(" -autosign");
        
        status = DosUtils.execute(sb.toString(), true, "任务完成_已签名");
        
        if (status)
        {
            LogUtils.i("加固完成");
        }
        else
        {
            LogUtils.i("加固失败");
            return;
        }
        
        File[] files = new File(outPath).listFiles();
        
        if (files.length == 0)
        {
            LogUtils.i("未找到加固包");
        }
        
        for (File file : files)
        {
            if (file.getName().indexOf("_jiagu_sign.apk") > -1)
            {
                apkName = file.getName().replace("_unsigned", "");
                tempApkPath = outPath + "/" + apkName;
                file.renameTo(new File(tempApkPath));
            }
        }
        
        LogUtils.i("加固包路径：" + tempApkPath);
        walle();
    }
    
    public static void walle()
    {
        
        String configJson = FileUtils
            .readFile(rootPath + "/" + projectName + "/config_walle.json ");
        
        Walle walle = JSON.parseObject(configJson, Walle.class);
        
        if (walle.getChannelPath() == null)
        {
            LogUtils.i("未配置渠道信息");
            return;
        }
        
        LogUtils.i("开始多渠道打包");
        
        StringBuffer sb = new StringBuffer();
        sb.append("java -jar " + rootPath
            + "\\libs\\Walle\\walle-cli-all.jar batch2 -f ");
        sb.append(walle.getChannelPath());
        sb.append(" ");
        sb.append(tempApkPath);
        sb.append(" ");
        sb.append(outPath);
        
        DosUtils.execute(sb.toString(), false, null);
       
        LogUtils.i("");
        LogUtils.i("  )               (");
        LogUtils.i(" / \\  .-\"\"\"\"\"-.  / \\");
        LogUtils.i("(   \\/ __   __ \\/   )");
        LogUtils.i(" )  ; / _\\ /_ \\ ;  (");
        LogUtils.i("(   |  / \\ / \\  |   )");
        LogUtils.i(" \\ (,  \\0/_\\0/  ,) /");
        LogUtils.i("  \\_|   /   \\   |_/");
        LogUtils.i("    | (_\\___/_) |");
        LogUtils.i("    .\\ \\ -.- / /.");
        LogUtils.i("   {  \\ `===' /  }");
        LogUtils.i("  {    `.___.'    }");
        LogUtils.i("   {             }");
        LogUtils.i("  `\"=\"=\"=\"=\"=\"`");
    }
}