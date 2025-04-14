package thai.phph48495.asm.activity

import android.content.Context
import thai.phph48495.asm.profile.User

/**
 * Lớp tiện ích để quản lý phiên người dùng trong toàn bộ ứng dụng
 */
object UserSession {
    private const val PREF_NAME = "login_prefs"
    private const val KEY_USER_ID = "userId"
    private const val KEY_USER_NAME = "userName"
    private const val KEY_USER_EMAIL = "userEmail"
    
    /**
     * Lấy ID của người dùng đã đăng nhập
     */
    fun getUserId(context: Context): String? {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY_USER_ID, null)
    }
    
    /**
     * Lấy thông tin của người dùng đã đăng nhập
     */
    fun getUser(context: Context): User? {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val userId = sharedPref.getString(KEY_USER_ID, null) ?: return null
        val userName = sharedPref.getString(KEY_USER_NAME, "")
        val userEmail = sharedPref.getString(KEY_USER_EMAIL, "")
        
        return User(
            id = userId,
            name = userName ?: "",
            email = userEmail ?: ""
        )
    }
    
    /**
     * Kiểm tra người dùng đã đăng nhập chưa
     */
    fun isLoggedIn(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY_USER_ID, null) != null
    }
    
    /**
     * Lưu thông tin người dùng khi đăng nhập
     */
    fun saveUser(context: Context, user: User) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        
        editor.putString(KEY_USER_ID, user.id)
        editor.putString(KEY_USER_NAME, user.name)
        editor.putString(KEY_USER_EMAIL, user.email)
        
        editor.apply()
    }
    
    /**
     * Đăng xuất, xóa thông tin người dùng
     */
    fun logout(context: Context) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        
        // Xóa tất cả thông tin đăng nhập
        editor.remove(KEY_USER_ID)
        editor.remove(KEY_USER_NAME)
        editor.remove(KEY_USER_EMAIL)
        
        editor.apply()
    }
} 