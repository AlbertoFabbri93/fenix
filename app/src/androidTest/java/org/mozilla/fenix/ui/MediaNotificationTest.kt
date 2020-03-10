/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.ui

import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mozilla.fenix.helpers.AndroidAssetDispatcher
import org.mozilla.fenix.helpers.HomeActivityTestRule
import org.mozilla.fenix.helpers.TestAssetHelper
import org.mozilla.fenix.ui.robots.navigationToolbar

/**
 *  Tests for verifying basic functionality of media notifications
 */
class MediaNotificationTest {
    /* ktlint-disable no-blank-line-before-rbrace */ // This imposes unreadable grouping.

    private lateinit var mockWebServer: MockWebServer

    @get:Rule
    val activityTestRule = HomeActivityTestRule()

    @Before
    fun setUp() {
        mockWebServer = MockWebServer().apply {
            setDispatcher(AndroidAssetDispatcher())
            start()
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun videoPlaybackSystemNotificationTest() {
        val videoTestPage = TestAssetHelper.getVideoPageAsset(mockWebServer)

        navigationToolbar {
        }.enterURLAndEnterToBrowser(videoTestPage.url) {
            clickMediaPlayerPlayButton()
        }.openNotificationShade {
            verifySystemNotificationExists(videoTestPage.title)
            clickMediaSystemNotificationControlButton("Pause")
            verifyMediaSystemNotificationButtonState("Play")
            clickMediaSystemNotificationControlButton("Play")
            verifyMediaSystemNotificationButtonState("Pause")
        }
    }

    @Test
    fun audioPlaybackSystemNotificationTest() {
        val audioTestPage = TestAssetHelper.getAudioPageAsset(mockWebServer)

        navigationToolbar {
        }.enterURLAndEnterToBrowser(audioTestPage.url) {
            clickMediaPlayerPlayButton()
        }.openNotificationShade {
            verifySystemNotificationExists(audioTestPage.title)
            clickMediaSystemNotificationControlButton("Pause")
            verifyMediaSystemNotificationButtonState("Play")
            clickMediaSystemNotificationControlButton("Play")
        }.exitNotificationShadeToBrowser {
            clickMediaPlayerPauseButton()
        }.openNotificationShade {
            verifyMediaSystemNotificationButtonState("Play")
        }
    }

    @Test
    fun tabMediaControlButton() {
        val audioTestPage = TestAssetHelper.getAudioPageAsset(mockWebServer)

        navigationToolbar {
        }.enterURLAndEnterToBrowser(audioTestPage.url) {
            clickMediaPlayerPlayButton()
        }.openHomeScreen {
            verifyTabMediaControlButtonState("Pause")
            clickTabMediaControlButton()
            verifyTabMediaControlButtonState("Play")
        }
    }
}
