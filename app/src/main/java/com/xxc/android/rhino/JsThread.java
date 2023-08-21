package com.xxc.android.rhino;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.faendir.rhino_android.RhinoAndroidHelper;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.NativeJSON;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrapFactory;
import org.mozilla.javascript.commonjs.module.Require;
import org.mozilla.javascript.commonjs.module.RequireBuilder;
import org.mozilla.javascript.commonjs.module.provider.SoftCachingModuleScriptProvider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okio.ByteString;

/**
 * js thread
 */
public class JsThread extends HandlerThread implements Handler.Callback {

    public static final String TAG = "JsThread";

    /**
     * init environment message id
     */
    public static final int INIT_JS_ENVIRONMENT = 1;
    private static final JsThread INSTANCE = new JsThread();
    private Context mJsContext;

    public static JsThread getInstance() {
        return INSTANCE;
    }

    private static String tracePath() {
        android.content.Context context = AndroidContextHolder.getContext();
        File cacheDir = context.getExternalCacheDir();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date = format.format(System.currentTimeMillis());
        return cacheDir.getAbsolutePath() + File.separator + date + ".trace";
    }

    public static void init() {
        getInstance().start();
    }

    private Handler mWorkHandler;
    private Scriptable mSharedScript;

    private JsThread() {
        super("Js-Runner");
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        initHandler();
    }

    public Handler getWorkHandler() {
        return mWorkHandler;
    }

    private void initHandler() {
        if (null == mWorkHandler) {
            mWorkHandler = new Handler(Looper.myLooper(), this);
        }
        mWorkHandler.sendEmptyMessage(INIT_JS_ENVIRONMENT);
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        if (msg.what == INIT_JS_ENVIRONMENT) {
            mSharedScript = initJsEnvironment();
            return true;
        }
        return false;
    }

    private Scriptable initJsEnvironment() {
        if (null != mSharedScript) {
            return mSharedScript;
        }
        mJsContext = new RhinoAndroidHelper(AndroidContextHolder.getContext()).enterContext();
        mJsContext.setOptimizationLevel(-1);
        mJsContext.setLanguageVersion(Context.VERSION_ES6);
        mJsContext.setLocale(Locale.getDefault());
        mJsContext.setWrapFactory(new WrapFactory());
        ImporterTopLevel scope = new ImporterTopLevel();
        scope.initStandardObjects(Context.getCurrentContext(), false);
        initRequireModule(scope);

        putProperty(scope, "console", new ConsoleImpl());
        return scope;
    }

    private void putProperty(Scriptable scope, String name, Object value) {
        ScriptableObject.putProperty(scope, name, Context.javaToJS(value, scope));
    }

    private void initRequireModule(Scriptable scope) {
        File file = new File("/");
        List<URI> urls = new ArrayList<>();
        urls.add(file.toURI());
        AssetAndUrlModuleSourceProvider provider = new AssetAndUrlModuleSourceProvider("modules", urls);
        Require require = new RequireBuilder()
                .setModuleScriptProvider(new SoftCachingModuleScriptProvider(provider))
                .setSandboxed(true)
                .createRequire(Context.getCurrentContext(), scope);
        require.install(scope);
    }

    public void invokeJs(String fName, List<Object> arguments, JsResultListener listener) {
        mWorkHandler.post(() -> {
            Context context = Context.getCurrentContext();
            Scriptable scriptable = context.newObject(mSharedScript);
            scriptable.setPrototype(mSharedScript);
            scriptable.setParentScope(null);

            try {
                readJs(scriptable, listener);
            } catch (Exception e) {
                e.printStackTrace();
                callJsResult(e, null, listener);
            }
        });
    }

    private void readJs(Scriptable scriptable, JsResultListener listener) throws Exception {
        android.content.Context appContext = AndroidContextHolder.getContext();
        InputStream reader = new BufferedInputStream(appContext.getAssets().open("test.js"));
        ByteString read = ByteString.read(reader, reader.available());
        Context scriptContext = Context.getCurrentContext();
        Object result = scriptContext.evaluateString(scriptable, read.utf8(), "test.js", 1, null);
        String jsonResult = NativeJSON.stringify(scriptContext, scriptable, result, null, null).toString();

        callJsResult(null, jsonResult, listener);
    }

    private void callJsResult(Exception error, String jsResult, JsResultListener listener) {
        try {
            if (listener != null) {
                listener.onResult(jsResult, error);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void start() {
        if (getInstance().mWorkHandler != null) {
            return;
        }
        super.start();
    }
}
