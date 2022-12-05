package bean

data class Item(
    var title: String,
    var name: String,
    var price: Int,
    var amountOrder: Int = 0,  //订购一项菜品的数量
)
