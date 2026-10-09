package com.jeremykenedy.rainonglass;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;

import java.util.Random;

final class RainSceneView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Random random = new Random(94103L);
    private final float[] glowX = new float[9];
    private final float[] glowY = new float[9];
    private final float[] glowRadius = new float[9];
    private final float[] surfaceX = new float[260];
    private final float[] surfaceY = new float[260];
    private final float[] surfaceSize = new float[260];
    private RainOptions options;
    private Drop[] drops;
    private LinearGradient background;
    private boolean running;
    private long startedAt;

    RainSceneView(Context context) {
        super(context);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        loadOptions();
        for (int i = 0; i < glowX.length; i++) {
            glowX[i] = random.nextFloat();
            glowY[i] = random.nextFloat();
            glowRadius[i] = 0.025f + random.nextFloat() * 0.07f;
        }
        for (int i = 0; i < surfaceX.length; i++) {
            surfaceX[i] = random.nextFloat();
            surfaceY[i] = random.nextFloat();
            surfaceSize[i] = 0.45f + random.nextFloat() * 1.4f;
        }
    }

    void start() {
        if (!running) {
            running = true;
            startedAt = SystemClock.uptimeMillis();
            postInvalidateOnAnimation();
        }
    }

    void stop() {
        running = false;
        removeCallbacks(invalidator);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getWidth() == 0 || getHeight() == 0) return;
        float time = (SystemClock.uptimeMillis() - startedAt) / 1000f;
        drawBackground(canvas, time);
        drawSurfaceDrops(canvas, time);
        drawRain(canvas, time);
        if (running) postDelayed(invalidator, 33L);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        if (width <= 0 || height <= 0) return;
        int top = options.light ? 0xff758b91 : 0xff101b2b;
        int bottom = options.light ? 0xffc0c6c2 : 0xff02060d;
        background = new LinearGradient(0, 0, width, height, top, bottom, Shader.TileMode.CLAMP);
    }

    private final Runnable invalidator = new Runnable() {
        @Override public void run() { if (running) invalidate(); }
    };

    private void loadOptions() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        options = RainOptions.resolve(preferences.getString("background", "night"),
                preferences.getString("density", "handful"), preferences.getString("motion", "steady"),
                preferences.getString("city_glow", "on"), preferences.getString("focus", "soft"),
                preferences.getBoolean("randomize_all", false), new Random(System.currentTimeMillis()));
        drops = new Drop[options.drops];
        for (int i = 0; i < drops.length; i++) {
            float x = random.nextFloat();
            float y = random.nextFloat();
            float length = 0.045f + random.nextFloat() * (options.sharp ? 0.16f : 0.12f);
            float width = 0.0028f + random.nextFloat() * 0.0035f;
            float velocity = 0.12f + random.nextFloat() * 0.38f;
            drops[i] = new Drop(x, y, length, width, velocity, random.nextFloat() * 6.28f,
                    random.nextFloat() < 0.42f);
        }
    }

    private void drawBackground(Canvas canvas, float time) {
        paint.setShader(background);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        paint.setShader(null);
        if (!options.glow) return;
        for (int i = 0; i < glowX.length; i++) {
            float drift = (float) Math.sin(time * 0.045f + i * 1.7f) * getWidth() * 0.035f;
            float x = glowX[i] * getWidth() + drift;
            float y = glowY[i] * getHeight();
            float radius = glowRadius[i] * Math.min(getWidth(), getHeight());
            int tint = options.light ? 0x387f9d83 : 0x5088a9ce;
            paint.setShader(new RadialGradient(x, y, radius,
                    new int[] {tint, tint & 0x00ffffff}, null, Shader.TileMode.CLAMP));
            canvas.drawCircle(x, y, radius, paint);
        }
        paint.setShader(null);
    }

    private void drawSurfaceDrops(Canvas canvas, float time) {
        paint.setColor(options.light ? 0xffe4f4f1 : 0xffc4dce8);
        for (int i = 0; i < surfaceX.length; i++) {
            float pulse = 0.48f + 0.36f * (float) Math.sin(time * 0.3f + i * 1.9f);
            paint.setAlpha((int) ((options.sharp ? 35 : 20) + (options.sharp ? 95 : 55) * pulse));
            float x = surfaceX[i] * getWidth();
            float y = surfaceY[i] * getHeight();
            float size = surfaceSize[i] * (options.sharp ? 1.5f : 1f);
            canvas.drawOval(x, y, x + size * 1.4f, y + size * 2.4f, paint);
        }
        paint.setAlpha(255);
    }

    private void drawRain(Canvas canvas, float time) {
        int color = options.light ? 0xaee6f3f3 : 0xbad8e9f4;
        for (int i = 0; i < drops.length; i++) {
            Drop drop = drops[i];
            float x = (drop.x + (float) Math.sin(time * 0.22f + drop.phase) * 0.0025f) * getWidth();
            float progress = (drop.y + time * drop.velocity * options.speed) % 1.08f;
            float y = (progress - 0.04f) * getHeight();
            float length = drop.length * getHeight();
            float width = Math.max(1f, drop.width * getWidth());
            if (drop.longDrop) drawRunoff(canvas, x, y, length, width, color);
            else drawBead(canvas, x, y, length, width, color);
        }
    }

    private void drawRunoff(Canvas canvas, float x, float y, float length, float width, int color) {
        paint.setShader(null);
        paint.setColor(options.light ? 0x4cffffff : 0x557a9cac);
        paint.setStrokeWidth(Math.max(1f, width * 0.75f));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(x, y - length, x, y, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        paint.setAlpha(color >>> 24);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawOval(x - width * 1.3f, y - width * 2.1f, x + width * 1.3f, y + width * 1.4f, paint);
        paint.setColor(options.light ? 0x87ffffff : 0x99d7f6ff);
        canvas.drawCircle(x - width * 0.45f, y - width * 0.7f, Math.max(1f, width * 0.38f), paint);
        paint.setColor(0xffffffff);
    }

    private void drawBead(Canvas canvas, float x, float y, float length, float width, int color) {
        paint.setColor(color);
        paint.setAlpha(options.sharp ? 145 : 105);
        float radius = Math.max(width * 1.25f, length * 0.2f);
        canvas.drawOval(x - width, y - radius, x + width, y + radius * 1.7f, paint);
        paint.setColor(options.light ? 0x68ffffff : 0x88f2fbff);
        canvas.drawCircle(x - width * 0.35f, y - radius * 0.45f, Math.max(1f, width * 0.34f), paint);
        paint.setColor(0xffffffff);
    }

    private static final class Drop {
        final float x;
        final float y;
        final float length;
        final float width;
        final float velocity;
        final float phase;
        final boolean longDrop;

        Drop(float x, float y, float length, float width, float velocity, float phase, boolean longDrop) {
            this.x = x;
            this.y = y;
            this.length = length;
            this.width = width;
            this.velocity = velocity;
            this.phase = phase;
            this.longDrop = longDrop;
        }
    }
}
