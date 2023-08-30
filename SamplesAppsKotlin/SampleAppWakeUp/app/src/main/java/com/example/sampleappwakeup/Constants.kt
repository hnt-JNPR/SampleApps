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
    val orgId : String = "6748cfa6-4e12-11e6-9188-0242ac110007"

    val noVBLETimeMs : Long = 5 * 60 * 1000

    val noVBLEFailCountLimit : Long = 200

    val beaconScanIntervalLocationSdkRunningMs : Long = 10 * 60 * 1000   /* In ms*/

    val beaconScanIntervalLocationSdkNotRunningMs : Long = 100   /* In ms*/

    val beaconPerScanDuration : Long = 1000
}