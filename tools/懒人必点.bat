@echo off
color 1F
echo *************************** ApkEasyProtect ***************************
echo **********************************************************************
echo ************************* Akon出品，必须废品 *************************
echo **********************************************************************
echo ********** AndResGuard资源文件混淆，360加固，Wall多渠道打包 **********
echo **********************************************************************
echo 使用说明：拖动APK文件至本窗口，回车键执行。
:start
set /p input=APK Path [Enter]:

::空判断
if "%input%"=="" (
echo LOG 路径不可为空！
goto start
)

::后缀合法性判断
echo %input%|findstr "apk" >nul  
if %errorlevel% equ 1 (
echo LOG 非APK文件，请重新操作！
goto start  
) 

::能偷懒就偷懒
java -jar ApkEasyProtect.jar %input%
goto start

pause