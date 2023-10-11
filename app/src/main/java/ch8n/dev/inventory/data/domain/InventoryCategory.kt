package ch8n.dev.inventory.data.domain

enum class CategoryAttributeTypes {
    Image,
    Numeric,
    DropDown,
    Text
}

sealed class CategoryAttribute {

    object None : CategoryAttribute()

    data class Image(
        val key: String = "",
        val selectedValues: List<String> = emptyList()
    ) : CategoryAttribute()

    data class Numeric(
        val key: String = "",
        val selectedValue: Int = 0,
    ) : CategoryAttribute()

    data class DropDown(
        val key: String = "",
        val optionValues: List<String> = emptyList(),
        val selectedValue: String = "",
    ) : CategoryAttribute()

    data class Text(
        val key: String = "",
        val value: String = "",
    ) : CategoryAttribute()
}


data class InventoryCategory(
    val categoryId: String,
    val name: String,
    val attributes: List<CategoryAttribute>,
) {
    companion object {
        val NO_CATEGORY = InventoryCategory(
            categoryId = "",
            name = "",
            attributes = listOf()
        )
    }
}

data class InventoryItem(
    val itemId: String,
    val name: String,
    val categoryId: String,
    val attributeValues: List<CategoryAttribute>,
)