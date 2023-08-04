package appli.model.domaine.util_srv;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.impl.datamatrix.DataMatrixBean;
import org.krysalis.barcode4j.impl.datamatrix.SymbolShapeHint;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.output.bitmap.BitmapEncoder;
import org.krysalis.barcode4j.output.bitmap.BitmapEncoderRegistry;
import org.krysalis.barcode4j.tools.UnitConv;

public class CodeBarreImageGenerator {

	public static void main(String[] args) {
		generateCodeBarreImg128("test matrix 123456");
	}

	public static void generateCodeBarreImg128(String codeBarre) {
		try {
			Code128Bean bean = new Code128Bean();
			final int dpi = 160;

			// Configure the barcode generator
			bean.setModuleWidth(UnitConv.in2mm(2.8f / dpi));

			bean.doQuietZone(false);

			// Open output file
			File outputFile = new File("C://caisse/" + codeBarre + ".JPG");

			FileOutputStream out = new FileOutputStream(outputFile);

			BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/x-png", dpi,
					BufferedImage.TYPE_BYTE_BINARY, false, 0);

			// Generate the barcode
			bean.generateBarcode(canvas, codeBarre);

			// Signal end of generation
			canvas.finish();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void generateCodeBarreImg39(String codeBarre) {
		try {
			Code39Bean bean39 = new Code39Bean();
			final int dpi = 160;

			// Configure the barcode generator
			bean39.setModuleWidth(UnitConv.in2mm(2.8f / dpi));

			bean39.doQuietZone(false);

			// Open output file
			File outputFile = new File("C://caisse/" + codeBarre + ".JPG");

			FileOutputStream out = new FileOutputStream(outputFile);

			// Set up the canvas provider for monochrome PNG output
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/x-png", dpi,
					BufferedImage.TYPE_BYTE_BINARY, false, 0);

			// Generate the barcode
			bean39.generateBarcode(canvas, codeBarre);

			// Signal end of generation
			canvas.finish();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void generateCodeBarreImgMatrix(String codeBarre) {
		OutputStream out = null;
		try {
			String msg = "Sample Message";
			String[] paramArr = new String[] { "Information 1", "Information 2", "Barcode4J is cool!" };

			// Create the barcode bean
			DataMatrixBean bean = new DataMatrixBean();

			final int dpi = 200;

			// Configure the barcode generator
			bean.setModuleWidth(UnitConv.in2mm(8.0f / dpi)); // makes a
																// dot/module
																// exactly eight
																// pixels
			bean.doQuietZone(false);
			bean.setShape(SymbolShapeHint.FORCE_RECTANGLE);

			boolean antiAlias = false;
			int orientation = 0;
			// Set up the canvas provider to create a monochrome bitmap
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(dpi, BufferedImage.TYPE_BYTE_BINARY, antiAlias,
					orientation);

			// Generate the barcode
			bean.generateBarcode(canvas, msg);

			// Signal end of generation
			canvas.finish();

			// Get generated bitmap
			BufferedImage symbol = canvas.getBufferedImage();

			int fontSize = 32; // pixels
			int lineHeight = (int) (fontSize * 1.2);
			Font font = new Font("Arial", Font.PLAIN, fontSize);
			int width = symbol.getWidth();
			int height = symbol.getHeight();
			FontRenderContext frc = new FontRenderContext(new AffineTransform(), antiAlias, true);
			for (int i = 0; i < paramArr.length; i++) {
				String line = paramArr[i];
				Rectangle2D bounds = font.getStringBounds(line, frc);
				width = (int) Math.ceil(Math.max(width, bounds.getWidth()));
				height += lineHeight;
			}

			// Add padding
			int padding = 2;
			width += 2 * padding;
			height += 3 * padding;

			BufferedImage bitmap = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
			Graphics2D g2d = (Graphics2D) bitmap.getGraphics();
			g2d.setBackground(Color.white);
			g2d.setColor(Color.black);
			g2d.clearRect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			g2d.setFont(font);

			// Place the barcode symbol
			AffineTransform symbolPlacement = new AffineTransform();
			symbolPlacement.translate(padding, padding);
			g2d.drawRenderedImage(symbol, symbolPlacement);

			// Add text lines (or anything else you might want to add)
			int y = padding + symbol.getHeight() + padding;
			for (int i = 0; i < paramArr.length; i++) {
				String line = paramArr[i];
				y += lineHeight;
				g2d.drawString(line, padding, y);
			}
			g2d.dispose();

			File outputFile = new File("C://caisse/" + codeBarre + ".JPG");
			// Encode bitmap as file
			String mime = "image/png";
			out = new FileOutputStream(outputFile);
			final BitmapEncoder encoder = BitmapEncoderRegistry.getInstance(mime);
			encoder.encode(bitmap, out, mime, dpi);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
