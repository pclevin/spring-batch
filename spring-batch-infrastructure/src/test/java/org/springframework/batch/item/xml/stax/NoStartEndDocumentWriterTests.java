package org.springframework.batch.item.xml.stax;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.events.XMLEvent;

import junit.framework.TestCase;

import static org.mockito.Mockito.mock;

/**
 * Tests for {@link NoStartEndDocumentStreamWriter}
 * 
 * @author Robert Kasanicky
 * @author Will Schipp
 */
public class NoStartEndDocumentWriterTests extends TestCase {

	// object under test
	private NoStartEndDocumentStreamWriter writer;

	private XMLEventWriter wrappedWriter;

	private XMLEventFactory eventFactory = XMLEventFactory.newInstance();

    @Override
	protected void setUp() throws Exception {
		wrappedWriter = mock(XMLEventWriter.class);
		writer = new NoStartEndDocumentStreamWriter(wrappedWriter);
	}

	/**
	 * StartDocument and EndDocument events are not passed to the wrapped
	 * writer.
	 */
	public void testNoStartEnd() throws Exception {
		XMLEvent event = eventFactory.createComment("testEvent");

		// mock expects only a single event
		wrappedWriter.add(event);

		writer.add(eventFactory.createStartDocument());
		writer.add(event);
		writer.add(eventFactory.createEndDocument());

	}
}
