package space.bielsolososdev.rdl.api.model;

import java.time.Instant;

public record DashboardStatsResponse(
        String appName,
        String message,
        Instant startedAt,
        UrlStats urlStats,
        SystemInfo systemInfo) {

    public record UrlStats(
            long total,
            long enabled,
            long disabled,
            double enabledPercentage) {
    }

    public record SystemInfo(
            String database,
            String javaVersion,
            String frameworkVersion) {
    }
}
