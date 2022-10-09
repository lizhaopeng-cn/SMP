package com.lzp.smp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {
    private static volatile SPUtil instance;
    private SharedPreferences sp;

    private SPUtil(Context context){
        sp = context.getSharedPreferences("smp", Context.MODE_PRIVATE); //私有数据
    }

    public static SPUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (SPUtil.class) {
                if (instance == null) {
                    instance = new SPUtil(context);
                }
            }
        }
        return instance;
    }

    public void setValue(String key, String value) {
        if (sp == null) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();//获取编辑器
        editor.putString(key, value);
        editor.commit();//提交修改
    }

    public String getValue(String key) {
        if (sp == null) {
            return "";
        }
        return sp.getString(key, "");
    }
}
