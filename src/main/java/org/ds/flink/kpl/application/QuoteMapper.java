package org.ds.flink.kpl.application;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;

public class QuoteMapper implements MapFunction<String, Quote> {

    @Override
    public Quote map(String s) throws Exception {
        String[] quoteParts = s.split(",");
        return new Quote(quoteParts[0], Double.valueOf(quoteParts[1]));
    }
}
