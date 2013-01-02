package org.example.jclouds.cli;

import java.io.File;

import org.jclouds.cloudfiles.CloudFilesApiMetadata;
import org.jclouds.cloudfiles.CloudFilesClient;
import org.jclouds.openstack.swift.CommonSwiftAsyncClient;
import org.jclouds.openstack.swift.CommonSwiftClient;
import org.jclouds.openstack.swift.domain.SwiftObject;
import org.jclouds.rest.RestContext;

public class CloudFilesDemo extends DemoBase {

	private RestContext<CommonSwiftClient, CommonSwiftAsyncClient> swift;
	private CloudFilesClient rackspace;

	public CloudFilesDemo(String provider, String authUser, String authKey) {
		super(provider, authUser, authKey);
		swift = context.unwrap();
		rackspace = context.unwrap(CloudFilesApiMetadata.CONTEXT_TOKEN)
				.getApi();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String user = args[0];
		String key = args[1];
		String provider = "cloudfiles-us";
		CloudFilesDemo demo = new CloudFilesDemo(provider, user, key);
		try {
			demo.run();
			demo.publishFileRackspace();
			demo.publishFileSwift();
		} finally {
			demo.cleanup();
		}
	}

	/**
	 * Publish a file using Swift api
	 */
	protected String publishFileSwift() throws Exception {
		File f = getFile(sampleFile);
		final String container = DEFAULT_CONTAINER;

		SwiftObject object = swift.getApi().newSwiftObject();
		String blobName = uniqueObjectName(f);
		object.getInfo().setName(blobName);
		object.setPayload(f);
		swift.getApi().putObject(container, object);
		System.out
				.println("Published (swift) " + blobName + " to " + container);
		return blobName;
	}

	/**
	 * Publish a file using Swift api
	 * 
	 * @throws Exception
	 */
	protected String publishFileRackspace() throws Exception {
		File f = getFile(sampleFile);
		final String container = DEFAULT_CONTAINER;
		SwiftObject object = rackspace.newSwiftObject();
		String blobName = uniqueObjectName(f);
		object.getInfo().setName(blobName);
		object.setPayload(f);
		rackspace.putObject(container, object);
		System.out.println("Published (rackspace) " + blobName + " to "
				+ container);
		return blobName;
	}
}
