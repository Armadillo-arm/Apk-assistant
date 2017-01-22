package com.perfume.UI;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import com.perfume.FileAdapter.DirectoryFragment;
import com.perfume.MainActivity;
import com.perfume.Utis.SnackbarShow;

public class FabOnClick extends MainActivity implements OnClickListener
   {

      @Override
      public void onClick ( View p1 )
         {
            SnackbarShow A=new SnackbarShow();
            A.LongShow(p1,"Close App","Yes");
            A.setOnItemClickListener ( new  SnackbarShow.OnTextClick ( ){
                     @Override
                     public void OnSnackbarClick ( View v )
                        {
                           finish();
                        }
                  } );
         }

}
