package com.sample.makeupapp.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sample.makeupapp.R
import com.sample.makeupapp.constants.CardName
import com.sample.makeupapp.constants.Constants
import com.sample.makeupapp.constants.Constants.MY_DEBUG_KEY
import com.sample.makeupapp.model.CardItem
import com.sample.makeupapp.ui.theme.MakeupAppTheme

// Class for display Dashboard option and deal with
class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(MY_DEBUG_KEY, "DashboardActivity - onCreate called")
        setContent {
            MakeupAppTheme {
                DashBoardScreen()
            }
        }
    }

    // TO design the Dashboard screen
    @Composable
    private fun DashBoardScreen() {
        val dashboardOptions = getDashboardItem()
        val context = LocalContext.current
        Scaffold(topBar = { AppBarTop(title = "Dashboard", icon = Icons.Default.Home) {} }) {
            Surface(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(dashboardOptions) { cardItem ->
                        MenuCard(cardItem.title, cardItem.backgroundColors) {
                            navigateToRespectiveModule(context, cardItem)
                        }
                    }
                }
            }
        }

    }

    // to prepare static data for display dynamic feature into the dashboard screen
    private fun getDashboardItem(): ArrayList<CardItem> {
        return arrayListOf(
            CardItem(
                "Product By Brand",
                Color(getColor(R.color.light_orange)),
                CardName.PRODUCT_BY_BRAND
            ),
            CardItem(
                "Product List",
                Color(getColor(R.color.light_purple)),
                CardName.PRODUCT_LIST
            ),
            CardItem(
                "Product By Category",
                Color(getColor(R.color.light_teal)),
                CardName.PRODUCT_BY_CATEGORY
            ),
            CardItem(
                "Product By Item",
                Color(getColor(R.color.light_purple)),
                CardName.PRODUCT_BY_ITEM
            )
        )
    }

    // To Navigate the respective screen wrt selection of feature
    private fun navigateToRespectiveModule(context: Context, cardItem: CardItem) {
        Log.d(MY_DEBUG_KEY, cardItem.title)
        when (cardItem.cardName) {
            CardName.PRODUCT_BY_BRAND, CardName.PRODUCT_LIST -> {
                context.startActivity(
                    Intent(context, ProductListActivity::class.java).putExtra(
                        Constants.EXTRA_DATA_KEY,
                        cardItem.cardName.toString()
                    )
                )
            }
            else -> {
                Toast.makeText(context, "Work in Progress...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // To Display feature list
    @Composable
    fun MenuCard(cardTitle: String, backgroundColor: Color, onItemClick: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .clickable(onClick = { onItemClick.invoke() }),
            elevation = CardDefaults.cardElevation(15.dp),
            colors = CardDefaults.cardColors(backgroundColor),
            shape = CutCornerShape(topEnd = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 180.dp), contentAlignment = Alignment.Center
            ) {
                Row {
                    Text(
                        text = cardTitle,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 25.sp,
                        modifier = Modifier.fillMaxWidth(0.8f),
                    )
                    Image(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                            .size(35.dp)
                    )
                }
            }
        }
    }

    //To Display preview of the UI design
    @Preview(showBackground = true)
    @Composable
    fun PreviewDashboardScreen() {
        MakeupAppTheme {
            DashBoardScreen()
        }
    }
}