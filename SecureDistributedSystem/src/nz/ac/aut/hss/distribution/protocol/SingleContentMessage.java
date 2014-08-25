package nz.ac.aut.hss.distribution.protocol;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class SingleContentMessage extends Message {
	public final String content;

	public SingleContentMessage(final String content) {
		this.content = content;
	}
}
