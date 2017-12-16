package com.cang.zhenpin.zhenpincang.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.base.IntentFlag;
import com.cang.zhenpin.zhenpincang.model.UserInfo;
import com.cang.zhenpin.zhenpincang.pref.PreferencesFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by victor on 2017/11/25.
 * Email: wwmdirk@gmail.com
 */

public class ShareUtil {
    
    public static Intent BuildShareIntent(Context context, List<File> files, String desc) {
        DialogUtil.getProgressDialog(context);
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", desc);
        cmb.setPrimaryClip(clipData);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        ArrayList<Uri> uris = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            Uri uri;
            File file = files.get(i);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try {
                    uri =Uri.parse(android.provider.MediaStore.Images.Media.insertImage(context.getContentResolver(),
                            file.getAbsolutePath(), i + ".jpg", null));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    uri = null;
                }
            } else {
                uri = Uri.fromFile(file);
            }
            uris.add(uri);
        }
        intent.putExtra(Intent.EXTRA_STREAM, uris);
        intent.putExtra(Intent.EXTRA_TEXT, desc);
        intent.putExtra(IntentFlag.K_DESCRIPTION, desc);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            intent = Intent.createChooser(intent, context.getString(R.string.share_to));
        }
        return intent;
    }

    public static boolean isShareEnabled() {
        return PreferencesFactory.getUserPref().getUserType() == UserInfo.TYPE_AGENT_INT;
    }
}
