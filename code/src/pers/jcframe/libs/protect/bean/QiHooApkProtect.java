package pers.jcframe.libs.protect.bean;

public class QiHooApkProtect
{
    private String login;
    
    private String importsign;

    private boolean checkUpdate;
    
    private String config_so;
    
    private String config_assets;
    
    private String config_so_private;
    
    public String getLogin()
    {
        return login;
    }
    
    public void setLogin(String login)
    {
        this.login = login;
    }

    
    public String getImportsign()
    {
        return importsign;
    }
    
    public void setImportsign(String importsign)
    {
        this.importsign = importsign;
    }

    public String getConfig_so()
    {
        return config_so;
    }
    
    public void setConfig_so(String config_so)
    {
        this.config_so = config_so;
    }
    
    public String getConfig_assets()
    {
        return config_assets;
    }
    
    public void setConfig_assets(String config_assets)
    {
        this.config_assets = config_assets;
    }
    
    public String getConfig_so_private()
    {
        return config_so_private;
    }
    
    public void setConfig_so_private(String config_so_private)
    {
        this.config_so_private = config_so_private;
    }
    
    public boolean isCheckUpdate()
    {
        return checkUpdate;
    }
    
    public void setCheckUpdate(boolean checkUpdate)
    {
        this.checkUpdate = checkUpdate;
    }
}
