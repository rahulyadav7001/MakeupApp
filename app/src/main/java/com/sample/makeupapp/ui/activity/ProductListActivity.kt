package com.sample.makeupapp.ui.activity

import android.graphics.Color.parseColor
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import com.sample.makeupapp.R
import com.sample.makeupapp.constants.CardName
import com.sample.makeupapp.constants.Constants.DEFAULT_VALUE
import com.sample.makeupapp.constants.Constants.EXTRA_DATA_KEY
import com.sample.makeupapp.constants.Constants.MY_DEBUG_KEY
import com.sample.makeupapp.model.Product
import com.sample.makeupapp.ui.theme.MakeupAppTheme
import com.sample.makeupapp.viewmodel.ProductListViewModel

// class for Display Product information
class ProductListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = intent.getStringExtra(EXTRA_DATA_KEY)
        val isForProductList = data == CardName.PRODUCT_LIST.toString()

        setContent {
            MakeupAppTheme {
                Navigation(isForProductList)
            }
        }
    }
}

// to Navigate between different composable
@Composable
fun Navigation(isForProductList: Boolean) {
    val navController = rememberNavController()
    val viewModel = ProductListViewModel()

    LaunchedEffect(key1 = "GET_PRODUCTS") {
        if (isForProductList)
            viewModel.getProducts()
        else
            viewModel.getProductsByBrand()
    }
    NavHost(navController = navController, startDestination = "product_list") {
        composable("product_list") {
            ProductListScreen(navController, viewModel, isForProductList)
        }
        composable(
            route = "product_detail/{product_id}",
            arguments = listOf(navArgument("product_id") {
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
            ProductDetailScreen(
                navBackStackEntry.arguments!!.getInt("product_id"),
                navController,
                viewModel
            )
        }
    }

}

// To design Product List UI
@Composable
fun ProductListScreen(
    navController: NavHostController,
    viewModel: ProductListViewModel,
    isForProductList: Boolean
) {
    ProductListScreenDesign(navController, viewModel, isForProductList)
}

// To design Product Detail UI
@Composable
fun ProductDetailScreen(
    productId: Int,
    navController: NavHostController,
    viewModel: ProductListViewModel
) {
    ProductDetailScreenDesign(productId, navController, viewModel)
}

// To design Product List screen Design
@Composable
fun ProductListScreenDesign(
    navController: NavHostController?,
    viewModel: ProductListViewModel?,
    isForProductList: Boolean
) {
    val context = LocalContext.current as ProductListActivity
    val screenTitle = if (isForProductList) "Product List" else " Product By Brand"
    viewModel?.let {
        Scaffold(topBar = {
            AppBarTop(
                title = screenTitle,
                icon = Icons.Default.ArrowBack
            ) { context.finish() }
        }) { paddingValue ->
            Column(
                modifier = Modifier
                    .padding(paddingValue)
                    .fillMaxSize()
            ) {
                ListScreenDesign(viewModel, navController)
            }
        }
    }
}

// To design Product detail screen Design
@Composable
fun ProductDetailScreenDesign(
    productId: Int,
    navController: NavHostController?,
    viewModel: ProductListViewModel
) {
    val productDetail = viewModel.productList.value?.first { element -> element.id == productId }
    Scaffold(topBar = {
        AppBarTop("Product Detail", Icons.Default.ArrowBack) {
            navController?.navigateUp()
        }
    }) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .background(color = colorResource(id = R.color.white))
                .verticalScroll(rememberScrollState())
        ) {
            if (productDetail != null) {
                ProductDetailDesign(productDetail, {}, isDetailScreen = true)
            }
        }
    }
}

// To display list of products
@Composable
private fun ListScreenDesign(viewModel: ProductListViewModel, navController: NavHostController?) {
    val productList by viewModel.productList.observeAsState(emptyList())
    val isLoading = viewModel.isLoading
    val isError = viewModel.isError
    val errorMessage = viewModel.errorMessage
    if (isLoading.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (isError.value) {
        Log.d(MY_DEBUG_KEY, errorMessage.value)
        ShowError(errorMessage.value)
    } else {
        LazyColumn {
            items(productList) {
                ProductListDesign(it, {
                    navController?.navigate("product_detail/${it.id}")
                }, isDetailScreen = false)
            }
        }
    }

}

@Composable
fun ShowError(errorMessage: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.Center, content = {
            Column {
                Text(
                    text = "Error!",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = errorMessage,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp),
                    textAlign = TextAlign.Center
                )
            }

        }
    )

}

// To design Product List elements
@Composable
fun ProductListDesign(
    productObject: Product,
    onItemClick: () -> Unit,
    isDetailScreen: Boolean
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 6.dp)
            .clickable(onClick = { onItemClick.invoke() })
    ) {
        Row(
            modifier = Modifier
                .animateContentSize()
                .fillMaxHeight()
                .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            ProductImage(productObject, 80.dp)
            ProductItemDetails(productObject, isDetailScreen)

        }
    }
}

// To design Product Detail screen Design
@Composable
fun ProductDetailDesign(
    productObject: Product,
    onItemClick: () -> Unit,
    isDetailScreen: Boolean
) {
    Card(
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 6.dp)
            .clickable(onClick = { onItemClick.invoke() }, enabled = !isDetailScreen)
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            ProductImage(productObject, 200.dp)
            ProductItemDetails(productObject, isDetailScreen)

        }
    }
}

// To design item elements of the product list
@Composable
fun ProductItemDetails(productObject: Product, isDetailScreen: Boolean) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = productObject.name ?: DEFAULT_VALUE,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 2
        )
        Text(text = "Price: ${productObject.price ?: "00"}$")
        Text(
            text = "Brand: ${productObject.brand ?: "D"} ",
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = "Description: ${productObject.description ?: DEFAULT_VALUE}",
            style = MaterialTheme.typography.bodyMedium,
            maxLines = if (isDetailScreen) 20 else 3,
            overflow = TextOverflow.Ellipsis
        )
        if (isDetailScreen && productObject.productColors.isNotEmpty()) {
            Column {
                Text(
                    text = "Color : ",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                )
                LazyRow {
                    items(productObject.productColors) {
                        Text(
                            text = "${it.colourName}",
                            modifier = Modifier
                                .padding(10.dp)
                                .background(getColor(it.hexValue)),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                        )
                    }
                }
            }
        }

    }
}

fun getColor(colorString: String?): Color {
    return Color(parseColor(colorString))
}

@Composable
fun ProductImage(productObject: Product, imageSize: Dp) {
    Image(
        painter = rememberAsyncImagePainter(productObject.imageLink),
        contentDescription = null,
        modifier = Modifier
            .size(imageSize)
            .padding(2.dp)
            .fillMaxHeight(),
        alignment = Alignment.Center,
        contentScale = ContentScale.Fit
    )
}

// To design Appbar of the app
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarTop(title: String, icon: ImageVector, onBackPress: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.surface,
        ),
        title = {
            Text(title)
        },
        navigationIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "",
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .clickable(onClick = { onBackPress.invoke() }),
                tint = Color.White
            )
        },

        )
}

// to see the design preview
@Preview(showBackground = true)
@Composable
fun PreviewScreenList() {
    MakeupAppTheme {
        ProductListScreenDesign(null, null, true)
    }
}

