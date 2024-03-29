package com.example.roomexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.roomexample.ui.theme.RoomExampleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoomExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    ScreenSetup()
                }
            }
        }
    }
}

@Composable
fun ScreenSetup(
    viewModel: MainViewModel = hiltViewModel()
) {
    val allProducts by viewModel.allProducts.collectAsState(listOf())
    val searchResults by viewModel.searchResults.collectAsState(listOf())

    MainScreen(
        allProducts = allProducts,
        searchResults = searchResults,
        viewModel
    )
}

@Composable
fun MainScreen(
    allProducts: List<Product>,
    searchResults: List<Product>,
    viewModel: MainViewModel
) {

    var productName by remember {
        mutableStateOf("")
    }
    var productQuantity by remember {
        mutableStateOf("")
    }
    var searching by remember {
        mutableStateOf(false)
    }
    val onProductTextChange = { text: String ->
        productName = text
    }
    val onQuantityTextChange = { text: String ->
        productQuantity = text
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        CustomTextField(
            title = "Product Name",
            textState = productName,
            onTextChange = onProductTextChange,
            keyboardType = KeyboardType.Text
        )

        CustomTextField(
            title = "Quantity",
            textState = productQuantity,
            onTextChange = onQuantityTextChange,
            keyboardType = KeyboardType.Text
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Button(onClick = {
                if (productQuantity.isNotEmpty()) {
                    viewModel.insertProduct(
                        Product(
                            productName,
                            productQuantity.toInt()
                        ))
                    searching = false
                }
            }) {
                Text(text = "Add")
            }

            Button(onClick = {
                searching = true
                viewModel.searchProducts(productName)
            }) {
                Text(text = "Search")
            }

            Button(onClick = {
                searching = false
                viewModel.deleteProduct(productName)
            }) {
                Text(text = "Delete")
            }

            Button(onClick = {
                searching = false
                productName = ""
                productQuantity = ""
            }) {
                Text(text = "Clear")
            }
        }

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
        ) {
            val list = if (searching) searchResults else allProducts

            item {
                TitleRow(
                    head1 = "ID",
                    head2 = "Product",
                    head3 = "Quantity"
                )
            }

            items(list) {product ->
                ProductRow(
                    id = product.id,
                    name = product.productName,
                    Quantity = product.quantity
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RoomExampleTheme {
        ScreenSetup()
    }
}

@Composable
fun TitleRow(head1: String, head2: String, head3: String) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Text(
            text = head1,
            color = Color.White,
            modifier = Modifier.weight(0.1f)
        )

        Text(
            text = head2,
            color = Color.White,
            modifier = Modifier.weight(0.2f)
        )

        Text(
            text = head3,
            color = Color.White,
            modifier = Modifier.weight(0.2f)
        )
    }
}

@Composable
fun ProductRow(
    id: Int,
    name: String,
    Quantity: Int
) {

    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)
    ) {
        Text(
            text = id.toString(),
            color = Color.Black,
            modifier = Modifier.weight(0.1f)
        )

        Text(
            text = name,
            color = Color.Black,
            modifier = Modifier.weight(0.2f)
        )

        Text(
            text = Quantity.toString(),
            color = Color.Black,
            modifier = Modifier.weight(0.2f)
        )
    }
}

@Composable
fun CustomTextField(
    title: String,
    textState: String,
    onTextChange: (String) -> Unit,
    keyboardType: KeyboardType
) {

    OutlinedTextField(
        value = textState,
        onValueChange = { onTextChange(it) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        label = { Text(text = title) },
        modifier = Modifier.padding(10.dp),
        textStyle = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )
    )
}