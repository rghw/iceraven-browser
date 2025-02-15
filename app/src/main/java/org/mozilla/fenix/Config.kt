/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix

enum class ReleaseChannel {
    Debug,
    ForkDebug,
    Nightly,
    Beta,
    Release,
    ForkRelease;

    val isReleased: Boolean
        get() = when (this) {
            Debug -> false
            ForkDebug -> false
            else -> true
        }

    /**
     * True if this is a debug release channel, false otherwise.
     *
     * This constant should often be used instead of [BuildConfig.DEBUG], which indicates
     * if the `debuggable` flag is set which can be true even on released channel builds
     * (e.g. performance).
     */
    val isDebug: Boolean
        get() = !this.isReleased

    val isReleaseOrBeta: Boolean
        get() = this == Release || this == Beta || this == ForkRelease

    val isRelease: Boolean
        get() = when (this) {
            Release -> true
            ForkRelease -> true
            else -> false
        }

    val isBeta: Boolean
        get() = this == Beta

    val isNightlyOrDebug: Boolean
        get() = this == Debug || this == Nightly || this == ForkDebug

    /**
     * Is this a rebranded fork?
     */
    val isFork: Boolean
        get() = when (this) {
            ForkDebug -> true
            ForkRelease -> true
            else -> false
        }

    /**
     * Is this a "Mozilla Online" build of Fenix? "Mozilla Online" is the Chinese branch of Mozilla
     * and this flag will be `true` for builds shipping to Chinese app stores.
     */
    val isMozillaOnline: Boolean
        get() = BuildConfig.MOZILLA_ONLINE
}

object Config {
    val channel = when (BuildConfig.BUILD_TYPE) {
        "debug" -> ReleaseChannel.Debug
        "nightly" -> ReleaseChannel.Nightly
        "beta" -> ReleaseChannel.Beta
        "release" -> ReleaseChannel.Release
        "forkDebug" -> ReleaseChannel.ForkDebug
        "forkRelease" -> ReleaseChannel.ForkRelease
        else -> {
            throw IllegalStateException("Unknown build type: ${BuildConfig.BUILD_TYPE}")
        }
    }
}
