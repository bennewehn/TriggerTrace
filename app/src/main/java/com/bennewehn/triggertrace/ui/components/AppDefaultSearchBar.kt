package com.bennewehn.triggertrace.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDefaultSearchBar(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector = Icons.Default.Search,
    placeHolder: String = "Search",
    searchBarActive: Boolean,
    searchBarActiveChanged: (Boolean) -> Unit,
    colors: SearchBarColors = SearchBarDefaults.colors(),
    onSearchQueryChanged: (String) -> Unit,
    content: @Composable () -> Unit,
) {

    var searchQuery by remember { mutableStateOf("") }

    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = searchQuery,
                onQueryChange = {
                    searchQuery = it
                    onSearchQueryChanged(it) },
                onSearch = {},
                expanded = searchBarActive,
                onExpandedChange = searchBarActiveChanged,
                placeholder = { Text(text = placeHolder) },
                leadingIcon = { Icon(leadingIcon, null) },
                trailingIcon = {
                    if (searchBarActive) {
                        Icon(
                            modifier = Modifier.clickable {
                                if (searchQuery.isNotEmpty()) {
                                    searchQuery = ""
                                    onSearchQueryChanged(searchQuery)
                                } else {
                                    searchBarActiveChanged(false)
                                }
                            },
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        expanded = searchBarActive,
        onExpandedChange = searchBarActiveChanged,
        modifier = modifier,
        colors = colors,
        windowInsets = WindowInsets(top = 0.dp),
        content = {
            content()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "AppDefaultSearchBar Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "AppDefaultSearchBar Preview Light")
@Composable
private fun AppDefaultSearchBarPreview() {
    TriggerTraceTheme {
        AppDefaultSearchBar(onSearchQueryChanged = {}, searchBarActive = true, searchBarActiveChanged = {}) {

        }
    }
}

