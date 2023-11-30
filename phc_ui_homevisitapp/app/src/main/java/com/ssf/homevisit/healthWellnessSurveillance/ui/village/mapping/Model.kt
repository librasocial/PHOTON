package com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping

data class SurveillanceMappingModel(
        val menuIconId: Int,
        val menuItem: SurveillanceMappingMenuItem,
        var isSelected:Boolean?=false
)

data class MappingRawModel(
    var surveillanceMappingModel: SurveillanceMappingModel,
    var breedingSpot: String? = "",
    var breedingSite: String? = "",
    var isLarvaFound: String? = "",
    var solutionProvided: String? = "",
    var image: String?="",
    var nextInspection: String? = "",
    var disabled:Boolean?=null
)

sealed class SurveillanceMappingMenuItem(val menuName: String) {
    object CementAndStoneTank : SurveillanceMappingMenuItem("Cement/Stone tank")
    object DrumAndBarrel : SurveillanceMappingMenuItem("Drum/Barrel")
    object Pot : SurveillanceMappingMenuItem("Pot")
    object FreezerAndCooler : SurveillanceMappingMenuItem("Freezer/Cooler")
    object Sump : SurveillanceMappingMenuItem("Sump")
    object SolidWaste : SurveillanceMappingMenuItem("Solid waste")
    object GrindingStone : SurveillanceMappingMenuItem("Grinding stone")
    object TapPits : SurveillanceMappingMenuItem("Tap pits")
    object Tire : SurveillanceMappingMenuItem("Tire")
    object CoconutShell : SurveillanceMappingMenuItem("Coconut Shell")
    object Well : SurveillanceMappingMenuItem("Well")
    object Pond : SurveillanceMappingMenuItem("Pond")
    object Others : SurveillanceMappingMenuItem("Others")
}