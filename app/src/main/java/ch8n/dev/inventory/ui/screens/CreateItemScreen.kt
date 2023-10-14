package ch8n.dev.inventory.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import ch8n.dev.inventory.ComposeStable
import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.data.domain.InventorySupplier
import ch8n.dev.inventory.rememberMutableState
import ch8n.dev.inventory.sdp
import ch8n.dev.inventory.ssp
import ch8n.dev.inventory.ui.LocalAppStore
import ch8n.dev.inventory.ui.LocalNavigator


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateItemScreen() {

    val navigator = LocalNavigator.current
    val store = LocalAppStore.current

    var itemName by rememberMutableState(init = "")
    var itemImages by rememberMutableState(init = ComposeStable(listOf<String>()))
    var selectedCategory by rememberMutableState(init = InventoryCategory.Empty)
    var selectedSupplier by rememberMutableState(init = InventorySupplier.Empty)
    var selectedSize by rememberMutableState(init = "")
    var itemColor by rememberMutableState(init = "")
    var itemQuantity by rememberMutableState(init = 0)
    var itemWeight by rememberMutableState(init = "")
    var purchasePrice by rememberMutableState(init = "")
    var sellPrice by rememberMutableState(init = "")

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
                    text = "Create Item Screen",
                    fontSize = 32.ssp,
                    color = Color.DarkGray
                )

                Divider(modifier = Modifier.padding(bottom = 24.sdp))
            }

            item {
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text(text = "Item Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.DarkGray
                    )
                )
            }

            item {
                ImageItemUI(
                    images = itemImages,
                    onImagesUpdated = { images ->
                        itemImages = ComposeStable(images)
                    }
                )
            }

            item {
                val categories by store.getCategory
                    .value.collectAsState(initial = ComposeStable(emptyList()))

                val dropdownOptions = remember(categories.value) {
                    categories.value.map { it.name }.toList().let { ComposeStable(it) }
                }

                OptionDropDown(
                    title = "Select Category",
                    dropdownOptions = dropdownOptions,
                    onSelected = { index ->
                        selectedCategory = categories.value.get(index)
                    }
                )

                AnimatedVisibility(visible = selectedCategory != InventoryCategory.Empty) {
                    OutlinedTextField(
                        value = selectedCategory.name,
                        onValueChange = {},
                        label = { Text(text = "Option Selected") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.sdp),
                        readOnly = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.DarkGray
                        ),
                        trailingIcon = {
                            IconButton(onClick = {
                                selectedCategory = InventoryCategory.Empty
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }
            }

            item {
                val supplier by store.getSupplier
                    .value.collectAsState(initial = ComposeStable(emptyList()))

                val dropdownOptions = remember(supplier.value) {
                    supplier.value.map { it.name }.toList().let { ComposeStable(it) }
                }

                OptionDropDown(
                    title = "Select Supplier",
                    dropdownOptions = dropdownOptions,
                    onSelected = { index ->
                        selectedSupplier = supplier.value.get(index)
                    }
                )

                AnimatedVisibility(visible = selectedSupplier != InventorySupplier.Empty) {
                    OutlinedTextField(
                        value = selectedSupplier.name,
                        onValueChange = {},
                        label = { Text(text = "Option Selected") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.sdp),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = {
                                selectedSupplier = InventorySupplier.Empty
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
            }

            item {
                OutlinedTextField(
                    value = itemColor,
                    onValueChange = {
                        itemColor = it
                    },
                    label = { Text(text = "Item Color") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    maxLines = 1,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.DarkGray
                    )
                )
            }

            item {
                OutlinedTextField(
                    value = itemWeight,
                    onValueChange = {
                        itemWeight = it
                    },
                    label = { Text(text = "Item weight") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    maxLines = 1,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.DarkGray
                    )
                )
            }

            item {

                val dropdownOptions = remember(selectedCategory) {
                    ComposeStable(selectedCategory.sizes)
                }

                OptionDropDown(
                    title = "Select Size",
                    dropdownOptions = dropdownOptions,
                    onSelected = { index ->
                        selectedSize = selectedCategory.sizes.get(index)
                    }
                )

                AnimatedVisibility(visible = selectedSize.isNotEmpty()) {
                    OutlinedTextField(
                        value = selectedSize,
                        onValueChange = {},
                        label = { Text(text = "Size Selected") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.sdp),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = {
                                selectedSize = ""
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
            }


            item {

                Text(text = "+ Item Quantity")

                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(onClick = { itemQuantity += 1 }) {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowUp,
                            contentDescription = null
                        )
                    }

                    Text(
                        text = itemQuantity.toString(),
                        color = Color.DarkGray,
                        fontSize = 14.ssp
                    )

                    IconButton(onClick = { itemQuantity -= 1 }) {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = purchasePrice,
                    onValueChange = {
                        purchasePrice = it
                    },
                    label = { Text(text = "Purchase Price") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    maxLines = 1,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.DarkGray
                    )
                )
            }

            item {
                OutlinedTextField(
                    value = sellPrice,
                    onValueChange = {
                        sellPrice = it
                    },
                    label = { Text(text = "Selling Price") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    maxLines = 1,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.DarkGray
                    )
                )
            }

        }

        OutlinedButton(
            onClick = {
                store.createItem.execute(
                    name = itemName,
                    images = itemImages.value,
                    category = selectedCategory,
                    itemQuantity = itemQuantity,
                    weight = itemWeight.toDoubleOrNull() ?: 0.0,
                    supplier = selectedSupplier,
                    sellPrice = sellPrice.toIntOrNull() ?: 0,
                    purchasePrice = purchasePrice.toIntOrNull() ?: 0,
                    itemSize = selectedSize,
                    itemColor = itemColor
                )
                navigator.back()
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Text(text = "+ Add Item")
        }
    }

}


@Composable
fun ImageItemUI(
    images: ComposeStable<List<String>>,
    onImagesUpdated: (images: List<String>) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.sdp)
            .border(1.sdp, Color.Gray, RoundedCornerShape(8.sdp))
            .padding(horizontal = 8.sdp),
        verticalArrangement = Arrangement.spacedBy(8.sdp)
    ) {

        Text(
            text = "Camera",
            fontSize = 16.ssp,
            color = Color.DarkGray
        )

        LazyRow() {
            item {
                Box(
                    modifier = Modifier
                        .size(150.sdp)
                        .border(2.sdp, Color.DarkGray)
                        .padding(4.sdp)
                        .border(2.sdp, Color.DarkGray)
                ) {
                    IconButton(
                        onClick = {
                            onImagesUpdated.invoke(images.value)
                        },
                        modifier = Modifier.align(Alignment.Center),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Rounded.AddCircle, contentDescription = null)
                            Text(text = "Camera")
                        }
                    }
                }
            }

            items(images.value) { attribute ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.sdp)
                        .size(150.sdp)
                        .border(2.sdp, Color.DarkGray)
                        .padding(4.sdp)
                        .border(2.sdp, Color.DarkGray)
                ) {
                    IconButton(
                        onClick = {

                        },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)
                    }
                }
            }
        }
    }
}


@Composable
fun OptionDropDown(
    title: String,
    dropdownOptions: ComposeStable<List<String>>,
    onSelected: (selectedIndex: Int) -> Unit
) {
    var popupMenuOpen by remember { mutableStateOf(false) }
    val onPopUpDismissed = remember { { popupMenuOpen = false } }

    Box {
        OutlinedButton(
            onClick = {
                popupMenuOpen = !popupMenuOpen
            },

            ) {
            Text(
                text = title,
                fontSize = 14.ssp
            )
        }
        DropdownMenu(
            expanded = popupMenuOpen,
            onDismissRequest = onPopUpDismissed,
            modifier = Modifier.background(Color.White),
        ) {
            dropdownOptions.value.forEachIndexed { index, options ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = options,
                            color = Color.Black,
                            fontSize = 14.ssp
                        )
                    },
                    onClick = {
                        onSelected.invoke(index)
                        onPopUpDismissed.invoke()
                    },
                    modifier = Modifier.background(Color.White)
                )
            }
        }
    }
}
