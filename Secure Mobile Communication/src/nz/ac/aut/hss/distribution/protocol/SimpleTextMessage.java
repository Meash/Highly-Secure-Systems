package nz.ac.aut.hss.distribution.protocol;

import nz.ac.aut.hss.distribution.crypt.Encryption;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class SimpleTextMessage extends Message {
	public final String content;

	public SimpleTextMessage(final String identifier, final String content, final Encryption... encryptions) {
		super(identifier);
		this.content = content;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final SimpleTextMessage that = (SimpleTextMessage) o;

		if (content != null ? !content.equals(that.content) : that.content != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return content != null ? content.hashCode() : 0;
	}
}
