package com.paloma.idealista.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.paloma.idealista.R


@Composable
fun HeaderView(
    title: String? = null,
    showSearch: Boolean = false,
    searchQuery: String = "",
    onSearchQueryChanged: (String) -> Unit = {},
    showFavoritesAction: Boolean = false,
    onFavoritesClick: () -> Unit = {},
    showBackAction: Boolean = false,
    onBackClick: () -> Unit = {}
) {
    var isSearchExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.header_yellow_bg))
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showBackAction) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = null,
                        tint = colorResource(R.color.header_black_text)
                    )
                }
            }

            if (title != null) {
                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.header_black_text)
                )
            } else {
                Text(
                    text = buildWordmark(),
                    modifier = Modifier.weight(1f),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    color = colorResource(R.color.header_black_text)
                )
            }

            if (showSearch) {
                IconButton(onClick = { isSearchExpanded = !isSearchExpanded }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = null,
                        tint = colorResource(R.color.header_black_text)
                    )
                }
            }

            if (showFavoritesAction) {
                IconButton(onClick = onFavoritesClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_favorite_filled),
                        contentDescription = null,
                        tint = colorResource(R.color.header_black_text)
                    )
                }
            }
        }

        if (showSearch) {
            AnimatedVisibility(
                visible = isSearchExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .border(
                            2.dp,
                            colorResource(R.color.header_black_text),
                            RoundedCornerShape(14.dp)
                        )
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChanged,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Buscar por zona o dirección") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun buildWordmark() = buildAnnotatedString {
    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
        append("idealista")
    }
    withStyle(SpanStyle(fontWeight = FontWeight.Normal)) {
        append(".challenge")
    }
}