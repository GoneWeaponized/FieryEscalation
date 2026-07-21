package fieryTypes.parseOperations

import fieryTypes.ResourceTypes
import java.nio.ByteBuffer
import fieryTypes.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InvParse {
    private val _inventory = MutableStateFlow<List<Resources>>(emptyList())
    val inventory: StateFlow<List<Resources>> = _inventory.asStateFlow()

    private var money: Resources = Resources(0, ResourceTypes.MONEY)
    private var manpower: Resources = Resources(0, ResourceTypes.MANPOWER)

    fun parseInv(data: ByteBuffer) {
        this.money = Resources(data.getInt() and 0xFFFFFF, type = ResourceTypes.MONEY)
        this.manpower = Resources(data.getInt() and 0xFFFFFF, type = ResourceTypes.MANPOWER)
        
        _inventory.value = listOf(this.money, this.manpower)
    }

    companion object {
        val instance = InvParse()
    }
}
