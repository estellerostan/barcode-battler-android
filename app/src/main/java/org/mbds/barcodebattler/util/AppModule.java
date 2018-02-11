package org.mbds.barcodebattler.util;

import org.mbds.barcodebattler.data.Creature;
import org.mbds.barcodebattler.data.ICreature;

import dagger.Module;
import dagger.Provides;

@Module
class AppModule {

    @Provides
    ICreature providesCreature() {
        return new Creature("", "", 0, 0, 0, "", "");
    }
}
