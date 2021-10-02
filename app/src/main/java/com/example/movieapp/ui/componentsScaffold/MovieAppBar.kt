package com.example.movieapp.ui.componentsScaffold

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.movieapp.R
import com.example.movieapp.services.repository.QueryStringDSL
import com.example.movieapp.services.repository.remoteApi.SortProperty
import com.example.movieapp.ui.extensionsModifier.glassiness
import com.example.movieapp.ui.theme.MovieAppColorTheme
import com.example.movieapp.ui.theme.MovieAppTypography
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun MovieAppBar(onNavIconPressed: () -> Unit = { }, title: String) {

    var searchExpand by remember { mutableStateOf(false) }
    var filterExpand by remember { mutableStateOf(false) }

    Column(Modifier) {
        TopAppBar(
            modifier = Modifier.glassiness(
                murkiness = 0.7F,
                glassColor = MovieAppColorTheme.colors.primary2
            ),
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            contentColor = MaterialTheme.colors.onSurface,
            actions = {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    FilterDropDown(expanded = filterExpand, Modifier.align(Alignment.CenterVertically)) { filterExpand = false }
                    Image(
                        painter = painterResource(id = R.drawable.filter_icon),
                        contentDescription = "Search button",
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(20.dp)
                            .clickable { filterExpand = true }
                    )

                    SearBarComposable(expanded = searchExpand, Modifier.align(Alignment.CenterVertically)) { searchExpand = false }
                    Image(
                        painter = painterResource(id = R.drawable.search_left_black),
                        contentDescription = "Search button",
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(20.dp)
                            .clickable { searchExpand = true }
                    )
                }
            },
            title = {
                Text(
                    text = title,
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
                    textAlign = TextAlign.Center,
                    style = MovieAppTypography.h5,
                    fontWeight = FontWeight.Bold,
                    color = MovieAppColorTheme.colors.secondary2
                )
            },
            navigationIcon = {
                Image(
                    painter = painterResource(R.drawable.menu_black),
                    contentDescription = "Navigate back stack",
                    modifier = Modifier
                        .clickable(onClick = onNavIconPressed)
                        .padding(horizontal = 16.dp)
                        .size(20.dp)
                )
            }
        )
        Divider()
    }
}

@Composable
fun SearBarComposable(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {}
) {

    var searchText by remember { mutableStateOf("") }

    DropdownMenu(
        expanded,
        onDismiss,
        modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = searchText, onValueChange = { value -> searchText = value },
            textStyle = MovieAppTypography.body1.copy(color = MovieAppColorTheme.colors.ascent1),
            placeholder = {
                Text(
                    text = "Search for a Movie",
                    style = MovieAppTypography.body1,
                    color = MovieAppColorTheme.colors.ascent2.copy(alpha = 0.5F)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MovieAppColorTheme.colors.primary2,
                    shape = RoundedCornerShape(5.dp)
                )
                .border(width = 3.dp, color = MovieAppColorTheme.colors.ascent2),
            trailingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.search_left_black),
                    contentDescription = "Search button",
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .size(20.dp)
                )
            }
        )
    }
}

@Composable
fun FilterDropDown(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {}
) {

    val queryState = MutableStateFlow(QueryStringDSL {
        language = "en-US"
        page = 1
        include_adult = false
        include_video = true
        sort_by { SortProperty.Popularity.Desc }
    })

    DropdownMenu(
        expanded,
        onDismiss,
        offset = DpOffset(0.dp, 20.dp),
        modifier = modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .padding()
    ) {

        Row(Modifier.fillMaxWidth().padding(vertical = 10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Icon(imageVector = Icons.Filled.Cancel, contentDescription = "Clear Filter parameters")
            Text(text = "Select Filter Options", style = MovieAppTypography.body1)
            Icon(imageVector = Icons.Filled.Check, contentDescription = "Set Filter parameters")
        }

        FilterRow(
            title = "Language", options = listOf(
                "English US" to { queryState.value = queryState.value.copy(language = "en_US") },
                "German" to { queryState.value = queryState.value.copy(language = "de_GE") },
                "Russian" to { queryState.value = queryState.value.copy(language = "ru_RU") },
                "Spanish" to { queryState.value = queryState.value.copy(language = "es") },
            )
        )
        FilterRow(
            title = "Order", options = listOf(
                "Ascending" to { queryState.value = queryState.value.apply { sort_by { SortProperty.Popularity.Asc } } },
                "Descending" to { queryState.value = queryState.value.apply { sort_by { SortProperty.Popularity.Desc } } },
            )
        )
        FilterRow(
            title = "Include Adult Movies", options = listOf(
                "Yes" to { queryState.value = queryState.value.copy(include_adult = true) },
                "No" to { queryState.value = queryState.value.copy(include_adult = false) },
            )
        )
        FilterRow(
            title = "Vote Average", options = listOf(
                "10" to { queryState.value = queryState.value.copy(voteAverage = 10F) },
                "9" to { queryState.value = queryState.value.copy(voteAverage = 9F) },
                "8" to { queryState.value = queryState.value.copy(voteAverage = 8F) },
                "7" to { queryState.value = queryState.value.copy(voteAverage = 7F) },
                "6" to { queryState.value = queryState.value.copy(voteAverage = 6F) },
                "5" to { queryState.value = queryState.value.copy(voteAverage = 5F) },
                "4" to { queryState.value = queryState.value.copy(voteAverage = 4F) },
                "3" to { queryState.value = queryState.value.copy(voteAverage = 3F) },
                "2" to { queryState.value = queryState.value.copy(voteAverage = 2F) },
                "1" to { queryState.value = queryState.value.copy(voteAverage = 1F) },
            )
        )
    }
}

@Composable
fun FilterRow(title: String, options: List<Pair<String, () -> Unit>>) {

    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)) {

        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 10.dp),
            style = MovieAppTypography.h6
        )

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.horizontalScroll(
            rememberScrollState())) {

            var activeIndex: Int? by remember { mutableStateOf(null) }

            val setAsActive = { index: Int -> activeIndex = index }

            options.forEachIndexed { index, (optString, optFun) ->

                FilterItem(index, activeIndex, setIndex = setAsActive, value = optString, onClick = optFun)

            }
        }
    }
}

@Composable
fun FilterItem(index: Int, activeIndex: Int?, setIndex: (Int) -> Unit, onClick: () -> Unit, value: String) {

    OutlinedButton(
        onClick = { onClick(); setIndex(index) },
        border = if (index == activeIndex) null else ButtonDefaults.outlinedBorder,
        colors = if (index == activeIndex) ButtonDefaults.buttonColors() else ButtonDefaults.outlinedButtonColors(),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier.padding(5.dp)
    ) {
        Text(
            text = value,
            modifier = Modifier.padding(3.dp),
            style = MovieAppTypography.body1
        )
    }
}