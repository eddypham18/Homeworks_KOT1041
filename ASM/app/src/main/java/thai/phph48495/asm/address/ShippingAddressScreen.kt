package thai.phph48495.asm.address

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import thai.phph48495.asm.activity.HeaderWithBack
import thai.phph48495.asm.activity.UserSession


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShippingAddressScreen(
    navController: NavController,
    addressViewModel: AddressViewModel = viewModel()
) {
    val context = LocalContext.current
    val userId = UserSession.getUserId(context) ?: ""
    
    // Hiển thị thông báo nếu chưa đăng nhập
    val snackbarHostState = remember { SnackbarHostState() }
    
    if (userId.isEmpty()) {
        DisposableEffect(Unit) {
            // Chuyển về trang đăng nhập nếu chưa đăng nhập
            navController.navigate("login") {
                popUpTo("shippingAddress") { inclusive = true }
            }
            onDispose { }
        }
        return
    }
    
    // Theo dõi dữ liệu từ ViewModel
    val user = addressViewModel.currentUser.value
    val isLoading = addressViewModel.isLoading.value ?: false
    val error = addressViewModel.error.value

    // Gọi API khi màn hình được hiển thị
    DisposableEffect(key1 = userId) {
        addressViewModel.getUserAddresses(userId)
        onDispose { /* Dọn dẹp nếu cần */ }
    }

    Scaffold(
        topBar = {
            HeaderWithBack(modifier = Modifier, text = "Address", navController = navController)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isLoading -> CircularProgressIndicator()
                    error != null -> Text(text = error, color = Color.Red)
                    user?.addresses.isNullOrEmpty() -> Text(text = "Bạn chưa có địa chỉ nào")
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            val addresses = user?.addresses ?: emptyList()
                            items(addresses) { address ->
                                CardShippingAddress(
                                    address = address,
                                    nameUser = user?.name ?: "",
                                    onDefaultChange = { isDefault ->
                                        if (isDefault) {
                                            addressViewModel.updateDefaultAddress(userId, address.id)
                                        }
                                    },
                                    onClickDetail = {
                                        navController.navigate("editAddress/${address.id}")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addShippingAddress") },
                shape = CircleShape,
                containerColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add address")
            }
        }
    )
}

@Composable
fun CardShippingAddress(
    address: Address,
    nameUser: String,
    onDefaultChange: (Boolean) -> Unit,
    onClickDetail: () -> Unit
) {
    var isDefault by remember { mutableStateOf(address.isDefault) }
    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(12.dp)
                .clickable { onClickDetail() },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(
                        text = nameUser,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Icon(
                        Icons.Default.Edit, 
                        contentDescription = "Edit address",
                        modifier = Modifier.clickable { onClickDetail() }
                    )
                }
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Hiển thị địa chỉ đầy đủ
                val fullAddress = buildString {
                    append(address.address)
                    if (address.district.isNotEmpty()) append(", ${address.district}")
                    if (address.city.isNotEmpty()) append(", ${address.city}")
                    if (address.country.isNotEmpty()) append(", ${address.country}")
                }
                
                Text(
                    text = fullAddress,
                    modifier = Modifier.padding(12.dp),
                    fontSize = 15.sp,
                    color = Color.Gray
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isDefault,
                onCheckedChange = { isChecked ->
                    isDefault = isChecked
                    onDefaultChange(isChecked)
                }
            )
            Text(
                text = "Use as the shipping address",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
