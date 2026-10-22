package com.jeremykenedy.rainonglass;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

final class RainSceneView extends GLSurfaceView {
  private boolean running;
  private final Runnable frame =
      new Runnable() {
        @Override
        public void run() {
          if (running) {
            requestRender();
            postDelayed(this, 33L);
          }
        }
      };

  RainSceneView(Context context) {
    super(context);
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    RainOptions options =
        RainOptions.resolve(
            preferences.getString("background", "night"),
            preferences.getString("density", "handful"),
            preferences.getString("motion", "steady"),
            preferences.getString("city_glow", "on"),
            preferences.getString("focus", "soft"),
            preferences.getBoolean("randomize_all", false),
            new Random());
    setEGLContextClientVersion(2);
    setEGLConfigChooser(8, 8, 8, 0, 0, 0);
    setPreserveEGLContextOnPause(true);
    setRenderer(new RainRenderer(context, options));
    setRenderMode(RENDERMODE_WHEN_DIRTY);
  }

  void start() {
    if (running) return;
    running = true;
    onResume();
    post(frame);
  }

  void stop() {
    if (!running) return;
    running = false;
    removeCallbacks(frame);
    onPause();
  }

  private static final class RainRenderer implements GLSurfaceView.Renderer {
    private final Context context;
    private final RainOptions options;
    private final FloatBuffer screen =
        buffer(new float[] {-1, -1, 1, -1, -1, 1, -1, 1, 1, -1, 1, 1});
    private final FloatBuffer drops;
    private final int count;
    private int backgroundProgram;
    private int dropProgram;
    private int sharpTexture;
    private int softTexture;
    private int dropBuffer;
    private int width;
    private int height;
    private long started;

    RainRenderer(Context context, RainOptions options) {
      this.context = context.getApplicationContext();
      this.options = options;
      int beads = options.drops * 3;
      count = (options.drops + beads) * 6;
      drops =
          ByteBuffer.allocateDirect(count * 8 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
      Random random = new Random(94103L);
      int[] cornersX = {-1, 1, -1, -1, 1, 1};
      int[] cornersY = {-1, -1, 1, 1, -1, 1};
      for (int i = 0; i < options.drops + beads; i++) {
        boolean moving = i < options.drops;
        float x = random.nextFloat();
        float y = random.nextFloat();
        float radius =
            moving ? .006f + random.nextFloat() * .009f : .0007f + random.nextFloat() * .002f;
        float velocity = moving ? .012f + random.nextFloat() * .038f : 0;
        float phase = random.nextFloat() * 6.283185f;
        float trail = moving ? 3f + random.nextFloat() * 6f : 1.15f;
        for (int c = 0; c < 6; c++) {
          drops.put(cornersX[c]).put(cornersY[c] > 0 ? trail : -1.3f);
          drops.put(x).put(y).put(radius).put(velocity).put(phase).put(trail);
        }
      }
      drops.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
      backgroundProgram = program(SCREEN_VERTEX, BACKGROUND_FRAGMENT);
      dropProgram = program(DROP_VERTEX, DROP_FRAGMENT);
      sharpTexture = texture(options.light ? R.drawable.window_overcast : R.drawable.window_night);
      softTexture =
          texture(
              options.light
                  ? options.sharp
                      ? R.drawable.window_overcast_crisp
                      : R.drawable.window_overcast_soft
                  : options.sharp ? R.drawable.window_night_crisp : R.drawable.window_night_soft);
      int[] ids = new int[1];
      GLES20.glGenBuffers(1, ids, 0);
      dropBuffer = ids[0];
      GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, dropBuffer);
      GLES20.glBufferData(
          GLES20.GL_ARRAY_BUFFER, drops.capacity() * 4, drops, GLES20.GL_STATIC_DRAW);
      GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
      started = SystemClock.uptimeMillis();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
      this.width = width;
      this.height = Math.max(1, height);
      GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
      float time = (SystemClock.uptimeMillis() - started) * .001f;
      GLES20.glDisable(GLES20.GL_DEPTH_TEST);
      GLES20.glDisable(GLES20.GL_BLEND);
      GLES20.glUseProgram(backgroundProgram);
      GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
      GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, softTexture);
      GLES20.glUniform1i(GLES20.glGetUniformLocation(backgroundProgram, "uTexture"), 0);
      GLES20.glUniform1f(
          GLES20.glGetUniformLocation(backgroundProgram, "uGlow"), options.glow ? 1 : 0);
      int position = GLES20.glGetAttribLocation(backgroundProgram, "aPosition");
      GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
      screen.position(0);
      GLES20.glEnableVertexAttribArray(position);
      GLES20.glVertexAttribPointer(position, 2, GLES20.GL_FLOAT, false, 0, screen);
      GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
      GLES20.glDisableVertexAttribArray(position);

