## 🏛️ 1. Architecture & Tech Stack

เราใช้สถาปัตยกรรม **Clean Architecture** ร่วมกับ **MVVM** เพื่อแบ่งแยกส่วนการทำงานอย่างชัดเจน ทำให้โค้ดสามารถทดสอบได้ง่ายและมีความยืดหยุ่นสูง

-   **Presentation Layer**: **MVVM (Model-View-ViewModel)**
-   **UI Framework**: **Jetpack Compose** 
-   **Dependency Injection**: **Koin**
-   **Asynchronous**: **Kotlin Coroutines & Flow**
-   **Network**: **Apollo GraphQL** (พร้อม Normalized Caching)
-   **Local Database**: **Room**
-   **Testing**: **MockK** (Unit Tests) & **Compose Test** (UI Tests)

---

## 📁 2. Project Structure & Naming

### Module Structure

-   `app`: โมดูลหลักสำหรับ UI และเป็นจุดเริ่มต้นของแอปพลิเคชัน
-   `repository`: โมดูลสำหรับจัดการ Data Source (Interfaces)
-   `chawan.fame.testmvvm`: แพ็กเกจหลักของแอปพลิเคชัน (Data, Domain, Presentation Layers)
-   `chawan.fame.testmvvm.database`: แพ็กเกจสำหรับจัดการ Local Database (Room)

### Package Structure

```
chawan.fame.testmvvm/
├── data/
│   └── repositories/         # Repository implementations (*RepositoryImpl.kt)
├── domain/
│   └── usecases/            # Business logic classes (*UseCase.kt)
└── presentation/
├── ui/
│   ├── <feature_name>/    # เช่น home, profile, bundle_detail
│   │   ├── composable/      # Screen & UI components
│   │   ├── model/           # UI Models (*UiModel.kt)
│   │   ├── viewmodel/       # ViewModels (*ViewModel.kt)
│   │   └── mapper/          # Mappers to convert Domain to UI model
│   └── base/              # Base UI components (Button, TextField, etc.)
└── extension/             # Extension functions

chawan.fame.testmvvm.database/
├── database/                # Database setup & configurations
├── model/                   # Room Entities (*Entity.kt)
├── query/                   # Room DAOs (*Dao.kt)
└── table/                   # Table definitions (if needed)

chawan.fame.testmvvm.repository.repo/
└── repositories/            # Repository interfaces (*Repository.kt)
```

### File Naming Conventions

| ประเภท | รูปแบบ | ตัวอย่าง |
| :--- | :--- | :--- |
| **Entities** | `[Name]Entity.kt` (Room), `[Name].kt` (Domain) | `UserEntity.kt`, `User.kt` |
| **DAOs** | `[Name]Dao.kt` | `UserDao.kt` |
| **Repositories** | `[Name]Repository.kt` (Interface), `[Name]RepositoryImpl.kt` (Impl) | `UserRepository.kt`, `UserRepositoryImpl.kt`|
| **Use Cases** | `[Action][Feature]UseCase.kt` | `GetUserUseCase.kt`, `SaveUserUseCase.kt` |
| **ViewModels** | `[Feature]ViewModel.kt` | `UserViewModel.kt` |
| **Screens** | `[Feature]Screen.kt` | `UserListScreen.kt` |
| **Components** | `[Name]Component.kt` or `[Name].kt` | `UserCard.kt`, `SearchBar.kt` |
| **Constants** | `SCREAMING_SNAKE_CASE` | `BASE_URL`, `MAX_RETRIES` |

---

## 💻 3. Development Guidelines

### Data Layer: Room Database

-   **Entity**: ออกแบบให้เป็น Data class ที่เรียบง่าย พร้อมกำหนด `tableName` และ `ColumnInfo` ให้ชัดเจนโดยใช้ค่าคงที่จาก `companion object`

```kotlin
@Entity(tableName = UserEntity.TABLE_NAME)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Long = 0,

    @ColumnInfo(name = COLUMN_USER_NAME)
    val userName: String
) {
    companion object {
        const val TABLE_NAME = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_USER_NAME = "user_name"
    }
}
```

-   **DAO**: สร้าง Interface สำหรับ Query โดยฟังก์ชันที่คืนค่าข้อมูลแบบ Real-time ให้ใช้ `Flow<T>`

