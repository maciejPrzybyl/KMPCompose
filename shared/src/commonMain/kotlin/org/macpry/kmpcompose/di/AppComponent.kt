package org.macpry.kmpcompose.di

import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.KmpComponentCreate
import org.macpry.kmpcompose.TimeManager

@Component
abstract class AppComponent {
    abstract val timeManager: TimeManager
}

@KmpComponentCreate
expect fun createAppComponent(): AppComponent
