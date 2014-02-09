package com.sprunck.openit;

import android.app.Application;
import dagger.ObjectGraph;

import java.util.Arrays;
import java.util.List;

/**
 * Base class to maintain global application state.
 *
 * @author Matthieu Sprunck
 */
public class ApplicationController extends Application {
    /**
     * Log or request TAG
     */
    public static final String TAG = ApplicationController.class.getSimpleName();

    /**
     * The injection object graph.
     */
    private ObjectGraph graph;

    @Override
    public void onCreate() {
        super.onCreate();
        Object[] modules = getModules().toArray();
        graph = ObjectGraph.create(modules);
    }

    /**
     * Retrieves all dagger modules.
     *
     * @return a list of dagger modules
     */
    List<Object> getModules() {
        return Arrays.<Object>asList(
                new OpenItModule(this)
        );
    }

    /**
     * Injects dependency to the provided object.
     *
     * @param object the object to inject
     */
    public void inject(Object object) {
        graph.inject(object);
    }
}