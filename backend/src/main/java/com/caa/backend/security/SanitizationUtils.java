package com.caa.backend.security;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

// Utility class for input sanitization. Uses OWASP Java HTML Sanitizer to prevent XSS attacks.
public class SanitizationUtils {
    private SanitizationUtils() {}

    private static final PolicyFactory STRIP_ALL_HTML = new HtmlPolicyBuilder().toFactory();

    // Sanitizes plain text input - strips aññ HTML tags and control characters
    public static String sanitize(String input){
        if (input == null) return null;
        // Strip all HTML tags via OWASP
        String clean = STRIP_ALL_HTML.sanitize(input.trim());
        // Remove control characters that could cause issue
        clean = clean.replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]", "");
        return clean.trim();
    }

    // Sanitizes a URL field — strips HTML and allows only safe URL characters.
    public static String sanitizeUrl(String input) {
        if (input == null) return null;
        String clean = STRIP_ALL_HTML.sanitize(input.trim());
        // Only allow characters valid in URLs
        clean = clean.replaceAll("[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\-._~:/?#\\[\\]@!$&'()*+,;=%]", "");
        return clean.trim();
    }
}
