package thai.phph48495.asm.paymentMethod

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import thai.phph48495.asm.activity.HeaderWithBack
import thai.phph48495.asm.R


// Fake ViewModel chứa dữ liệu PaymentMethod mẫu
class FakePaymentMethodViewModel {
    var paymentMethodList by mutableStateOf(
        listOf(
            PaymentMethod(
                idMethod = "1",
                cardNumber = "**** **** **** 1234",
                cvv = "123",
                expirationDate = "12/25",
                isDefault = true
            ),
            PaymentMethod(
                idMethod = "2",
                cardNumber = "**** **** **** 5678",
                cvv = "456",
                expirationDate = "11/24",
                isDefault = false
            ),
            PaymentMethod(
                idMethod = "3",
                cardNumber = "**** **** **** 9012",
                cvv = "789",
                expirationDate = "10/23",
                isDefault = false
            )
        )
    )

    // Cập nhật trạng thái mặc định: khi đánh dấu một thẻ, các thẻ còn lại sẽ được đặt là false
    fun updateDefaultPaymentMethod(method: PaymentMethod, isChecked: Boolean) {
        paymentMethodList = paymentMethodList.map { m ->
            if (m.idMethod == method.idMethod) m.copy(isDefault = isChecked)
            else m.copy(isDefault = false)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodScreen(navController: NavController) {
    // Sử dụng fake ViewModel thay cho thật
    val fakeViewModel = remember { FakePaymentMethodViewModel() }
    val paymentMethodList = fakeViewModel.paymentMethodList
    val coroutineScope = rememberCoroutineScope()
    // Fake tên người dùng mặc định
    val defaultUserName = "Fake User"

    Scaffold(
        topBar = {
            HeaderWithBack(modifier = Modifier, text = "Payment", navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addPaymentMethod") },
                shape = CircleShape,
                containerColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add paymentMethod")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (paymentMethodList.isEmpty()) {
                Text(
                    text = "Loading payment methods...",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(paymentMethodList) { method ->
                        CardPaymentMethod(
                            method = method,
                            nameUser = defaultUserName,
                            onDefaultChange = { isChecked ->
                                coroutineScope.launch {
                                    fakeViewModel.updateDefaultPaymentMethod(method, isChecked)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CardPaymentMethod(method: PaymentMethod, nameUser: String, onDefaultChange: (Boolean) -> Unit) {
    var isDefault by remember { mutableStateOf(method.isDefault) }
    Column(modifier = Modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(12.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xEB1A1A1A))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .clip(RectangleShape)
                        .background(Color.LightGray)
                        .width(31.dp)
                        .height(24.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mastercard2),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = method.cardNumber,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Card Holder Name",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xEBC9C9C9)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = nameUser,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                    }
                    Column {
                        Text(
                            text = "Expiry Date",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xEBC9C9C9)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = method.expirationDate,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                    }
                }
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
                text = "Use as the payment method",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
