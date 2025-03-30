package thai.phph48495.asm.address

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import thai.phph48495.asm.activity.ButtonSplash
import thai.phph48495.asm.activity.HeaderWithBack
import thai.phph48495.asm.profile.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAddressScreen(address: Address,navController: NavController) {
    val defaultUserName = "User Name"

    var addressState by remember {
        mutableStateOf(TextFieldValue(address.address))
    }
    var countryState by remember {
        mutableStateOf(TextFieldValue(address.country))
    }
    var cityState by remember {
        mutableStateOf(TextFieldValue(address.city))
    }
    var districtState by remember {
        mutableStateOf(TextFieldValue(address.district))
    }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            HeaderWithBack(modifier = Modifier, text = "Shipping Address", navController = navController)
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ButtonSplash(modifier = Modifier.weight(1f), text = "Edit") {
                    val editedAddress = Address(
                        idAddress = address.idAddress,
                        address = addressState.text,
                        country = countryState.text,
                        city = cityState.text,
                        district = districtState.text
                    )
                    editAddress(editedAddress, navController)
                }
                ButtonSplash(modifier = Modifier.weight(1f), text = "Delete") {
                    deleteAddress(address, navController)
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)){
            EditAddress(
                defaultName = defaultUserName,
                addressState = addressState,
                countryState = countryState,
                cityState = cityState,
                districtState = districtState,
                onAddressChange = {addressState = it},
                onCountryChange = {countryState = it},
                onCityChange = {cityState = it},
                onDistrictChange = {districtState = it},
                errorMessage = errorMessage
            )
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
    errorMessage: String?
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

    }

}
fun editAddress(address: Address, navController: NavController) {
}

fun deleteAddress(address: Address, navController: NavController) {
}