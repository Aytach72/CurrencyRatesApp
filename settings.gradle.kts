pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal() // <-- ЭТА СТРОКА ОБЯЗАТЕЛЬНА ДЛЯ HILT
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CurrencyRatesApp"
include(":app")