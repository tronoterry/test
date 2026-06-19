package com.nswsurfcams.widget

/**
 * Source of truth for the NSW surf cam list shown in the widget and the app.
 *
 * No provider (Coastalwatch, Swellnet, Surfline, etc.) currently publishes a free,
 * ToS-compliant raw stream URL for their live cams - access requires a paid
 * subscription and goes through their own apps/players. Until that access is set
 * up, every cam below points at [PLACEHOLDER_STREAM_URL], a public HLS test
 * stream, so the playback pipeline can be exercised end to end.
 *
 * To go live: replace `streamUrl` per cam with the licensed feed URL (and
 * `thumbnailUrl` with the matching still/preview image) once available.
 */
object SurfCamRepository {

    private const val PLACEHOLDER_STREAM_URL = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"

    val cams: List<SurfCam> = listOf(
        cam("bondi", "Bondi Beach"),
        cam("bronte", "Bronte Beach"),
        cam("coogee", "Coogee Beach"),
        cam("maroubra", "Maroubra Beach"),
        cam("north_cronulla", "North Cronulla"),
        cam("manly", "Manly Beach"),
        cam("dee_why", "Dee Why Beach"),
        cam("north_narrabeen", "North Narrabeen"),
        cam("avalon", "Avalon Beach"),
        cam("avoca", "Avoca Beach"),
        cam("nobbys_newcastle", "Nobbys Beach, Newcastle"),
        cam("merewether", "Merewether Beach"),
        cam("north_beach_wollongong", "North Beach, Wollongong"),
        cam("the_pass_byron_bay", "The Pass, Byron Bay"),
    )

    private fun cam(slug: String, name: String) = SurfCam(
        id = slug,
        name = name,
        thumbnailUrl = "https://picsum.photos/seed/$slug/400/300",
        streamUrl = PLACEHOLDER_STREAM_URL,
    )
}
