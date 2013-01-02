package org.example.jclouds.cli;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Iterator;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.cloudfiles.CloudFilesApiMetadata;
import org.jclouds.cloudfiles.CloudFilesClient;
import org.jclouds.http.HttpRequest;
import org.jclouds.openstack.swift.CommonSwiftAsyncClient;
import org.jclouds.openstack.swift.CommonSwiftClient;
import org.jclouds.openstack.swift.domain.SwiftObject;
import org.jclouds.openstack.swift.extensions.TemporaryUrlKeyApi;
import org.jclouds.rest.RestContext;

public class CloudFilesDemo {

	private BlobStoreContext context;
	private BlobStore storage;
	private RestContext<CommonSwiftClient, CommonSwiftAsyncClient> swift;
	private CloudFilesClient rackspace;

	private String sampleFile;

	private static final String DEFAULT_CONTAINER = "jclouds-test";

	private static final int MINUTE = 60;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new CloudFilesDemo().run(args);
	}

	public void run(String[] args) throws Exception {
		setup(args);

		try {
			listContainerContent(DEFAULT_CONTAINER);

			// publishFileSwift();
			// publishFileRackspace();
			// String blobName = publishFileGeneric();

			String blobName = "1356130315403-Bos_grunniens_at_Letdar_on_Annapurna_Circuit.jpg";
			createTempUrl3(blobName);
			createTempUrl2(blobName);
			// createTempUrl(blobName);
		} finally {
			cleanup();
			System.out.println("DONE!");
		}
	}

	private void listContainerContent(String container) {
		Iterator<?> itr = context.getBlobStore().list(container).iterator();
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}
	}

	private void cleanup() {
		context.close();
	}

	private String createTempUrl3(String name) throws Exception {
		// String name =
		// "1356130315403-Bos_grunniens_at_Letdar_on_Annapurna_Circuit.jpg";
		String container = "jclouds-test";
		String url = null;
		HttpRequest request = context.getSigner().signGetBlob(container, name,
				300 /* seconds */);
		url = request.getEndpoint().toASCIIString();
		System.out.println("URL (3)=" + url);
		return url;
	}

	private String createTempUrl2(String blobName) {
		System.out.println("Blob name: " + blobName);
		// String name = "hello";
		// String text = "fooooooooooooooooooooooo";
		// BlobStore blobStore = context.getBlobStore();
		// Blob blob =
		// context.getBlobStore().blobBuilder(name).payload(text).contentType("text/plain").build();
		String container = DEFAULT_CONTAINER;
		String tempUrl = null;
		try {
			// HttpRequest request = context.getSigner().signGetBlob(container,
			// blobName); // this works but not really because it doesn't
			// actually sign the request
			HttpRequest request = context.getSigner().signGetBlob(container,
					blobName, 1000); // this throws a 400 error
			tempUrl = request.getEndpoint().toASCIIString();
			System.out.println("get yer file here: " + tempUrl);
		} finally {
		}
		return tempUrl;
	}

	/**
	 * Publish a file using Swift api
	 */
	private String publishFileSwift() {
		File f = new File(sampleFile);
		final String container = DEFAULT_CONTAINER;
		SwiftObject object = swift.getApi().newSwiftObject();
		String blobName = uniqueObjectName(f);
		object.getInfo().setName(blobName);
		object.setPayload(f);
		swift.getApi().putObject(container, object);
		System.out.println("Published " + blobName + " to " + container);
		return blobName;
	}

	/**
	 * Publish a file using generic/portable cloud api
	 */
	private String publishFileGeneric() {
		File f = new File(sampleFile);
		String blobName = uniqueObjectName(f);
		Blob blob = storage.blobBuilder(blobName).payload(f).build();
		storage.putBlob(DEFAULT_CONTAINER, blob);
		System.out
				.println("Published " + blobName + " to " + DEFAULT_CONTAINER);
		return blobName;
	}

	/**
	 * Publish a file using Swift api
	 */
	private String publishFileRackspace() {
		File f = new File(sampleFile);
		final String container = DEFAULT_CONTAINER;
		SwiftObject object = rackspace.newSwiftObject();
		String blobName = uniqueObjectName(f);
		object.getInfo().setName(blobName);
		object.setPayload(f);
		rackspace.putObject(container, object);
		System.out.println("Published " + blobName + " to " + container);
		return blobName;
	}

	private void setup(String[] args) {
		if (args.length != 3) {
			throw new IllegalArgumentException(
					"Usage: CloudFilesDemo <RACKSPACE_USERNAME> <RACKSPACE_KEY> <SAMPLE FILE>");
		}
		sampleFile = args[2];

		String username = args[0];
		String key = args[1];
		String provider = "cloudfiles-us";
		context = ContextBuilder.newBuilder(provider)
				.credentials(username, key).buildView(BlobStoreContext.class);
		storage = context.getBlobStore();
		swift = context.unwrap();
		rackspace = context.unwrap(CloudFilesApiMetadata.CONTEXT_TOKEN)
				.getApi();
	}

	private String uniqueObjectName(File f) {
		return System.currentTimeMillis() + "-" + f.getName();
	}
}
