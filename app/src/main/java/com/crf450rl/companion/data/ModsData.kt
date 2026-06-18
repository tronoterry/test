package com.crf450rl.companion.data

object ModsData {

    const val GENERAL_LEGAL_NOTE =
        "The CRF450RL is sold as a street-legal, emissions-compliant motorcycle. Removing or modifying emissions-related " +
            "equipment (catalytic converter, EVAP components) or significantly altering the exhaust can be illegal for " +
            "street use depending on your country/state/province, and can void warranty coverage. Check local regulations " +
            "before modifying emissions or noise-related components, and consider off-road-only use for non-compliant setups."

    val mods: List<ModGuide> = listOf(
        ModGuide(
            id = "exhaust",
            title = "Slip-On / Full Exhaust Upgrade",
            category = "Performance",
            icon = Icons2.Exhaust,
            difficulty = Difficulty.MODERATE,
            timeEstimate = "1-2 hrs",
            toolsNeeded = listOf("Socket set", "Spring puller (for full systems)", "Torque wrench"),
            partsNeeded = listOf("Aftermarket slip-on or full exhaust system", "New gaskets/springs as required"),
            legalNote = GENERAL_LEGAL_NOTE,
            summary = "A popular first mod — reduces weight, often improves mid-to-top power, and changes the exhaust note. A full system gives a bigger gain than a slip-on alone but costs more and may need a remap to use fully.",
            steps = listOf(
                GuideStep(
                    title = "Choose slip-on vs. full system",
                    description = "A slip-on replaces just the muffler/silencer and bolts to the stock header — cheaper and simpler. A full system replaces the header too, for a bigger but pricier gain."
                ),
                GuideStep(
                    title = "Remove the stock exhaust",
                    description = "Remove the side panel/seat as needed, unbolt the muffler mount, disconnect the header springs or flange bolts, and slide the system off."
                ),
                GuideStep(
                    title = "Install the new system",
                    description = "Fit new gaskets at every joint, install springs or bolts per the kit instructions, and make sure the muffler doesn't contact bodywork or the rider's leg at full suspension travel."
                ),
                GuideStep(
                    title = "Torque all fasteners",
                    description = "Torque header and mounting bolts to spec — exhaust fasteners that work loose can rattle out entirely on rough trails.",
                    diagram = DiagramType.TORQUE_WRENCH
                ),
                GuideStep(
                    title = "Consider remapping",
                    description = "A full exhaust system changes backpressure enough that an ECU remap or fuel controller is often recommended to get the most out of it and avoid running lean."
                )
            )
        ),
        ModGuide(
            id = "ecu_tuning",
            title = "ECU Remapping / Fuel Tuning",
            category = "Performance",
            icon = Icons2.Fuel,
            difficulty = Difficulty.MODERATE,
            timeEstimate = "30-60 min",
            toolsNeeded = listOf("Manufacturer tuning interface/app", "Laptop or phone (device-dependent)"),
            partsNeeded = listOf("Aftermarket fuel/ignition tuning device or ECU flash tool"),
            legalNote = GENERAL_LEGAL_NOTE,
            summary = "Adjusting fuel and ignition mapping can smooth out throttle response, fix lean spots from an aftermarket exhaust/filter, or tailor power delivery for terrain.",
            steps = listOf(
                GuideStep(
                    title = "Establish a baseline",
                    description = "Note how the bike runs stock (or on your current map) before changing anything, so you have something to compare against."
                ),
                GuideStep(
                    title = "Confirm compatibility",
                    description = "Check that the tuning device/app explicitly supports the 2025 CRF450RL — fueling needs change between model years, so always use a map made for your specific year."
                ),
                GuideStep(
                    title = "Start with a manufacturer-provided map",
                    description = "Use a known-good preset map for your combination of exhaust/filter rather than building a custom map from scratch on your first attempt."
                ),
                GuideStep(
                    title = "Test incrementally",
                    description = "Ride and evaluate one change at a time — idle quality, low-end response, then top end — so you know what each adjustment actually did.",
                    warning = "Overly aggressive maps can cause detonation/knock on a high-compression engine, which can damage the piston and rings. When in doubt, tune conservatively."
                ),
                GuideStep(
                    title = "Recheck spark plug color",
                    description = "After settling on a map, pull the plug after a ride at sustained load to check the color matches a healthy, correctly-fueled engine."
                )
            )
        ),
        ModGuide(
            id = "suspension_resprung",
            title = "Suspension Springs & Valving",
            category = "Chassis",
            icon = Icons2.Suspension,
            difficulty = Difficulty.ADVANCED,
            timeEstimate = "Varies — typically a specialist shop job",
            toolsNeeded = listOf("Suspension-specific tools (spanner sockets, seal drivers)"),
            partsNeeded = listOf("Correct-rate fork springs and shock spring for rider weight", "Suspension oil"),
            summary = "Stock springs are chosen for an average rider weight. If you're well outside that range, correct-rate springs (and often re-valving) make a bigger handling improvement than any clicker adjustment can.",
            steps = listOf(
                GuideStep(
                    title = "Determine correct spring rates",
                    description = "Use a spring rate calculator or supplier chart based on your weight with gear on, and your riding style/terrain."
                ),
                GuideStep(
                    title = "Set sag as a baseline",
                    description = "Measure sag on the current setup first as a reference point before changes.",
                    diagram = DiagramType.SUSPENSION_SAG
                ),
                GuideStep(
                    title = "Replace springs",
                    description = "Forks and shock must be disassembled to swap springs — this involves releasing spring preload and handling pressurized components safely.",
                    warning = "Shocks have a separate gas-charged reservoir. Don't disassemble a shock body without the correct tools and knowledge of how to safely depressurize it — consider a suspension shop for this part."
                ),
                GuideStep(
                    title = "Consider valving changes",
                    description = "A spring rate change shifts the suspension's operating range, so re-valving (changing internal shim stacks) is often recommended alongside a respring for the best result."
                ),
                GuideStep(
                    title = "Re-set sag and clickers",
                    description = "After the rebuild, reset race/free sag and re-baseline your compression/rebound clickers since spring and valving changes reset what 'neutral' feels like."
                )
            )
        ),
        ModGuide(
            id = "gearing",
            title = "Sprocket Gearing Change",
            category = "Drivetrain",
            icon = Icons2.Chain,
            difficulty = Difficulty.EASY,
            timeEstimate = "30-45 min",
            toolsNeeded = listOf("Socket set", "Torque wrench", "Chain breaker/press tool if changing chain length"),
            partsNeeded = listOf("Front and/or rear sprocket", "New chain if length changes"),
            summary = "Changing the front/rear sprocket tooth count trades top speed for acceleration (or vice versa) without touching the engine. A common, reversible mod for matching the bike to trail vs. open terrain.",
            steps = listOf(
                GuideStep(
                    title = "Decide the direction",
                    description = "A larger rear sprocket (or smaller front) gears down for stronger acceleration and easier low-speed control, at the cost of top speed — popular for tight trails."
                ),
                GuideStep(
                    title = "Check chain length",
                    description = "A significant gearing change may need a longer or shorter chain to maintain correct rear wheel adjustment range. Check before ordering parts."
                ),
                GuideStep(
                    title = "Replace the sprocket(s)",
                    description = "Remove the rear wheel (for the rear sprocket) or the front sprocket cover and circlip/lockwasher (for the front), swap the sprocket, and reassemble."
                ),
                GuideStep(
                    title = "Set chain slack and alignment",
                    description = "After reinstalling, reset chain slack and confirm wheel alignment marks before riding.",
                    diagram = DiagramType.CHAIN_SLACK
                ),
                GuideStep(
                    title = "Torque everything",
                    description = "Torque the rear axle nut and front sprocket retaining hardware to spec — these are high-consequence fasteners.",
                    diagram = DiagramType.TORQUE_WRENCH
                )
            )
        ),
        ModGuide(
            id = "protection",
            title = "Skid Plate, Hand Guards & Bark Busters",
            category = "Protection",
            icon = Icons2.Fabrication,
            difficulty = Difficulty.EASY,
            timeEstimate = "30-60 min total",
            toolsNeeded = listOf("Socket/wrench set appropriate to the kit"),
            partsNeeded = listOf("Skid plate", "Hand guards/bark busters", "Mounting hardware (usually included)"),
            summary = "Low-cost, high-value protection mods. A skid plate guards the frame rails and water pump area from rock strikes; hand guards protect levers and hands from brush and falls.",
            steps = listOf(
                GuideStep(
                    title = "Install the skid plate",
                    description = "Most bolt to existing frame mounting points under the engine. Check it doesn't trap mud against the exhaust header or block oil drain/filter access for maintenance."
                ),
                GuideStep(
                    title = "Mount hand guards",
                    description = "Hand guard mounts typically clamp to the handlebar alongside the lever perch and bar end. Set the angle so the guard protects the lever without your hand hitting the bar mount in a crash."
                ),
                GuideStep(
                    title = "Check lever and cable clearance",
                    description = "After installing guards, run the bars full lock to lock and check nothing binds against cables, the brake line, or the bodywork."
                ),
                GuideStep(
                    title = "Re-torque after a break-in ride",
                    description = "Bolt-on plastic and aluminum guards often settle slightly after the first ride — recheck all mounting bolts shortly after installation."
                )
            )
        ),
        ModGuide(
            id = "graphics_seat",
            title = "Graphics Kit & Seat Cover",
            category = "Cosmetic",
            icon = Icons2.Fabrication,
            difficulty = Difficulty.EASY,
            timeEstimate = "1-2 hrs",
            toolsNeeded = listOf("Heat gun or hair dryer", "Squeegee/credit card", "Trim removal tools"),
            partsNeeded = listOf("Graphics kit", "Seat cover", "Spray adhesive (for some background kits)"),
            summary = "Purely cosmetic but the most commonly done mod — also a good opportunity to inspect plastics and the seat foam for cracks or rot while everything is off.",
            steps = listOf(
                GuideStep(
                    title = "Remove and clean panels",
                    description = "Remove each plastic panel and the seat, then thoroughly clean with isopropyl alcohol — any wax, dirt, or oil residue will cause graphics to peel later."
                ),
                GuideStep(
                    title = "Dry fit the graphics",
                    description = "Lay each piece in place without removing the backing first to confirm orientation and trim lines around vents and bolt holes."
                ),
                GuideStep(
                    title = "Apply with heat",
                    description = "Peel back a small section of backing at a time, apply, and use gentle heat with a squeegee to work out air bubbles, especially over curves."
                ),
                GuideStep(
                    title = "Trim and finish edges",
                    description = "Trim excess material at panel edges with a sharp blade for a clean line, then heat-set the edges so they don't lift in the wind."
                ),
                GuideStep(
                    title = "Fit the seat cover",
                    description = "Stretch the new cover over the foam pan, working from the middle outward, and re-staple or clip it per the kit's instructions before reinstalling the seat."
                )
            )
        ),
        ModGuide(
            id = "bars_ergos",
            title = "Handlebar & Ergonomics Setup",
            category = "Ergonomics",
            icon = Icons2.Wrench,
            difficulty = Difficulty.EASY,
            timeEstimate = "30-45 min",
            toolsNeeded = listOf("Allen key/socket set", "Torque wrench"),
            partsNeeded = listOf("Handlebar (optional)", "Bar risers (optional)", "Grips"),
            summary = "Bend, height, and grip choice make a real difference in comfort and control, especially for taller/shorter riders than the stock setup is designed around.",
            steps = listOf(
                GuideStep(
                    title = "Assess your current fit",
                    description = "Stand on the pegs in your normal riding position — your handlebar should let you stand and sit comfortably without excessive reach or wrist bend."
                ),
                GuideStep(
                    title = "Swap bars or add risers",
                    description = "Loosen and remove the top clamp, controls, and bar, then install the new bar or add risers under the existing one, keeping cable/line routing in mind."
                ),
                GuideStep(
                    title = "Reposition controls",
                    description = "Reinstall levers, switches, and grips at a comfortable angle, keeping the front brake and clutch lever angle consistent with a relaxed wrist."
                ),
                GuideStep(
                    title = "Torque the clamps",
                    description = "Torque the bar clamp bolts evenly in a crossing pattern to spec — uneven torque can crack a bar or clamp under load.",
                    diagram = DiagramType.TORQUE_WRENCH,
                    warning = "Never exceed the torque spec on carbon or thin-wall aluminum bars — overtightening clamps is a known cause of handlebar failure."
                ),
                GuideStep(
                    title = "Fit new grips",
                    description = "Use grip glue or safety wire so grips can't spin or pull off the bar — a slipping grip in a turn is a serious hazard."
                )
            )
        )
    )
}
