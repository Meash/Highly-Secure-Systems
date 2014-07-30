package nz.at.aut.hss.steganography;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Steganography {
	public static void main(String[] args) throws Exception {
		String message = "Hello World";
		File source = new File("resources/rainbow.png");
		File dest = new File("resources/rainbow.encrypted.png");
		System.out.println("Encrypting...");
		new SteganographyEncrypter().encrypt(message, source, dest);
		System.out.println("Decrypting...");
		String decryptedMessage = new SteganographyDecrypter().decrypt(dest);
		System.out.println(decryptedMessage.substring(0,
				message.length())); // this is kind of cheating but we would print a lot of byte-trash if we would not know the message length
	}
}


class SteganographyEncrypter {
	public void encrypt(String message, File imageFile, File destFile)
			throws IOException {
		BufferedImage image = ImageIO.read(imageFile);
		image = encode(message, image);
		save(image, destFile);
	}

	private void save(BufferedImage image, File outputFile) throws IOException {
		ImageIO.write(image, "png", outputFile);
	}

	private BufferedImage encode(String message, BufferedImage image) {
		byte[] bytes = message.getBytes();
		Queue<Integer> bitsQueue = new ConcurrentLinkedQueue<>();
		bitsQueue = fillQueue(bitsQueue, bytes);
		for (int x = 0; x < image.getWidth() && !bitsQueue.isEmpty(); x++) {
			for (int y = 0; y < image.getHeight() && !bitsQueue.isEmpty(); y++) {
				int bits = bitsQueue.poll();
				int rgb = image.getRGB(x, y);
				int encryptedRgb = encodePixel(bits, rgb);
				image.setRGB(x, y, encryptedRgb);
			}
		}
		return image;
	}

	private int encodePixel(int bits, int rgb) {
		Color pixel = new Color(rgb, true);
		int red = pixel.getRed();
		// the bits value is odd if the last bit is set and even if not
		// change the 0-bit of the red value accordingly if it is not equal to the desired bit value
		if (bits % 2 == 0 && red % 2 != 0)
			red--;
		else if (bits % 2 != 0 && red % 2 == 0)
			red++;
		pixel = new Color(red, pixel.getGreen(), pixel.getBlue(),
				pixel.getAlpha());
		return pixel.getRGB();
	}

	private Queue<Integer> fillQueue(Queue<Integer> queue, byte[] bytes) {
		for (int i = 0; i < bytes.length; i++) {
			for (int bitCounter = 0; bitCounter < 8; bitCounter++) {
				int bit = (int) bytes[i] & (int) Math.pow(2, bitCounter);
				bit = bit >> bitCounter;
				queue.add(bit);
			}
		}
		return queue;
	}
}

class SteganographyDecrypter {
	public String decrypt(File imageFile) throws IOException {
		BufferedImage image = ImageIO.read(imageFile);
		return decrypt(image);
	}

	private String decrypt(BufferedImage image)
			throws UnsupportedEncodingException {
		int bitCounter = 0;
		int currentByte = 0;
		List<Byte> result = new ArrayList<>();
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
