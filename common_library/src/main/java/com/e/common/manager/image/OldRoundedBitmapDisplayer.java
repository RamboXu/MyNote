/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.e.common.manager.image;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.utils.L;

/**
 * Displays bitmap with rounded corners. This implementation works only with ImageViews wrapped in ImageViewAware.<br />
 * <b>NOTE:</b> It's strongly recommended your {@link android.widget.ImageView} has defined width (<i>layout_width</i>) and height
 * (<i>layout_height</i>) .<br />
 * <b>NOTE:</b> New {@link android.graphics.Bitmap} object is created for displaying. So this class needs more memory and can cause
 * {@link OutOfMemoryError}.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.5.6
 */
public class OldRoundedBitmapDisplayer extends FadeInBitmapDisplayer {

    private final int roundRate;

    private boolean isBlackWhite;

    public OldRoundedBitmapDisplayer(int roundRate, boolean isFadeIn) {
        super(isFadeIn ? 1000 : 0);
        this.roundRate = roundRate;
    }

    public OldRoundedBitmapDisplayer isBlackWhite(boolean isBlackWhite) {
        this.isBlackWhite = isBlackWhite;
        return this;
    }

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        if (!(imageAware instanceof ImageViewAware)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        }
        Bitmap roundedBitmap = roundCorners(bitmap, (ImageViewAware) imageAware, roundRate);
//		imageAware.setImageBitmap(roundedBitmap);
        super.display(roundedBitmap, imageAware, loadedFrom);
    }

    /**
     * Process incoming {@linkplain android.graphics.Bitmap} to make rounded corners according to target
     * {@link com.nostra13.universalimageloader.core.imageaware.ImageViewAware}.<br />
     * This method <b>doesn't display</b> result bitmap in {@link android.widget.ImageView}
     *
     * @param bitmap     Incoming Bitmap to process
     * @param imageAware Target {@link com.nostra13.universalimageloader.core.imageaware.ImageAware ImageAware} to
     *                   display bitmap in
     * @param roundRate  Rounded roundRate of corner
     * @return Result bitmap with rounded corners
     */
    public static Bitmap roundCorners(Bitmap bitmap, ImageViewAware imageAware, int roundRate) {
        ImageView imageView = imageAware.getWrappedView();
        if (imageView == null) {
            L.w("View is collected probably. Can't round bitmap corners without view properties.");
            return bitmap;
        }

        Bitmap roundBitmap;

        int bw = bitmap.getWidth();
        int bh = bitmap.getHeight();
        int vw = imageAware.getWidth();
        int vh = imageAware.getHeight();
        if (vw <= 0) vw = bw;
        if (vh <= 0) vh = bh;

        final ImageView.ScaleType scaleType = imageView.getScaleType();
        if (scaleType == null) {
            return bitmap;
        }

        int width, height;
        Rect srcRect;
        Rect destRect;
        switch (scaleType) {
            case CENTER_INSIDE:
                float vRation = (float) vw / vh;
                float bRation = (float) bw / bh;
                int destWidth;
                int destHeight;
                if (vRation > bRation) {
                    destHeight = Math.min(vh, bh);
                    destWidth = (int) (bw / ((float) bh / destHeight));
                } else {
                    destWidth = Math.min(vw, bw);
                    destHeight = (int) (bh / ((float) bw / destWidth));
                }
                int x = (vw - destWidth) / 2;
                int y = (vh - destHeight) / 2;
                srcRect = new Rect(0, 0, bw, bh);
                destRect = new Rect(x, y, x + destWidth, y + destHeight);
                width = vw;
                height = vh;
                break;
            case FIT_CENTER:
            case FIT_START:
            case FIT_END:
            default:
                vRation = (float) vw / vh;
                bRation = (float) bw / bh;
                if (vRation > bRation) {
                    width = (int) (bw / ((float) bh / vh));
                    height = vh;
                } else {
                    width = vw;
                    height = (int) (bh / ((float) bw / vw));
                }
                srcRect = new Rect(0, 0, bw, bh);
                destRect = new Rect(0, 0, width, height);
                break;
            case CENTER_CROP:
                vRation = (float) vw / vh;
                bRation = (float) bw / bh;
                int srcWidth;
                int srcHeight;
                if (vRation > bRation) {
                    srcWidth = bw;
                    srcHeight = (int) (vh * ((float) bw / vw));
                    x = 0;
                    y = (bh - srcHeight) / 2;
                } else {
                    srcWidth = (int) (vw * ((float) bh / vh));
                    srcHeight = bh;
                    x = (bw - srcWidth) / 2;
                    y = 0;
                }
                width = srcWidth;// Math.min(vw, bw);
                height = srcHeight;//Math.min(vh, bh);
                srcRect = new Rect(x, y, x + srcWidth, y + srcHeight);
                destRect = new Rect(0, 0, width, height);
                break;
            case FIT_XY:
                width = vw;
                height = vh;
                srcRect = new Rect(0, 0, bw, bh);
                destRect = new Rect(0, 0, width, height);
                break;
            case CENTER:
            case MATRIX:
                width = Math.min(vw, bw);
                height = Math.min(vh, bh);
                x = (bw - width) / 2;
                y = (bh - height) / 2;
                srcRect = new Rect(x, y, x + width, y + height);
                destRect = new Rect(0, 0, width, height);
                break;
        }

        try {
            roundBitmap = getRoundedCornerBitmap(bitmap, roundRate, srcRect, destRect, width, height);
        } catch (OutOfMemoryError e) {
            L.e(e, "Can't create bitmap with rounded corners. Not enough memory.");
            roundBitmap = bitmap;
        }

        return roundBitmap;
    }

    private static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int roundRate, Rect srcRect, Rect destRect, int width,
                                                 int height) {
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final RectF destRectF = new RectF(destRect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xFF000000);

        int pixels = roundRate;
        /**
         * 当routeRate在0和50之间时，通过比例换算为pixels
         */
        if (roundRate < 50 && roundRate != 0) {
            pixels = bitmap.getWidth() * roundRate / 100;
        }
        canvas.drawRoundRect(destRectF, pixels, pixels, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, srcRect, destRectF, paint);

        return output;
    }
}
