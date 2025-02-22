package moe.sigma;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



@SuppressLint("PrivateApi")
public class SyncUtils {
    private static final Map<Long, Collection<String>> sTlsFlags = new HashMap<>();
    private static Handler sHandler;
    private static final ExecutorService sExecutor = Executors.newCachedThreadPool();
    private SyncUtils() {
        throw new AssertionError("No instance for you!");
    }



    public static void runOnUiThread(@NonNull Runnable r) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            r.run();
        } else {
            post(r);
        }
    }

    public static void async(@NonNull Runnable r) {
        sExecutor.execute(r);
    }

    @SuppressLint("LambdaLast")
    public static void postDelayed(@NonNull Runnable r, long ms) {
        if (sHandler == null) {
            sHandler = new Handler(Looper.getMainLooper());
        }
        sHandler.postDelayed(r, ms);
    }

    public static void postDelayed(long ms, @NonNull Runnable r) {
        postDelayed(r, ms);
    }

    public static void post(@NonNull Runnable r) {
        postDelayed(r, 0L);
    }

    public static void requiresUiThread() {
        requiresUiThread(null);
    }

    public static void requiresUiThread(@Nullable String msg) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException(msg == null ? "UI thread required" : msg);
        }
    }

    public static void requiresNonUiThread() {
        requiresNonUiThread(null);
    }

    public static void requiresNonUiThread(@Nullable String msg) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException(msg == null ? "non-UI thread required" : msg);
        }
    }
}
