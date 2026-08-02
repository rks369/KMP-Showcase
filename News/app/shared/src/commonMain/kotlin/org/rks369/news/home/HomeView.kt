package org.rks369.news.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.rks369.news.asyncSnapshotBuilder.AsyncSnapshotBuilder
import org.rks369.news.home.data.Article
import org.rks369.news.home.data.Section


@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    homeViewModel: HomeViewModel = viewModel { HomeViewModel()}
) {

    val selectedSection by homeViewModel.selectedSection.collectAsState()
    val articlesSnapshot by homeViewModel.articlesSnapshot.collectAsState()

    Scaffold {

        if (homeViewModel.canShowSelection.value) {
            SelectionBottomSheet(
                sections = homeViewModel.sectionsAvailable,
                selectedSection = selectedSection,
                onClose = {
                    homeViewModel.closeSelection()
                },
                onConfirm = { selected ->
                    homeViewModel.updateSelectedSection(selected)
                }
            )
        }

        Column {
            Text(
                homeViewModel.heading
            )
            Button(
                onClick = {
                    homeViewModel.openSelection()
                }
            ) {
                Text(
                    selectedSection?.title ?: "Select Section",
                )
            }

            AsyncSnapshotBuilder(
                snapshot = articlesSnapshot,
                onRetry = { homeViewModel.retry() },
                idle = { Text("Select a section to load news") }
            ) { articles: List<Article> ->
                LazyColumn {
                    items(articles) { article ->
                        Text(article.title, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionBottomSheet(
    sections: List<Section>,
    selectedSection: Section?,
    onClose: () -> Unit,
    onConfirm: (Section?) -> Unit,

    ) {
    ModalBottomSheet(
        onDismissRequest = onClose,
    ) {
        Column {
            Text(
                text = "Select Section",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )

            HorizontalDivider()

            LazyColumn {
                items(
                    items = sections,
                ) { section ->
                    section.title?.let { text ->

                        val isSelected = section.key == selectedSection?.key;
                        Column {
                            Text(
                                text = text,
                                style =  MaterialTheme.typography.bodyLarge,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onConfirm(section)
                                        onClose()
                                    }
                                    .padding(horizontal = 20.dp, vertical = 16.dp)
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}