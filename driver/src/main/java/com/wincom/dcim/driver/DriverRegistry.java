package com.wincom.dcim.driver;

import static java.lang.System.out;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.Option;

public class DriverRegistry {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private Map<String, DriverFactory> factories;

	private DriverRegistry() {
		this.factories = new TreeMap<>();
	}

	Option<Driver> create(String name, Map<String, String> params) {
		DriverFactory factory = factories.get(name);
		if (factory != null) {
			return factory.create(params);
		} else {
			return Option.apply(null);
		}
	}

	public void initialize() {
		try {
			Set<Class<? extends DriverFactory>> allTypes = new HashSet<>();
			for (Package p : Package.getPackages()) {
				// if(p.getName().startsWith("java")
				// || p.getName().startsWith("sun")
				// || p.getName().startsWith("ch")
				// || p.getName().startsWith("org")
				// ) {
				// log.info("SKIPPED: {}", p.getName());
				// continue;
				// }
				if (p.getName().startsWith("com.wincom")) {
					log.info("LOADING: {}", p.getName());
					Reflections r = new Reflections(p.getName());
					allTypes.addAll(r.getSubTypesOf(DriverFactory.class));
				}
			}
			for (Class<? extends DriverFactory> c : allTypes) {
				DriverFactory f = c.newInstance();
				if (factories.containsKey(f.name())) {
					log.warn("Duplicate DriverFactory name '{}': {} and {}", f.name(), c,
							factories.get(f.name()).getClass());
				} else {
					factories.put(f.name(), f);
				}
			}
		} catch (Exception ex) {
			log.error("DriverFactory initializing failed: {}", ex);
		}
	}

	public static void main(String[] args) throws Exception {
		DriverRegistry registry = new DriverRegistry();
		registry.initialize();
		for (Map.Entry<String, DriverFactory> e : registry.factories.entrySet()) {
			out.println(String.format("DriverFactory(%s, %s)", e.getKey(), e.getValue()));
		}
	}
}
