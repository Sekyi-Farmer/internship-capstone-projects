package com.genkey.configmaster.model;

/**
 * One schema or merge problem. Collected rather than thrown on the first
 * error so operators see the full set of issues in one run.
 */
public record ValidationIssue(String path, String message) {}
