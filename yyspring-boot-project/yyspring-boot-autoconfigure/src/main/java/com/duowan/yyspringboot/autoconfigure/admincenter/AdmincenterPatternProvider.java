package com.duowan.yyspringboot.autoconfigure.admincenter;

import com.duowan.yyspringboot.autoconfigure.udb.PatternProvider;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/8/28 16:30
 */
public class AdmincenterPatternProvider implements PatternProvider {

    private Set<String> excludePatterns = new HashSet<>();

    private Set<String> includePatterns = new HashSet<>();

    public AdmincenterPatternProvider(Set<String> excludePatterns, Set<String> includePatterns) {
        if (excludePatterns != null && !excludePatterns.isEmpty()) {
            this.excludePatterns.addAll(excludePatterns);
        }
        this.excludePatterns.add("/privilege.xml");
        this.excludePatterns.add("/admin/privileges.do");


        if (includePatterns != null && !includePatterns.isEmpty()) {
            this.includePatterns.addAll(includePatterns);
        }
    }

    @Override
    public Set<String> getExcludePatterns() {
        return this.excludePatterns;
    }

    @Override
    public Set<String> getIncludePatterns() {
        return includePatterns;
    }
}
