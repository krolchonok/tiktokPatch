package com.ushastoe.statusBarXposed;
import static android.util.TypedValue.COMPLEX_UNIT_DIP;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import android.content.res.XResources;
import android.widget.TextView;

import java.util.Objects;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage, IXposedHookInitPackageResources {

    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.android.systemui"))
            return;

        findAndHookMethod("com.android.systemui.statusbar.policy.Clock", lpparam.classLoader, "updateClock", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                TextView tv = (TextView) param.thisObject;
                String text = tv.getText().toString();
                tv.setText(text + " :)");
                tv.setTextSize(10);
            }
        });
    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resParam) throws Throwable {
        if (!resParam.packageName.equals("com.android.systemui"))
            return;

        int status_bar_padding_end = resParam.res.getIdentifier("status_bar_padding_end", "dimen", resParam.packageName);
        resParam.res.setReplacement(status_bar_padding_end, new XResources.DimensionReplacement(0, COMPLEX_UNIT_DIP));

    }
}
