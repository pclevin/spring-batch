/*
 * Copyright 2006-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.batch.core.step.item;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;
import org.springframework.batch.core.StepListener;
import org.springframework.batch.core.configuration.xml.DummyCompletionPolicy;
import org.springframework.batch.core.configuration.xml.DummyItemReader;
import org.springframework.batch.core.configuration.xml.DummyItemWriter;
import org.springframework.batch.core.configuration.xml.DummyTasklet;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.core.step.JobRepositorySupport;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.batch.retry.listener.RetryListenerSupport;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Dan Garrette
 * @since 2.0
 */
public class StepFactoryBeanTests {

	@Test(expected = IllegalStateException.class)
	public void testNothingSet() throws Exception {
		StepFactoryBean<Object, Object> fb = new StepFactoryBean<Object, Object>();
		fb.getObject();
	}

	@Test
	public void testOnlyTaskletSet() throws Exception {
		StepFactoryBean<Object, Object> fb = new StepFactoryBean<Object, Object>();
		fb.setTasklet(new DummyTasklet());
		Object step = fb.getObject();
		assertTrue(step instanceof TaskletStep);
		Object tasklet = ReflectionTestUtils.getField(step, "tasklet");
		assertTrue(tasklet instanceof DummyTasklet);
	}

	@Test(expected = IllegalStateException.class)
	public void testSkipLimitSet() throws Exception {
		StepFactoryBean<Object, Object> fb = new StepFactoryBean<Object, Object>();
		fb.setSkipLimit(5);
		fb.getObject();
	}

	@Test
	public void testTaskletStep_All() throws Exception {
		StepFactoryBean<Object, Object> fb = new StepFactoryBean<Object, Object>();
		fb.setBeanName("step1");
		fb.setAllowStartIfComplete(true);
		fb.setJobRepository(new JobRepositorySupport());
		fb.setStartLimit(5);
		fb.setTasklet(new DummyTasklet());
		fb.setTransactionManager(new ResourcelessTransactionManager());
		fb.setListeners(new StepExecutionListenerSupport[] { new StepExecutionListenerSupport() });
		fb.setTransactionAttributeList(new ArrayList<String>());
		Object step = fb.getObject();
		assertTrue(step instanceof TaskletStep);
		Object tasklet = ReflectionTestUtils.getField(step, "tasklet");
		assertTrue(tasklet instanceof DummyTasklet);
	}

	@Test(expected = IllegalStateException.class)
	public void testSimpleStep_All() throws Exception {
		StepFactoryBean<Object, Object> fb = new StepFactoryBean<Object, Object>();
		fb.setBeanName("step1");
		fb.setAllowStartIfComplete(true);
		fb.setJobRepository(new JobRepositorySupport());
		fb.setStartLimit(5);
		fb.setTransactionManager(new ResourcelessTransactionManager());
		fb.setListeners(new StepListener[] { new StepExecutionListenerSupport() });
		fb.setTransactionAttributeList(new ArrayList<String>());
		fb.setChunkCompletionPolicy(new DummyCompletionPolicy());
		fb.setCommitInterval(5);
		fb.setTaskExecutor(new SyncTaskExecutor());
		fb.setItemReader(new DummyItemReader());
		fb.setItemWriter(new DummyItemWriter());
		fb.setStreams(new ItemStream[] { new FlatFileItemReader<Object>() });

		Object step = fb.getObject();
		assertTrue(step instanceof TaskletStep);
		Object tasklet = ReflectionTestUtils.getField(step, "tasklet");
		assertTrue(tasklet instanceof ChunkOrientedTasklet);
	}

