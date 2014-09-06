package nz.ac.aut.hss.client.communication;

/**
 * @author Martin Schrimpf
 * @created 06.09.2014
 */
public class SavedException<T extends Exception> {
	private T saved;

	public void save(T e) {
		this.saved = e;
	}

	public void throwIfSaved() throws T {
		if(saved != null)
			throw saved;
	}
}
