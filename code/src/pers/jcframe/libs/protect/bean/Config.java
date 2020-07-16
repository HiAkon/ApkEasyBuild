package pers.jcframe.libs.protect.bean;

/**
 *
 * 类名称：Config.java
 * <p>
 * 类描述：工具配置
 * <p>
 * 创建人：Zhang Huai Kang
 * <p>
 * 创建时间：2020/7/15
 * <p>
 * 
 * @version
 */
public class Config
{
    /**
     * 开启资源文件混淆
     */
    private boolean enabledAndResGuard = false;
    
    private String outputPath;
    
    public boolean isEnabledAndResGuard()
    {
        return enabledAndResGuard;
    }
    
    public void setEnabledAndResGuard(boolean enabledAndResGuard)
    {
        this.enabledAndResGuard = enabledAndResGuard;
    }
    
    public String getOutputPath()
    {
        return outputPath;
    }
    
    public void setOutputPath(String outputPath)
    {
        this.outputPath = outputPath;
    }
}