	@Test(expected = IllegalStateException.class)
	public void testFaultTolerantStep_All() throws Exception {
		StepFactoryBean<Object, Object> fb = new StepFactoryBean<Object, Object>();
		fb.setBeanName("step1");
		fb.setAllowStartIfComplete(true);
		fb.setJobRepository(new JobRepositorySupport());
		fb.setStartLimit(5);
		fb.setTransactionManager(new ResourcelessTransactionManager());
		fb.setListeners(new StepListener[] { new StepExecutionListenerSupport() });
		fb.setTransactionAttributeList(new ArrayList<String>());
		fb.setChunkCompletionPolicy(new DummyCompletionPolicy());
		fb.setCommitInterval(5);
		fb.setTaskExecutor(new SyncTaskExecutor());
		fb.setItemReader(new DummyItemReader());
		fb.setItemWriter(new DummyItemWriter());
		fb.setStreams(new ItemStream[] { new FlatFileItemReader<Object>() });
		fb.setCacheCapacity(5);
		fb.setIsReaderTransactionalQueue(true);
		fb.setRetryLimit(5);
		fb.setSkipLimit(100);
		fb.setRetryListeners(new RetryListenerSupport());
		fb.setSkippableExceptionClasses(new ArrayList<Class<? extends Throwable>>());
		fb.setRetryableExceptionClasses(new ArrayList<Class<? extends Throwable>>());
		fb.setFatalExceptionClasses(new ArrayList<Class<? extends Throwable>>());

		Object step = fb.getObject();
		assertTrue(step instanceof TaskletStep);
		Object tasklet = ReflectionTestUtils.getField(step, "tasklet");
		assertTrue(tasklet instanceof ChunkOrientedTasklet);
	}

	@Test
	public void testSimpleStep() throws Exception {
		StepFactoryBean<Object, Object> fb = new StepFactoryBean<Object, Object>();
		fb.setHasTaskletElement(true);
		fb.setBeanName("step1");
		fb.setAllowStartIfComplete(true);
		fb.setJobRepository(new JobRepositorySupport());
		fb.setStartLimit(5);
		fb.setTransactionManager(new ResourcelessTransactionManager());
		fb.setListeners(new StepListener[] { new StepExecutionListenerSupport() });
		fb.setTransactionAttributeList(new ArrayList<String>());
		fb.setChunkCompletionPolicy(new DummyCompletionPolicy());
		fb.setTaskExecutor(new SyncTaskExecutor());
		fb.setItemReader(new DummyItemReader());
		fb.setItemProcessor(new PassThroughItemProcessor<Object>());
		fb.setItemWriter(new DummyItemWriter());
		fb.setStreams(new ItemStream[] { new FlatFileItemReader<Object>() });
		
		Object step = fb.getObject();
		assertTrue(step instanceof TaskletStep);
		Object tasklet = ReflectionTestUtils.getField(step, "tasklet");
		assertTrue(tasklet instanceof ChunkOrientedTasklet);
	}

	@Test
	public void testFaultTolerantStep() throws Exception {
		StepFactoryBean<Object, Object> fb = new StepFactoryBean<Object, Object>();
		fb.setHasTaskletElement(true);
		fb.setBeanName("step1");
		fb.setAllowStartIfComplete(true);
		fb.setJobRepository(new JobRepositorySupport());
		fb.setStartLimit(5);
		fb.setTransactionManager(new ResourcelessTransactionManager());
		fb.setListeners(new StepListener[] { new StepExecutionListenerSupport() });
		fb.setTransactionAttributeList(new ArrayList<String>());
		fb.setChunkCompletionPolicy(new DummyCompletionPolicy());
		fb.setTaskExecutor(new SyncTaskExecutor());
		fb.setItemReader(new DummyItemReader());
		fb.setItemProcessor(new PassThroughItemProcessor<Object>());
		fb.setItemWriter(new DummyItemWriter());
		fb.setStreams(new ItemStream[] { new FlatFileItemReader<Object>() });
		fb.setCacheCapacity(5);
		fb.setIsReaderTransactionalQueue(true);
		fb.setRetryLimit(5);
		fb.setSkipLimit(100);
		fb.setRetryListeners(new RetryListenerSupport());
		fb.setSkippableExceptionClasses(new ArrayList<Class<? extends Throwable>>());
		fb.setRetryableExceptionClasses(new ArrayList<Class<? extends Throwable>>());
		fb.setFatalExceptionClasses(new ArrayList<Class<? extends Throwable>>());

		Object step = fb.getObject();
		assertTrue(step instanceof TaskletStep);
		Object tasklet = ReflectionTestUtils.getField(step, "tasklet");
		assertTrue(tasklet instanceof ChunkOrientedTasklet);
	}
}