      GLES20.glUseProgram(dropProgram);
      GLES20.glEnable(GLES20.GL_BLEND);
      GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
      GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, sharpTexture);
      GLES20.glUniform1i(GLES20.glGetUniformLocation(dropProgram, "uTexture"), 0);
      GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
      GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, softTexture);
      GLES20.glUniform1i(GLES20.glGetUniformLocation(dropProgram, "uBlur"), 1);
      GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
      GLES20.glUniform2f(GLES20.glGetUniformLocation(dropProgram, "uResolution"), width, height);
      GLES20.glUniform1f(GLES20.glGetUniformLocation(dropProgram, "uTime"), time);
      GLES20.glUniform1f(GLES20.glGetUniformLocation(dropProgram, "uSpeed"), options.speed);
      GLES20.glUniform1f(GLES20.glGetUniformLocation(dropProgram, "uSharp"), options.sharp ? 1 : 0);
      GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, dropBuffer);
      attribute("aCorner", 2, 0);
      attribute("aDrop", 4, 2);
      attribute("aDetail", 2, 6);
      GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, count);
      GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    private void attribute(String name, int size, int offset) {
      int location = GLES20.glGetAttribLocation(dropProgram, name);
      GLES20.glEnableVertexAttribArray(location);
      GLES20.glVertexAttribPointer(location, size, GLES20.GL_FLOAT, false, 32, offset * 4);
    }

    private int texture(int resource) {
      BitmapFactory.Options decode = new BitmapFactory.Options();
      decode.inScaled = false;
      Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resource, decode);
      if (bitmap == null) throw new IllegalStateException("Missing window background");
      int[] ids = new int[1];
      GLES20.glGenTextures(1, ids, 0);
      GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, ids[0]);
      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
      GLES20.glTexParameteri(
          GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
      GLES20.glTexParameteri(
          GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
      GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
      bitmap.recycle();
      return ids[0];
    }

    private static FloatBuffer buffer(float[] values) {
      FloatBuffer result =
          ByteBuffer.allocateDirect(values.length * 4)
              .order(ByteOrder.nativeOrder())
              .asFloatBuffer();
      result.put(values).position(0);
      return result;
    }

    private static int program(String vertex, String fragment) {
      int program = GLES20.glCreateProgram();
      int v = shader(GLES20.GL_VERTEX_SHADER, vertex);
      int f = shader(GLES20.GL_FRAGMENT_SHADER, fragment);
      GLES20.glAttachShader(program, v);
      GLES20.glAttachShader(program, f);
      GLES20.glLinkProgram(program);
      int[] status = new int[1];
      GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, status, 0);
      if (status[0] == 0) throw new IllegalStateException(GLES20.glGetProgramInfoLog(program));
      GLES20.glDeleteShader(v);
      GLES20.glDeleteShader(f);
      return program;
    }

    private static int shader(int type, String source) {
      int shader = GLES20.glCreateShader(type);
      GLES20.glShaderSource(shader, source);
      GLES20.glCompileShader(shader);
      int[] status = new int[1];
      GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0);
      if (status[0] == 0) throw new IllegalStateException(GLES20.glGetShaderInfoLog(shader));
      return shader;
    }

    private static final String SCREEN_VERTEX =
        "precision mediump float;attribute vec2 aPosition;varying vec2 vUv;void main(){vUv=vec2(aPosition.x*.5+.5,.5-aPosition.y*.5);gl_Position=vec4(aPosition,0.,1.);}";
    private static final String BACKGROUND_FRAGMENT =
        "precision mediump float;uniform sampler2D uTexture;uniform float uGlow;varying vec2 vUv;"
            + "void main(){vec3 c=texture2D(uTexture,vUv).rgb;float light=max(c.r,max(c.g,c.b));"
            + "c+=c*smoothstep(.45,.9,light)*uGlow*.16;gl_FragColor=vec4(c,1.);}";
    private static final String DROP_VERTEX =
        "precision highp float;attribute vec2 aCorner,aDetail;attribute vec4 aDrop;uniform mediump vec2 uResolution;uniform float uTime,uSpeed;"
            + "varying mediump vec2 vLocal;varying mediump float vRadius,vPhase,vTrail;void main(){"
            + "float t=uTime*uSpeed;float progress=mod(aDrop.y+aDrop.w*(t+.25*sin(t*.8+aDetail.x)),1.35)-.15;"
            + "if(aDrop.w<.001)progress=aDrop.y;float x=aDrop.x+sin(t*.22+aDetail.x)*.0015;"
            + "vec2 uv=vec2(x,progress)+vec2(aCorner.x*uResolution.y/uResolution.x,-aCorner.y)*aDrop.z;"
            + "gl_Position=vec4(uv.x*2.-1.,1.-uv.y*2.,0.,1.);vLocal=aCorner;vRadius=aDrop.z;vPhase=aDetail.x;vTrail=aDetail.y;}";
    private static final String DROP_FRAGMENT =
        "precision mediump float;uniform sampler2D uTexture,uBlur;uniform vec2 uResolution;uniform float uSharp;"
            + "varying vec2 vLocal;varying float vRadius,vPhase,vTrail;void main(){"
            + "vec2 uv=vec2(gl_FragCoord.x/uResolution.x,1.-gl_FragCoord.y/uResolution.y);"
            + "vec2 p=vLocal;float neck=.89-.13*smoothstep(-.8,.8,p.y);vec2 q=vec2(p.x/neck,(p.y+.12)/1.13);"
            + "float r=length(q);float head=1.-smoothstep(.89+uSharp*.055,1.,r);"
            + "float bend=sin(p.y*2.8+vPhase)*.10;float streak=1.-smoothstep(.045,.13,abs(p.x+bend));"
            + "streak*=smoothstep(.65,1.2,p.y)*(1.-smoothstep(1.,vTrail,p.y))*.42;"
            + "float alpha=max(head,streak);if(alpha<.01)discard;"
            + "vec2 normal=q*sqrt(max(0.,1.-min(r*r,1.)));"
            + "vec2 refract=vec2(normal.x*uResolution.y/uResolution.x,-normal.y)*vRadius*12.;"
            + "if(head<.05)refract=vec2((p.x+bend)*vRadius*.6,0.);"
            + "vec3 c=texture2D(uTexture,clamp(uv+refract,vec2(.001),vec2(.999))).rgb;"
            + "c=mix(texture2D(uBlur,uv+refract*.35).rgb,c,.24);float rim=pow(min(r,1.),7.)*.08*head;"
            + "float highlight=exp(-dot(q-vec2(-.32,.48),q-vec2(-.32,.48))*70.);"
            + "float lip=exp(-dot(q-vec2(.08,-.72),q-vec2(.08,-.72))*60.)*.28;"
            + "float edge=pow(max(0.,dot(normalize(q+vec2(.0001)),normalize(vec2(-.6,.8)))),12.)*smoothstep(.65,.93,r)*(1.-smoothstep(.93,1.,r));c=c*(1.-rim)+vec3(.85,.93,1.)*(highlight*.65+lip*.2+edge*.3);"
            + "gl_FragColor=vec4(c,alpha);}";
  }
}
