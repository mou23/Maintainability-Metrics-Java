/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.web.method.support;

import org.junit.Before;
import org.junit.Test;

import org.springframework.core.MethodParameter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Test fixture with {@link HandlerMethodReturnValueHandlerComposite}.
 *
 * @author Rossen Stoyanchev
 */
@SuppressWarnings("unused")
public class HandlerMethodReturnValueHandlerCompositeTests {

	private HandlerMethodReturnValueHandlerComposite handlers;

	private HandlerMethodReturnValueHandler integerHandler;

	ModelAndViewContainer mavContainer;

	private MethodParameter integerType;

	private MethodParameter stringType;


	@Before
	public void setup() throws Exception {
		this.integerType = new MethodParameter(getClass().getDeclaredMethod("handleInteger"), -1);
		this.stringType = new MethodParameter(getClass().getDeclaredMethod("handleString"), -1);

		this.integerHandler = mock(HandlerMethodReturnValueHandler.class);
		when(this.integerHandler.supportsReturnType(this.integerType)).thenReturn(true);

		this.handlers = new HandlerMethodReturnValueHandlerComposite();
		this.handlers.addHandler(this.integerHandler);

		mavContainer = new ModelAndViewContainer();
	}


	@Test
	public void supportsReturnType() throws Exception {
		assertTrue(this.handlers.supportsReturnType(this.integerType));
		assertFalse(this.handlers.supportsReturnType(this.stringType));
	}

	@Test
	public void handleReturnValue() throws Exception {
		this.handlers.handleReturnValue(55, this.integerType, this.mavContainer, null);
		verify(this.integerHandler).handleReturnValue(55, this.integerType, this.mavContainer, null);
	}

	@Test
	public void handleReturnValueWithMultipleHandlers() throws Exception {
		HandlerMethodReturnValueHandler anotherIntegerHandler = mock(HandlerMethodReturnValueHandler.class);
		when(anotherIntegerHandler.supportsReturnType(this.integerType)).thenReturn(true);

		this.handlers.handleReturnValue(55, this.integerType, this.mavContainer, null);

		verify(this.integerHandler).handleReturnValue(55, this.integerType, this.mavContainer, null);
		verifyNoMoreInteractions(anotherIntegerHandler);
	}

	@Test  // SPR-13083
	public void handleReturnValueWithAsyncHandler() throws Exception {
		Promise<Integer> promise = new Promise<>();
		MethodParameter promiseType = new MethodParameter(getClass().getDeclaredMethod("handlePromise"), -1);

		HandlerMethodReturnValueHandler responseBodyHandler = mock(HandlerMethodReturnValueHandler.class);
		when(responseBodyHandler.supportsReturnType(promiseType)).thenReturn(true);
		this.handlers.addHandler(responseBodyHandler);

		AsyncHandlerMethodReturnValueHandler promiseHandler = mock(AsyncHandlerMethodReturnValueHandler.class);
		when(promiseHandler.supportsReturnType(promiseType)).thenReturn(true);
		when(promiseHandler.isAsyncReturnValue(promise, promiseType)).thenReturn(true);
		this.handlers.addHandler(promiseHandler);

		this.handlers.handleReturnValue(promise, promiseType, this.mavContainer, null);

		verify(promiseHandler).isAsyncReturnValue(promise, promiseType);
		verify(promiseHandler).supportsReturnType(promiseType);
		verify(promiseHandler).handleReturnValue(promise, promiseType, this.mavContainer, null);
		verifyNoMoreInteractions(promiseHandler);
		verifyNoMoreInteractions(responseBodyHandler);
	}

	@Test(expected = IllegalArgumentException.class)
	public void noSuitableReturnValueHandler() throws Exception {
		this.handlers.handleReturnValue("value", this.stringType, null, null);
	}


	private Integer handleInteger() {
		return null;
	}

	private String handleString() {
		return null;
	}

	private Promise<Integer> handlePromise() {
		return null;
	}

	private static class Promise<T> {}

}
