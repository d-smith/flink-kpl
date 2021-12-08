package org.ds.flink.kpl.setup;

import software.amazon.awscdk.core.App;
import software.amazon.awscdk.core.StackProps;

public class SetupApp {
    public static void main(final String[] args) {
        App app = new App();

        new InfrastructureStack(app, "FlinkKPLInf", StackProps.builder()
                .build());

        app.synth();
    }
}
