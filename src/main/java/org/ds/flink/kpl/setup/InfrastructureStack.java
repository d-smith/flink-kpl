package org.ds.flink.kpl.setup;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.kinesis.CfnStreamConsumer;
import software.amazon.awscdk.services.kinesis.Stream;

public class InfrastructureStack extends Stack {
    public InfrastructureStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public InfrastructureStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);


        Stream stream = Stream.Builder.create(this, "kpltest")
                .streamName("kpltest")
                .build();

        CfnStreamConsumer.Builder.create(this, "kpltestcon")
                .streamArn(stream.getStreamArn())
                .consumerName("kpltestcon")
                .build();
    }
}

