package com.example.newsapp.screens.newsScreen

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.newsapp.data.api.Article

@Composable
fun NewsScreen(
    navController: NavHostController,
    viewModel: NewsViewModel = hiltViewModel()
) {
    val articles by viewModel.articles.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    if (articles.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Loading articles...")
        }
    } else {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refresh() }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(articles) { article ->
                    ArticleCard(article = article, context = context)
                }
            }
        }
    }
}

@Composable
fun ArticleCard(article: Article, context: android.content.Context) {
    // Survives recomposition and lazy list recycling
    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            // --- Image (tapping opens the URL) ---
            AsyncImage(
                model = article.image,
                contentDescription = article.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                        context.startActivity(intent)
                    }
            )

            // --- Title row + chevron (tapping toggles the body) ---
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = !expanded },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = if (expanded)
                            Icons.Default.KeyboardArrowUp
                        else
                            Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = article.dateTime,
                    style = MaterialTheme.typography.bodySmall
                )

                // --- Animated body dropdown ---
                AnimatedVisibility(
                    visible = expanded && article.body != null,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Text(
                            text = article.body ?: "",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}