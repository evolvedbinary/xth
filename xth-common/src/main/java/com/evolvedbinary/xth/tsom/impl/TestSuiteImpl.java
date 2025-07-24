/*
 * X Test Harness
 * Copyright (C) 2024, Evolved Binary Ltd
 *
 * admin@evolvedbinary.com
 * https://www.evolvedbinary.com
 *
 * SPDX-License-Identifier: BUSL-1.1
 *
 * Use of this software is governed by the Business Source License 1.1
 * included in the LICENSE file and at www.mariadb.com/bsl11.
 *
 * Change Date: 2029-06-06
 *
 * On the date above, in accordance with the Business Source License, use
 * of this software will be governed by the Apache License, Version 2.0.
 *
 * Additional Use Grant: Production use of the Licensed Work for a permitted
 * purpose. A Permitted Purpose is any purpose other than a Competing Use.
 * A Competing Use means making the Software available to others in a commercial
 * product or service that: substitutes for the Software; substitutes for any
 * other product or service we offer using the Software that exists as of the
 * date we make the Software available; or offers the same or substantially
 * similar functionality as the Software.
 */
package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.AbstractBase;
import com.evolvedbinary.xth.tsom.TestSuite;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.UUID;

public final class TestSuiteImpl extends AbstractBase implements TestSuite {
    private final UUID testSuiteInstanceId;
    private final String name;
    private final BigDecimal version;
    private final Path file;

    public TestSuiteImpl(final UUID testSuiteInstanceId, final String xmlId, final String name, final BigDecimal version, final Path file) {
        super(xmlId);
        this.testSuiteInstanceId = testSuiteInstanceId;
        this.name = name;
        this.version = version;
        this.file = file;
    }

    @Override
    public UUID getTestSuiteInstanceId() {
        return testSuiteInstanceId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BigDecimal getVersion() {
        return version;
    }

    @Override
    public Path getFile() {
        return file;
    }

    public static TestSuiteImpl.Builder builder(final UUID testSuiteInstanceId, final String name, final BigDecimal version) {
        return new TestSuiteImpl.Builder(testSuiteInstanceId, name, version);
    }

    public static class Builder extends AbstractBase.Builder implements TestSuite.Builder {
        private final UUID testSuiteInstanceId;
        private final String name;
        private final BigDecimal version;
        private Path file = null;

        private Builder(final UUID testSuiteInstanceId, final String name, final BigDecimal version) {
            this.testSuiteInstanceId = testSuiteInstanceId;
            this.name = name;
            this.version = version;
        }

        @Override
        public Builder setFile(final Path file) {
            this.file = file;
            return this;
        }

        @Override
        public TestSuite build() {
            return new TestSuiteImpl(
                testSuiteInstanceId,
                xmlId,
                name,
                version,
                file
            );
        }
    }
}
