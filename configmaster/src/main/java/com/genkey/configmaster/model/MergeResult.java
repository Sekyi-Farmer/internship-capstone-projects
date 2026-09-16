package com.genkey.configmaster.model;

import java.util.List;

/** Result of merging layers, including any conflict or type issues. */
public record MergeResult(ConfigDocument document, List<ValidationIssue> issues) {

    public boolean ok() {
        return issues.isEmpty();
    }
}
