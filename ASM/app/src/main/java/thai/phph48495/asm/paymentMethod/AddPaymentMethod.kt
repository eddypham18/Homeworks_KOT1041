package thai.phph48495.asm.paymentMethod

import android.widget.Toast
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import thai.phph48495.asm.activity.ButtonSplash
import thai.phph48495.asm.activity.HeaderWithBack
import thai.phph48495.asm.activity.UserSession
import thai.phph48495.asm.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPaymentScreen(navController: NavController) {
    val context = LocalContext.current
    val paymentViewModel: PaymentViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    
    // Lấy thông tin người dùng
    val user = UserSession.getUser(context)
    val userId = user?.id ?: ""
    val userName = user?.name ?: ""
    
    // Nếu người dùng chưa đăng nhập, hiển thị thông báo và quay lại
    if (userId.isEmpty()) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Vui lòng đăng nhập để thêm phương thức thanh toán", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    // Theo dõi trạng thái từ ViewModel
    val loading by paymentViewModel.loading.collectAsState()
    val error by paymentViewModel.error.collectAsState()

    // Trạng thái các trường nhập liệu
    var cardNumberState by remember { mutableStateOf(TextFieldValue("")) }
    var cvvState by remember { mutableStateOf(TextFieldValue("")) }
    var expirationDateState by remember { mutableStateOf(TextFieldValue("")) }
    var showDialog by remember { mutableStateOf(false) }
    
    // Format số thẻ khi nhập
    val formattedCardNumber = remember(cardNumberState.text) {
        formatInputCardNumber(cardNumberState.text)
    }
    
    // Kiểm tra lỗi nhập liệu
    var cardNumberError by remember { mutableStateOf<String?>(null) }
    var cvvError by remember { mutableStateOf<String?>(null) }
    var expirationDateError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            HeaderWithBack(modifier = Modifier, text = "Adding payment method", navController = navController)
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                ButtonSplash(
                    modifier = Modifier
                        .width(335.dp)
                        .height(60.dp),
                    text = "THÊM THẺ MỚI"
                ) {
                    // Validate các trường nhập liệu
                    cardNumberError = validateCardNumber(cardNumberState.text)
                    cvvError = validateCVV(cvvState.text)
                    expirationDateError = validateExpirationDate(expirationDateState.text)
                    
                    // Nếu không có lỗi, thêm phương thức thanh toán mới
                    if (cardNumberError == null && cvvError == null && expirationDateError == null) {
                        coroutineScope.launch {
                            paymentViewModel.addPaymentMethod(
                                userId = userId,
                                cardNumber = cardNumberState.text,
                                cvv = cvvState.text,
                                expirationDate = expirationDateState.text,
                                isDefault = true,
                                onSuccess = {
                                    showDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            // Hiển thị lỗi từ API nếu có
            if (error != null) {
                Snackbar(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = error ?: "Đã xảy ra lỗi")
                }
                LaunchedEffect(error) {
                    if (error != null) {
                        paymentViewModel.resetError()
                    }
                }
            }
            
            // Hiển thị loading
            if (loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Hiển thị thẻ với thông tin được cập nhật theo thời gian thực
                    CardPaymentMethodDemo(
                        cardNumber = formattedCardNumber,
                        holderName = userName,
                        expiryDate = expirationDateState.text.ifEmpty { "MM/YY" }
                    )
                    
                    // Form nhập thông tin thẻ
                    AddPaymentMethodForm(
                        cardNumberState = cardNumberState,
                        cvvState = cvvState,
                        expiryState = expirationDateState,
                        onCardNumberChange = { value ->
                            // Giới hạn độ dài của số thẻ thành 16 chữ số (không tính khoảng trắng)
                            val cleanText = value.text ?: ""
                            if (cleanText.replace(" ", "").length <= 16) {
                                cardNumberState = value
                            }
                        },
                        onCvvChange = { value ->
                            // Giới hạn độ dài của CVV thành 4 chữ số
                            val cleanText = value.text ?: ""
                            if (cleanText.length <= 4) {
                                cvvState = value
                            }
                        },
                        onExpiryChange = { value ->
                            // Tự động thêm dấu '/' khi người dùng nhập 2 số đầu tiên
                            val text = value.text ?: ""
                            val formatted = formatExpiryDate(text)
                            if (formatted != expirationDateState.text) {
                                expirationDateState = TextFieldValue(formatted)
                            } else {
                                expirationDateState = value
                            }
                        },
                        cardNumberError = cardNumberError,
                        cvvError = cvvError,
                        expirationDateError = expirationDateError
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Thông báo") },
            text = { Text(text = "Thêm thẻ thành công!") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    navController.navigate("paymentMethod") {
                        popUpTo("paymentMethod") { inclusive = true }
                    }
                }) {
                    Text(text = "OK")
                }
            }
        )
    }
}

// Format số thẻ để hiển thị khi người dùng đang nhập
fun formatInputCardNumber(cardNumber: String?): String {
    if (cardNumber == null) return "**** **** **** ****"
    
    val digitsOnly = cardNumber.replace("\\D".toRegex(), "")
    val formatted = StringBuilder()
    
    for (i in digitsOnly.indices) {
        if (i > 0 && i % 4 == 0) {
            formatted.append(" ")
        }
        formatted.append(digitsOnly[i])
    }
    
    // Thêm dấu * nếu chuỗi chưa đủ 16 chữ số
    val result = formatted.toString()
    if (result.isEmpty()) {
        return "**** **** **** ****"
    }
    
    return result
}

// Format ngày hết hạn khi nhập
fun formatExpiryDate(input: String?): String {
    if (input == null) return ""
    
    val digitsOnly = input.replace("\\D".toRegex(), "")
    
    return when {
        digitsOnly.isEmpty() -> ""
        digitsOnly.length <= 2 -> digitsOnly
        else -> "${digitsOnly.substring(0, 2)}/${digitsOnly.substring(2, minOf(4, digitsOnly.length))}"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPaymentMethodForm(
    cardNumberState: TextFieldValue,
    cvvState: TextFieldValue,
    expiryState: TextFieldValue,
    onCardNumberChange: (TextFieldValue) -> Unit,
    onCvvChange: (TextFieldValue) -> Unit,
    onExpiryChange: (TextFieldValue) -> Unit,
    cardNumberError: String?,
    cvvError: String?,
    expirationDateError: String?
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        OutlinedTextField(
            value = cardNumberState,
            onValueChange = onCardNumberChange,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = if (cardNumberError != null) Color.Red else Color.LightGray,
                focusedBorderColor = if (cardNumberError != null) Color.Red else Color.LightGray,
            ),
            label = {
                Text(text = "Số thẻ", style = MaterialTheme.typography.titleMedium)
            },
            isError = cardNumberError != null,
            supportingText = {
                if (cardNumberError != null) {
                    Text(text = cardNumberError, color = Color.Red)
                }
            }
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = cvvState,
                onValueChange = onCvvChange,
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = if (cvvError != null) Color.Red else Color.LightGray,
                    focusedBorderColor = if (cvvError != null) Color.Red else Color.LightGray,
                ),
                label = {
                    Text(text = "CVV", style = MaterialTheme.typography.titleMedium)
                },
                isError = cvvError != null,
                supportingText = {
                    if (cvvError != null) {
                        Text(text = cvvError, color = Color.Red)
                    }
                }
            )
            
            OutlinedTextField(
                value = expiryState,
                onValueChange = onExpiryChange,
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = if (expirationDateError != null) Color.Red else Color.LightGray,
                    focusedBorderColor = if (expirationDateError != null) Color.Red else Color.LightGray,
                ),
                label = {
                    Text(text = "MM/YY", style = MaterialTheme.typography.titleMedium)
                },
                isError = expirationDateError != null,
                supportingText = {
                    if (expirationDateError != null) {
                        Text(text = expirationDateError, color = Color.Red)
                    }
                }
            )
        }
    }
}

