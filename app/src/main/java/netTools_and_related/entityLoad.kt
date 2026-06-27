package netTools_and_related

//=================INFO==================
/*
Will be used to unify the server entity responses so that, the horrible code
will be less horrible
 */

import netTools_and_related.*

enum class CommandType (val command: String) {
    GET_PLAYERS("GET_PLAYERS"),
    GET_ENTITIES("OBTAIN_ENTITIES"),
    GET_DETECTED("OBTAIN_DETECTED"), // gonna add more later
}