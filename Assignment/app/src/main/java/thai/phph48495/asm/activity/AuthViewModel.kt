package thai.phph48495.asm.activity

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import thai.phph48495.asm.profile.User

class AuthViewModel: ViewModel() {
    //lay du lieu tu server
    private val authService = AuthService.getInstance()
    private val _listUser = mutableStateOf<List<User>?>(null)
    val listUser: State<List<User>?> = _listUser
    
    // Login state
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState
    
    // Register state
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState
    
    // User state
    private val _currentUser = mutableStateOf<User?>(null)
    val currentUser: State<User?> = _currentUser
    
    fun getAllUsers() {
        viewModelScope.launch {
            try {
                _listUser.value = authService.getUsers()
            } catch (e: Exception) {
                Log.d("zzzzz", e.message.toString())
            }
        }
    }
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _loginState.value = LoginState.Loading
                val users = authService.getUserByEmail(email)
                if (users.isNotEmpty()) {
                    val user = users.first()
                    if (user.password == password) {
                        _currentUser.value = user
                        _loginState.value = LoginState.Success(user)
                    } else {
                        _loginState.value = LoginState.Error("Mật khẩu không chính xác")
                    }
                } else {
                    _loginState.value = LoginState.Error("Email không tồn tại")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Lỗi không xác định")
                Log.d("LoginError", e.message.toString())
            }
        }
    }
    
    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }
    
    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _registerState.value = RegisterState.Loading
                
                // Kiểm tra email đã tồn tại chưa
                val existingUsers = authService.getUserByEmail(email)
                if (existingUsers.isNotEmpty()) {
                    _registerState.value = RegisterState.Error("Email đã được sử dụng")
                    return@launch
                }
                
                // Tạo user mới
                val newUser = User(
                    id = "US" + UUID.randomUUID().toString().substring(0, 3),
                    name = name,
                    email = email,
                    password = password
                )
                
                // Đăng ký user mới
                val registeredUser = authService.registerUser(newUser)
                _currentUser.value = registeredUser
                _registerState.value = RegisterState.Success(registeredUser)
                
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.message ?: "Lỗi đăng ký không xác định")
                Log.d("RegisterError", e.message.toString())
            }
        }
    }
    
    fun resetRegisterState() {
        _registerState.value = RegisterState.Idle
    }
    
    // Lấy thông tin người dùng đã đăng nhập từ SharedPreferences
    fun getUserFromPrefs(context: Context): User? {
        val sharedPref = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("userId", null) ?: return null
        val userName = sharedPref.getString("userName", "")
        val userEmail = sharedPref.getString("userEmail", "")
        
        return User(
            id = userId,
            name = userName ?: "",
            email = userEmail ?: ""
        )
    }
    
    // Kiểm tra người dùng đã đăng nhập chưa
    fun isLoggedIn(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("userId", null) != null
    }
    
    // Đăng xuất
    fun logout(context: Context) {
        val sharedPref = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        
        // Chỉ xóa thông tin đăng nhập, giữ lại thông tin "rememberMe" nếu cần
        editor.remove("userId")
        editor.remove("userName")
        editor.remove("userEmail")
        
        editor.apply()
        _currentUser.value = null
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val user: User) : RegisterState()
    data class Error(val message: String) : RegisterState()
}