@file:Suppress("ClassName")

package com.example.movieapp

object versions {
    const val kotlin = "1.5.30"
    const val coroutines = "1.5.1"
    const val compose = "1.1.0-alpha04"
    const val navigation = "2.4.0-alpha08"
    const val accompanist = "0.15.0"
    const val koin = "3.1.2"
    const val ktor_client = "1.6.3"
    const val logback = "1.2.5"
    const val lifecycle = "2.4.0-alpha03"
    const val ktx_datetime = "0.2.1"
    const val ktx_serialization = "1.2.2"
    const val coil = "1.3.2"
    const val room = "2.4.0-alpha01"
    const val paging = "3.0.0"
}

object dep {

    const val legacy_support = "androidx.legacy:legacy-support-v4:1.0.0"
    const val material = "com.google.android.material:material:1.4.0"
    const val coil = "io.coil-kt:coil-compose:${versions.coil}"
    const val junit = "junit:junit:4.13.2"

    object coroutines {
        private const val version = versions.coroutines
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object androidX {
        const val core_ktx = "androidx.core:core-ktx:1.6.0"
        const val appcompat = "androidx.appcompat:appcompat:1.3.1"

        object activity {
            const val compose = "androidx.activity:activity-compose:1.3.1"
        }

        object compose {
            private const val version = versions.compose

            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val layout = "androidx.compose.foundation:foundation-layout:$version"

            const val material = "androidx.compose.material:material:$version"
            const val material_icons_core = "androidx.compose.material:material-icons-core:$version"
            const val material_icons_extended = "androidx.compose.material:material-icons-extended:$version"

            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val tooling = "androidx.compose.ui:ui-tooling:$version"
            const val ui_util = "androidx.compose.ui:ui-util:$version"
            /*const val ui_ui = "androidx.compose.ui:ui:$version"*/
            const val view_binding = "androidx.compose.ui:ui-viewbinding:$version"

            const val test = "androidx.compose.ui:ui-test:$version"
            const val ui_test = "androidx.compose.ui:ui-test-junit4:$version"
        }

        object navigation {
            private const val version = versions.navigation
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val ui_ktx = "androidx.navigation:navigation-ui-ktx:$version"
            const val compose = "androidx.navigation:navigation-compose:$version"
            const val test = "androidx.navigation:navigation-testing:$version"

            // Feature module Support
            /*"androidx.navigation:navigation-dynamic-features-fragment:$navigation"
            "androidx.legacy:legacy-support-v4:1.0.0"*/
        }

        object paging {
            private const val version = versions.paging
            const val runtime = "androidx.paging:paging-runtime-ktx:$version"
            const val compose = "androidx.paging:paging-compose:1.0.0-alpha12"
            const val common = "androidx.paging:paging-compose:1.0.0-alpha12"
            const val test = "androidx.paging:paging-common-ktx:$version"
        }

        object lifecycle {
            private const val version = versions.lifecycle
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
            const val view_model = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val view_model_compose = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07"
        }

        object test {
            private const val version = "1.3.0"
            const val core = "androidx.test:core:$version"
            const val rules = "androidx.test:rules:$version"

            object ext {
                private const val version = "1.1.3"
                const val junit = "androidx.test.ext:junit-ktx:$version" /*"androidx.test.ext:junit:1.1.3"*/
            }

            const val espresso_core = "androidx.test.espresso:espresso-core:3.4.0"
        }
    }

    object room {
        private const val version = versions.room
        const val room_ktx = "androidx.room:room-ktx:$version"
        const val room_test= "androidx.room:room-testing:$version"
        const val room_compiler = "androidx.room:room-compiler:$version"
    }

    object accompanist {
        private const val version = versions.accompanist
        const val insets = "com.google.accompanist:accompanist-insets:$version"
    }

    object koin {
        private const val version = versions.koin
        const val android = "io.insert-koin:koin-android:$version"
        const val test = "io.insert-koin:koin-test:$version"
        const val android_test = "io.insert-koin:koin-test-junit4:$version"
    }

    object kt_extended {
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${versions.ktx_serialization}"
        const val date_time = "org.jetbrains.kotlinx:kotlinx-datetime:${versions.ktx_datetime}"
    }

    object ktor_client {
        private const val version = versions.ktor_client
        const val okhttp = "io.ktor:ktor-client-okhttp:$version"
        const val logging = "io.ktor:ktor-client-logging:$version"
        const val serializer = "io.ktor:ktor-client-serialization:$version"
        const val logback = "ch.qos.logback:logback-classic:${versions.logback}"
        const val mock = "io.ktor:ktor-client-mock:$version"
    }
}