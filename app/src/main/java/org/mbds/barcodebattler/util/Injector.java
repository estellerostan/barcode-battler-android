package org.mbds.barcodebattler.util;

public class Injector { // c'est auprès de ce singleton que CreatureGeneratorActivity demandera ses dépendances
    private static Injector ourInstance = new Injector();
    private AppComponent appComponent;

    private Injector() {
    }

    public static Injector getInstance() {
        return ourInstance;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    void setAppComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
    }
}