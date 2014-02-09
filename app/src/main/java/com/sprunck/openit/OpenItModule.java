package com.sprunck.openit;

import android.content.Context;
import com.sprunck.openit.activity.CommandFragment;
import com.sprunck.openit.activity.LoginActivity;
import com.sprunck.openit.activity.MainActivity;
import com.sprunck.openit.api.OpenItApi;
import com.sprunck.openit.api.VolleyOpenItApi;
import com.sprunck.openit.session.Session;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Dagger module that define how to satisfy dependencies.
 *
 * @author Matthieu Sprunck
 */
@Module(
        injects = {
                MainActivity.class,
                LoginActivity.class,
                CommandFragment.class
        },
        library = true,
        complete = false
)
public class OpenItModule {
    /**
     * Application context.
     */
    private final Context context;

    /**
     * Constructor
     *
     * @param context The application context
     */
    public OpenItModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context provideApplicationContext() {
        return this.context;
    }

    @Singleton
    @Provides
    public OpenItApi provideOpenItApi() {
        return new VolleyOpenItApi(context);
    }

    @Singleton
    @Provides
    public Session provideSession() {
        return new Session(context);
    }
}
