package com.example.sampleappwakeup

class Constants {
    /**
     * Provide SDK token in ORG_SECRET,
     * we can get org secret from MIST UI (Organization —> Mobile SDK)
     */
    val ORG_SECRET : String = ""
    /**
     * Provide orgId for alt beacon scanning here
     */
    val ORG_ID : String = ""

    val NO_VBLE_TIMEOUT_MS : Long = 5 * 60 * 1000

    val NO_VBLE_FAIL_COUNT_LIMIT : Long = 200

    val BEACON_SCAN_INTERVAL_LOCATION_SDK_RUNNING_MS : Long = 10 * 60 * 1000   /* In ms*/

    val BEACON_SCAN_INTERVAL_LOCATION_SDK_NOT_RUNNING_MS : Long = 100   /* In ms*/

    val BEACON_PER_SCAN_DURATION : Long = 1000
}