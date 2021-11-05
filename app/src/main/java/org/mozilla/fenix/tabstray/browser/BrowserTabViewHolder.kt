/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.tabstray.browser

import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import mozilla.components.browser.state.state.TabSessionState
import mozilla.components.browser.tabstray.TabsTray
import mozilla.components.browser.tabstray.TabsTrayStyling
import mozilla.components.concept.base.images.ImageLoader
import org.mozilla.fenix.R
import org.mozilla.fenix.databinding.TabTrayGridItemBinding
import org.mozilla.fenix.ext.increaseTapArea
import org.mozilla.fenix.utils.Settings
import kotlin.math.max
import org.mozilla.fenix.selection.SelectionHolder
import org.mozilla.fenix.tabstray.TabsTrayStore

sealed class BrowserTabViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    /**
     * A RecyclerView ViewHolder implementation for "tab" items with grid layout.
     *
     * @param imageLoader [ImageLoader] used to load tab thumbnails.
     * @param browserTrayInteractor [BrowserTrayInteractor] handling tabs interactions in a tab tray.
     * @param store [TabsTrayStore] containing the complete state of tabs tray and methods to update that.
     * @param selectionHolder [SelectionHolder]<[TabSessionState]> for helping with selecting
     * any number of displayed [TabSessionState]s.
     * @param itemView [View] that displays a "tab".
     * @param featureName [String] representing the name of the feature displaying tabs. Used in telemetry reporting.
     */
    @Suppress("LongParameterList")
    class GridViewHolder(
        imageLoader: ImageLoader,
        override val browserTrayInteractor: BrowserTrayInteractor,
        store: TabsTrayStore,
        selectionHolder: SelectionHolder<TabSessionState>? = null,
        itemView: View,
        featureName: String,
        settings: Settings? = null
    ) : AbstractBrowserTabViewHolder(itemView, imageLoader, store, selectionHolder, featureName, settings) {

        private val closeButton: AppCompatImageButton = itemView.findViewById(R.id.mozac_browser_tabstray_close)

        override val thumbnailSize: Int
            get() = max(
                itemView.resources.getDimensionPixelSize(R.dimen.tab_tray_grid_item_thumbnail_height),
                itemView.resources.getDimensionPixelSize(R.dimen.tab_tray_grid_item_thumbnail_width)
            )

        override fun updateSelectedTabIndicator(showAsSelected: Boolean) {
            val binding = TabTrayGridItemBinding.bind(itemView)
            binding.tabTrayGridItem.background = if (showAsSelected) {
                AppCompatResources.getDrawable(itemView.context, R.drawable.tab_tray_grid_item_selected_border)
            } else {
                null
            }
            return
        }

        override fun bind(
            tab: TabSessionState,
            isSelected: Boolean,
            styling: TabsTrayStyling,
            delegate: TabsTray.Delegate
        ) {
            super.bind(tab, isSelected, styling, delegate)

            closeButton.increaseTapArea(GRID_ITEM_CLOSE_BUTTON_EXTRA_DPS)
        }

        companion object {
            const val LAYOUT_ID = R.layout.tab_tray_grid_item
        }
    }

    /**
     * A RecyclerView ViewHolder implementation for "tab" items with list layout.
     *
     * @param imageLoader [ImageLoader] used to load tab thumbnails.
     * @param browserTrayInteractor [BrowserTrayInteractor] handling tabs interactions in a tab tray.
     * @param store [TabsTrayStore] containing the complete state of tabs tray and methods to update that.
     * @param selectionHolder [SelectionHolder]<[TabSessionState]> for helping with selecting
     * any number of displayed [TabSessionState]s.
     * @param itemView [View] that displays a "tab".
     * @param featureName [String] representing the name of the feature displaying tabs. Used in telemetry reporting.
     */
    @Suppress("LongParameterList")
    class ListViewHolder(
        imageLoader: ImageLoader,
        override val browserTrayInteractor: BrowserTrayInteractor,
        store: TabsTrayStore,
        selectionHolder: SelectionHolder<TabSessionState>? = null,
        itemView: View,
        featureName: String,
        settings: Settings? = null
    ) : AbstractBrowserTabViewHolder(itemView, imageLoader, store, selectionHolder, featureName, settings) {
        override val thumbnailSize: Int
            get() = max(
                itemView.resources.getDimensionPixelSize(R.dimen.tab_tray_list_item_thumbnail_height),
                itemView.resources.getDimensionPixelSize(R.dimen.tab_tray_list_item_thumbnail_width)
            )

        override fun updateSelectedTabIndicator(showAsSelected: Boolean) {
            val color = if (showAsSelected) {
                R.color.tab_tray_item_selected_background_normal_theme
            } else {
                R.color.tab_tray_item_background_normal_theme
            }
            itemView.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    color
                )
            )
        }

        companion object {
            const val LAYOUT_ID = R.layout.tab_tray_item
        }
    }
}
