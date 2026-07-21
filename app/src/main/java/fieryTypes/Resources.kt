package fieryTypes

data class Resources(
    var count: Int?,
    val type: ResourceTypes // Why not use defs.kt for telling what resource it is...? I might be dumb
)