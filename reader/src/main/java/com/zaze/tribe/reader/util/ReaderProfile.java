/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zaze.tribe.reader.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;


/**
 * 一些不变的配置
 */
public class ReaderProfile {

    private static float DEFAULT_TEXT_SIZE = 20;
    private static int screenWidthPixels;
    private static int screenHeightPixels;
    // --------------------------------------------------
    private static float screenDensity;
    private static int screenDensityDpi;
    // --------------------------------------------------
    private static float screenWidthDp;
    private static float screenHeightDp;
    // --------------------------------------------------
    private static DisplayMetrics metrics;


    public static void init(Context context, int width, int height) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);

        metrics = context.getResources().getDisplayMetrics();
        screenWidthPixels = metrics.widthPixels;
        screenHeightPixels = metrics.heightPixels;
        screenDensity = metrics.density;
        screenDensityDpi = metrics.densityDpi;
        screenWidthDp = dpiFromPx(screenWidthPixels);
        screenHeightDp = dpiFromPx(screenHeightPixels);
    }


    public static float dpiFromPx(int size) {
        float densityRatio = (float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT;
        return (size / densityRatio);
    }
}