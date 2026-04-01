package com.caa.backend.model.enums;
/**
 * Roles available in the system.
 * Used by Spring Security to determine access permissions.
 *
 * TUTOR → Full access: manage boards, child profiles, pictograms, settings
 * ADMIN → Reserved for future system administration use
 */
public enum Role {
    TUTOR,
    ADMIN
}

