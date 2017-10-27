package org.mbds.barcodebattler.util;

import org.mbds.barcodebattler.CreatureGeneratorActivity;

import dagger.Component;

@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(CreatureGeneratorActivity activity);
}
