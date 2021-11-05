

package org.ds.flink.kpl.application;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisConsumer;
import org.apache.flink.streaming.connectors.kinesis.config.ConsumerConfigConstants;

import java.util.Properties;


public class StreamingJob {
	private static final String region = "us-east-1";
	private static final String inputStreamName = "kpltest";

	private static DataStream<String> createSourceFromStaticConfig(StreamExecutionEnvironment env) {
		Properties inputProperties = new Properties();
		inputProperties.setProperty(ConsumerConfigConstants.AWS_REGION, region);
		inputProperties.setProperty(ConsumerConfigConstants.STREAM_INITIAL_POSITION, "LATEST");

		return env.addSource(new FlinkKinesisConsumer<>(inputStreamName, new SimpleStringSchema(), inputProperties));
	}

	public static void main(String[] args) throws Exception {
		// set up the streaming execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		DataStream<String> input = createSourceFromStaticConfig(env);
		input
				.map(new QuoteMapper())
				.keyBy(quote -> quote.symbol)
				.window(TumblingProcessingTimeWindows.of(Time.seconds(1)))
				.reduce(new ReduceFunction<Quote>() {
					@Override
					public Quote reduce(Quote quote, Quote t1) throws Exception {
						return t1; //Conflate all quotes in a window to the last quote
					}
				})
				.print();

		// execute program
		env.execute("Flink Streaming Java API Skeleton");
	}
}
