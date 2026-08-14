# idealista Android Challenge 🏠

Android application developed as a response to the **idealista Android Challenge**: an app that allows users to browse a listing of real estate ads and view the detail of each one, fulfilling the minimum requirements of the challenge and going further with several additional features (search, favorites filter, sorting, persistence, and Jetpack Compose components).

> *"idealista Android crew needs you! We need a fellow to face our everyday challenges..."* — This is the result of accepting that challenge. 🚀

## 📱 Features

- Ads listing screen with image, price, location and key characteristics
- Detail screen with extended information
- Mark/unmark ads as favorites, with the date saved shown
- Favorites persistence across sessions (Room)
- Search by area or address
- Instant favorites filter
- Price sorting (ascending/descending)
- Pull-to-refresh on the listing
- Loading (skeleton), empty and error states on both screens

## 🛠 Tech stack

| Area | Technology |
|---|---|
| Language | Kotlin |
| UI | XML Views + Jetpack Compose (integrated selectively) |
| Architecture | MVVM |
| Dependency injection | Hilt |
| Networking | Retrofit + OkHttp + Gson |
| Persistence | Room |
| Navigation | Navigation Component + Safe Args |
| Concurrency | Coroutines + Flow (StateFlow) |
| Image loading | Coil |
| Tests | JUnit + Coroutines Test |

## 🏗 Architecture

The project follows an MVVM architecture with separation by layers:
data/
├── remote/ → DTOs, ApiService, Retrofit client
├── local/ → Room entity and DAO (favorites)
├── mapper/ → DTO → domain model transformation
└── repository/ → Repository implementation

domain/
├── model/ → Domain models (AdModel, AdDetailModel)
└── repository/ → Repository interface (contract)

ui/
├── list/ → Listing screen (Fragment, ViewModel, Adapter)
├── detail/ → Detail screen (Fragment, ViewModel)
└── common/ → Reusable components (header and skeletons in Compose, UiState)

di/ → Hilt module
util/ → Constants and utilities (date format, locale)

### Design decisions

- **Single-Activity + Navigation Component**: a single `MainActivity` hosts a `NavHostFragment`; the listing and detail screens are fragments navigated with Safe Args.
- **Repository pattern with interface in `domain/`**: the ViewModel depends on the abstraction (`AdsRepository`), not on the concrete implementation, following the dependency inversion principle.
- **Sealed `UiState<T>`** (`Loading` / `Success` / `Error`) shared between the listing and detail screens to handle the different UI states consistently.
- **Favorites filter as an instant filter, not a separate screen**: instead of a dedicated favorites screen, the listing combines search, favorites filter and sorting over the data already loaded in memory (`combine()` of several `StateFlow` in the ViewModel), avoiding extra network calls and simplifying navigation.
- **Compose integrated into an XML-based app**: the header (with an animated search field) and the *skeleton* loading states are built in Jetpack Compose and integrated via `ComposeView`, as a demonstration of interoperability between both UI systems.

## ⚠️ Note on the detail endpoint

The `detail.json` endpoint of the challenge API **always returns the same ad** (`adid: 1`), regardless of which ad was selected from the listing. This is documented behavior of the challenge's mock API, not a bug in this implementation — it is flagged as a comment in `IdealistaApiService.kt`.

For the favorites feature to work correctly despite this limitation, marking an ad as favorite in the detail screen uses the `id` received via navigation (the one the user tapped in the listing), not the `id` returned by the detail endpoint itself — this keeps the favorite correctly associated with the right ad and in sync with the listing.

## 🤖 Use of AI in development

This project was developed through iterative conversation with Claude (Anthropic's chat interface) throughout the entire process: architecture design, Gradle configuration, resolving build errors, UI/UX decisions, feature implementation, and final code refactoring.

Claude Code and no persistent context file (`CLAUDE.md`, `.cursorrules`, etc.) were used, as these tools require a paid subscription that wasn't considered necessary for the scope of this technical challenge. The project's context was maintained throughout the chat conversation itself rather than through a dedicated context file.

## ✅ Tests

The project includes unit tests covering the layers with non-trivial logic:

- **`AdMapperTest`** — DTO to domain model transformation, including optional field fallbacks
- **`DateFormatterTest`** — favorite date formatting
- **`AdsListViewModelTest`** — combined search, favorites filter and sorting
- **`AdsRepositoryImplTest`** — ad deduplication and favorite toggle logic

Fake implementations of dependencies were used instead of heavy mocks, prioritizing test readability and maintainability.

## 🚀 How to run the project

1. Clone the repository:
```bash
   git clone https://github.com/prvaca/idealista-android-challenge.git
```
2. Open it with Android Studio (recommended version: latest stable available)
3. Sync the project with Gradle
4. Run the app on an emulator or physical device with Android 6.0 (API 23) or higher

No additional configuration is required (API keys, environment variables, etc.) — the app consumes the challenge's public endpoints directly.

### Running the tests

```bash
./gradlew test
```

## 📌 Challenge scope

### Minimum requirements
- ✅ Ads listing and detail screens
- ✅ Kotlin + XML Views
- ✅ Favorites with visible date
- ✅ Use of AI tools during development

### Bonus implemented
- ✅ Jetpack Compose alongside XML
- ✅ Persistent storage (Room)
- ✅ Tests
- ✅ Additional features: search, favorites filter, sorting, pull-to-refresh, skeleton loading states, coherent visual redesign

