package ch8n.dev.inventory.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.rememberMutableState
import ch8n.dev.inventory.sdp
import ch8n.dev.inventory.ssp
import ch8n.dev.inventory.ui.LocalUseCaseProvider
import ch8n.dev.inventory.ui.LocalNavigator
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ManageCategoryContent() {

    val store = LocalUseCaseProvider.current
    val navigator = LocalNavigator.current
    val scope = rememberCoroutineScope()

    val categories by store.getCategory.value.collectAsState()

    BottomSheet(
        backgroundContent = { bottomSheetState ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.sdp)
            ) {

                LazyColumn(
                    modifier = Modifier.padding(bottom = 64.sdp),
                    verticalArrangement = Arrangement.spacedBy(8.sdp)
                ) {

                    item {
                        Text(
                            text = "Manage Category Screen",
                            fontSize = 32.ssp,
                            color = Color.DarkGray
                        )
                    }

                    item {
                        Divider(modifier = Modifier.padding(bottom = 24.sdp))
                    }

                    item {
                        OutlinedButton(
                            onClick = {
                                scope.launch {
                                    bottomSheetState.show()
                                }
                            }
                        ) {
                            Text(text = "+ Add new Category")
                        }
                    }


                    itemsIndexed(categories) { index, category ->
                        OutlinedTextField(
                            value = category.name,
                            onValueChange = {},
                            label = { Text(text = "Option ${index + 1}") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.sdp),
                            trailingIcon = {
                                IconButton(onClick = {
                                    store.deleteCategory.execute(category.id)
                                }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = null
                                    )
                                }
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = Color.DarkGray
                            ),
                            readOnly = true
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(
                                    rememberScrollState()
                                )
                        ) {
                            category.sizes.forEach { size ->
                                Chip(
                                    onClick = { },
                                    modifier = Modifier.padding(
                                        horizontal = 16.sdp,
                                        vertical = 16.sdp
                                    )
                                ) {
                                    Text(text = size)
                                }
                            }
                        }
                    }

                    item {
                        Divider(modifier = Modifier.padding(bottom = 24.sdp))
                    }


                }
            }
        },
        sheetContent = { bottomSheetState ->
            CreateCategoryBottomSheetContent(
                onCreate = { category ->
                    store.createCategory.execute(category)
                    scope.launch {
                        bottomSheetState.hide()
                    }
                }
            )
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCategoryBottomSheetContent(
    onCreate: (InventoryCategory) -> Unit
) {

    var categoryName by rememberMutableState(init = "")
    var categorySizes by remember { mutableStateOf(emptyList<String>()) }

    LazyColumn(
        modifier = Modifier.padding(24.sdp),
        verticalArrangement = Arrangement.spacedBy(8.sdp)
    ) {

        item {
            Text(
                text = "Create Category",
                fontSize = 32.ssp,
                color = Color.DarkGray
            )
        }

        item {
            Divider(modifier = Modifier.padding(bottom = 24.sdp))
        }

        item {
            OutlinedTextField(
                value = categoryName,
                onValueChange = { categoryName = it },
                label = { Text(text = "Category Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.DarkGray
                )
            )
        }

        item {
            Text(
                text = "Category Item Sizes",
                color = Color.DarkGray,
                fontSize = 16.ssp,
            )
        }

        item {
            OutlinedButton(
                onClick = {
                    categorySizes = categorySizes + ""
                }
            ) {
                Text(text = "+ Add Size Option")
            }
        }

        itemsIndexed(categorySizes) { index, sizeOption ->

            OutlinedTextField(
                value = sizeOption,
                onValueChange = {
                    val current = categorySizes.toMutableList()
                    current.set(index, it)
                    categorySizes = current
                },
                label = { Text(text = "Option ${index + 1}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.sdp),
                trailingIcon = {
                    IconButton(onClick = {
                        val current = categorySizes.toMutableList()
                        current.removeAt(index)
                        categorySizes = current
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = null
                        )
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.DarkGray
                )
            )
        }


        item {
            OutlinedButton(
                onClick = {
                    onCreate.invoke(
                        InventoryCategory(
                            name = categoryName,
                            sizes = categorySizes
                        )
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 24.sdp, vertical = 16.sdp)
                    .fillMaxWidth()
            ) {
                Text(text = "+ Create Category")
            }
        }
    }
}