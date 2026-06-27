package com.caa.backend.model.enums;
/**
 * Defines the communication level assigned to a child profile.
 * Each level determines the complexity of boards and features available.
 *
 * LEVEL_1 → Few pictograms and boards, no phrase construction
 * LEVEL_2 → More pictograms and boards, phrase construction enabled
 * LEVEL_3 → Same as LEVEL_2 but with even more pictograms and boards
 */
public enum Level {
    LEVEL_1,
    LEVEL_2,
    LEVEL_3
}
