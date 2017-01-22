package com.perfume.Utis;

import android.app.Activity;
import com.canyinghao.candialog.CanDialog;
import com.perfume.R;
import com.zhl.cbdialog.CBDialogBuilder;
/*
Dialog类
*/
public class ShowDialog
{
      public static void showErrorBox(Activity thzz, String error) {
            new CanDialog.Builder(thzz)
               .setIconType(CanDialog.ICON_WARNING)
               .setTitle(thzz.getString(R.string.app_name))
               .setMessage(error)
               .setCircularRevealAnimator(CanDialog.CircularRevealStatus.BOTTOM_RIGHT)
               .setNegativeButton("OK", true, null)
               .show();
         }
         
      public static void showLogin(Activity thzz, String a)
         {
            new CBDialogBuilder(thzz, CBDialogBuilder.DIALOG_STYLE_PROGRESS_AVLOADING)
               .setTouchOutSideCancelable(false)
               .showCancelButton(true)
               .setMessage("正在"+a+"请稍后...")
               .setDialogAnimation(CBDialogBuilder.DIALOG_ANIM_SLID_BOTTOM)
               .setProgressIndicatorColor(0xaa198675)
               .setProgressIndicator(CBDialogBuilder.INDICATOR_BallRotate)
               .create().show();
         }
}
