# Drivo

Assignment 4 implementation adds:
- REST API module (`Api` tab) with `RecyclerView` display.
- SQLite local persistence using `SQLiteOpenHelper` with relational schema.
- Full local CRUD for vehicles + maintenance logs.
- SQL-based search, filter, and sort from local DB.
- Dashboard header scroll fix (header stays fixed).

## API Configuration (Pakistan Fuel API)

Update these two values in `app/build.gradle.kts`:
- `FUEL_API_BASE_URL` -> base domain ending with `/`
- `FUEL_API_ENDPOINT` -> endpoint path

Then sync Gradle and run the app. API calls are wired through:
- `app/src/main/java/com/example/drivo/data/remote/RetrofitProvider.kt`
- `app/src/main/java/com/example/drivo/data/remote/FuelApiService.kt`
- `app/src/main/java/com/example/drivo/fragments/ApiFeedFragment.kt`

## Local Database

SQLite helper and repositories:
- `app/src/main/java/com/example/drivo/data/local/DrivoDbHelper.kt`
- `app/src/main/java/com/example/drivo/data/repository/VehicleRepository.kt`

Tables:
- `vehicles` (autoincrement primary key)
- `maintenance_logs` (autoincrement primary key + foreign key to `vehicles.id`)