@Composable
fun CardPaymentMethodDemo(
    cardNumber: String?,
    holderName: String?,
    expiryDate: String?
) {
    val safeCardNumber = cardNumber ?: "**** **** **** ****"
    val safeHolderName = holderName.takeIf { !it.isNullOrEmpty() } ?: "YOUR NAME"
    val safeExpiryDate = expiryDate.takeIf { !it.isNullOrEmpty() } ?: "MM/YY"
    
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
                    text = safeCardNumber,
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
                            text = safeHolderName,
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
                            text = safeExpiryDate,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// Validate card number (chỉ kiểm tra đơn giản)
fun validateCardNumber(cardNumber: String?): String? {
    if (cardNumber == null) return "Vui lòng nhập số thẻ"
    
    return when {
        cardNumber.isBlank() -> "Vui lòng nhập số thẻ"
        cardNumber.replace("\\D".toRegex(), "").length < 16 -> "Số thẻ phải có ít nhất 16 chữ số"
        else -> null
    }
}

// Validate CVV (chỉ kiểm tra đơn giản)
fun validateCVV(cvv: String?): String? {
    if (cvv == null) return "Vui lòng nhập mã CVV"
    
    return when {
        cvv.isBlank() -> "Vui lòng nhập mã CVV"
        cvv.length < 3 -> "Mã CVV phải có ít nhất 3 chữ số"
        else -> null
    }
}

// Validate expiration date (chỉ kiểm tra đơn giản)
fun validateExpirationDate(expirationDate: String?): String? {
    if (expirationDate == null) return "Vui lòng nhập ngày hết hạn"
    
    return when {
        expirationDate.isBlank() -> "Vui lòng nhập ngày hết hạn"
        !expirationDate.matches(Regex("^\\d{1,2}/\\d{2}$")) -> "Định dạng phải là MM/YY"
        else -> null
    }
}
