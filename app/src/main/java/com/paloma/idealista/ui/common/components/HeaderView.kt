package com.paloma.idealista.ui.common.components

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.paloma.idealista.R
import com.paloma.idealista.ui.list.SortOrder
import com.paloma.idealista.util.AppConstants.EMPTY_STRING

@Composable
fun HeaderView(
    title: String? = null,
    showSearch: Boolean = false,
    searchQuery: String = EMPTY_STRING,
    onSearchQueryChanged: (String) -> Unit = {},
    showFavoritesFilter: Boolean = false,
    isFavoritesFilterActive: Boolean = false,
    onToggleFavoritesFilter: () -> Unit = {},
    showSortAction: Boolean = false,
    sortOrder: SortOrder = SortOrder.NONE,
    onToggleSort: () -> Unit = {},
    showBackAction: Boolean = false,
    onBackClick: () -> Unit = {}
) {
    var isSearchExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.header_background))
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showBackAction) {
                BackButton(onClick = onBackClick)
            }

            HeaderTitle(title = title, modifier = Modifier.weight(1f))

            if (showSearch) {
                SearchToggleButton(onClick = { isSearchExpanded = !isSearchExpanded })
            }

            if (showSortAction) {
                SortButton(sortOrder = sortOrder, onClick = onToggleSort)
            }

            if (showFavoritesFilter) {
                FavoritesFilterButton(
                    isActive = isFavoritesFilterActive,
                    onClick = onToggleFavoritesFilter
                )
            }
        }

        if (showSearch) {
            AnimatedVisibility(
                visible = isSearchExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                SearchField(
                    searchQuery = searchQuery,
                    onSearchQueryChanged = onSearchQueryChanged
                )
            }
        }
    }
}

@Composable
private fun BackButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_back),
            contentDescription = stringResource(R.string.header_back_content_description),
            tint = colorResource(R.color.header_text)
        )
    }
}

@Composable
private fun HeaderTitle(title: String?, modifier: Modifier = Modifier) {
    if (title != null) {
        Text(
            text = title,
            modifier = modifier,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.header_text)
        )
    } else {
        Text(
            text = buildWordmark(),
            modifier = modifier,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            color = colorResource(R.color.header_text)
        )
    }
}

@Composable
private fun SearchToggleButton(onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = Modifier.size(48.dp)) {
        Icon(
            painter = painterResource(R.drawable.ic_search),
            contentDescription = stringResource(R.string.header_search_content_description),
            tint = colorResource(R.color.header_text),
            modifier = Modifier.size(26.dp)
        )
    }
}

@Composable
private fun SortButton(sortOrder: SortOrder, onClick: () -> Unit) {
    val description = when (sortOrder) {
        SortOrder.NONE -> stringResource(R.string.header_sort_default_description)
        SortOrder.PRICE_ASC -> stringResource(R.string.header_sort_asc_description)
        SortOrder.PRICE_DESC -> stringResource(R.string.header_sort_desc_description)
    }
    val isActive = sortOrder != SortOrder.NONE
    val icon = when (sortOrder) {
        SortOrder.PRICE_ASC -> R.drawable.ic_sort_ascending
        SortOrder.PRICE_DESC -> R.drawable.ic_sort_descending
        SortOrder.NONE -> R.drawable.ic_sort
    }

    IconButton(onClick = onClick, modifier = Modifier.size(48.dp)) {
        Icon(
            painter = painterResource(icon),
            contentDescription = description,
            tint = if (isActive) colorResource(R.color.header_background) else colorResource(R.color.header_text),
            modifier = Modifier
                .size(26.dp)
                .background(
                    if (isActive) colorResource(R.color.header_text) else Color.Transparent,
                    CircleShape
                )
                .padding(2.dp)
        )
    }
}

@Composable
private fun FavoritesFilterButton(isActive: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = Modifier.size(48.dp)) {
        Icon(
            painter = painterResource(
                if (isActive) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
            ),
            contentDescription = if (isActive)
                stringResource(R.string.header_favorites_filter_active_description)
            else
                stringResource(R.string.header_favorites_filter_inactive_description),
            tint = colorResource(R.color.header_text),
            modifier = Modifier.size(26.dp)
        )
    }
}

@Composable
private fun SearchField(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .border(2.dp, colorResource(R.color.header_text), RoundedCornerShape(14.dp))
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.header_search_hint)) },
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

@Composable
private fun buildWordmark() = buildAnnotatedString {
    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
        append(stringResource(R.string.header_idealista_title))
    }
    withStyle(SpanStyle(fontWeight = FontWeight.Normal)) {
        append(stringResource(R.string.header_challenge_with_dot_title))
    }
}