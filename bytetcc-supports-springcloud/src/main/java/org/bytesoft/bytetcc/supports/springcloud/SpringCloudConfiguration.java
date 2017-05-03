/**
 * Copyright 2014-2017 yangming.liu<bytefox@126.com>.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, see <http://www.gnu.org/licenses/>.
 */
package org.bytesoft.bytetcc.supports.springcloud;

import org.bytesoft.bytetcc.supports.springcloud.ext.CompensableFeignInterceptor;
import org.bytesoft.bytetcc.supports.springcloud.ext.CompensableHandlerInterceptor;
import org.bytesoft.bytetcc.supports.springcloud.ext.CompensableRequestInterceptor;
import org.bytesoft.bytetcc.supports.springcloud.ext.CompensableRibbonRule;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import feign.Feign;
import feign.Feign.Builder;
import feign.InvocationHandlerFactory;

@Configuration
public class SpringCloudConfiguration extends WebMvcConfigurerAdapter {

	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(this.getCompensableHandlerInterceptor());
		// registry.addWebRequestInterceptor(this.getCompensableInterceptor());
	}

	// @org.springframework.context.annotation.Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		return registration;
	}

	@org.springframework.context.annotation.Bean
	public InvocationHandlerFactory getInvocationHandlerFactory() {
		return this.getCompensableFeignInterceptor();
	}

	@org.springframework.context.annotation.Bean
	public Builder getFeignBuilder() {
		return Feign.builder().invocationHandlerFactory(this.getInvocationHandlerFactory());
	}

	@org.springframework.context.annotation.Bean
	public CompensableRibbonRule getCompensableRibbonRule() {
		return new CompensableRibbonRule();
	}

	// @org.springframework.context.annotation.Bean
	// public CompensableInterceptor getCompensableInterceptor() {
	// return new CompensableInterceptor();
	// }

	@org.springframework.context.annotation.Bean
	public CompensableFeignInterceptor getCompensableFeignInterceptor() {
		return new CompensableFeignInterceptor();
	}

	@org.springframework.context.annotation.Bean
	public CompensableHandlerInterceptor getCompensableHandlerInterceptor() {
		return new CompensableHandlerInterceptor();
	}

	@org.springframework.context.annotation.Bean
	public CompensableRequestInterceptor getCompensableRequestInterceptor() {
		return new CompensableRequestInterceptor();
	}

	@org.springframework.context.annotation.Bean("transactionTemplate")
	public RestTemplate transactionTemplate() {
		RestTemplate restTemplate = new RestTemplate();

		CompensableRequestInterceptor interceptor = this.getCompensableRequestInterceptor();
		restTemplate.getInterceptors().add(interceptor);

		return restTemplate;
	}

	@org.springframework.cloud.client.loadbalancer.LoadBalanced
	@org.springframework.context.annotation.Bean
	public RestTemplate defaultRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();

		CompensableRequestInterceptor interceptor = this.getCompensableRequestInterceptor();
		restTemplate.getInterceptors().add(interceptor);

		return restTemplate;
	}

}
