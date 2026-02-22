# Проект "Sphere"

Это простое Android-приложение, в котором сфера подпрыгивает при нажатии на экран. Проект демонстрирует базовую структуру Android-приложения, включая работу с несколькими экранами (Activity), анимацию и кастомизацию интерфейса с использованием Material Design 3.

## Зависимости и версии

Проект использует централизованное управление версиями через `gradle/libs.versions.toml`.

- **Плагины**:
    - `com.android.application`: `9.0.1`
- **Библиотеки**:
    - `androidx.core:core-ktx`: `1.10.1`
    - `androidx.appcompat:appcompat`: `1.6.1`
    - `com.google.android.material:material`: `1.13.0`

## Сборка проекта

Для работы с проектом из командной строки можно использовать Gradle Wrapper (`gradlew`).

### Debug-сборка (для разработки)

- **Очистка проекта** (удаление всех сгенерированных файлов):
  ```bash
  ./gradlew clean
  ```

- **Сборка Debug-версии APK**:
  ```bash
  ./gradlew assembleDebug
  ```

- **Установка Debug-версии на подключенное устройство**:
  ```bash
  ./gradlew installDebug
  ```

### Release-сборка (для публикации)

#### 1. Создание ключа подписи (Keystore)

Для публикации в Google Play необходимо подписать приложение уникальным цифровым ключом. Если у вас его еще нет, его можно сгенерировать с помощью `keytool` (входит в состав Java Development Kit).

```bash
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-alias
```

Эта команда запросит у вас пароли для хранилища ключей и для самого ключа, а также информацию для сертификата. **Обязательно сохраните этот файл и пароли в надежном месте!**

#### 2. Настройка Gradle для использования ключа

Никогда не храните пароли в `build.gradle.kts`! Безопасный способ — вынести их в файл `gradle.properties`, который не должен попадать в систему контроля версий (добавьте его в `.gitignore`).

1.  **Поместите `my-release-key.jks`** в папку `app` вашего проекта.

2.  **Добавьте следующие строки** в файл `gradle.properties` (если файла нет, создайте его в корневой директории проекта):
    ```properties
    MYAPP_RELEASE_STORE_FILE=my-release-key.jks
    MYAPP_RELEASE_STORE_PASSWORD=ваш_пароль_от_хранилища
    MYAPP_RELEASE_KEY_ALIAS=my-alias
    MYAPP_RELEASE_KEY_PASSWORD=ваш_пароль_от_ключа
    ```

3.  **Настройте `signingConfigs`** в файле `app/build.gradle.kts`:

    ```kotlin
    android {
        // ...
        signingConfigs {
            create("release") {
                storeFile = file(System.getenv("MYAPP_RELEASE_STORE_FILE") ?: "my-release-key.jks")
                storePassword = System.getenv("MYAPP_RELEASE_STORE_PASSWORD")
                keyAlias = System.getenv("MYAPP_RELEASE_KEY_ALIAS")
                keyPassword = System.getenv("MYAPP_RELEASE_KEY_PASSWORD")
            }
        }

        buildTypes {
            release {
                isMinifyEnabled = true
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
                signingConfig = signingConfigs.getByName("release")
            }
        }
        // ...
    }
    ```

#### 3. Сборка и установка

- **Сборка Release-версии APK**:
  ```bash
  ./gradlew assembleRelease
  ```

- **Установка Release-версии на подключенное устройство**:
  Собранный APK-файл (`app-release.apk`) будет находиться в папке `app/build/outputs/apk/release/`. Установить его можно с помощью `adb`:
  ```bash
  adb install app/build/outputs/apk/release/app-release.apk
  ```

## Структура Проекта

Ниже приведено описание основных файлов и директорий проекта.

### Корневая директория (`/`)

- **`settings.gradle.kts`**: Скрипт Gradle, который определяет имя корневого проекта (`sphere`) и включает в сборку все необходимые модули (в нашем случае только `:app`).
- **`build.gradle.kts`**: Скрипт Gradle для всего проекта. В современных проектах он обычно используется для объявления плагинов, общих для всех модулей.

### Модуль приложения (`/app`)

Это основной модуль, содержащий весь код и ресурсы приложения.

- **`build.gradle.kts`**: Скрипт сборки для модуля `:app`. Здесь указываются:
    - `applicationId`: Уникальный идентификатор приложения (`com.sphere`).
    - `minSdk` / `targetSdk`: Минимальная и целевая версии Android.
    - `dependencies`: Список всех библиотек, от которых зависит приложение (например, `appcompat`, `material`).

- **`/src/main`**: Директория с основным исходным кодом.

    - **`AndroidManifest.xml`**: "Паспорт" приложения. Объявляет все его компоненты (экраны, сервисы), запрашиваемые разрешения, а также задает иконку, название и тему.

    - **`/java/com/sphere`**: Исходный код приложения на языке Kotlin.
        - **`MenuActivity.kt`**: Код для главного экрана. Отвечает за логику кнопок "Играть" (запускает `MainActivity`) и "Выход" (закрывает приложение).
        - **`MainActivity.kt`**: Код для игрового экрана. Отвечает за анимацию прыжка сферы и обработку нажатия кнопки "Назад".

    - **`/res`**: Директория со всеми ресурсами приложения.
        - **`/drawable`**: Векторные изображения и фигуры.
            - `sphere_drawable.xml`: Описание красного круга, который используется как сфера.
            - `ic_launcher_background.xml`: XML-файл, определяющий фон для адаптивной иконки приложения.
            - `ic_launcher_foreground.xml`: XML-файл, определяющий передний план (саму сферу) для адаптивной иконки.

        - **`/layout`**: XML-файлы с разметкой пользовательского интерфейса.
            - `activity_menu.xml`: Разметка для главного экрана с двумя кнопками.
            - `activity_main.xml`: Разметка для игрового экрана со сферой, полом и кнопкой "Назад".

        - **`/mipmap`**: Растровые иконки приложения для разных разрешений экрана. Включает в себя `ic_launcher.xml` и `ic_launcher_round.xml`, которые объединяют фон и передний план для создания адаптивной иконки.

        - **`/values`**: XML-файлы с различными значениями.
            - `colors.xml`: Цветовая палитра приложения.
            - `strings.xml`: Все текстовые строки, используемые в приложении. Это позволяет легко переводить приложение на другие языки.
            - `themes.xml`: Определение основной темы приложения в стиле Material 3.

        - **`/xml`**: Прочие конфигурационные XML-файлы.
            - `backup_rules.xml` & `data_extraction_rules.xml`: Файлы для настройки правил резервного копирования. В данном проекте они не сконфигурированы.