```kotlin
@Dao
interface UserDao {
    @Query("SELECT * FROM ${UserEntity.TABLE_NAME}")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM ${UserEntity.TABLE_NAME} WHERE ${UserEntity.COLUMN_ID} = :id")
    suspend fun getUserById(id: Long): UserEntity?
}
```

### Domain Layer: Use Case

-   **Single Responsibility**: Use Case แต่ละคลาสควรรับผิดชอบ Business Logic เพียงอย่างเดียว
-   **Logic Hub**: คำนวณหรือประมวลผลข้อมูลที่จำเป็นสำหรับ UI ทั้งหมดที่นี่
-   **Model Mapping**: ใช้ Mapper เพื่อแปลง Domain Model เป็น UI Model ก่อนส่งไปยัง Presentation Layer

### Presentation Layer: Jetpack Compose & ViewModel & Activity & Fragment

#### การจัดการ State และ Event

-   **UI State**: ใช้ `sealed interface` เพื่อจำลองสถานะของ UI (Loading, Success, Error)
-   **ViewModel**:
-   ใช้ `viewModelScope` สำหรับ Coroutines ทั้งหมด
-   สร้างฟังก์ชันสำหรับรับ Event จาก UI เพื่อนำไปประมวลผล (e.g., `handleEvent(event: UserEvent)`)

```kotlin
// UI State
sealed interface UserUiState {
    object Loading : UserUiState
    data class Success(val user: UserUiModel) : UserUiState
    data class Error(val message: String) : UserUiState
}

// ViewModel
class UserViewModel(private val getUserUseCase: GetUserUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    fun loadUser(id: String) {
        viewModelScope.launch {
            //... call use case and update _uiState
        }
    }
}
```

#### โครงสร้าง Composable

-   **Screen Composable (Stateful)**: ทำหน้าที่เชื่อมต่อกับ ViewModel, สังเกตการณ์ State (`collectAsStateWithLifecycle`) และส่ง State กับ Event ไปยัง Content Composable
-   **Content Composable (Stateless)**: รับ State เพื่อแสดงผล และเรียกใช้ Event callback เท่านั้น (State Hoisting) ทำให้ง่ายต่อการ Preview และ Test
-   **Preview**: ต้องสร้าง Preview ให้ครอบคลุมทุกสถานการณ์:
-   `@PhoneDayPreview`, `@PhoneDarkPreview`
-   `@TabletDayPreview`, `@TabletDarkPreview`
-   Preview สำหรับทุก State ของ UI (Loading, Success, Error)
-   **Shimmer Effect**: ทุก Composable ที่มีการโหลดข้อมูลต้องมี Shimmer UI ของตัวเองเพื่อประสบการณ์ใช้งานที่ดี

```kotlin
// Screen Composable
@Composable
fun UserScreen(viewModel: UserViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    UserContent(uiState = uiState)
}

// Content Composable (Stateless)
@Composable
private fun UserContent(uiState: UserUiState) {
    when (uiState) {
        is UserUiState.Loading -> UserShimmer() // Shimmer UI
        is UserUiState.Success -> UserDetails(user = uiState.user)
        is UserUiState.Error -> ErrorMessage(message = uiState.message)
    }
}
```

---

## 🛡️ 4. Error Handling

ใช้ `Result` Wrapper เพื่อจัดการสถานะของ Operation (Success, Error, Loading) อย่างเป็นระบบ และสร้างคลาส Exception เฉพาะตามประเภทของข้อผิดพลาด

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

// Custom Exception Hierarchy
sealed class AppException(error: String) : BaseException(message)

