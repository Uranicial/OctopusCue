pluginManagement {
    //这里声明的仓库就是 Gradle 查找这些插件的位置
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    //集中管理项目使用的依赖仓库
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "OctopusCue"
//这是整个 Gradle 工程的名称
//工程包含相应模块路径
include(":app")

include(":core:model")
include(":core:contracts")
include(":domain:engine")
include(":data:local")