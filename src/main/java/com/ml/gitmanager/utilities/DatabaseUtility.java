package com.ml.gitmanager.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseUtility {

	private static final Logger LOG = LoggerFactory.getLogger(DatabaseUtility.class);

	private static SessionFactory factory;

	private DatabaseUtility() {
		// private default constructor
	}

	static {
		try (InputStream inStream = new FileInputStream("application.properties")) {
			Properties props = new Properties();
			props.load(inStream);
			StandardServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(props).build();
			MetadataSources sources = new MetadataSources();
			sources.addPackage("com.ml.gitmanager.entity");
			Metadata metadata = sources.buildMetadata(registry);
			factory = metadata.buildSessionFactory();
		} catch (NullPointerException e) {
			LOG.error("SessionFactory instance can not be null.");
			getDetailedStackTrace(e);
		} catch (IOException e) {
			LOG.error("application.properties file could not be loaded properly.");
			getDetailedStackTrace(e);
		} catch (Exception e) {
			LOG.error("Exception occurred at the time of database configuration.");
			getDetailedStackTrace(e);
		}
	}

	public static SessionFactory getSessionFactory() {

		return (factory != null) ? factory : null;
	}

	public static void getDetailedStackTrace(Throwable t) {
		Throwable cause = t.getCause();
		StringBuilder message = new StringBuilder(t.getMessage());
		while (cause != null) {
			cause = cause.getCause();
			message = message.append(cause.getMessage());
		}
		if (LOG.isErrorEnabled())
			LOG.error(message.toString());
	}

}
