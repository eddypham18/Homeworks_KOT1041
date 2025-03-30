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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import thai.phph48495.asm.activity.HeaderWithBack


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShippingAddressScreen(navController: NavController) {
    // Tạo dữ liệu giả cho danh sách địa chỉ
    val fakeAddresses = remember {
        listOf(
            Address(
                idAddress = "1",
                address = "123 Fake Street, Fake City, Country",
                isDefault = true
            ),
            Address(
                idAddress = "2",
                address = "456 Another Rd, Some City, Country",
                isDefault = false
            ),
            Address(
                idAddress = "3",
                address = "789 Example Ave, Other City, Country",
                isDefault = false
            )
        )
    }
    // Fake tên người dùng mặc định
    val defaultUsername = "Fake User"

    Scaffold(
        topBar = {
            HeaderWithBack(modifier = Modifier, text = "Address", navController = navController)
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                if (fakeAddresses.isEmpty()) {
                    Text(text = "Loading addresses...", modifier = Modifier.padding(16.dp))
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(fakeAddresses) { address ->
                            CardShippingAddress(
                                address = address,
                                nameUser = defaultUsername,
                                onDefaultChange = { /* Fake: không xử lý logic */ },
                                onClickDetail = {
                                    navController.navigate("addressDetail/${address.idAddress}")
                                }
                            )
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
                    Icon(Icons.Default.Edit, contentDescription = "Edit address")
                }
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = address.address,
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
