package com.example.sampleappwakeup

class Constants {
    /**
     * Provide SDK token in ORG_SECRET,
     * we can get org secret from MIST UI (Organization —> Mobile SDK)
     */
    val orgSecret : String = ""
    /**
     * Provide orgId for alt beacon scanning here
     */
    val orgId : String = "f0f979b4-d1ce-443e-8c41-5f5b9b716439"

    val noVBLETimeMs : Long = 5 * 60 * 1000

    val noVBLEFailCountLimit : Long = 200

    val beaconScanIntervalLocationSdkRunningMs : Long = 10 * 60 * 1000   /* In ms*/

    val beaconScanIntervalLocationSdkNotRunningMs : Long = 100   /* In ms*/

    val beaconPerScanDuration : Long = 1000
}