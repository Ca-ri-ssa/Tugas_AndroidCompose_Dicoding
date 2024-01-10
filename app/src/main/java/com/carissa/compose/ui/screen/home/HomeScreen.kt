package com.carissa.compose.ui.screen.home

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carissa.compose.data.SeaAnimalsRepository
import com.carissa.compose.model.SeaAnimals
import com.carissa.compose.ui.common.UiState
import com.carissa.compose.ui.component.SeaAnimalsItem
import com.carissa.compose.ui.component.SearchBarHome
import com.carissa.compose.ui.screen.ViewModelFactory

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = ViewModelFactory(SeaAnimalsRepository())),
    navigateToDetail: (Int) -> Unit,
    ) {
    val query by viewModel.query
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.search(query)
            }
            is UiState.Success -> {
                HomeContent(
                    query = query,
                    onQueryChange = viewModel::search,
                    listSeaAnimals = uiState.data,
                    iconStarredClicked = { id, newState ->
                        viewModel.updateListSeaAnimals(id, newState)
                    },
                    navigateToDetail = navigateToDetail
                )
            }
            is UiState.Error -> {}
        }
    }
}

@Composable
fun HomeContent(
    query: String,
    onQueryChange: (String) -> Unit,
    listSeaAnimals: List<SeaAnimals>,
    iconStarredClicked: (id: Int, newState: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    navigateToDetail: (Int) -> Unit,
) {
    var snackbarVisible by remember { mutableStateOf(false) }

    Column {
        SearchBarHome(
            query = query,
            onQueryChange = onQueryChange
        )
        if (listSeaAnimals.isNotEmpty()) {
            ListSeaAnimals(
                listSeaAnimals = listSeaAnimals,
                iconStarredClicked = iconStarredClicked,
                navigateToDetail = navigateToDetail
            )
        } else {
            LaunchedEffect(query) {
                if (query.isNotEmpty()) {
                    snackbarVisible = true
                }
            }
        }
        if (snackbarVisible) {
            SnackbarMessage(
                message = "Sorry, there is no result of your search",
                onDismiss = { snackbarVisible = false }
            )
        }
    }
}

@Composable
fun SnackbarMessage(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Snackbar(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onPrimary
            )
            IconButton(
                onClick = onDismiss
            ) {
                Icon(Icons.Default.Close, contentDescription = null)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListSeaAnimals(
    listSeaAnimals: List<SeaAnimals>,
    iconStarredClicked: (id: Int, newState: Boolean) -> Unit,
    navigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(listSeaAnimals, key = { it.id }) { seaAnimal ->
                SeaAnimalsItem(
                    id = seaAnimal.id,
                    name = seaAnimal.name,
                    imgUrl = seaAnimal.imgUrl,
                    latinName = seaAnimal.latinName,
                    modifier = modifier
                        .clickable { navigateToDetail(seaAnimal.id) }
                        .animateItemPlacement(tween(durationMillis = 100))
                        .fillMaxWidth()
                )
            }
        }
    }
}