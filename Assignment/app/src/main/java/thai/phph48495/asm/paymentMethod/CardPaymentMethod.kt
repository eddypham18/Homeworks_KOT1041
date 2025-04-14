package thai.phph48495.asm.paymentMethod

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import thai.phph48495.asm.R

/**
 * Component hiển thị thẻ thanh toán với chức năng đặt làm mặc định
 * 
 * @param method Phương thức thanh toán cần hiển thị
 * @param nameUser Tên người dùng
 * @param onDefaultChange Callback khi trạng thái mặc định thay đổi
 */
@Composable
fun CardPaymentMethod(
    method: PaymentMethod, 
    nameUser: String, 
    onDefaultChange: (Boolean) -> Unit
) {
    // Sử dụng trực tiếp giá trị isDefault từ model
    val isDefault = method.isDefault
    
    Column(modifier = Modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E3A8A)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF1E3A8A),
                                Color(0xFF0284C7)
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top section with logo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .padding(4.dp)
                                .width(48.dp)
                                .height(32.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.mastercard2),
                                contentDescription = "Card Logo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }
                        
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0x66FFFFFF))
                        ) {
                            // Chip icon or decoration
                        }
                    }
                    
                    // Card number
                    Text(
                        text = method.cardNumber,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    // Bottom section with card holder and expiry
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "CARD HOLDER",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xAAFFFFFF)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = nameUser,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "EXPIRES",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xAAFFFFFF)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = method.expirationDate,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
        
        // Checkbox để đặt làm phương thức mặc định
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isDefault,
                onCheckedChange = { newValue -> 
                    // Gọi callback với giá trị mới, cho phép bật/tắt
                    onDefaultChange(newValue)
                }
            )
            Text(
                text = if (isDefault) "Phương thức mặc định" else "Đặt làm mặc định",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
} 