package ch8n.dev.inventory.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.rememberModalBottomSheetState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import ch8n.dev.inventory.ComposeStable
import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.data.domain.InventoryItem
import ch8n.dev.inventory.data.domain.InventorySupplier
import ch8n.dev.inventory.rememberMutableState
import ch8n.dev.inventory.sdp
import ch8n.dev.inventory.ssp
import ch8n.dev.inventory.ui.LocalAppStore
import ch8n.dev.inventory.ui.LocalNavigator
import kotlinx.coroutines.launch


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
fun ManageItemScreen() {

    val navigator = LocalNavigator.current
    val store = LocalAppStore.current
    val scope = rememberCoroutineScope()

    val categories by store.getCategory.value.collectAsState(initial = emptyList())
    var selectedItem by rememberMutableState(init = InventoryItem.New)

    var purchasePrice by rememberMutableState(init = "")
    var sellingPrice by rememberMutableState(init = "")
    var weight by rememberMutableState(init = "")

    LaunchedEffect(weight) {
        if (weight.toDoubleOrNull() != null) {
            selectedItem = selectedItem.copy(
                weight = weight.toDouble()
            )
        }
    }

    LaunchedEffect(sellingPrice) {
        if (sellingPrice.toIntOrNull() != null) {
            selectedItem = selectedItem.copy(
                sellingPrice = sellingPrice.toInt()
            )
        }
    }

    LaunchedEffect(purchasePrice) {
        if (purchasePrice.toIntOrNull() != null) {
            selectedItem = selectedItem.copy(
                purchasePrice = purchasePrice.toInt()
            )
        }
    }

    BottomSheet(
        backgroundContent = { bottomSheetState ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(24.sdp)
            ) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f),
                    verticalArrangement = Arrangement.spacedBy(8.sdp)
                ) {

                    item {
                        Text(
                            text = "Create/Update Item",
                            fontSize = 32.ssp,
                            color = Color.DarkGray
                        )

                        Divider(modifier = Modifier.padding(bottom = 24.sdp))
                    }

                    item {
                        OutlinedTextField(
                            value = selectedItem.name,
                            onValueChange = {
                                selectedItem = selectedItem.copy(name = it)
                            },
                            label = { Text(text = "Item Name") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = Color.DarkGray
                            )
                        )
                    }

                    item {
                        ImageItemUI(
                            images = ComposeStable(selectedItem.images),
                            onImagesUpdated = { images ->
                                selectedItem = selectedItem.copy(images = images)
                            }
                        )
                    }

                    item {

                        OptionDropDown(
                            title = "Select Category",
                            dropdownOptions = categories.map { it.name },
                            onSelected = { index ->
                                selectedItem = selectedItem.copy(
                                    category = categories.get(index)
                                )
                            }
                        )

                        AnimatedVisibility(visible = selectedItem.category != InventoryCategory.Empty) {
                            OutlinedTextField(
                                value = selectedItem.category.name,
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
                                        selectedItem = selectedItem.copy(
                                            category = InventoryCategory.Empty
                                        )
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

                        val dropdownOptions = supplier.value.map { it.name }

                        OptionDropDown(
                            title = "Select Supplier",
                            dropdownOptions = dropdownOptions,
                            onSelected = { index ->
                                selectedItem = selectedItem.copy(
                                    supplier = supplier.value.get(index)
                                )
                            }
                        )

                        AnimatedVisibility(visible = selectedItem.supplier != InventorySupplier.Empty) {
                            OutlinedTextField(
                                value = selectedItem.supplier.name,
                                onValueChange = {},
                                label = { Text(text = "Option Selected") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.sdp),
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = {
                                        selectedItem = selectedItem.copy(
                                            supplier = InventorySupplier.Empty
                                        )
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
                            value = selectedItem.itemColor,
                            onValueChange = {
                                selectedItem = selectedItem.copy(
                                    itemColor = it
                                )
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
                            value = weight,
                            onValueChange = {
                                weight = it
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

                        OptionDropDown(
                            title = "Select Size",
                            dropdownOptions = selectedItem.category.sizes,
                            onSelected = { index ->
                                selectedItem = selectedItem.copy(
                                    itemSize = selectedItem.category.sizes.get(index)
                                )
                            }
                        )

                        AnimatedVisibility(visible = selectedItem.itemSize.isNotEmpty()) {
                            OutlinedTextField(
                                value = selectedItem.itemSize,
                                onValueChange = {},
                                label = { Text(text = "Size Selected") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.sdp),
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = {
                                        selectedItem = selectedItem.copy(
                                            itemSize = ""
                                        )
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

                            IconButton(onClick = {
                                selectedItem = selectedItem.copy(
                                    itemQuantity = selectedItem.itemQuantity + 1
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.Rounded.KeyboardArrowUp,
                                    contentDescription = null
                                )
                            }

                            Text(
                                text = selectedItem.itemQuantity.toString(),
                                color = Color.DarkGray,
                                fontSize = 14.ssp
                            )

                            IconButton(onClick = {
                                selectedItem = selectedItem.copy(
                                    itemQuantity = selectedItem.itemQuantity - 1
                                )
                            }) {
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
                            value = sellingPrice,
                            onValueChange = {
                                sellingPrice = it
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

                Column(
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {

                    OutlinedButton(
                        onClick = {
                            store.upsertItem.execute(selectedItem)
                            navigator.back()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "+ Save Item Changes")
                    }

                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                bottomSheetState.show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "+ Select Existing Item")
                    }
                }


            }
        },
        sheetContent = { bottomSheetState ->
            SearchItemBottomSheetContent(
                onSelect = { item ->
                    selectedItem = item
                    weight = item.weight.toString()
                    purchasePrice = item.purchasePrice.toString()
                    sellingPrice = item.sellingPrice.toString()
                    scope.launch {
                        bottomSheetState.hide()
                    }
                },
                onDelete = { item ->
                    store.deleteItem.execute(item.id)
                    navigator.back()
                }
            )
        }
    )


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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(
    sheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    ),
    backgroundContent: @Composable (sheetState: ModalBottomSheetState) -> Unit,
    sheetContent: @Composable ColumnScope.(sheetState: ModalBottomSheetState) -> Unit,
) {

    ModalBottomSheetLayout(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.sdp, topEnd = 16.sdp),
        sheetContent = {
            sheetContent(sheetState)
        },
        content = {
            backgroundContent.invoke(sheetState)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SearchItemBottomSheetContent(
    onSelect: (item: InventoryItem) -> Unit,
    onDelete: (item: InventoryItem) -> Unit,
) {

    val store = LocalAppStore.current
    var searchQuery by rememberMutableState(init = "")
    var selectedCategory by rememberMutableState(init = InventoryCategory.Empty)
    val items by store.getItems.filter(searchQuery, selectedCategory)
        .collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.95f)
            .padding(24.sdp)
    ) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.sdp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            item {
                Text(
                    text = "Items list",
                    fontSize = 32.ssp,
                    color = Color.DarkGray
                )
            }

            item {
                Divider(modifier = Modifier.padding(bottom = 24.sdp))
            }

            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                    },
                    label = { Text(text = "Search Item") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.DarkGray
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            searchQuery = ""
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = null
                            )
                        }
                    }
                )
            }

            item {
                val categories by store.getCategory.value.collectAsState(initial = emptyList())

                OptionDropDown(
                    title = "Select Category",
                    dropdownOptions = categories.map { it.name },
                    onSelected = { index ->
                        selectedCategory = categories.get(index)
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

            itemsIndexed(items) { index, item ->

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.sdp, Color.Gray)
                        .padding(8.sdp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.sdp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(150.sdp)
                                .border(2.sdp, Color.DarkGray)
                        )

                        Column {
                            Text(text = item.name)
                            Text(text = "Color : ${item.itemColor}")
                            Text(text = "Size : ${item.itemSize}")
                            Text(text = "Weight : ${item.weight}")
                            Text(text = "Total Quantity : ${item.itemQuantity}")
                            Text(text = "Selling : ${item.sellingPrice}")
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        OutlinedButton(
                            onClick = {
                                onDelete.invoke(item)
                            },
                        ) {
                            Text(
                                text = "Delete Item",
                                fontSize = 14.ssp
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                onSelect.invoke(item)
                            },
                        ) {
                            Text(
                                text = "Select Item",
                                fontSize = 14.ssp
                            )
                        }
                    }

                }
            }
        }
    }
}


@Composable
fun OptionDropDown(
    title: String,
    dropdownOptions: List<String>,
    onSelected: (selectedIndex: Int) -> Unit,
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
            dropdownOptions.forEachIndexed { index, options ->
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
