package ch8n.dev.inventory.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ch8n.dev.inventory.ComposeStable
import ch8n.dev.inventory.data.domain.CategoryAttribute
import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.sdp
import ch8n.dev.inventory.ssp
import ch8n.dev.inventory.ui.LocalAppStore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen() {

    val store = LocalAppStore.current
    var itemName by remember { mutableStateOf("") }
    var itemCategory by remember { mutableStateOf(InventoryCategory.NO_CATEGORY) }

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
                    text = "ItemScreen",
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
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                CategoryOptionDropDown(
                    onSelected = { category ->
                        itemCategory = category
                    }
                )
            }

            itemsIndexed(itemCategory.attributes) { index, attribute ->
                when (attribute) {
                    is CategoryAttribute.DropDown -> DropDownItemUI(
                        attribute = attribute,
                        onSelectOption = { attribute ->

                        }
                    )

                    is CategoryAttribute.Image -> ImageItemUI(
                        attribute = attribute,
                        onImagesUpdated = { images ->

                        }
                    )

                    is CategoryAttribute.Numeric -> NumericItemUI(
                        attribute = attribute,
                        onValueUpdated = { number ->

                        }
                    )

                    is CategoryAttribute.Text -> TextItemUI(
                        attribute = attribute,
                        onValueUpdated = { updated ->

                        }
                    )

                    is CategoryAttribute.None -> {}
                }
            }

        }

        OutlinedButton(
            onClick = {
                store.createItem.execute(
                    name = itemName,
                    attribute = itemCategory.attributes,
                    categoryId = itemCategory.categoryId,
                )
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
fun DropDownItemUI(
    attribute: CategoryAttribute.DropDown,
    onSelectOption: (attribute: String) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.sdp)
            .border(1.sdp, Color.Gray, RoundedCornerShape(8.sdp))
            .padding(horizontal = 8.sdp),
        verticalArrangement = Arrangement.spacedBy(8.sdp)
    ) {

        AttributeHeader(text = attribute.key)

        attribute.optionValues.forEachIndexed { index, attribute ->
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.sdp),
                onClick = { onSelectOption.invoke(attribute) },
            ) {
                Text(text = attribute)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextItemUI(
    attribute: CategoryAttribute.Text,
    onValueUpdated: (value: String) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.sdp)
            .border(1.sdp, Color.Gray, RoundedCornerShape(8.sdp))
            .padding(horizontal = 8.sdp),
        verticalArrangement = Arrangement.spacedBy(8.sdp)
    ) {

        AttributeHeader(text = attribute.key)

        var value by remember { mutableStateOf(attribute.value) }

        LaunchedEffect(value) {
            onValueUpdated.invoke(value)
        }

        OutlinedTextField(
            value = value,
            onValueChange = {
                value = it
            },
            label = { Text(text = "${attribute.key} value") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun NumericItemUI(
    attribute: CategoryAttribute.Numeric,
    onValueUpdated: (value: Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.sdp)
            .border(1.sdp, Color.Gray, RoundedCornerShape(8.sdp))
            .padding(horizontal = 8.sdp),
        verticalArrangement = Arrangement.spacedBy(8.sdp)
    ) {


        AttributeHeader(text = attribute.key)

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            var value by remember { mutableStateOf(attribute.selectedValue) }

            LaunchedEffect(value) {
                onValueUpdated.invoke(value)
            }

            IconButton(onClick = { value += 1 }) {
                Icon(imageVector = Icons.Rounded.KeyboardArrowUp, contentDescription = null)
            }

            Text(
                text = value.toString(),
                color = Color.DarkGray,
                fontSize = 14.ssp
            )

            IconButton(onClick = { value -= 1 }) {
                Icon(imageVector = Icons.Rounded.KeyboardArrowDown, contentDescription = null)
            }
        }
    }
}

@Composable
fun ImageItemUI(
    attribute: CategoryAttribute.Image,
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

        AttributeHeader(text = attribute.key)

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
                            onImagesUpdated.invoke(attribute.selectedValues)
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

            items(attribute.selectedValues) { attribute ->
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
fun CategoryOptionDropDown(
    onSelected: (category: InventoryCategory) -> Unit
) {
    val store = LocalAppStore.current

    var popupMenuOpen by remember { mutableStateOf(false) }
    val onPopUpDismissed = remember { { popupMenuOpen = false } }
    val categoryOptions by store.getCategory.value.collectAsState(initial = ComposeStable(emptyList()))

    Box {
        OutlinedButton(
            onClick = {
                popupMenuOpen = !popupMenuOpen
            }
        ) {
            Text(text = "+ Select Category")
        }
        DropdownMenu(
            expanded = popupMenuOpen,
            onDismissRequest = onPopUpDismissed,
            modifier = Modifier.background(Color.White),
        ) {
            categoryOptions.value.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = category.name,
                            color = Color.Black,
                            fontSize = 14.ssp
                        )
                    },
                    onClick = {
                        onSelected.invoke(category)
                        onPopUpDismissed.invoke()
                    },
                    modifier = Modifier.background(Color.White)
                )
            }
        }
    }
}
