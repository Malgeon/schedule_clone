package com.example.schedule_clone.domain.sessions

import com.example.schedule_clone.model.ConferenceDay

class ConferenceDayIndexer(
    /**
     * Mapping of ConferenceDay to start position.
     * Values (indexes) must be >= 0 and in ascending order.
     */
    mapping: Map<ConferenceDay, Int>
) {
    init {
        var previous = -1

    }
}