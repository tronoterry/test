package com.crf450rl.companion.data

object SpecsData {

    const val DISCLAIMER =
        "Figures below are general reference values for the CRF450RL platform compiled for this app, not a substitute " +
            "for Honda's official owner's manual or service manual. Exact specifications can vary by model year and " +
            "market — always confirm critical torque values, fluid capacities, and intervals against the official " +
            "documentation for your specific bike before performing maintenance."

    val sections: List<SpecSection> = listOf(
        SpecSection(
            title = "Engine",
            rows = listOf(
                SpecRow("Type", "449cc liquid-cooled SOHC Unicam, 4-valve single"),
                SpecRow("Bore x Stroke", "≈96.0 x 62.1 mm"),
                SpecRow("Fuel system", "PGM-FI electronic fuel injection"),
                SpecRow("Starting", "Electric start"),
                SpecRow("Transmission", "5-speed"),
                SpecRow("Final drive", "Chain")
            )
        ),
        SpecSection(
            title = "Chassis & Suspension",
            rows = listOf(
                SpecRow("Front suspension", "Showa coil-spring USD fork, ≈11.7 in travel"),
                SpecRow("Rear suspension", "Showa Pro-Link shock, ≈11.6 in travel"),
                SpecRow("Front brake", "Single disc, hydraulic"),
                SpecRow("Rear brake", "Single disc, hydraulic"),
                SpecRow("Front tire", "80/100-21"),
                SpecRow("Rear tire", "100/100-18")
            )
        ),
        SpecSection(
            title = "Dimensions & Capacity",
            rows = listOf(
                SpecRow("Seat height", "≈37.3 in"),
                SpecRow("Wheelbase", "≈58.5 in"),
                SpecRow("Curb weight", "≈260 lb"),
                SpecRow("Fuel tank capacity", "≈2.0 US gal")
            )
        ),
        SpecSection(
            title = "Common Service Reference Points",
            rows = listOf(
                SpecRow("Engine oil change", "≈Every 6 operating hours"),
                SpecRow("Valve clearance check", "More frequent than a street engine — confirm manual interval"),
                SpecRow("Chain slack", "≈8-13 mm typical range — confirm manual spec"),
                SpecRow("Coolant", "50/50 pre-mixed, motorcycle-specific, non-silicate"),
                SpecRow("Brake fluid", "DOT 4"),
                SpecRow("Engine oil grade", "10W-30 / 10W-40 4-stroke motorcycle oil")
            )
        )
    )
}
