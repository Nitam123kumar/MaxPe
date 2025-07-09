package com.vuvrecharge.utils;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.SimpleResource;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.InputStream;

public class SvgDecoder implements ResourceDecoder<InputStream, SVG> {
    @Override
    public boolean handles(@NonNull InputStream source, @NonNull Options options) {
        return true;
    }

    @Override
    public Resource<SVG> decode(@NonNull InputStream source, int width, int height, @NonNull Options options) {
        SVG svg = null;
        try {
            svg = SVG.getFromInputStream(source);
        } catch (SVGParseException e) {
            throw new RuntimeException(e);
        }
        return new SimpleResource<>(svg);
    }
}