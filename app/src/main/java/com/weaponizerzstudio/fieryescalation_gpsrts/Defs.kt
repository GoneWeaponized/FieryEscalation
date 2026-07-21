package com.weaponizerzstudio.fieryescalation_gpsrts

// Kindly, do NOT change values: Might cause instability.
public class Defs {
    val login: Int = 0 //  used to indicate a login (more like register a player) event
    val getPlayers: Int = 1 //  self-explanatory
    val getStructures: Int = 2 // self-explanatory
    val updateInvEvent: Int = 3 // Purchased Nukes, Open for trade etc...
    val playerDataMine: Int = 4
    val playerEvent: Int = 5 // join, died, farted, banned etc...
    val error: Int = 6 // Multiples of 6 will be used to indicate errors.
}