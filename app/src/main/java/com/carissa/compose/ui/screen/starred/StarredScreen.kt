package com.carissa.compose.ui.screen.starred

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.carissa.compose.di.Injection
import com.carissa.compose.model.SeaAnimals
import com.carissa.compose.ui.common.UiState
import com.carissa.compose.ui.screen.ViewModelFactory
import com.carissa.compose.ui.screen.home.ListSeaAnimals

@Composable
fun StarredScreen(
    navigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StarredViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    )
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getStarredItem()
            }
            is UiState.Success -> {
                StarredContent(
                    listSeaAnimals = uiState.data,
                    navigateToDetail = navigateToDetail,
                    iconStarredClicked = { id, newState ->
                        viewModel.updateListSeaAnimals(id, newState)
                    }
                )
            }
            is UiState.Error -> {}
        }
    }
}

@Composable
fun StarredContent(
    listSeaAnimals: List<SeaAnimals>,
    navigateToDetail: (Int) -> Unit,
    iconStarredClicked: (id: Int, newState: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var snackbarVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        if (listSeaAnimals.isNotEmpty()) {
            ListSeaAnimals(
                listSeaAnimals = listSeaAnimals,
                iconStarredClicked = iconStarredClicked,
                navigateToDetail = navigateToDetail
            )
        } else {
            LaunchedEffect(Unit) {
                snackbarVisible = true
            }
        }
        if (snackbarVisible) {
            SnackbarMessage(
                message = "There is no starred items",
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