package com.crf450rl.companion.navigation

object Routes {
    const val HOME = "home"
    const val MAINTENANCE_LIST = "maintenance_list"
    const val MAINTENANCE_DETAIL = "maintenance_detail/{guideId}"
    const val TROUBLESHOOTING_LIST = "troubleshooting_list"
    const val TROUBLESHOOTING_DETAIL = "troubleshooting_detail/{issueId}"
    const val MODS_LIST = "mods_list"
    const val MODS_DETAIL = "mods_detail/{modId}"
    const val SPECS = "specs"
    const val ABOUT = "about"

    fun maintenanceDetail(id: String) = "maintenance_detail/$id"
    fun troubleshootingDetail(id: String) = "troubleshooting_detail/$id"
    fun modsDetail(id: String) = "mods_detail/$id"
}

data class DrawerDestination(
    val route: String,
    val label: String
)

val drawerDestinations = listOf(
    DrawerDestination(Routes.HOME, "Home"),
    DrawerDestination(Routes.MAINTENANCE_LIST, "Maintenance"),
    DrawerDestination(Routes.TROUBLESHOOTING_LIST, "Troubleshooting"),
    DrawerDestination(Routes.MODS_LIST, "Mods & Upgrades"),
    DrawerDestination(Routes.SPECS, "Specs Reference"),
    DrawerDestination(Routes.ABOUT, "About & Disclaimer")
)
