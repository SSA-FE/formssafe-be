package com.formssafe.util;

import java.util.function.Consumer;
import org.assertj.core.api.SoftAssertions;

public final class AssertionUtil {

    private AssertionUtil() {
    }

    public static void assertWithSoftAssertions(Consumer<SoftAssertions> assertionConsumer) {
        SoftAssertions sa = new SoftAssertions();
        assertionConsumer.accept(sa);
        sa.assertAll();
    }
}
