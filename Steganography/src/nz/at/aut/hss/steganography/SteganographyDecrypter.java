package nz.at.aut.hss.steganography;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class SteganographyDecrypter {
	public String decrypt(File imageFile) throws IOException {
		BufferedImage image = ImageIO.read(imageFile);
		return decrypt(image);
	}

	private String decrypt(BufferedImage image)
			throws UnsupportedEncodingException {
		int bitCounter = 0;
		int currentByte = 0;
		List<Byte> result = new ArrayList<Byte>();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				int rgb = image.getRGB(x, y);
				Color pixel = new Color(rgb);
				int red = pixel.getRed();
				int bits = red & 1; // get least significant bit

				if (bitCounter == 8) { // reset
					result.add((byte) currentByte);
					currentByte = bitCounter = 0;
				}
				bits = bits << bitCounter;
				currentByte |= bits;
				bitCounter++;
			}
		}
		byte[] bytes = toPrimitive(result.toArray(new Byte[result.size()]));
		return new String(bytes);
	}

	private byte[] toPrimitive(Byte[] array) {
		byte[] result = new byte[array.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = array[i];
		}
		return result;
	}
}
