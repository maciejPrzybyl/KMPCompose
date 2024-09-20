package org.macpry.kmpcompose.di

import me.tatarka.inject.annotations.KmpComponentCreate
import org.macpry.kmpcompose.TimeManager
import org.macpry.kmpcompose.TimeProvider
import kotlin.reflect.KClass

public fun KClass<AppComponent>.create(): AppComponent = InjectKMPAppComponent()

public class InjectKMPAppComponent() : AppComponent() {
    override val timeManager: TimeManager
        get() = TimeManager(
            timeProvider = TimeProvider()
        )
}

@KmpComponentCreate
public actual fun createAppComponent(): AppComponent = AppComponent::class.create()