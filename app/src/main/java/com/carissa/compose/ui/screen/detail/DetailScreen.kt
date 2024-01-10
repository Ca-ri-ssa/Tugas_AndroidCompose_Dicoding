package com.carissa.compose.ui.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.carissa.compose.R
import com.carissa.compose.di.Injection
import com.carissa.compose.ui.common.UiState
import com.carissa.compose.ui.screen.ViewModelFactory

@Composable
fun DetailScreen(
    detailId: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    )
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getSeaAnimalsId(detailId)
            }
            is UiState.Success -> {
                val data = uiState.data
                DetailContent(
                    imgUrl = data.imgUrl,
                    id = data.id,
                    name = data.name,
                    latinName = data.latinName,
                    description = data.description,
                    funFact = data.funFact,
                    isStarred = data.isStarred,
                    onBackClick = onBackClick,
                    onStarredIconClicked = { id, state ->
                        viewModel.updateListSeaAnimals(id, state)
                    }
                )
            }
            is UiState.Error -> {}
        }
    }
}

@Composable
fun DetailContent(
    id: Int,
    name: String,
    latinName: String,
    description: String,
    imgUrl: String,
    funFact: String,
    isStarred: Boolean,
    onStarredIconClicked: (id: Int, state: Boolean) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            Box {
                AsyncImage(
                    model = imgUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back_icon_detail),
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(CircleShape)
                        .size(32.dp)
                        .background(Color.White.copy(alpha = 0.7f))
                        .clickable { onBackClick() }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp)
                        .heightIn(min = 40.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    )
                    IconButton(
                        onClick = {
                            onStarredIconClicked(id, isStarred)
                        },
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = if (!isStarred) Icons.Default.Star else Icons.Default.Star,
                            contentDescription = if (!isStarred) stringResource(R.string.add_starred) else stringResource(R.string.remove_starred),
                            tint = if (!isStarred) Color.LightGray else Color.Magenta
                        )
                    }
                }

                Text(
                    text = latinName,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    style = LocalTextStyle.current.copy(fontStyle = FontStyle.Italic)
                )
                Spacer(modifier = Modifier.height(16.dp))

                FunFactBox(funFact)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = description
                )
            }
        }
    }
}

@Composable
fun FunFactBox(text: String) {
    val boxColor = MaterialTheme.colorScheme.primaryContainer

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(boxColor)
                .padding(8.dp)
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}