package com.perfume.Utis.Apktool;






import brut.androlib.AndrolibException;
import brut.androlib.res.decoder.NinePatch;
import brut.androlib.res.decoder.res9;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import brut.androlib.res.AndrolibResources;
import java.util.logging.Logger;
/*
APKTOOL .9图片处理
*/
public class Res9 extends res9
   {
      private static final Logger LOGGER = Logger.getLogger(Res9.class.getName());

      @Override
      public void decode(InputStream in, OutputStream out)
      throws AndrolibException
         {
            try
               {
                  byte[] data = IOUtils.toByteArray(in);

                  Bitmap im=BitmapFactory.decodeByteArray(data,0,data.length);
                  int w = im.getWidth(), h = im.getHeight();

                  Bitmap im2 = Bitmap.createBitmap(w + 2, h + 2, im.getConfig());
                  for (int x=0;x < w;x++)
                     {
                        for (int y=0;y < h;y++)
                           {
                              int color=im.getPixel(x, y);
                              im2.setPixel(x + 1, y + 1, color);
                           }
                     }

                  NinePatch np = NinePatch.getNinePatch(data);
                  drawHLine(im2, h + 1, np.padLeft + 1, w - np.padRight);
                  drawVLine(im2, w + 1, np.padTop + 1, h - np.padBottom);

                  int[] xDivs = np.xDivs;
                  for (int i = 0; i < xDivs.length; i += 2)
                     {
                        drawHLine(im2, 0, xDivs[i] + 1, xDivs[i + 1]);
                     }

                  int[] yDivs = np.yDivs;
                  for (int i = 0; i < yDivs.length; i += 2)
                     {
                        drawVLine(im2, 0, yDivs[i] + 1, yDivs[i + 1]);
                     }

                  im2.compress(Bitmap.CompressFormat.PNG, 100, out);
               }
            catch (IOException ex)
               {
                  throw new AndrolibException(ex);
               }
            catch (NullPointerException ex)
               {
                  // In my case this was triggered because a .png file was
                  // containing a html document instead of an image.
                  // This could be more verbose and try to MIME ?
                  throw new AndrolibException(ex);
               }
         }

      private void drawHLine(Bitmap im, int y, int x1, int x2)
         {
            for (int x = x1; x <= x2; x++)
               {
                  im.setPixel(x, y, NP_COLOR);
               }
         }

      private void drawVLine(Bitmap im, int x, int y1, int y2)
         {
            for (int y = y1; y <= y2; y++)
               {
                  im.setPixel(x, y, NP_COLOR);
               }
         }

      private static final int NP_COLOR = 0xff000000;
   }


