package com.adapter.ax.inmobiNativeAd;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ViewBinder {
    public final static class Builder {
        public final int layoutId;
        public int titleId;
        public int textId;
        public int callToActionId;
        public int mainImageId;
        public int iconImageId;
        public int privacyContainerId;
        @NonNull public Map<String, Integer> extras = Collections.emptyMap();

        public Builder(final int layoutId) {
            this.layoutId = layoutId;
            this.extras = new HashMap<String, Integer>();
        }

        @NonNull
        public final Builder titleId(final int titleId) {
            this.titleId = titleId;
            return this;
        }

        @NonNull
        public final Builder textId(final int textId) {
            this.textId = textId;
            return this;
        }

        @NonNull
        public final Builder callToActionId(final int callToActionId) {
            this.callToActionId = callToActionId;
            return this;
        }

        @NonNull
        public final Builder mainImageId(final int mediaLayoutId) {
            this.mainImageId = mediaLayoutId;
            return this;
        }

        @NonNull
        public final Builder iconImageId(final int iconImageId) {
            this.iconImageId = iconImageId;
            return this;
        }

        @NonNull
        public final Builder privacyContainerId(final int privacyContainerId) {
            this.privacyContainerId = privacyContainerId;
            return this;
        }

        @NonNull
        public final Builder addExtras(final Map<String, Integer> resourceIds) {
            this.extras = new HashMap<String, Integer>(resourceIds);
            return this;
        }

        @NonNull
        public final Builder addExtra(final String key, final int resourceId) {
            this.extras.put(key, resourceId);
            return this;
        }

        @NonNull
        public final ViewBinder build() {
            return new ViewBinder(this);
        }
    }

    final public int layoutId;
    final public int titleId;
    final public int textId;
    final public int callToActionId;
    final public int mainImageId;
    final public int iconImageId;
    final public int privacyContainer;
    @NonNull public final Map<String, Integer> extras;

    private ViewBinder(@NonNull final Builder builder) {
        this.layoutId = builder.layoutId;
        this.titleId = builder.titleId;
        this.textId = builder.textId;
        this.callToActionId = builder.callToActionId;
        this.mainImageId = builder.mainImageId;
        this.iconImageId = builder.iconImageId;
        this.privacyContainer = builder.privacyContainerId;
        this.extras = builder.extras;
    }
}