package com.crf450rl.companion.data

object TroubleshootingData {

    val issues: List<TroubleshootingIssue> = listOf(
        TroubleshootingIssue(
            id = "wont_start",
            title = "Engine Won't Start",
            category = "Starting",
            icon = Icons2.Electrical,
            symptoms = listOf("No crank from starter", "Cranks but won't fire", "Fires then immediately dies"),
            summary = "Work through fuel, air, spark, and compression in order — almost every no-start traces back to one of these.",
            steps = listOf(
                GuideStep(
                    title = "Check the kill switch and side-stand cutoff",
                    description = "Confirm the engine stop switch is in the run position. If equipped, the side-stand safety system can prevent starting in gear with the stand down — try with the stand up and in neutral."
                ),
                GuideStep(
                    title = "Listen to the starter",
                    description = "If the starter doesn't spin at all, suspect a dead/disconnected battery, a blown fuse, or a loose ground strap.",
                    diagram = DiagramType.BATTERY_FUSE
                ),
                GuideStep(
                    title = "Check for fuel delivery",
                    description = "Confirm there's fuel in the tank and the fuel line isn't kinked or the tank vent isn't blocked (a blocked vent can starve the engine after running briefly)."
                ),
                GuideStep(
                    title = "Pull the spark plug",
                    description = "Remove the plug, ground it against the cylinder head, and crank the engine while watching for a strong blue spark. A weak orange spark or no spark points to ignition/electrical issues.",
                    diagram = DiagramType.SPARK_PLUG_GAP,
                    warning = "Keep the plug well away from the spark plug hole and any fuel while testing for spark to avoid igniting fuel vapor."
                ),
                GuideStep(
                    title = "Check for flooding",
                    description = "If the plug is wet/black and smells of fuel, the engine may be flooded. Follow your manual's flooded-engine starting procedure, or remove and dry the plug before retrying."
                ),
                GuideStep(
                    title = "Check compression",
                    description = "If fuel and spark both check out, a sudden loss of compression (e.g. a valve or piston ring problem) will also prevent starting. This requires a compression tester and is a sign of a deeper mechanical issue."
                )
            )
        ),
        TroubleshootingIssue(
            id = "stalling_bogging",
            title = "Stalling or Bogging",
            category = "Running",
            icon = Icons2.Valve,
            symptoms = listOf("Dies at idle", "Bogs when you crack the throttle", "Hesitates under load"),
            summary = "Bogging and stalling on a fuel-injected bike are usually airflow, fueling, or a fouled plug rather than a carburetor jetting issue.",
            steps = listOf(
                GuideStep(
                    title = "Check the air filter",
                    description = "A clogged or oversaturated filter restricts airflow and richens the mixture unpredictably, causing bogging.",
                    diagram = DiagramType.AIR_FILTER
                ),
                GuideStep(
                    title = "Inspect the spark plug",
                    description = "A fouled or worn plug can cause intermittent misfires that feel like bogging, especially under load."
                ),
                GuideStep(
                    title = "Check for fuel contamination or stale fuel",
                    description = "Old or water-contaminated fuel is a common cause of erratic running, especially after winter storage."
                ),
                GuideStep(
                    title = "Inspect for air leaks",
                    description = "A loose airbox boot, cracked intake boot, or loose hose can let in unmetered air, which throws off the air/fuel ratio the ECU is trying to control."
                ),
                GuideStep(
                    title = "Check the idle/fuel mapping",
                    description = "If the bike has had aftermarket ECU tuning or a fuel controller installed, an aggressive map can cause bogging off idle. Try reverting to a stock or milder map to isolate the cause."
                )
            )
        ),
        TroubleshootingIssue(
            id = "overheating",
            title = "Overheating",
            category = "Cooling",
            icon = Icons2.Coolant,
            symptoms = listOf("Coolant boiling/overflowing", "Steam from overflow tube", "Noticeable power loss after sustained running"),
            summary = "Liquid-cooled high-compression singles can overheat quickly at low speed or in technical terrain where airflow over the radiators drops.",
            steps = listOf(
                GuideStep(
                    title = "Stop and let it idle or shut off",
                    description = "If you see steam or the temperature feels excessive, stop in a safe spot. Idling can help circulate coolant, but if it's boiling over, shut the engine off and let it cool.",
                    warning = "Never remove the radiator cap while the engine is hot — pressurized, near-boiling coolant can cause severe burns."
                ),
                GuideStep(
                    title = "Check coolant level once cool",
                    description = "Once fully cooled, check the coolant level. A low level after overheating suggests it boiled out — top up and watch for a recurring leak.",
                    diagram = DiagramType.COOLANT_LEVEL
                ),
                GuideStep(
                    title = "Inspect for leaks and debris",
                    description = "Check hoses, the radiator, and water pump seal area for leaks, and check radiators for mud/debris packed into the fins blocking airflow."
                ),
                GuideStep(
                    title = "Check the radiator cap and fan (if equipped)",
                    description = "A weak radiator cap won't hold system pressure, lowering the boiling point. If your bike has a cooling fan, confirm it's coming on as expected."
                ),
                GuideStep(
                    title = "Reconsider riding style/gearing",
                    description = "Extended low-speed technical riding or overly tall gearing that forces high RPM at low road speed both increase heat load — adjusting pace or gearing can help in extreme conditions."
                )
            )
        ),
        TroubleshootingIssue(
            id = "power_loss",
            title = "Loss of Power / Poor Performance",
            category = "Running",
            icon = Icons2.Tire,
            symptoms = listOf("Feels flat compared to normal", "Won't pull to redline", "Excessive smoke"),
            summary = "Gradual power loss is often maintenance-related; sudden power loss points to a specific new fault.",
            steps = listOf(
                GuideStep(
                    title = "Air filter and exhaust",
                    description = "A clogged filter restricts intake; a dented or packed exhaust/spark arrestor restricts exit flow. Both directly reduce power.",
                    diagram = DiagramType.AIR_FILTER
                ),
                GuideStep(
                    title = "Spark plug condition",
                    description = "A worn or incorrectly gapped plug causes weak combustion, especially noticeable at high RPM."
                ),
                GuideStep(
                    title = "Valve clearance",
                    description = "Tight valve clearance reduces compression and can burn valves over time; this is a common cause of slowly fading power on a high-strung single.",
                    diagram = DiagramType.VALVE_CLEARANCE
                ),
                GuideStep(
                    title = "Fuel quality and octane",
                    description = "High-compression engines are sensitive to fuel quality. Old fuel or too-low octane can cause power loss and pinging/knock."
                ),
                GuideStep(
                    title = "Drivetrain drag",
                    description = "An over-tight chain, dragging brake, or worn clutch can all rob power that feels like an engine problem but isn't.",
                    diagram = DiagramType.CHAIN_SLACK
                )
            )
        ),
        TroubleshootingIssue(
            id = "electrical_lighting",
            title = "Electrical / Lighting Faults",
            category = "Electrical",
            icon = Icons2.Battery,
            symptoms = listOf("Headlight/taillight out", "Turn signals not working", "Intermittent electrical dropout"),
            summary = "The RL has more electrical systems than a pure off-road bike (lighting, signals), and off-road vibration is hard on connectors.",
            steps = listOf(
                GuideStep(
                    title = "Check the fuse box first",
                    description = "A blown fuse is the fastest thing to check and the most common cause of a single circuit dying all at once.",
                    diagram = DiagramType.BATTERY_FUSE
                ),
                GuideStep(
                    title = "Check the bulb/LED",
                    description = "Confirm the bulb itself hasn't failed and its socket isn't corroded — vibration is hard on bulb filaments."
                ),
                GuideStep(
                    title = "Inspect connectors along the harness",
                    description = "Trace the relevant circuit and check connectors for mud intrusion, corrosion, or a partially unplugged connector — extremely common after a crash or deep water crossing."
                ),
                GuideStep(
                    title = "Check the battery and charging system",
                    description = "Intermittent dropouts across multiple systems often point to a marginal battery or charging system rather than one specific wire."
                ),
                GuideStep(
                    title = "Use a multimeter to trace voltage",
                    description = "Work from the battery toward the faulty component checking for voltage at each connector to isolate exactly where the circuit breaks."
                )
            )
        ),
        TroubleshootingIssue(
            id = "clutch_issues",
            title = "Clutch Slipping or Dragging",
            category = "Drivetrain",
            icon = Icons2.Chain,
            symptoms = listOf("Engine revs without matching road speed", "Hard to find neutral", "Bike creeps forward in gear at idle with lever pulled in"),
            summary = "Clutch problems are usually adjustment, fluid (if hydraulic), or worn plates — work from simplest to most involved.",
            steps = listOf(
                GuideStep(
                    title = "Check cable/hydraulic free play",
                    description = "Confirm the clutch lever has correct free play per the manual. Too little free play can cause dragging; excessive cable stretch causes slipping or incomplete disengagement."
                ),
                GuideStep(
                    title = "Inspect cable routing or fluid level",
                    description = "For a cable clutch, check for fraying or a kinked housing. For hydraulic clutches, check the reservoir fluid level and look for air in the line."
                ),
                GuideStep(
                    title = "Check engine oil condition and level",
                    description = "Since the clutch runs in engine oil, the wrong oil type/viscosity, too much oil, or badly degraded oil can all cause slipping or dragging."
                ),
                GuideStep(
                    title = "Inspect clutch plates",
                    description = "If adjustment and oil are correct but slipping continues, the friction plates are likely worn beyond spec and need replacement.",
                    warning = "Riding with a badly slipping clutch generates excess heat that can warp plates and damage the basket, turning a simple plate replacement into a bigger repair."
                )
            )
        ),
        TroubleshootingIssue(
            id = "handling_issues",
            title = "Unstable Handling / Headshake",
            category = "Chassis",
            icon = Icons2.Suspension,
            symptoms = listOf("Front end feels vague or twitchy", "Headshake at speed", "Bike doesn't track straight"),
            summary = "Handling complaints are almost always tires, suspension setup, or wheel alignment before they're ever a frame problem.",
            steps = listOf(
                GuideStep(
                    title = "Check tire pressure",
                    description = "Pressure too low or too high for the conditions is the single most common cause of vague or twitchy handling.",
                    diagram = DiagramType.TIRE_PRESSURE
                ),
                GuideStep(
                    title = "Check wheel alignment",
                    description = "Confirm the rear wheel is aligned using the chassis marks — a misaligned wheel from a chain adjustment can cause the bike to handle asymmetrically."
                ),
                GuideStep(
                    title = "Recheck suspension sag and clickers",
                    description = "Sag set too far from spec, or compression/rebound clickers far from baseline, both commonly cause headshake or vague front-end feel.",
                    diagram = DiagramType.SUSPENSION_SAG
                ),
                GuideStep(
                    title = "Check steering head bearings",
                    description = "With the front wheel off the ground, check for roughness or play when turning the bars lock to lock. Worn or improperly torqued steering head bearings cause instability at speed."
                ),
                GuideStep(
                    title = "Check for a bent component",
                    description = "After any crash, check the forks for binding through their travel and sight down the frame/swingarm for visible misalignment."
                )
            )
        )
    )
}
