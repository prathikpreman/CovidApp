package com.covidapp

import android.app.Application
import android.os.Bundle
import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.regex.Pattern

class CovidApp : Application(){

    override fun onCreate() {
        super.onCreate()

        Realm.init(applicationContext)
        val config = RealmConfiguration.Builder()
            .name("covdiApp.realm")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(config)


        Realm.init(applicationContext)

        Stetho.initialize(
            Stetho.newInitializerBuilder(applicationContext)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(applicationContext))
                .enableWebKitInspector(RealmInspectorModulesProvider.builder(applicationContext).build())
                .build())

        RealmInspectorModulesProvider.builder(this)
            .withFolder(cacheDir)
            .withMetaTables()
            .withDescendingOrder()
            .withLimit(1000)
            .databaseNamePattern(Pattern.compile(".+\\.realm"))
            .build()
    }
}
