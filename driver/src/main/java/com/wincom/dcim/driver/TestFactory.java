package com.wincom.dcim.driver;

import java.util.Map;

import scala.Option;

public class TestFactory implements DriverFactory {

	@Override
	public String name() {
		return "test";
	}

	@Override
	public Option<Driver> create(Map<String, String> params) {
		return null;
	}

}
