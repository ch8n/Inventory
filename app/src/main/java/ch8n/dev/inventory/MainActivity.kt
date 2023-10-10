package ch8n.dev.inventory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ch8n.dev.inventory.data.domain.CategoryAttribute
import ch8n.dev.inventory.data.domain.CategoryAttributeTypes
import ch8n.dev.inventory.data.usecase.CreateInventoryCategory
import ch8n.dev.inventory.data.usecase.CreateInventoryItem
import ch8n.dev.inventory.data.usecase.DeleteInventoryCategory
import ch8n.dev.inventory.data.usecase.DeleteInventoryItem
import ch8n.dev.inventory.data.usecase.UpdateInventoryCategory
import ch8n.dev.inventory.data.usecase.UpdateInventoryItem
import ch8n.dev.inventory.ui.theme.InventoryTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store = AppStore()
        setContent {
            InventoryTheme {

                var categoryName by remember { mutableStateOf("") }
                val attributes = remember { mutableStateListOf<CategoryAttribute>() }


                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.sdp)
                ) {

                    LazyColumn(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.spacedBy(8.sdp)
                    ) {

                        item {
                            OutlinedTextField(
                                value = categoryName,
                                onValueChange = { categoryName = it },
                                label = { Text(text = "Category Name") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            AttributeOptionDropDown(
                                onAttributeSelected = { selectedAttribute ->
                                    attributes.add(selectedAttribute)
                                }
                            )
                        }


                        itemsIndexed(attributes) { index, attribute ->
                            when (attribute) {

                                is CategoryAttribute.DropDown -> DropDownCategoryUI(
                                    attribute = attribute,
                                    onAttributeUpdate = { updated ->
                                        attributes.set(index, updated)
                                    },
                                    onDeleteAttribute = {
                                        attributes.removeAt(index)
                                    }
                                )

                                is CategoryAttribute.Image -> ImageAttributeUI(
                                    attribute = attribute,
                                    onAttributeUpdate = { updated ->
                                        attributes.set(index, updated)
                                    },
                                    onDeleteAttribute = {
                                        attributes.removeAt(index)
                                    }
                                )

                                is CategoryAttribute.Numeric -> NumericAttributeUI(
                                    attribute = attribute,
                                    onAttributeUpdate = { updated ->
                                        attributes.set(index, updated)
                                    },
                                    onDeleteAttribute = {
                                        attributes.removeAt(index)
                                    }
                                )

                                is CategoryAttribute.Text -> TextAttributeUI(
                                    attribute = attribute,
                                    onAttributeUpdate = { updated ->
                                        attributes.set(index, updated)
                                    },
                                    onDeleteAttribute = {
                                        attributes.removeAt(index)
                                    }
                                )
                            }
                        }
                    }

                    OutlinedButton(
                        onClick = {
//                            store.createCategory(
//                                name = categoryName,
//                                attribute = listOf()
//                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                    ) {
                        Text(text = "+ Add Category")
                    }
                }
            }
        }
    }


}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DropDownCategoryUI(
    attribute: CategoryAttribute.DropDown,
    onAttributeUpdate: (attribute: CategoryAttribute.DropDown) -> Unit,
    onDeleteAttribute: () -> Unit,
) {
    var attributeName by remember { mutableStateOf(attribute.key) }
    var attributeOptions by remember { mutableStateOf(attribute.value) }

    LaunchedEffect(attributeName, attributeOptions) {
        onAttributeUpdate.invoke(
            attribute.copy(
                key = attributeName,
                value = attributeOptions
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.sdp)
            .border(1.sdp, Color.Gray, RoundedCornerShape(8.sdp))
            .padding(horizontal = 8.sdp),
        verticalArrangement = Arrangement.spacedBy(8.sdp)
    ) {

        AttributeHeader(
            text = "Drop Down Attribute",
            onDeleteAttribute = onDeleteAttribute
        )

        OutlinedTextField(
            value = attributeName,
            onValueChange = {
                attributeName = it
            },
            label = { Text(text = "Attribute Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedButton(
            onClick = {
                attributeOptions = attributeOptions + ""
            }
        ) {
            Text(text = "+ Add Option")
        }

        attributeOptions.forEachIndexed { index, text ->

            OutlinedTextField(
                value = text,
                onValueChange = {
                    val current = attributeOptions.toMutableList()
                    current.set(index, it)
                    attributeOptions = current
                },
                label = { Text(text = "Option ${index + 1}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.sdp),
                trailingIcon = {
                    IconButton(onClick = {
                        val current =
                            attributeOptions.toMutableList()
                        current.removeAt(index)
                        attributeOptions = current
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
}

@Composable
fun AttributeHeader(text: String, onDeleteAttribute: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            color = Color.DarkGray,
            fontSize = 24.ssp,
        )

        IconButton(onClick = onDeleteAttribute) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = null,
                modifier = Modifier.size(24.sdp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageAttributeUI(
    attribute: CategoryAttribute.Image,
    onAttributeUpdate: (updated: CategoryAttribute.Image) -> Unit,
    onDeleteAttribute: () -> Unit,
) {

    var attributeName by remember { mutableStateOf(attribute.key) }

    LaunchedEffect(attributeName) {
        onAttributeUpdate.invoke(
            attribute.copy(
                key = attributeName,
                value = emptyList()
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.sdp)
            .border(1.sdp, Color.Gray, RoundedCornerShape(8.sdp))
            .padding(horizontal = 8.sdp),
        verticalArrangement = Arrangement.spacedBy(8.sdp)
    ) {
        AttributeHeader(
            text = "Image Attribute",
            onDeleteAttribute = onDeleteAttribute
        )

        OutlinedTextField(
            value = attributeName,
            onValueChange = {
                attributeName = it
            },
            label = { Text(text = "Attribute Name") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NumericAttributeUI(
    attribute: CategoryAttribute.Numeric,
    onAttributeUpdate: (updated: CategoryAttribute.Numeric) -> Unit,
    onDeleteAttribute: () -> Unit,
) {
    var attributeName by remember { mutableStateOf(attribute.key) }
    var attributeValue by remember { mutableStateOf(attribute.value.toString()) }

    LaunchedEffect(attributeName, attributeValue) {
        onAttributeUpdate.invoke(
            attribute.copy(
                key = attributeName,
                value = attributeValue.toDoubleOrNull() ?: 0.0
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.sdp)
            .border(1.sdp, Color.Gray, RoundedCornerShape(8.sdp))
            .padding(horizontal = 8.sdp),
        verticalArrangement = Arrangement.spacedBy(8.sdp)
    ) {
        AttributeHeader(
            text = "Numeric Attribute",
            onDeleteAttribute = onDeleteAttribute
        )

        OutlinedTextField(
            value = attributeName,
            onValueChange = {
                attributeName = it
            },
            label = { Text(text = "Attribute Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = attributeValue,
            onValueChange = { attributeValue = it },
            label = { Text(text = "Attribute Value") },
            modifier = Modifier.fillMaxWidth()
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextAttributeUI(
    attribute: CategoryAttribute.Text,
    onAttributeUpdate: (updated: CategoryAttribute.Text) -> Unit,
    onDeleteAttribute: () -> Unit,
) {
    var attributeName by remember { mutableStateOf(attribute.key) }
    var attributeValue by remember { mutableStateOf(attribute.value) }

    LaunchedEffect(attributeName, attributeValue) {
        onAttributeUpdate.invoke(
            attribute.copy(
                key = attributeName,
                value = attributeValue
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.sdp)
            .border(1.sdp, Color.Gray, RoundedCornerShape(8.sdp))
            .padding(horizontal = 8.sdp),
        verticalArrangement = Arrangement.spacedBy(8.sdp)
    ) {
        AttributeHeader(
            text = "Text Attribute",
            onDeleteAttribute = onDeleteAttribute
        )

        OutlinedTextField(
            value = attributeName,
            onValueChange = {
                attributeName = it
            },
            label = { Text(text = "Attribute Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = attributeValue,
            onValueChange = { attributeValue = it },
            label = { Text(text = "Attribute Value") },
            modifier = Modifier.fillMaxWidth()
        )
    }

}

@Composable
fun AttributeOptionDropDown(
    onAttributeSelected: (attributes: CategoryAttribute) -> Unit
) {
    var attributePopupMenuOpen by remember { mutableStateOf(false) }
    val onPopUpDismissed = remember { { attributePopupMenuOpen = false } }
    val attributeOption = remember { CategoryAttributeTypes.values() }

    Box {
        OutlinedButton(
            onClick = {
                attributePopupMenuOpen = !attributePopupMenuOpen
            }
        ) {
            Text(text = "+ Add Attribute")
        }
        DropdownMenu(
            expanded = attributePopupMenuOpen,
            onDismissRequest = onPopUpDismissed,
            modifier = Modifier.background(Color.White),
        ) {
            attributeOption.forEach { type ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = type.name,
                            color = Color.Black,
                            fontSize = 14.ssp
                        )
                    },
                    onClick = {
                        when (type) {
                            CategoryAttributeTypes.Image -> {
                                onAttributeSelected.invoke(CategoryAttribute.Image())
                            }

                            CategoryAttributeTypes.Numeric -> {
                                onAttributeSelected.invoke(CategoryAttribute.Numeric())
                            }

                            CategoryAttributeTypes.DropDown -> {
                                onAttributeSelected.invoke(CategoryAttribute.DropDown())
                            }

                            CategoryAttributeTypes.Text -> {
                                onAttributeSelected.invoke(CategoryAttribute.Text())
                            }
                        }
                        onPopUpDismissed.invoke()
                    },
                    modifier = Modifier.background(Color.White)
                )
            }
        }
    }
}

class AppStore(
    val createCategory: CreateInventoryCategory = CreateInventoryCategory(),
    val updateCategory: UpdateInventoryCategory = UpdateInventoryCategory(),
    val deleteCategory: DeleteInventoryCategory = DeleteInventoryCategory(),
    val createItem: CreateInventoryItem = CreateInventoryItem(),
    val updateItem: UpdateInventoryItem = UpdateInventoryItem(),
    val deleteItem: DeleteInventoryItem = DeleteInventoryItem(),
) {

    fun createCategory(name: String, attribute: List<CategoryAttribute>) {
        createCategory.execute(name, attribute)
    }

}
