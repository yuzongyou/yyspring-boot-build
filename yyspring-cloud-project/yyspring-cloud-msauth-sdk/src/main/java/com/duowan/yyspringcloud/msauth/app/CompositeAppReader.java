package com.duowan.yyspringcloud.msauth.app;

import com.duowan.yyspringcloud.msauth.exception.EmptyAppReaderException;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/18 9:33
 */
public class CompositeAppReader implements AppReader {

    private final List<AppReader> appReaders;

    public CompositeAppReader(List<AppReader> appReaders) {
        if (appReaders == null || appReaders.isEmpty()) {
            throw new EmptyAppReaderException();
        }
        AnnotationAwareOrderComparator.sort(appReaders);
        this.appReaders = appReaders;
    }

    @Override
    public App read(String appId) {

        for (AppReader reader : appReaders) {
            App app = reader.read(appId);
            if (null != app) {
                return app;
            }
        }

        return null;
    }
}
