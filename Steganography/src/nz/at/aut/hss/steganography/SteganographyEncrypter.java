package nz.at.aut.hss.steganography;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SteganographyEncrypter {
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
		Queue<Integer> bitsQueue = new ConcurrentLinkedQueue<Integer>();
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
