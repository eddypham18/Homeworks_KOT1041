package thai.phph48495.asm.address

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import thai.phph48495.asm.activity.ButtonSplash
import thai.phph48495.asm.activity.HeaderWithBack
import thai.phph48495.asm.activity.UserSession

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAddressScreen(
    addressId: String,
    navController: NavController,
    addressViewModel: AddressViewModel = viewModel()
) {
    val context = LocalContext.current
    val userId = UserSession.getUserId(context) ?: ""

    // Xử lý khi chưa đăng nhập
    if (userId.isEmpty()) {
        DisposableEffect(Unit) {
            // Chuyển về trang đăng nhập nếu chưa đăng nhập
            navController.navigate("login") {
                popUpTo("editAddress/${addressId}") { inclusive = true }
            }
            onDispose { }
        }
        return
    }

    val currentUser = addressViewModel.currentUser.value
    val isLoading = addressViewModel.isLoading.value ?: false
    val error = addressViewModel.error.value
    val operationSuccess = addressViewModel.operationSuccess.value ?: false

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Load thông tin người dùng khi vào màn hình
    DisposableEffect(key1 = userId) {
        addressViewModel.getUserAddresses(userId)
        onDispose { /* Dọn dẹp nếu cần */ }
    }

    // Hiển thị lỗi nếu có
    LaunchedEffect(error) {
        if (error != null) {
            snackbarHostState.showSnackbar(error)
        }
    }

    // Biến theo dõi hành động đang thực hiện
    var actionType by remember { mutableStateOf("") }

    // Chuyển hướng về màn hình danh sách địa chỉ khi cập nhật/xóa thành công
    LaunchedEffect(operationSuccess) {
        if (operationSuccess) {
            // Hiển thị thông báo thành công dựa trên hành động
            val message = when (actionType) {
                "delete" -> "Xóa địa chỉ thành công"
                else -> "Cập nhật địa chỉ thành công"
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

            // Reset trạng thái thành công
            addressViewModel.resetOperationStatus()

            // Đảm bảo quay về màn hình ShippingAddressScreen thay vì chỉ popBackStack
            navController.navigate("shippingAddress") {
                popUpTo("shippingAddress") { inclusive = false }
                launchSingleTop = true
            }
        }
    }

    // Tìm địa chỉ cần chỉnh sửa từ danh sách
    val address = currentUser?.addresses?.find { it.id == addressId }

    if (address == null && !isLoading) {
        LaunchedEffect(Unit) {
            snackbarHostState.showSnackbar("Không tìm thấy địa chỉ")
            navController.popBackStack()
        }
        return
    }

    val defaultUserName = currentUser?.name ?: "Người dùng"

    var addressState by remember(address) {
        mutableStateOf(TextFieldValue(address?.address ?: ""))
    }
    var countryState by remember(address) {
        mutableStateOf(TextFieldValue(address?.country ?: ""))
    }
    var cityState by remember(address) {
        mutableStateOf(TextFieldValue(address?.city ?: ""))
    }
    var districtState by remember(address) {
        mutableStateOf(TextFieldValue(address?.district ?: ""))
    }
    
    // Thêm biến trạng thái cho tùy chọn "Địa chỉ mặc định"
    var isDefault by remember(address) {
        mutableStateOf(address?.isDefault ?: false)
    }

    Scaffold(
        topBar = {
            HeaderWithBack(modifier = Modifier, text = "Shipping Address", navController = navController)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ButtonSplash(modifier = Modifier.weight(1f).padding(8.dp), text = "Save") {
                    if (addressState.text.isNotBlank()) {
                        val editedAddress = Address(
                            id = addressId,
                            address = addressState.text,
                            country = countryState.text,
                            city = cityState.text,
                            district = districtState.text,
                            isDefault = isDefault
                        )
                        actionType = "update"
                        addressViewModel.updateAddress(userId, editedAddress)
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Vui lòng nhập địa chỉ")
                        }
                    }
                }
                ButtonSplash(modifier = Modifier.weight(1f).padding(8.dp), text = "Delete") {
                    actionType = "delete"
                    addressViewModel.deleteAddress(userId, addressId)
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                EditAddress(
                    defaultName = defaultUserName,
                    addressState = addressState,
                    countryState = countryState,
                    cityState = cityState,
                    districtState = districtState,
                    onAddressChange = { addressState = it },
                    onCountryChange = { countryState = it },
                    onCityChange = { cityState = it },
                    onDistrictChange = { districtState = it },
                    errorMessage = error,
                    isDefault = isDefault,
                    onDefaultChange = { isDefault = it }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAddress(
    defaultName : String,
    addressState: TextFieldValue,
    countryState: TextFieldValue,
    cityState: TextFieldValue,
    districtState: TextFieldValue,
    onAddressChange: (TextFieldValue) -> Unit,
    onCountryChange: (TextFieldValue) -> Unit,
    onCityChange: (TextFieldValue) -> Unit,
    onDistrictChange: (TextFieldValue) -> Unit,
    errorMessage: String?,
    isDefault: Boolean,
    onDefaultChange: (Boolean) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        OutlinedTextField(value = TextFieldValue(defaultName),
            onValueChange = {},
            modifier = Modifier
                .height(66.dp)
                .width(335.dp)
            ,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                containerColor = Color.LightGray
            ),
            readOnly = true
        )
        OutlinedTextField(value = addressState,
            onValueChange = onAddressChange,
            modifier = Modifier
                .height(66.dp)
                .width(335.dp)
            ,
            label = {
                Text(text = "Address",style = MaterialTheme.typography.titleMedium)
            },
            placeholder = {
                Text(text = "Ex: 19 My Dinh", color = Color.Gray)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                containerColor = Color.LightGray
            )
        )
        OutlinedTextField(value = countryState,
            onValueChange = onCountryChange,
            modifier = Modifier
                .height(66.dp)
                .width(335.dp)
            ,
            label = {
                Text(text = "Country",style = MaterialTheme.typography.titleMedium)
            },
            placeholder = {
                Text(text = "Ex: 19 My Dinh", color = Color.Gray)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                containerColor = Color.LightGray
            )
        )
        OutlinedTextField(value = cityState,
            onValueChange = onCityChange,
            modifier = Modifier
                .height(66.dp)
                .width(335.dp)
            ,
            label = {
                Text(text = "City",style = MaterialTheme.typography.titleMedium)
            },
            placeholder = {
                Text(text = "Ex: 19 My Dinh", color = Color.Gray)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                containerColor = Color.LightGray
            )
        )
        OutlinedTextField(value = districtState,
            onValueChange = onDistrictChange,
            modifier = Modifier
                .height(66.dp)
                .width(335.dp)
            ,
            label = {
                Text(text = "District",style = MaterialTheme.typography.titleMedium)
            },
            placeholder = {
                Text(text = "Ex: 19 My Dinh", color = Color.Gray)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                containerColor = Color.LightGray
            )
        )
        
        // Thêm tùy chọn đánh dấu địa chỉ mặc định
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isDefault,
                onCheckedChange = { onDefaultChange(it) }
            )
            Text(
                text = "Use as the shipping address",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}