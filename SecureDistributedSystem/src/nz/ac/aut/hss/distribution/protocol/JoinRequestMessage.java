package nz.ac.aut.hss.distribution.protocol;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class JoinRequestMessage extends SimpleTextMessage {
	private static final String IDENTIFIER = "join_request";

	public JoinRequestMessage() {
		super(IDENTIFIER, "Knock knock!");
	}
}
