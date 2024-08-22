package io.acyloxy.cloverproxy.util;

import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.Patch;

import java.util.List;

@SuppressWarnings("deprecation")
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    public static List<String> lines(String s) {
        return List.of(s.split("\n"));
    }

    public static String diff(String s1, String s2, String originalName, String revisedName) {
        List<String> lines1 = lines(s1);
        List<String> lines2 = lines(s2);
        Patch<String> patch = DiffUtils.diff(lines1, lines2);
        return String.join("\n", UnifiedDiffUtils.generateUnifiedDiff(originalName, revisedName, lines1, patch, 0));
    }
}
