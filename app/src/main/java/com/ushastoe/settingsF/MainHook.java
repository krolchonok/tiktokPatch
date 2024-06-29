package com.ushastoe.settingsF;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (lpparam.packageName.contains("com.android.settings")) {
            try {
                XposedHelpers.findAndHookMethod("com.android.settings.applications.appinfo.AppVersionPreferenceController", lpparam.classLoader, "getSummary", new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        Object mParent = XposedHelpers.getObjectField(param.thisObject, "mParent");
                        Object packageInfo = XposedHelpers.callMethod(mParent, "getPackageInfo");

                        if (packageInfo != null) {
                            String versionName = (String) XposedHelpers.getObjectField(packageInfo, "versionName");
                            String packageName = (String) XposedHelpers.getObjectField(packageInfo, "packageName");
                            String newSummary = versionName + "\n" + packageName;
                            XposedBridge.log("packageName - " + packageName);
                            param.setResult(newSummary);
                        } else {
                            param.setResult(null);
                        }
                    }
                });
            } catch (Throwable t) {
                XposedBridge.log(t);
            }

        }
    }
}
