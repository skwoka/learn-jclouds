package org.example.jclouds.cli;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.http.HttpRequest;

/**
 * Base class for jcloud demos.
 */
public abstract class DemoBase {

	static final String DEFAULT_CONTAINER = "kashoo-jclouds-test";

	protected BlobStoreContext context;
	protected BlobStore storage;

	final protected String authUser;
	final protected String authKey;
	final protected String provider;
	final protected String sampleFile = "Bos_grunniens_at_Letdar_on_Annapurna_Circuit.jpg";

	public DemoBase(String provider, String authUser, String authKey) {
		this.authUser = authUser;
		this.authKey = authKey;
		this.provider = provider;
		setup();
	}

	protected void setup() {
		context = ContextBuilder.newBuilder(provider)
				.credentials(authUser, authKey)
				.buildView(BlobStoreContext.class);
		storage = context.getBlobStore();
	}

	protected String uniqueObjectName(File f) {
		return System.currentTimeMillis() + "-" + f.getName();
	}

	public void run() throws Exception {
		listContainerContent(DEFAULT_CONTAINER);

		String blobName = publishFileGeneric();
		String url = createTempUrl(blobName);
		System.out.println("Temp URL: " + url);
	}

	protected void cleanup() {
		context.close();
	}

	protected String createTempUrl(String name) throws Exception {
		// HttpRequest request =
		// context.getSigner().signGetBlob(DEFAULT_CONTAINER, name);
		// System.out.println(request);

		HttpRequest request = context.getSigner().signGetBlob(
				DEFAULT_CONTAINER, name, 60 /* seconds */);
		return request.getEndpoint().toASCIIString();
	}

	protected void listContainerContent(String container) {
		Iterator<?> itr = context.getBlobStore().list(container).iterator();
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}
	}

	/**
	 * Publish a file using generic/portable cloud api
	 */
	protected String publishFileGeneric() throws Exception {
		File f = getFile(sampleFile);
		String blobName = uniqueObjectName(f);
		Blob blob = storage.blobBuilder(blobName).payload(f).build();
		storage.putBlob(DEFAULT_CONTAINER, blob);
		System.out
				.println("Published " + blobName + " to " + DEFAULT_CONTAINER);
		return blobName;
	}

	protected File getFile(String filename) throws URISyntaxException {
		URL fileUrl = getClass().getResource("/" + filename);
		return new File(fileUrl.toURI());
	}
}
