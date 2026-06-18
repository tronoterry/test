package com.crf450rl.companion.data

object MaintenanceData {

    val guides: List<Guide> = listOf(
        Guide(
            id = "pre_ride",
            title = "Pre-Ride Inspection",
            category = "General",
            icon = Icons2.Bike,
            difficulty = Difficulty.EASY,
            timeEstimate = "10-15 min",
            interval = "Before every ride",
            toolsNeeded = listOf("Tire pressure gauge", "Flashlight"),
            summary = "A quick head-to-toe check before you ride catches most problems while they're still cheap and safe to fix.",
            steps = listOf(
                GuideStep(
                    title = "Tires & wheels",
                    description = "Check both tires for correct pressure, cuts, or embedded debris. Spin each wheel and confirm it runs true with no wobble, and check spokes for looseness by tapping them.",
                    diagram = DiagramType.TIRE_PRESSURE,
                    tip = "Off-road riders typically run lower pressures than street bikes for grip — see the Specs tab for a starting point, then adjust to terrain."
                ),
                GuideStep(
                    title = "Controls & cables",
                    description = "Check clutch and throttle for smooth, full travel with no binding. Throttle must snap closed on its own from full open."
                ),
                GuideStep(
                    title = "Lights & electrical",
                    description = "Since the RL is street-legal, verify headlight, taillight/brake light, and turn signals all work, and the battery is securely mounted.",
                    diagram = DiagramType.BATTERY_FUSE
                ),
                GuideStep(
                    title = "Oil & coolant levels",
                    description = "With the bike level, check the oil level window and confirm coolant is visible in the reservoir/radiator.",
                    diagram = DiagramType.OIL_LEVEL
                ),
                GuideStep(
                    title = "Chassis & fasteners",
                    description = "Quickly check axle nuts, sprocket bolts, bar clamps, and footpeg bolts are tight. Look underneath for fresh leaks.",
                    diagram = DiagramType.TORQUE_WRENCH,
                    warning = "Never ride with a loose axle nut or sprocket bolt — these can fail suddenly and cause loss of control."
                ),
                GuideStep(
                    title = "Chain",
                    description = "Check chain slack and lubrication. A dry or over-tight chain wears the sprockets and can damage the case if it snaps.",
                    diagram = DiagramType.CHAIN_SLACK
                ),
                GuideStep(
                    title = "Side stand & kill switch",
                    description = "Confirm the side stand retracts fully and spring-loads correctly, and that the engine stop switch and (if equipped) the side-stand safety cutoff work."
                )
            )
        ),
        Guide(
            id = "oil_change",
            title = "Engine Oil & Filter Change",
            category = "Engine",
            icon = Icons2.Oil,
            difficulty = Difficulty.MODERATE,
            timeEstimate = "30-45 min",
            interval = "Every ~6 operating hours, or per your owner's manual schedule",
            toolsNeeded = listOf("17mm socket/wrench (drain bolt)", "Oil filter wrench", "Torque wrench", "Drain pan", "Funnel"),
            partsNeeded = listOf("Engine oil (10W-30 or 10W-40 4-stroke MX oil)", "OEM oil filter", "Drain bolt sealing washer"),
            summary = "The CRF450RL has a small oil capacity and works hard, so oil changes are far more frequent than a street bike. Always change the filter at every oil change.",
            safetyNote = "Run the engine for a few minutes first so the oil drains warm and carries more contaminants out with it — but not so long that it's hot enough to burn you.",
            steps = listOf(
                GuideStep(
                    title = "Warm up and position the bike",
                    description = "Start the engine and let it idle for 3-5 minutes, then shut it off. Place the bike upright and level on a stand."
                ),
                GuideStep(
                    title = "Drain the old oil",
                    description = "Place a drain pan underneath, remove the drain bolt at the bottom of the engine, and let the oil fully drain. Inspect the drain bolt washer and replace it if flattened or damaged.",
                    diagram = DiagramType.OIL_LEVEL,
                    warning = "Drained oil can be hot. Wear gloves and eye protection."
                ),
                GuideStep(
                    title = "Replace the oil filter",
                    description = "Remove the filter cover bolts, pull the old filter and O-ring, and install a new OEM filter with a fresh O-ring lightly oiled before fitting."
                ),
                GuideStep(
                    title = "Reinstall the drain bolt",
                    description = "Fit the drain bolt with its washer and torque it to the spec in your service manual — typically a light automotive-style torque, not gorilla-tight on the aluminum case.",
                    diagram = DiagramType.TORQUE_WRENCH,
                    warning = "Overtightening the drain bolt strips the aluminum threads in the case, which turns a $10 job into an expensive repair."
                ),
                GuideStep(
                    title = "Refill with the correct quantity",
                    description = "Pour in the specified amount of fresh oil through the filler. Refer to the Specs tab for the approximate capacity, and always confirm with the level window — start lower and top up rather than overfilling."
                ),
                GuideStep(
                    title = "Check the level and check for leaks",
                    description = "Start the engine, let it idle, then shut off and let it settle for a minute. With the bike level, confirm the oil sits within the level window, and check the drain bolt and filter cover for leaks.",
                    diagram = DiagramType.OIL_LEVEL
                )
            )
        ),
        Guide(
            id = "air_filter",
            title = "Air Filter Cleaning",
            category = "Engine",
            icon = Icons2.Air,
            difficulty = Difficulty.EASY,
            timeEstimate = "15-20 min",
            interval = "Every ride in dusty/sandy conditions, every 2-3 rides otherwise",
            toolsNeeded = listOf("8mm/10mm wrench or screwdriver depending on seat/cover fasteners"),
            partsNeeded = listOf("Foam air filter oil", "Filter cleaning solvent"),
            summary = "A dirty or improperly oiled filter is the single most common cause of premature engine wear on a dirt bike. This is cheap insurance.",
            steps = listOf(
                GuideStep(
                    title = "Access the filter",
                    description = "Remove the seat and side panel/airbox cover as needed to expose the air filter element and cage."
                ),
                GuideStep(
                    title = "Remove the filter",
                    description = "Unbolt the filter cage, slide the foam filter off, and inspect the airbox for dirt or debris that may have passed through a torn filter."
                ),
                GuideStep(
                    title = "Clean the foam element",
                    description = "Wash the foam in a dedicated air filter cleaning solvent (not gasoline), working it gently until the rinse runs clear, then squeeze (don't wring or twist) dry.",
                    diagram = DiagramType.AIR_FILTER,
                    warning = "Never use gasoline to clean a foam filter — the fumes and fire risk aren't worth it. Use a proper bio air filter cleaner."
                ),
                GuideStep(
                    title = "Re-oil the filter",
                    description = "Once fully dry, apply foam filter oil evenly, working it through with your hands until the foam is uniformly tinted and there are no dry spots, with no oil dripping out."
                ),
                GuideStep(
                    title = "Reinstall",
                    description = "Seat the filter fully on the cage with a good seal against the airbox boot — any gap lets unfiltered air and dust straight into the engine. Reinstall the cover, side panel, and seat."
                )
            )
        ),
        Guide(
            id = "chain_maintenance",
            title = "Chain Cleaning, Lube & Slack Adjustment",
            category = "Drivetrain",
            icon = Icons2.Chain,
            difficulty = Difficulty.EASY,
            timeEstimate = "20-30 min",
            interval = "Lube every ride; check/adjust slack every ride",
            toolsNeeded = listOf("Chain brush", "17mm/19mm axle wrench or socket", "Ruler or chain slack tool"),
            partsNeeded = listOf("Chain cleaner", "Chain lube"),
            summary = "Off-road riding is brutal on chains. Keeping it clean, lubed, and correctly adjusted protects your sprockets and reduces the chance of a chain failure.",
            steps = listOf(
                GuideStep(
                    title = "Clean the chain",
                    description = "With the bike on a stand and rear wheel free to spin, brush off mud and grit, then apply chain cleaner and wipe down with a rag, rotating the wheel to reach the full loop."
                ),
                GuideStep(
                    title = "Check slack",
                    description = "Find the tightest point of the chain by rotating the wheel, then measure up-and-down play at the midpoint of the lower chain run between the sprockets.",
                    diagram = DiagramType.CHAIN_SLACK,
                    tip = "Typical slack specs run roughly 8-13mm for this class of bike, but always check your owner's manual — running it too tight is more damaging than running it slightly loose."
                ),
                GuideStep(
                    title = "Adjust if needed",
                    description = "Loosen the rear axle nut, turn both adjuster bolts evenly to keep the wheel aligned with the chassis alignment marks, then recheck slack before fully retightening.",
                    warning = "Always adjust both sides equally. An uneven adjustment pulls the wheel out of alignment, causing odd handling and accelerated tire/chain wear."
                ),
                GuideStep(
                    title = "Torque the axle nut",
                    description = "Once slack and alignment are correct, torque the rear axle nut to the manual's specification and double-check it after a short ride.",
                    diagram = DiagramType.TORQUE_WRENCH
                ),
                GuideStep(
                    title = "Lubricate",
                    description = "With the chain still slightly warm from cleaning, apply chain lube to the inside of the lower chain run while rotating the wheel, then let it sit a few minutes before riding so the carrier fluid evaporates."
                )
            )
        ),
        Guide(
            id = "valve_clearance",
            title = "Valve Clearance Inspection",
            category = "Engine",
            icon = Icons2.Valve,
            difficulty = Difficulty.ADVANCED,
            timeEstimate = "1.5-2.5 hrs",
            interval = "Per your service manual schedule — high-revving single-cylinder engines need this checked far more often than a car",
            toolsNeeded = listOf("Feeler gauges", "Socket set", "Torque wrench", "Valve adjustment shims (if out of spec)"),
            summary = "Valve clearance drifts as the valve seats wear in. Clearances outside spec cause hard starting, power loss, and in the worst case valve damage, so this is one of the most important checks on a high-performance single.",
            safetyNote = "This job involves removing the valve cover and rotating the engine to top dead center (TDC). If you haven't done this before, consider having a shop perform the first check while you watch.",
            steps = listOf(
                GuideStep(
                    title = "Let the engine cool",
                    description = "Valve clearance must be checked with the engine completely cold, since clearances change with temperature."
                ),
                GuideStep(
                    title = "Remove the valve cover",
                    description = "Remove the seat, tank (if required for access), and the cylinder head cover to expose the camshaft and valve train."
                ),
                GuideStep(
                    title = "Find top dead center",
                    description = "Rotate the engine using the crankshaft to align the TDC mark on the flywheel/rotor with the case index mark, with the cam lobes pointing away from the valves (intake and exhaust lobes facing roughly horizontal/outward).",
                    diagram = DiagramType.VALVE_CLEARANCE
                ),
                GuideStep(
                    title = "Measure each clearance",
                    description = "Slide the correct feeler gauge between the cam lobe and the valve shim/tappet for each valve. You should feel light drag — not free play, not a hard bind.",
                    diagram = DiagramType.VALVE_CLEARANCE,
                    tip = "Write down every measurement as you go. It's easy to lose track of which valve you just checked."
                ),
                GuideStep(
                    title = "Compare to spec",
                    description = "Compare each measurement to the intake and exhaust clearance specifications in your service manual — intake and exhaust specs are different."
                ),
                GuideStep(
                    title = "Shim out-of-spec valves",
                    description = "For any valve outside spec, the camshaft and shim/bucket must come out so a different shim thickness can be installed, then the clearance is rechecked.",
                    warning = "Mark every component's position before removal (cam cap orientation, shim locations). Mixed-up parts can cause serious engine damage on startup."
                ),
                GuideStep(
                    title = "Reassemble and verify",
                    description = "Torque the cam caps and valve cover to spec in the correct sequence, rotate the engine through a few full revolutions, and recheck clearances before buttoning everything back up.",
                    diagram = DiagramType.TORQUE_WRENCH
                )
            )
        ),
        Guide(
            id = "spark_plug",
            title = "Spark Plug Inspection & Replacement",
            category = "Engine",
            icon = Icons2.Plug,
            difficulty = Difficulty.EASY,
            timeEstimate = "15 min",
            interval = "Inspect periodically; replace per manual interval or if fouled/worn",
            toolsNeeded = listOf("Spark plug socket & wrench", "Feeler gauge / plug gap tool", "Torque wrench"),
            partsNeeded = listOf("OEM-spec spark plug"),
            summary = "The spark plug tells you a lot about how the engine is running, and a worn or incorrectly gapped plug causes hard starting and misfires.",
            steps = listOf(
                GuideStep(
                    title = "Remove the plug",
                    description = "Pull the spark plug cap, blow or brush away any debris around the plug base so nothing falls into the cylinder, then remove the plug with a spark plug socket."
                ),
                GuideStep(
                    title = "Read the plug",
                    description = "A healthy plug shows light tan/grey deposits. Black sooty deposits suggest running rich or a fouled plug; white/blistered deposits suggest running lean or overheating.",
                    warning = "Persistent fouling or unusual deposits can point to a bigger issue (carb/FI tuning, air leak, oil consumption) — don't just keep swapping plugs without investigating the cause."
                ),
                GuideStep(
                    title = "Check or set the gap",
                    description = "Using a feeler/wire gauge, confirm the gap matches spec on a new plug before installing — most plugs ship pre-gapped but it's worth confirming.",
                    diagram = DiagramType.SPARK_PLUG_GAP
                ),
                GuideStep(
                    title = "Install the new plug",
                    description = "Thread the plug in by hand until it seats to avoid cross-threading, then torque it to spec with a torque wrench.",
                    diagram = DiagramType.TORQUE_WRENCH,
                    tip = "If you don't have a torque wrench that fits, a common rule of thumb is finger-tight then about 1/4 to 1/2 turn further with a wrench for a new gasket-seat plug — but a torque wrench is always better."
                ),
                GuideStep(
                    title = "Reconnect and test",
                    description = "Push the plug cap on firmly until it clicks/seats, then start the engine and confirm it idles smoothly."
                )
            )
        ),
        Guide(
            id = "coolant",
            title = "Coolant Check & Replacement",
            category = "Engine",
            icon = Icons2.Coolant,
            difficulty = Difficulty.MODERATE,
            timeEstimate = "30-40 min",
            interval = "Check before every ride; replace per manual interval (commonly every 1-2 years)",
            toolsNeeded = listOf("Drain pan", "Funnel", "8/10mm wrench for drain bolt"),
            partsNeeded = listOf("50/50 pre-mixed coolant (motorcycle-specific, non-silicate)"),
            summary = "Liquid-cooled high-compression singles run hot. Low or old coolant is a fast way to cook an engine.",
            steps = listOf(
                GuideStep(
                    title = "Check the level cold",
                    description = "With the bike on level ground and the engine cold, check the coolant reservoir/radiator sight level. Top up with pre-mixed coolant if low.",
                    diagram = DiagramType.COOLANT_LEVEL,
                    warning = "Never remove a radiator cap on a hot engine — pressurized coolant can cause severe burns."
                ),
                GuideStep(
                    title = "Drain the system",
                    description = "With the engine cold, place a drain pan under the water pump/radiator drain bolt, remove the radiator cap to vent pressure, then remove the drain bolt and let the system fully drain."
                ),
                GuideStep(
                    title = "Flush if due",
                    description = "If the coolant is old, discolored, or due per the manual's interval, flush the system with distilled water until it runs clear before refilling."
                ),
                GuideStep(
                    title = "Refill and bleed air",
                    description = "Reinstall the drain bolt, refill slowly with pre-mixed coolant to avoid trapping air pockets, squeeze hoses to help purge bubbles, and top off the reservoir."
                ),
                GuideStep(
                    title = "Warm up and recheck",
                    description = "Run the engine to operating temperature with the cap on, let it cool, then recheck the level and top up as needed."
                )
            )
        ),
        Guide(
            id = "brakes",
            title = "Brake Fluid & Pad Inspection",
            category = "Chassis",
            icon = Icons2.Brake,
            difficulty = Difficulty.MODERATE,
            timeEstimate = "20-30 min",
            interval = "Inspect pads/fluid every few rides; replace fluid per manual interval (commonly every 1-2 years)",
            toolsNeeded = listOf("Flashlight", "Brake bleeding kit (if servicing fluid)"),
            partsNeeded = listOf("DOT 4 brake fluid", "Replacement pads as needed"),
            summary = "This bike is your only way to slow down. Don't skip brake checks, especially after muddy or wet rides where pads wear faster.",
            steps = listOf(
                GuideStep(
                    title = "Inspect pad thickness",
                    description = "Look through the caliper at the friction material on both front and rear pads. Replace before they wear down to the wear-limit groove/line.",
                    diagram = DiagramType.BRAKE_PAD_WEAR
                ),
                GuideStep(
                    title = "Check fluid level and color",
                    description = "Check the front master cylinder sight glass and rear reservoir level. Fluid should be clear to light amber — dark or cloudy fluid has absorbed moisture and should be replaced.",
                    warning = "Brake fluid is hygroscopic and corrosive to paint. Cover surrounding plastics before opening a reservoir and wipe up any spills immediately."
                ),
                GuideStep(
                    title = "Check the discs",
                    description = "Inspect both discs for deep grooves, cracking, or a lip at the outer edge, and check thickness against the minimum stamped on the disc if visible."
                ),
                GuideStep(
                    title = "Test lever and pedal feel",
                    description = "The front lever and rear pedal should feel firm, not spongy. A soft, sinking feel usually means air in the lines and the system needs bleeding."
                ),
                GuideStep(
                    title = "Bleed if needed",
                    description = "If the feel is soft or fluid is being replaced, bleed each caliper from the bleeder valve closest to the master cylinder outward until fluid runs clear with no bubbles."
                )
            )
        ),
        Guide(
            id = "suspension_sag",
            title = "Suspension Sag Setup",
            category = "Chassis",
            icon = Icons2.Suspension,
            difficulty = Difficulty.MODERATE,
            timeEstimate = "20-30 min",
            interval = "Whenever rider weight/gear changes, or after a spring change",
            toolsNeeded = listOf("Tape measure", "Shock spring preload tool (C-spanner)", "A helper"),
            summary = "Sag is the foundation of suspension setup. Getting it right makes every other clicker adjustment actually mean something.",
            steps = listOf(
                GuideStep(
                    title = "Measure free sag",
                    description = "With the bike off the stand, unloaded, and held vertical, measure from the rear axle straight up to a fixed reference point on the chassis. This is your fully-extended measurement (L1).",
                    diagram = DiagramType.SUSPENSION_SAG
                ),
                GuideStep(
                    title = "Measure race sag",
                    description = "With the rider in full gear sitting in the normal riding position (use a helper to hold the bike upright, no feet down), measure the same two points again (L2). Race sag = L1 - L2.",
                    diagram = DiagramType.SUSPENSION_SAG,
                    tip = "Bounce the suspension a couple of times before each measurement to settle the seals and get a repeatable number."
                ),
                GuideStep(
                    title = "Adjust shock spring preload",
                    description = "If race sag is outside the typical off-road range (commonly around 100-110mm as a starting point — confirm your bike's target in the manual), adjust the shock spring preload collar to bring it into range.",
                    warning = "Always loosen the locking collar before turning the adjuster collar, and retighten the lock ring against it afterward or it will work loose."
                ),
                GuideStep(
                    title = "Recheck free sag",
                    description = "After adjusting race sag, recheck free sag. If free sag is too low even with correct race sag, the spring rate is likely too soft for your weight; if free sag is too high, the spring may be too stiff."
                ),
                GuideStep(
                    title = "Fine-tune clickers",
                    description = "With sag set, adjust compression and rebound clickers from full stiff, counting clicks out from the manual's baseline, then fine-tune a click or two at a time based on how the bike feels on the trail."
                )
            )
        ),
        Guide(
            id = "tires",
            title = "Tire Pressure & Wear Inspection",
            category = "Chassis",
            icon = Icons2.Tire,
            difficulty = Difficulty.EASY,
            timeEstimate = "10 min",
            interval = "Every ride",
            toolsNeeded = listOf("Low-pressure tire gauge"),
            summary = "Off-road tire pressures run much lower than street pressures for grip, which makes checking before every ride important — an unnoticed slow leak is far more obvious off-road than at street pressures.",
            steps = listOf(
                GuideStep(
                    title = "Check pressure cold",
                    description = "Check both tires before riding while they're cold, using a gauge designed for low pressures (street gauges are often inaccurate below ~15 psi).",
                    diagram = DiagramType.TIRE_PRESSURE
                ),
                GuideStep(
                    title = "Set for conditions",
                    description = "Lower pressure increases grip and cushioning on rocks/roots but raises the risk of pinch flats and rim damage; higher pressure resists pinch flats but reduces grip. Adjust within a safe range for terrain.",
                    tip = "Many riders run noticeably lower pressure off-road than on pavement — start near your manual's recommendation and adjust from there for terrain and tire choice."
                ),
                GuideStep(
                    title = "Inspect tread and casing",
                    description = "Look for chunked knobs, sidewall cuts, and embedded rocks or thorns. Check that the rim lock(s) are tight so the tire can't spin on the rim at low pressure."
                ),
                GuideStep(
                    title = "Check spokes and rim",
                    description = "Tap spokes with a wrench — a dull thud instead of a clean ping indicates a loose spoke. Check the rim for dents that could affect sealing or trueness."
                )
            )
        ),
        Guide(
            id = "battery_electrical",
            title = "Battery & Electrical Check",
            category = "Electrical",
            icon = Icons2.Battery,
            difficulty = Difficulty.EASY,
            timeEstimate = "10-15 min",
            interval = "Periodically, and any time you have electric-start or lighting issues",
            toolsNeeded = listOf("Multimeter", "Battery terminal wrench"),
            summary = "The RL's electric start and street lighting depend on a healthy battery and good connections, which is one more thing a pure off-road bike doesn't have to worry about.",
            steps = listOf(
                GuideStep(
                    title = "Inspect terminals",
                    description = "Check the battery terminals for corrosion or looseness. Clean any corrosion and ensure connections are snug.",
                    diagram = DiagramType.BATTERY_FUSE
                ),
                GuideStep(
                    title = "Check voltage",
                    description = "With the engine off, a healthy battery should read approximately 12.5-13.0V at rest. Below ~12.3V, plan to charge it."
                ),
                GuideStep(
                    title = "Check charging voltage",
                    description = "With the engine running at a moderate idle/rev, voltage across the battery should rise into the high-13V to low-14V range, confirming the charging system is working."
                ),
                GuideStep(
                    title = "Check fuses",
                    description = "If anything electrical isn't working, check the fuse box first before chasing more complex wiring issues — it's the fastest thing to rule out.",
                    diagram = DiagramType.BATTERY_FUSE
                ),
                GuideStep(
                    title = "Secure the battery",
                    description = "Make sure the battery strap/bracket holds it firmly — a battery that's allowed to bounce around off-road will eventually fail or vibrate a terminal loose."
                )
            )
        ),
        Guide(
            id = "fastener_torque",
            title = "Fastener Torque Check",
            category = "General",
            icon = Icons2.Wrench,
            difficulty = Difficulty.EASY,
            timeEstimate = "15-20 min",
            interval = "After the first ride on a new bike, and periodically thereafter — especially after a crash",
            toolsNeeded = listOf("Torque wrench", "Socket set"),
            summary = "Vibration and impacts work bolts loose over time. A regular torque check catches problems before something falls off on the trail.",
            steps = listOf(
                GuideStep(
                    title = "Wheels and axles",
                    description = "Check front and rear axle nuts/pinch bolts to spec.",
                    diagram = DiagramType.TORQUE_WRENCH
                ),
                GuideStep(
                    title = "Suspension linkage",
                    description = "Check swingarm pivot, linkage bolts, and shock mounting bolts — these see huge cyclic loads."
                ),
                GuideStep(
                    title = "Handlebar & controls",
                    description = "Check bar clamp bolts, lever perches, and the steering stem/triple clamp pinch bolts."
                ),
                GuideStep(
                    title = "Engine mounts & footpegs",
                    description = "Check all engine mounting bolts and footpeg pivot/mount bolts, both of which take constant impact loading off-road."
                ),
                GuideStep(
                    title = "Sprockets & chain guide",
                    description = "Check rear sprocket bolts and the chain guide/slider mounting bolts — a loose sprocket bolt can quickly wear out the hub.",
                    warning = "Always torque fasteners to the spec in your service manual rather than guessing — both over- and under-tightening cause failures."
                )
            )
        )
    )
}