open class BaseException : RuntimeException {
    constructor() : super()
    constructor(cause: String) : super(cause)
}
class ApolloException(error: String) : BaseException(error)
class UnAuthorizeException : BaseException("require login")
class NoInternetException : BaseException("no internet")
```

---

## 🧪 5. Testing Strategy

### Unit Tests (ViewModel & Use Case)

-   ใช้ **MockK** เพื่อจำลอง Dependencies
-   ทดสอบ Logic การทำงานและ State ที่เปลี่ยนแปลงไปตาม Input
-   ตั้งชื่อเทสให้สื่อความหมาย: `[UnitOfWork]_[StateUnderTest]_[ExpectedBehavior]`
-   `fun loadUser_validId_returnsSuccessState()`

```kotlin
@Test
fun `loadUser with valid id returns success state`() = runTest {
    // Given
    val user = User(id = "1", name = "John Doe")
    coEvery { getUserUseCase("1") } returns flowOf(Result.Success(user))

    // When
    val viewModel = UserViewModel(getUserUseCase)
    viewModel.loadUser("1")

    // Then
    // Assert that viewModel.uiState transitions to Success
}
```

### UI Tests (Jetpack Compose)

-   ใช้ `createComposeRule()` เพื่อทดสอบ UI Component
-   ทดสอบว่า UI แสดงผลถูกต้องตาม State ที่กำหนด
-   ยืนยันการมีอยู่และการโต้ตอบของ Component

---

## ✨ 6. Code Quality & Best Practices

### Do (ควรทำ) ✅

-   **Clarity**: เขียนโค้ดให้ชัดเจน อ่านง่าย ใช้ expression bodies สำหรับฟังก์ชันสั้นๆ
-   **Immutability**: ใช้ `val` และ Immutable Collections ให้มากที่สุด
-   **Type Safety**: ระบุ Return Type ของ Public API ให้ชัดเจน
-   **DI**: ใช้ Koin ในการ Inject Dependencies ทั้งหมด
-   **UI Model**: สร้าง UI Model และ Mapper แยกสำหรับแต่ละฟีเจอร์เสมอ
-   **Events**: ใช้ `sealed class` สำหรับ Events ที่ส่งจาก UI ไปยัง ViewModel

### Don't (ควรเลี่ยง) ❌

-   **No Business Logic in UI**: อย่าใส่ Business Logic ใน Composable หรือ Activity/Fragment
-   **No MutableState Exposure**: อย่าเปิดเผย `MutableStateFlow` จาก ViewModel สู่ภายนอก
-   **Avoid `lateinit var`**: หลีกเลี่ยงการใช้ `lateinit var` ใน ViewModel เพราะเสี่ยงต่อ `UninitializedPropertyAccessException`
-   **Avoid `GlobalScope`**: ห้ามใช้ `GlobalScope` ให้ใช้ Scope ที่เหมาะสมกับ Lifecycle เช่น `viewModelScope`

---

## 🚀 7. Contribution Workflow

### Git Flow

1.  **Branching**: รูปแบบชื่อ branch:
-   `feature/<feature-name>`
-   `bugfix/<bug-name>`
-   `hotfix/<hotfix-name>`
2.  **Commit Messages**: เขียน Commit Message ให้สื่อความหมายตามรูปแบบ Conventional Commits
-   `feat: add user profile screen`
-   `fix: correct login validation error`
-   `refactor: improve user repository performance`
-   `docs: update readme file`
3.  **Pull Request (PR)**: เมื่อทำเสร็จแล้ว ให้สร้าง PR ไปยัง `develop` พร้อมอธิบายการเปลี่ยนแปลงและ Tag ผู้เกี่ยวข้องเพื่อ Review

### ✅ Pre-Commit Checklist

ก่อนทำการ `commit` และสร้าง `PR` **ต้องรันคำสั่งต่อไปนี้เสมอ** เพื่อให้แน่ใจว่าโค้ดมีคุณภาพและไม่ทำให้โปรเจกต์เสียหาย

```bash
# รัน ktlint check และ detekt เพื่อตรวจสอบคุณภาพโค้ด
./gradlew check

# รัน Unit Tests ทั้งหมด
./gradlew test

# (Optional) รัน Connected Tests (UI Tests)
./gradlew connectedCheck
```

---

## 🎲 Lottery Analysis Engine

This project now includes a simple prediction engine located in `co.storylog.pinto.domain.analysis`. It provides functions for:

- Calculating digit frequencies across historical results.
- Determining hot and cold numbers based on recent draws.
- Finding the most common two-digit pairs.
- Generating naive predictions for two and three digit prizes.

Unit tests demonstrating the usage of these functions can be found in `app/src/test/java/co/storylog/pinto/domain/analysis`.

### New Sample Implementation

A simple Compose UI demonstrates a clean separation between layers:

- **Repository** – `FakeLotteryRepository` supplies lottery data.
- **UseCase** – `GeneratePredictionsUseCase` performs analysis and exposes predictions.
- **ViewModel** – `LotteryViewModel` holds UI state and triggers the use case.
- **UI** – `LotteryScreen` renders the predictions using Jetpack Compose.

See `MainActivity` for how these pieces are wired together.
